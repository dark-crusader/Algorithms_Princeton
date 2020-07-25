
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

public class SAP {
    private final Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        checkArg(G);
        this.digraph = new Digraph(G);
    }

    private void checkArg(Object obj) {
        if (obj == null) throw new IllegalArgumentException("null cannot be passed as argument");
    }

    private void checkVertex(int v) {
        int maxV = digraph.V();
        if (v < 0 || v >= maxV)
            throw new IllegalArgumentException(
                    "vertex " + v + " is not between 0 and " + (maxV - 1));
    }

    // checks args and returns true is iterable is of zero length
    private boolean checkVertices(Iterable<Integer> vertices) {
        checkArg(vertices);
        int totalEle = 0;
        for (Integer ele : vertices) {
            checkArg(ele);
            checkVertex(ele);
            totalEle++;
        }
        return totalEle == 0;
    }

    private int[] calcShortest(int v, int w) {
        int shortestLength = Integer.MAX_VALUE;
        int ancestor = -1;
        int[] res = { -1, -1 };
        // Get BFS traversals for the two vertices
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(digraph, w);

        for (int i = 0; i < digraph.V(); i++) {
            if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {
                int len = bfsv.distTo(i) + bfsw.distTo(i);
                if (len < shortestLength) {
                    shortestLength = len;
                    ancestor = i;
                }
            }
        }

        if (ancestor != -1) {
            res[0] = shortestLength;
            res[1] = ancestor;
        }
        return res;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        checkVertex(v);
        checkVertex(w);
        return calcShortest(v, w)[0];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        checkVertex(v);
        checkVertex(w);
        return calcShortest(v, w)[1];
    }

    private int[] calcShortest(Iterable<Integer> v, Iterable<Integer> w) {
        int shortestLength = Integer.MAX_VALUE;
        int ancestor = -1;
        int[] res = { -1, -1 };

        // Get BFS traversals for the two set of vertices
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(digraph, w);

        for (int i = 0; i < digraph.V(); i++) {
            if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {
                int len = bfsv.distTo(i) + bfsw.distTo(i);
                if (len < shortestLength) {
                    shortestLength = len;
                    ancestor = i;
                }
            }
        }

        if (ancestor != -1) {
            res[0] = shortestLength;
            res[1] = ancestor;
        }
        return res;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        // For zero length iterables, no path
        if (checkVertices(v) || checkVertices(w)) {
            return -1;
        }
        return calcShortest(v, w)[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        // For zero length iterables, no path
        if (checkVertices(v) || checkVertices(w)) {
            return -1;
        }
        return calcShortest(v, w)[1];
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}