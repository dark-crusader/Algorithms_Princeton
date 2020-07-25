
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class PointSET {
    private final SET<Point2D> points;
    // construct an empty set of points
    public PointSET() {
        points = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.size() == 0;
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // check args
    private void checkArg(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Null pointer cannot be passed as argument");
        }
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        checkArg(p);
        if (!this.points.contains(p)) {
            points.add(p);
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        checkArg(p);
        return this.points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p: this.points) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        checkArg(rect);
        List<Point2D> res = new ArrayList<>();
        for (Point2D p: this.points) {
            if (rect.contains(p)) {
                res.add(p);
            }
        }
        return res;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        checkArg(p);
        double minDist = Double.POSITIVE_INFINITY;
        Point2D nearest = null;
        for (Point2D point: this.points) {
            double newDist = p.distanceSquaredTo(point);
            if (newDist < minDist) {
                nearest = point;
                minDist = newDist;
            }
        }
        return nearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET test = new PointSET();
        test.insert(new Point2D(0.22, 0.22));
        test.insert(new Point2D(0.1, 0.1));
        test.insert(new Point2D(0.02, 0));
        System.out.println(test.nearest(new Point2D(1, 1)));
        new RectHV(0, 0, 0.2, 0.2).draw();
        System.out.println(test.range(new RectHV(0, 0, 0.2, 0.2)));
        StdDraw.setPenRadius(0.01);
        test.draw();
    }
}