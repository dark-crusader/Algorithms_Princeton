import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public final class InsertionSort {
    private static long opcount = 0;

    public static void sort(Comparable[] arr) {
        insertionSort(arr, 0, arr.length);
    }

    public static long sort(Comparable[] arr, int beg, int end) {
        return insertionSort(arr, beg, end);
    }

    private static long insertionSort(Comparable[] arr, int beg, int end) {
        long nops = 0;
        for (int i = beg + 1; i < end; i++) {
            int j = i;
            Comparable key = arr[j];
            while (j > beg && key.compareTo(arr[j - 1]) < 0) {
                opcount++;
                nops++;
                swap(arr, j-1, j);
                j--;
            }
            arr[j] = key;
        }
        return nops;
    }

    private static void swap(Comparable[] arr, int i, int j) {
        Comparable temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
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
//        Integer[] toSort = new Integer[10000000];
//        Random rand = new Random();
//        for (int i = 0; i < toSort.length; i++) {
//            toSort[i] = rand.nextInt(100);
//        }
//        System.out.println();
//        Integer[] sorted = MergeSort.sort(toSort);
//        if (test(sorted)) {
//            System.out.println("True Assertion.");
//        } else {
//            System.out.println("Error");
//        }
//
//
//    }
    public static void main(String[] args) throws FileNotFoundException {
        List<Integer> arr = new ArrayList<>();
        File file = new File("/testing.txt");
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            arr.add(Integer.parseInt(scanner.next()));
        }
        Integer[] toSort = new Integer[arr.size()];
        arr.toArray(toSort);
        InsertionSort.sort(toSort);
        if (test(toSort)) {
            System.out.println("True Assertion.");
        } else {
            System.out.println("Error");
        }
        System.out.println("Opcount is " + opcount);
    }
}
