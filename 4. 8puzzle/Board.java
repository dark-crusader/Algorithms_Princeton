
import edu.princeton.cs.algs4.Queue;

public class Board {
    private final char[][] tiles;
    private char emptyTileRow;
    private char emptyTileCol;
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = new char[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j] == 0) {
                    emptyTileRow = (char) i;
                    emptyTileCol = (char) j;
                }
                this.tiles[i][j] = (char) tiles[i][j];
            }
        }
    }

    private Board(char[][] tiles) {
        this.tiles = new char[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j] == 0) {
                    emptyTileRow = (char) i;
                    emptyTileCol = (char) j;
                }
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(tiles.length);
        sb.append("\n");
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                sb.append(String.format("%2d ", (int) tiles[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        int cost = 0;
        int pos = 1;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (i == tiles.length -1 && j == tiles.length - 1) {
                    continue;
                }
                if (tiles[i][j] != pos) {
                    cost++;
                }
                pos++;
            }
        }
        return cost;
    }

    // Utility functions for calculating manhattan distance
    private int getRow(int num) {
        return (num - 1) / tiles.length;
    }
    private int getCol(int num) {
        return (num - 1) - getRow(num) * tiles.length;
    }
    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int cost = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                int num = tiles[i][j];
                if (num == 0) continue;
                cost += Math.abs(i - getRow(num)) + Math.abs(j - getCol(num));
            }
        }
        return cost;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    @Override
    public boolean equals(Object y) {
        if (this == y)
            return true;
        if (y == null)
            return false;
        if (y.getClass() != this.getClass())
            return false;
        Board that = (Board) y;
        if (this.emptyTileRow != that.emptyTileRow ||
                this.emptyTileCol != that.emptyTileCol)
            return false;

        if (that.tiles.length != this.tiles.length)
            return false;

        for (int i = 0; i < this.tiles.length; i++) {
            for (int j = 0; j < this.tiles.length; j++) {
                if (this.tiles[i][j] != that.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void exchange(char[] pos1, char[] pos2) {
        char tmp = tiles[pos1[0]][pos1[1]];
        tiles[pos1[0]][pos1[1]] = tiles[pos2[0]][pos2[1]];
        tiles[pos2[0]][pos2[1]] = tmp;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> iterBoards = new Queue<>();
        char[] index = {emptyTileRow, emptyTileCol};
        if (index[0] - 1 >= 0) {
            Board top = new Board(tiles);
            char[] topIndex = {(char) (index[0] - 1), index[1]};
            top.exchange(index, topIndex);
            top.emptyTileRow = topIndex[0];
            top.emptyTileCol = topIndex[1];
            iterBoards.enqueue(top);
        }
        if (index[0] + 1 < tiles.length) {
            Board bottom = new Board(tiles);
            char[] bottomIndex = {(char) (index[0] + 1), index[1]};
            bottom.exchange(index, bottomIndex);
            bottom.emptyTileRow = bottomIndex[0];
            bottom.emptyTileCol = bottomIndex[1];
            iterBoards.enqueue(bottom);
        }
        if (index[1] - 1 >= 0) {
            Board left = new Board(tiles);
            char[] leftIndex = {index[0], (char) (index[1] - 1)};
            left.exchange(index, leftIndex);
            left.emptyTileRow = leftIndex[0];
            left.emptyTileCol = leftIndex[1];
            iterBoards.enqueue(left);
        }
        if (index[1] + 1 < tiles.length) {
            Board right = new Board(tiles);
            char[] rightIndex = {index[0], (char) (index[1] + 1)};
            right.exchange(index, rightIndex);
            right.emptyTileRow = rightIndex[0];
            right.emptyTileCol = rightIndex[1];
            iterBoards.enqueue(right);
        }
        return iterBoards;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board board = new Board(tiles);
        char[] pos1 = {0, 0};
        char[] pos2 = {0, 1};
        if (emptyTileRow == 0) {
            pos1[0] = 1;
            pos2[0] = 1;
        }
        board.exchange(pos1, pos2);
        return board;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // int[][] arr = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        char[][] arr = {{0, 5, 3}, {4, 2, 6}, {7, 1, 8}};
        Board board = new Board(arr);
        // Board board1 = new Board(arr1);
        // System.out.println("Dimention of board is: " + board.dimension());
        // System.out.println(board.toString());
        // System.out.println("Hamming distance: " + board.hamming());
        // System.out.println("manhattan distance: " + board.manhattan());
        // System.out.println("board isGoal: " + board.isGoal());
        // System.out.println("board isGoal: " + board1.isGoal());
        // System.out.println(board.equals(board1));
        // System.out.println("Neighbours of board: ");
        // for (Board b: board.neighbors()) {
        //     System.out.println(b);
        // }
        System.out.println("Twin to board ");
        System.out.println(board.twin());
    }

}
