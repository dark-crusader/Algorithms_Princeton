
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public final class MergeSort {
    private static long opcount = 0;
    // Driver method
    public static <T extends Comparable<T>> T[] sort(T[] arr) {
        return mergeSort(arr);
    }

    // Method to merge
    private static <T extends Comparable<T>> void merge(T[] arr, T[] aux, int beg, int mid, int end) {
        int i = beg;
        int j = mid;
        for (int count = beg; count < end; count++) {
            opcount++;
            if (i == mid) {
                aux[count] = arr[j++];
            } else if (j == end) {
                aux[count] = arr[i++];
            } else if (arr[i].compareTo(arr[j]) <= 0) {
                aux[count] = arr[i++];
            } else {
                aux[count] = arr[j++];
            }
        }
    }

    // Method to perform iterative merge sort
    private static <T extends Comparable<T>> T[] mergeSort(T[] arr) {
        int length = arr.length;
        T[] aux = arr.clone();
        for (int i = 1; i < length; i += i) {
            for (int j = 0; j < length - 1 ; j += i + i) {
                if (2 * i <= 7) {
                    opcount += InsertionSort.sort(aux, j, Math.min(length, j + 2 * i));
                } else {
                    merge(arr, aux, j, Math.min(j + i, length), Math.min(length, j + 2 * i));
                }
            }
            T[] temp = aux;
            aux = arr;
            arr = temp;
        }
        return arr;
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

    public static void main(String[] args) throws FileNotFoundException {
        List<Integer> arr = new ArrayList<>();
        File file = new File("/testing.txt");
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            arr.add(Integer.parseInt(scanner.next()));
        }
        Integer[] toSort = new Integer[arr.size()];
        arr.toArray(toSort);
        Integer[] sorted = MergeSort.sort(toSort);
        if (test(sorted)) {
            System.out.println("True Assertion.");
        } else {
            System.out.println("Error");
        }
        System.out.println("Opcount is " + opcount);
    }
}
