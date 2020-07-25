
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    // Stores solution of board
    private Node solution;

    // Class to emulate search nodes for boards
    private class Node implements Comparable<Node> {
        private Board board;
        private final int moves;
        private Node previous;
        private int manhattanPriority;

        Node(Board board, int moves, Node previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
            this.manhattanPriority = board.manhattan() + moves;
        }

        public Board getBoard() {
            return board;
        }

        @Override
        public int compareTo(Node node) {
            return Integer.compare(this.manhattanPriority, node.manhattanPriority);
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("null cannot be provided as argument");
        }
        solution = null;
        solve(initial);
    }

    // Method to solve the board
    private void solve(Board initial) {
        // Initializing priority queues
        MinPQ<Node> primary = new MinPQ<>();
        MinPQ<Node> twin = new MinPQ<>();
        primary.insert(new Node(initial, 0, null));
        twin.insert(new Node(initial.twin(), 0, null));

        while (true) {
            Node minPrimary = primary.delMin();
            if (minPrimary.getBoard().isGoal()) {
                makeSolution(minPrimary);
                break;
            }
            appendNeighbours(minPrimary, primary);

            Node minTwin = twin.delMin();
            if (minTwin.getBoard().isGoal()) {
                break;
            }
            appendNeighbours(minTwin, twin);
        }
    }

    // Method that constructs the Iterable
    private void makeSolution(Node sol) {
        solution = sol;
    }

    // Method to append neighbours of current node
    private void appendNeighbours(Node cur, MinPQ<Node> target) {
        for (Board b: cur.getBoard().neighbors()) {
            if (cur.previous != null && b.equals(cur.previous.getBoard())) {
                continue;
            }
            target.insert(new Node(b, cur.moves + 1, cur));
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solution != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (solution == null) {
            return -1;
        }
        return solution.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (solution == null) {
            return null;
        }
        Stack<Board> res = new Stack<>();
        Node sol = solution;
        while (sol != null) {
            res.push(sol.getBoard());
            sol = sol.previous;
        }
        return res;
    }

    // test client (see below)
    public static void main(String[] args) {
        // Solution check for board
        // int[][] arr = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        int[][] arr = {{1, 2, 3}, {4, 5, 6}, {8, 7, 0}};
        Board board = new Board(arr);
        Solver solver = new Solver(board);
        solutionPrint(solver);
    }

    private static void solutionPrint(Solver solver) {
        if (solver.isSolvable()) {
            System.out.println("Took " + solver.moves() + " moves to solve.");
            System.out.println("Solution steps are: ");
            for (Board b: solver.solution()) {
                System.out.println(b);
            }
        } else {
            System.out.println("Not solvable.");
        }
    }
}
