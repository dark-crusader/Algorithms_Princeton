
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KdTree {
    private Node root;
    private Point2D nearestPoint;
    private double minDist;
    // Construct empty 2d tree
    public KdTree() {
        minDist = Double.POSITIVE_INFINITY;
    }

    // Class to emulate a node in the tree
    private class Node {
        private final Point2D point;
        private int size;
        private Node left;
        private Node right;
        Node(Point2D point) {
            this.point = point;
            // A new node has 0 size
            this.size = 1;
            // Left and right pointers of new node left as null
        }
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size(root);
    }

    private int size(Node node) {
        if (node == null) return 0;
        return node.size;
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
        root = insert(root, p, true);
    }

    private Node insert(Node node, Point2D point, boolean compareX) {
        // If node is null return new node with point
        if (node == null) return new Node(point);
        // If point already present, return the node
        if (node.point.equals(point)) return node;
        // Select comparator based on level in tree
        Comparator<Point2D> comparator = compareX ? Point2D.X_ORDER : Point2D.Y_ORDER;
        int cmp = comparator.compare(point, node.point);
        // If on left side or below, insert into left node
        if (cmp < 0) node.left = insert(node.left, point, !compareX);
        // Else insert into right node
        if (cmp >= 0) node.right = insert(node.right, point, !compareX);
        // Update the size of node
        node.size = size(node.left) + 1 + size(node.right);
        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        checkArg(p);
        return contains(root, p, true);
    }

    private boolean contains(Node node, Point2D p, boolean compareX) {
        // If null node reached, not found
        if (node == null) return false;
        // Found
        if (node.point.equals(p)) return true;
        // Select comparator based on tree level
        Comparator<Point2D> comparator = compareX ? Point2D.X_ORDER : Point2D.Y_ORDER;
        int cmp = comparator.compare(p, node.point);
        if (cmp < 0) return contains(node.left, p, !compareX);
        return contains(node.right, p, !compareX);
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, true);
    }

    private void draw(Node node, boolean xCompare) {
        if (node != null) {
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.setPenRadius(0.01);
            node.point.draw();
            StdDraw.setPenRadius();
            if (xCompare) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.point.x(), 0, node.point.x(), 1);
            } else {
                StdDraw.setPenColor(Color.BLUE);
                StdDraw.line(0, node.point.y(), 1, node.point.y());
            }
            draw(node.left, !xCompare);
            draw(node.right, !xCompare);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        checkArg(rect);
        return range(root, new RectHV(0, 0, 1, 1), rect, true);
    }

    private List<Point2D> range(Node n, RectHV rect, RectHV ref, boolean xCompare) {
        List<Point2D> res = new ArrayList<>();
        if (!rect.intersects(ref) || n == null) return res;
        if (ref.contains(n.point)) res.add(n.point);
        RectHV left;
        RectHV right;
        if (xCompare) {
            left = new RectHV(rect.xmin(), rect.ymin(), n.point.x(), rect.ymax());
            right = new RectHV(n.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
        } else {
            left = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), n.point.y());
            right = new RectHV(rect.xmin(), n.point.y(), rect.xmax(), rect.ymax());
        }
        res.addAll(range(n.left, left, ref, !xCompare));
        res.addAll(range(n.right, right, ref, !xCompare));
        return res;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        checkArg(p);
        if (isEmpty()) return null;
        nearest(p, root, new RectHV(0.0, 0.0, 1.0, 1.0), true);
        Point2D res = this.nearestPoint;
        this.nearestPoint = null;
        this.minDist = Double.POSITIVE_INFINITY;
        return res;
    }

    private void nearest(Point2D query, Node node, RectHV rect, boolean xCompare) {
        if (node == null) return;
        double curDist = node.point.distanceSquaredTo(query);
        if (curDist < minDist) {
            this.nearestPoint = node.point;
            this.minDist = curDist;
        }
        RectHV left, leftHalf;
        RectHV right;
        if (xCompare) {
            left = new RectHV(rect.xmin(), rect.ymin(), node.point.x(), rect.ymax());
            leftHalf = new RectHV(0.0, 0.0, node.point.x(), 1.0);
            right = new RectHV(node.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
        } else {
            left = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.point.y());
            leftHalf = new RectHV(0.0, 0.0, 1.0, node.point.y());
            right = new RectHV(rect.xmin(), node.point.y(), rect.xmax(), rect.ymax());
        }

        if (leftHalf.contains(query)) {
            nearest(query, node.left, left, !xCompare);
            if (right.distanceSquaredTo(query) < minDist) {
                nearest(query, node.right, right, !xCompare);
            }
        } else {
            nearest(query, node.right, right, !xCompare);
            if (left.distanceSquaredTo(query) < minDist) {
                nearest(query, node.left, left, !xCompare);
            }
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.372, 0.497));
        tree.insert(new Point2D(0.564, 0.413));
        tree.insert(new Point2D(0.226, 0.577));
        tree.insert(new Point2D(0.144, 0.179));
        tree.insert(new Point2D(0.083, 0.51));
        tree.insert(new Point2D(0.32, 0.708));
        tree.insert(new Point2D(0.417, 0.362));
        tree.insert(new Point2D(0.862, 0.825));
        tree.insert(new Point2D(0.785, 0.725));
        tree.insert(new Point2D(0.499, 0.208));
        // for (Point2D p: tree.range(new RectHV(0.0, 0.0, 0.5, 0.5))) {
        //     System.out.println(p);
        // }
        // tree.draw();
        // StdDraw.setPenColor(Color.BLACK);
        // StdDraw.setPenRadius(0.01);
        // Point2D newP = new Point2D(0.07, 0.07);
        // newP.draw();
        // System.out.println(tree.nearest(newP));
        // tree.insert(new Point2D(0.7, 0.2));
        // tree.insert(new Point2D(0.5, 0.4));
        // tree.insert(new Point2D(0.2, 0.3));
        // tree.insert(new Point2D(0.4, 0.7));
        // tree.insert(new Point2D(0.9, 0.6));

        System.out.println(tree.nearest(new Point2D(0.4, 0.27)));
    }
}