import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> strings = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            strings.enqueue(StdIn.readString());
        }
        Iterator<String> iterator = strings.iterator();
        for (int i = 0; i < k; i++) {
            if (iterator.hasNext()) {
                StdOut.println(iterator.next());
            }
        }
    }
}