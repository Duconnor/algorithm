import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double[] threshold;
    private final int trials;
    private double mea;
    private double stdde;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new java.lang.IllegalArgumentException("invalid arguments");
        this.trials = trials;
        threshold = new double[trials];
        int row, col;
        for (int i = 0; i < trials; i++) {
            Percolation per = new Percolation(n);
            while (!per.percolates()) {
                do {
                    row = StdRandom.uniform(n) + 1;
                    col = StdRandom.uniform(n) + 1;
                } while (per.isOpen(row, col));
                per.open(row, col);
            }
            threshold[i] = (double) per.numberOfOpenSites() / (double) (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        mea = StdStats.mean(threshold);
        return mea;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (trials == 1)
            return Double.NaN;
        stdde = StdStats.stddev(threshold);
        return stdde;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return (mea - (CONFIDENCE_95 * stdde) / Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (mea + (CONFIDENCE_95 * stdde) / Math.sqrt(trials));
    }

    // test client
    public static void main(String[] args) {
        int n, t;
        StdOut.println("hello world");
        n = StdIn.readInt();
        t = StdIn.readInt();
        PercolationStats per = new PercolationStats(n, t);
        StdOut.println("mean =" + per.mean());
        StdOut.println("stddev =" + per.stddev());
        StdOut.println("95% confidence interval = [" + per.confidenceLo() + "," + per.confidenceHi() + "]");
    }
}