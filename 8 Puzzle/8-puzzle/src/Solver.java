import java.util.Comparator;
import java.util.LinkedList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public final class Solver {

	private final SearchNode goal; // the goal node
	private final LinkedList<Board> solution;

	private class SearchNode {
		private final Board board; // present board
		private final int moves; // number of moves to reach this board
		private final SearchNode predecessor; // previous SearchNode
		private final boolean istwin; // true for twin
		private final int hammingpriority; // the hamming priority function
		private final int manhattanpriority;

		private class ByHamming implements Comparator<SearchNode> {

			public int compare(SearchNode a, SearchNode b) {
				return a.hammingpriority - b.hammingpriority;
			}
		}

		private class ByManhattan implements Comparator<SearchNode> {

			public int compare(SearchNode a, SearchNode b) {
				return a.manhattanpriority - b.manhattanpriority;
			}
		}

		public SearchNode(Board board, int moves, SearchNode predecessor, boolean istwin) {
			this.board = board;
			this.moves = moves;
			this.predecessor = predecessor;
			this.istwin = istwin;
			hammingpriority = board.hamming() + moves;
			manhattanpriority = board.manhattan() + moves;
		}

		// comparator
		public Comparator<SearchNode> hammingPriority() {
			return new ByHamming();
		}

		public Comparator<SearchNode> manhattanPriority() {
			return new ByManhattan();
		}

	}

	// find a solution to the initial board
	public Solver(Board initial) {
		if (initial == null)
			throw new java.lang.IllegalArgumentException("Illegal Argument");
		int type = 0; // 0 for unknown, 1 for solvable, -1 for unsolvable

		SearchNode node = new SearchNode(initial, 0, null, false);
		MinPQ<SearchNode> mpq = new MinPQ<SearchNode>(node.manhattanPriority());
		mpq.insert(new SearchNode(initial, 0, null, false));
		mpq.insert(new SearchNode(initial.twin(), 0, null, true));

		// loop

		while (type == 0) {
			node = mpq.delMin();
			// is goal?
			// if yes, then break the loop
			if (node.board.isGoal() && node.istwin) {
				type = -1;
				break;
			} else if (node.board.isGoal() && !node.istwin) {
				type = 1;
				break;
			}

			// here means not the goal
			// insert new nodes on the Priority Queue
			for (Board board : node.board.neighbors()) {
				// if tempnode's board is the same as the previous search node of node
				// do not insert it in
				if (node.predecessor != null && node.predecessor.board.equals(board))
					continue;
				// now it is guaranteed that it is not the same as the previous one
				// so just insert it in
				SearchNode tempnode = new SearchNode(board, node.moves + 1, node, node.istwin);
				mpq.insert(tempnode);
			}
		}

		// now we find the goal
		// if we find the goal, initialize the goal
		// else just set goal to null

		if (type == 1)
			goal = new SearchNode(node.board, node.moves, node.predecessor, node.istwin);
		else
			goal = null;

		// if goal is not null, we the backtrack all the way back to find the solution

		if (goal != null) {
			solution = new LinkedList<Board>();
			SearchNode previous = goal.predecessor;
			SearchNode present = goal;
			solution.addFirst(present.board);
			while (previous != null) {
				present = previous;
				previous = previous.predecessor;
				solution.addFirst(present.board);
			}
		} else
			solution = null;
	}

	// is the initial board solvable?
	public boolean isSolvable() {
		if (goal != null)
			return true;
		else
			return false;
	}

	// min number of moves to solve initial board; -1 if unsolvable
	public int moves() {
		if (goal != null)
			return goal.moves;
		else
			return -1;
	}

	// sequence of boards in a shortest solution; null if unsolvable
	public Iterable<Board> solution() {
		return solution;
	}

	// solve a slider puzzle
	public static void main(String[] args) {

		System.out.println("hello world");
		// create initial board from file
		In in = new In(args[0]);
		int n = in.readInt();
		int[][] blocks = new int[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);

		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output
		if (!solver.isSolvable())
			StdOut.println("No solution possible");
		else {
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
		}
	}
}
