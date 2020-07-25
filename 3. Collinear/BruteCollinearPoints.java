
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private LineSegment[] lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        checkArgs(points);
        Point[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints);
        checkDuplicates(sortedPoints);
        findLineSegments(sortedPoints);
    }

    // Method to check argument validity
    private void checkArgs(Point[] arr) {
        // Checking if arr variable is valid
        if (arr == null) {
            throw new IllegalArgumentException("Null array provided as input");
        }

        // Check null values
        for (Point obj: arr) {
            if (obj == null) {
                throw new IllegalArgumentException("Array contains null elements");
            }
        }
    }

    // Checking for any duplicates
    private void checkDuplicates(Point[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i].compareTo(arr[i + 1]) == 0) {
                throw new IllegalArgumentException("Array contains duplicate elements");
            }
        }
    }

    // Creates array of line segments
    private void findLineSegments(Point[] points) {
        List<LineSegment> res = new ArrayList<>();
        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                double curSlope = points[i].slopeTo(points[j]);
                for (int k = j + 1; k < points.length - 1; k++) {
                    if (points[i].slopeTo(points[k]) == curSlope) {
                        for (int l = k + 1; l < points.length; l++) {
                            if (points[i].slopeTo(points[l]) == curSlope) {
                                res.add(new LineSegment(points[i], points[l]));
                            }
                        }
                    }
                }
            }
        }
        this.lineSegments = new LineSegment[res.size()];
        res.toArray(lineSegments);
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.length;
    }

    // return the line segments
    public LineSegment[] segments() {
        return lineSegments.clone();
    }

    // Testing
    public static void main(String[] args) {

    }
}