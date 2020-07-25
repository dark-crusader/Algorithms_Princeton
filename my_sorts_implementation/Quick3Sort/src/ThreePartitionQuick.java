import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ThreePartitionQuick {
    static long totalOps = 0;

    public static void sort(Comparable[] arr) {
        randomPerm(arr, 0, arr.length);
        quickSort(arr, 0, arr.length);
        totalOps += InsertionSort.sort(arr, 0, arr.length);
    }
    public static void sort(Comparable[] arr, int beg, int end) {
        randomPerm(arr, beg, end);
        quickSort(arr, beg, end);
        totalOps += InsertionSort.sort(arr, beg, end);
    }

    /**
     * Sorts from beg (inclusive) to end (exclusive)
     * @param arr Array of Objects (Objects should implement comparable)
     * @param beg from index (inclusive)
     * @param end to index (exclusive)
     */
    private static void quickSort(Comparable[] arr, int beg, int end) {
        if (beg >= end) {
            return;
        }
        if (end -  beg <= 7) {
            return;
        }
        int i = beg;
        int f = beg;
        int r = end - 1;
        while (i <= r) {
            totalOps++;
            if (arr[f].compareTo(arr[i]) == 0) {
                i++;
            } else if (arr[i].compareTo(arr[f]) < 0) {
                swap(arr, i++, f++);
            } else {
                swap(arr, i, r--);
            }
        }
        quickSort(arr, beg, f);
        quickSort(arr, i, end);
    }

    // Auxiliary method to perform swaps
    private static void swap(Comparable[] arr, int i, int j) {
        Comparable temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Method to generate a random permutation
    private static void randomPerm(Comparable[] arr, int beg, int end) {
        Random random = new Random();
        for (int i = beg; i < end - 1; i++) {
            int randIndex = i + random.nextInt(end - i);
            swap(arr, i, randIndex);
        }
    }

    // Testing
    private static boolean test(Integer[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i - 1] > arr[i]) {
                System.out.println("At index " + i + " values are: " + arr[i-1]+ " and " + arr[i]);
                return false;
            }
        }
        return true;
    }

//    public static void main(String[] args) {
//        Integer[] arr = {6,3,9,0,2,5,1};
//        ThreePartitionQuick.sort(arr);
//        for(int i: arr) {
//            System.out.print(i + " ");
//        }
//        System.out.println();
//        if (test(arr)) {
//            System.out.println("Assertion passed.");
//        } else {
//            System.out.println("Assertion failed.");
//        }
//    }

    public static void main(String[] args) throws FileNotFoundException {
        List<Integer> arr = new ArrayList<>();
        File file = new File("F:\\testing.txt");
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            arr.add(Integer.parseInt(scanner.next()));
        }
        Integer[] toSort = new Integer[arr.size()];
        arr.toArray(toSort);
        ThreePartitionQuick.sort(toSort);
        if (test(toSort)) {
            System.out.println("Assertion passed.");
        } else {
            System.out.println("Assertion failed.");
        }
        System.out.println("Opcount is " + totalOps);
    }
}
