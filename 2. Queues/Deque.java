
/* *****************************************************************************
 *  Name: Shubham Anand
 *  Date:
 *  Description: A simple class to implement deque
 **************************************************************************** */

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    // Private class to represent a node in deque
    private class Node {
        private Node prev;
        private Item data;
        private Node next;

        Node(Item data) {
            this.data = data;
        }

        public Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }
    }

    // Class fields
    private Node first;
    private Node last;
    private int count;

    // construct an empty deque
    public Deque() {

    }

    // is the deque empty?
    public boolean isEmpty() {
        return (count == 0);
    }

    // return the number of items on the deque
    public int size() {
        return count;
    }

    private void validateArgs(Item i) {
        if (i == null) {
            throw new IllegalArgumentException("A null pointer was passed.");
        }
    }

    // add the item to the front
    public void addFirst(Item item) {
        validateArgs(item);
        // Create new Node
        Node newNode = new Node(item);
        // If empty initialize both pointers to new element
        if (count == 0) {
            this.last = newNode;
            this.first = newNode;
        } else {
            newNode.next = first;
            first.setPrev(newNode);
            first = newNode;
        }
        // Increment count by 1
        count++;
    }

    // add the item to the back
    public void addLast(Item item) {
        validateArgs(item);
        // Create new Node
        Node newNode = new Node(item);
        // If empty initialize both pointers to new element
        if (count == 0) {
            this.last = newNode;
            this.first = newNode;
        } else {
            newNode.setPrev(last);
            last.next = newNode;
            last = newNode;
        }
        // Increment count by 1
        count++;
    }

    private void checkRemoval() {
        if (count == 0) {
            throw new java.util.NoSuchElementException();
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        checkRemoval();
        Item removed = first.data;
        if (count == 1) {
            last = null;
            first = null;
        } else {
            first = first.next;
            first.setPrev(null);
        }
        // Decrement by 1
        count--;
        // Return removed Item
        return removed;
    }

    // remove and return the item from the back
    public Item removeLast() {
        checkRemoval();
        // Get item to be removed
        Item removed = last.data;
        if (count == 1) {
            last = null;
            first = null;
        } else {
            last = last.getPrev();
            last.next = null;
        }
        // Decrement by 1
        count--;
        // Return removed Item
        return removed;
    }

    // Iterator class
    private class DequeIterator implements Iterator<Item> {
        private Node curr = first;
        @Override
        public boolean hasNext() {
            return (curr != null);
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException("Iterator has been exhausted");
            }
            Item item = curr.data;
            curr = curr.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Operation not supported.");
        }
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // Auxiliary method to print the Deque
    private void print() {
        System.out.println("The status of deque is:");
        Iterator<Item> iterator = iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addLast(3);
        System.out.println(deque.size());
        System.out.println(deque.removeFirst());
        System.out.println(deque.removeLast());
        System.out.println(deque.size());
        System.out.println(deque.removeLast());
        System.out.println(deque.isEmpty());
        System.out.println("The status of deque is:");
        for (Integer integer : deque) {
            System.out.print(integer + " ");
        }
    }

}
