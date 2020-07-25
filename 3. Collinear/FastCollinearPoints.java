
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FastCollinearPoints {
    private LineSegment[] lineSegments;
    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
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

    // Method to compute all line segments
    private void findLineSegments(Point[] points) {
        List<LineSegment> res = new LinkedList<>();
        for (int i = 0; i < points.length; i++) {
            Point reference = points[i];
            Point[] sorted = points.clone();
            // Sort points by slope w.r.t. reference point
            Arrays.sort(sorted, reference.slopeOrder());

            int j = 1;
            while (j < points.length) {
                LinkedList<Point> sameSlopePoints = new LinkedList<>();
                final double SLOPE = reference.slopeTo(sorted[j]);
                do {
                    sameSlopePoints.add(sorted[j++]);
                } while (j < points.length && reference.slopeTo(sorted[j]) == SLOPE);

                // Ensuring the line segment has at least 4 points and is not a subsegment
                if (sameSlopePoints.size() >= 3 && reference.compareTo(sameSlopePoints.peek()) < 0) {
                    res.add(new LineSegment(reference, sameSlopePoints.removeLast()));
                }
            }
        }

        this.lineSegments = new LineSegment[res.size()];
        res.toArray(lineSegments);
    }

    // the number of line segments
    public int numberOfSegments() {
        return this.lineSegments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return this.lineSegments.clone();
    }
}