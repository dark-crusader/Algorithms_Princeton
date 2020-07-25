
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF backwash;
    private int gridSide;
    private boolean[][] openSlots;
    private final int TOP;
    private final int BOTTOM;
    private int totalOpen;
    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Invalid argumenet. Should be greater than 0.");
        }
        gridSide = n;
        int totalEle = n * n + 2;
        uf = new WeightedQuickUnionUF(totalEle);
        backwash = new WeightedQuickUnionUF(totalEle);
        openSlots = new boolean[n][n];
        TOP = 0;
        BOTTOM = totalEle - 1;
        totalOpen = 0;
    }

    // Private method to check for argument correctnessa
    private int checkArgs(int i) {
        if (i < 1 || i > gridSide) {
            throw new IllegalArgumentException("Invalid argumenet. Should be greater than 0.");
        }
        return i - 1;
    }

    // Method to convert 2D coordinates to 1D
    private int convertDimentions(int x, int y) {
        return (x * gridSide) + (y + 1);
    }

    private void joinTop(int row, int col) {
        if (row > 0) {
            if (openSlots[row - 1][col]) {
                uf.union(convertDimentions(row, col), convertDimentions(row - 1, col));
                backwash.union(convertDimentions(row, col), convertDimentions(row - 1, col));
            }
        }
    }

    private void joinBottom(int row, int col) {
        if (row < gridSide - 1) {
            if (openSlots[row + 1][col]) {
                uf.union(convertDimentions(row, col), convertDimentions(row + 1, col));
                backwash.union(convertDimentions(row, col), convertDimentions(row + 1, col));
            }
        }
    }

    private void joinLeft(int row, int col) {
        if (col > 0) {
            if (openSlots[row][col - 1]) {
                uf.union(convertDimentions(row, col), convertDimentions(row, col - 1));
                backwash.union(convertDimentions(row, col), convertDimentions(row, col - 1));
            }
        }
    }

    private void joinRight(int row, int col) {
        if (col < gridSide - 1) {
            if (openSlots[row][col + 1]) {
                uf.union(convertDimentions(row, col), convertDimentions(row, col + 1));
                backwash.union(convertDimentions(row, col), convertDimentions(row, col + 1));
            }
        }
    }

    // Join all adjecent open vertices
    private void joinAdjecent(int row, int col) {
        if (row == 0) {
            uf.union(TOP, convertDimentions(row, col));
            backwash.union(TOP, convertDimentions(row, col));
        }
        if (row == gridSide - 1) {
            backwash.union(BOTTOM, convertDimentions(row, col));
        }
        joinTop(row, col);
        joinBottom(row, col);
        joinLeft(row, col);
        joinRight(row, col);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        row = checkArgs(row);
        col = checkArgs(col);
        if (!openSlots[row][col]) {
            openSlots[row][col] = true;
            totalOpen++;
            joinAdjecent(row, col);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        row = checkArgs(row);
        col = checkArgs(col);
        return openSlots[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        row = checkArgs(row);
        col = checkArgs(col);
        return (uf.find(convertDimentions(row, col)) == uf.find(TOP));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return totalOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return (backwash.find(TOP) == backwash.find(BOTTOM));
    }

    // private void testing() {
    //     System.out.println(uf.find(convertDimentions(0,0)));
    //     System.out.println(uf.find(convertDimentions(1,1)));
    //     System.out.println(uf.find(convertDimentions(0,1)));
    // }

    // test client (optional)
    public static void main(String[] args) {
        Percolation test = new Percolation(2);
        test.open(1, 1);
        test.open(2, 2);
        test.open(1, 2);
        System.out.println(test.percolates());
    }
}