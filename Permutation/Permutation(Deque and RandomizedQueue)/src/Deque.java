import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int number;

    private class Node {
        private Item val;
        private Node next;
        private Node previous;
    }

    private class ListIterator implements Iterator<Item> {
        private Node ite = first;

        public boolean hasNext() {
            return (ite != null);
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("no elements in the deque");
            Item item = ite.val;
            ite = ite.next;
            return item;
        }
    }

    // construct an empty deque
    public Deque() {
        first = new Node();
        last = new Node();
        first = null;
        last = null;
        number = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (size() == 0);
    }

    // return the number of items on the deque
    public int size() {
        return number;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException("illegal argument");
        Node newnode = new Node();
        newnode.val = item;
        newnode.previous = null;
        if (isEmpty()) {
            first = newnode;
            last = newnode;
            newnode.next = null;
        } else {
            newnode.next = first;
            first.previous = newnode;
            first = newnode;
        }
        /*
         * newnode.next = first; newnode.previous = null; if (isEmpty()) { first =
         * newnode; last = first; } else { first.previous = newnode; first = newnode; }
         */
        number++;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException("illegal argument");
        Node newnode = new Node();
        newnode.val = item;
        newnode.next = null;
        if (isEmpty()) {
            newnode.previous = null;
            last = newnode;
            first = last;
        } else {
            newnode.previous = last;
            last.next = newnode;
            last = newnode;
        }
        number++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException("deque is empty");
        Node delete = first;
        Item item = delete.val;
        delete.val = null;
        if (size() == 1) {
            first = null;
            last = null;
        } else {
            first = delete.next;
            first.previous = null;
            delete.next = null;
        }
        /*
         * first = delete.next; first.previous = null; delete.next = null;
         */
        number--;
        return item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException("deque is empty");
        Node delete = last;
        Item item = delete.val;
        delete.val = null;
        if (size() == 1) {
            first = null;
            last = null;
        } else {
            last = delete.previous;
            last.next = null;
            delete.previous = null;
        }
        /*
         * last = delete.previous; last.next = null; delete.previous = null;
         */
        number--;
        return item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    // unit testing
    public static void main(String[] args) {
        Deque<Integer> deq = new Deque<Integer>();
        deq.addFirst(0);
        /*
         * deq.addFirst(1); deq.addFirst(2); deq.addFirst(3); deq.addFirst(4);
         * deq.addFirst(5); deq.addFirst(6); deq.addFirst(7);
         */
        deq.removeLast();
        deq.addFirst(3);
        // deq.addLast(4);
        // deq.addFirst(5);
        if (!deq.isEmpty())
            System.out.println("false");
        for (int i : deq)
            System.out.print(i + " ");
        System.out.println(deq.number);
        deq.removeFirst();
        int x = deq.removeLast();
        System.out.println(x + "xxx");
        for (int i : deq)
            System.out.println(i);
    }

}