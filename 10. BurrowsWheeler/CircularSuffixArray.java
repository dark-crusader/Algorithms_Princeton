import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
    private final Integer[] indices;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if  (s == null) {
            throw new IllegalArgumentException("null cannot be passed as argument");
        }

        indices = new Integer[s.length()];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = i;
        }
        Arrays.sort(indices, rotationComparator(s.toCharArray()));
    }

    // length of s
    public int length() {
        return indices.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= this.indices.length) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        return this.indices[i];
    }

    private Comparator<Integer> rotationComparator(char[] str) {
        return (first, second) -> {
            for (int i = 0; i < str.length; i++) {
                int index1 = (first + i) % str.length;
                int index2 = (second + i) % str.length;
                int result = str[index1] - str[index2];
                if (result != 0) { return result; }
            }
            return 0;
        };
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray suffixArray = new CircularSuffixArray(args[0]);
        for (int i = 0; i < suffixArray.length(); i++) {
            StdOut.print(suffixArray.index(i) + " ");
        }
    }
}