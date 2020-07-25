
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue;
    private int size;
    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = (Item[]) new Object[1];
        size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return (size == 0);
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    private void doubleArraySize() {
        Item[] newQueue = (Item[]) new Object[queue.length * 2];
        for (int i = 0; i < size; i++) {
            newQueue[i] = queue[i];
        }
        queue = newQueue;
    }

    private void halfArraySize() {
        Item[] newQueue = (Item[]) new Object[queue.length / 2];
        for (int i = 0; i < size; i++) {
            newQueue[i] = queue[i];
        }
        queue = newQueue;
    }

    // Method to resize if required
    private void resizeCheck() {
        if (size == queue.length) {
            doubleArraySize();
        } else if (size <= queue.length / 4.0d && !isEmpty()) {
            halfArraySize();
        }
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Null item cannot be enqued.");
        }
        resizeCheck();
        queue[size++] = item;
    }

    private void checkSize() {
        if (size == 0) {
            throw new java.util.NoSuchElementException("Queue is empty.");
        }
    }

    // remove and return a random item
    public Item dequeue() {
        checkSize();
        int returnIndex = StdRandom.uniform(size);
        Item toReturn = queue[returnIndex];
        queue[returnIndex] = queue[--size];
        queue[size] = null;
        resizeCheck();
        return toReturn;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        checkSize();
        return queue[StdRandom.uniform(size)];
    }

    private class RandIterator implements Iterator<Item> {
        private int[] iterationArr;
        private int curr;

        RandIterator(int total) {
            iterationArr = StdRandom.permutation(total);
            curr = 0;
        }

        public boolean hasNext() {
            return (curr != iterationArr.length);
        }

        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException("The iterator is exhausted.");
            }
            return queue[iterationArr[curr++]];
        }

        public void remove() {
            throw new UnsupportedOperationException("Operation not supported");
        }
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandIterator(this.size);
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(4);
        for (Integer i: queue) {
            System.out.print(i + "->");
        }
        System.out.println();
        System.out.println("Removed: " + queue.dequeue());
        System.out.println("Removed: " + queue.dequeue());
        System.out.println("Removed: " + queue.dequeue());
        for (Integer i: queue) {
            System.out.print(i + "->");
        }
        System.out.println();
        System.out.println("Size is: " + queue.size());
        System.out.println("Sample value: " + queue.sample());
    }

}
