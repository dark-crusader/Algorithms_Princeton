import edu.princeton.cs.algs4.Picture;

import java.util.Arrays;
import java.util.PriorityQueue;

public class SeamCarver {
    private static final int[] EMPTY_PATH = { };
    private Picture picture;
    private double[][] energy;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException("Cannot pass in null argument to constructor. ");
        this.picture = new Picture(picture);
        this.energy = new double[picture.width()][picture.height()];
        setEnergy();
    }

    private void setEnergy() {
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                energy[x][y] = getEnergy(x, y);
            }
        }
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    private double getEnergy(int x, int y) {
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
            return 1000.0;
        double dx = 0, dy = 0;
        for (int i = 16; i >= 0; i -= 8) {
            dx += Math
                    .pow(((picture.getRGB(x - 1, y) >> i) & 0xFF) - ((picture.getRGB(x + 1, y) >> i)
                            & 0xFF), 2);
            dy += Math
                    .pow(((picture.getRGB(x, y - 1) >> i) & 0xFF) - ((picture.getRGB(x, y + 1) >> i)
                            & 0xFF), 2);
        }
        return Math.sqrt(dx + dy);
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height())
            throw new IllegalArgumentException(
                    "Expected x in [0, width - 1] and y in [0, height - 1]");
        return energy[x][y];
    }

    // Class to represent pixels energies in picture
    private static class State implements Comparable<State> {
        private final int r, c;
        private final double cost;

        State(int r, int c, double cost) {

            this.r = r;
            this.c = c;
            this.cost = cost;
        }

        @Override
        public int compareTo(State s) {
            return Double.compare(cost, s.cost);
        }
    }

    private int[] getPath(double[][] ene) {
        int n = ene.length;
        int m = ene[0].length;

        double[][] dist = new double[n][m];
        int[][] prev = new int[n][m];
        PriorityQueue<State> pq = new PriorityQueue<>();

        for (int i = 0; i < n; i++)
            Arrays.fill(dist[i], Double.POSITIVE_INFINITY);

        for (int i = 0; i < m; i++) {
            dist[0][i] = ene[0][i];
            prev[0][i] = i;
            pq.offer(new State(0, i, dist[0][i]));
        }

        while (!pq.isEmpty()) {
            State curr = pq.poll();

            // found path
            if (curr.r == n - 1) {
                int[] ret = new int[n];
                int c = curr.c;
                for (int i = n - 1; i >= 0; i--) {
                    ret[i] = c;
                    c = prev[i][c];
                }
                return ret;
            }

            for (int dc = -1; dc <= 1; dc++) {
                int nc = curr.c + dc;
                if (nc < 0 || nc >= m) continue;

                double ncost = curr.cost + ene[curr.r + 1][nc];
                if (dist[curr.r + 1][nc] <= ncost) continue;

                dist[curr.r + 1][nc] = ncost;
                prev[curr.r + 1][nc] = curr.c;
                pq.offer(new State(curr.r + 1, nc, ncost));
            }
        }
        return EMPTY_PATH;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return getPath(energy);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energyTranspose = new double[height()][width()];
        for (int i = 0; i < height(); i++)
            for (int j = 0; j < width(); j++)
                energyTranspose[i][j] = this.energy[j][i];
        return getPath(energyTranspose);
    }

    // Checks if a point is outside boundary
    private boolean isOutOfBoundary(int x, int y) {
        if (x < 0 || x >= width()) return true;
        if (y < 0 || y >= height()) return true;
        return false;
    }

    // Checks if the seam is valid
    private void checkSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("Expected non-null seam");

        for (int i = 1; i < seam.length; i++)
            if (Math.abs(seam[i] - seam[i - 1]) > 1)
                throw new IllegalArgumentException(
                        "Expected adjacent elements of seam with have a absolute difference of at most 1");
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        checkSeam(seam);
        if (seam.length != width())
            throw new IllegalArgumentException("Expected seam with length " + width());

        if (height() <= 1)
            throw new IllegalArgumentException("Cannot remove horizontal seam on height <= 1");

        Picture np = new Picture(width(), height() - 1);
        double[][] newEnergy = new double[width()][height() - 1];
        for (int i = 0; i < width(); i++) {
            int k = 0;
            for (int j = 0; j < height(); j++) {
                if (j != seam[i]) {
                    np.setRGB(i, k, picture.getRGB(i, j));
                    newEnergy[i][k] = energy[i][j];
                    k++;
                }
            }
        }
        this.energy = newEnergy;
        this.picture = np;
        // Adjust energy values that lie on the boundary of removed seam
        for (int i = 0; i < seam.length; i++) {
            if (!isOutOfBoundary(i, seam[i])) energy[i][seam[i]] = getEnergy(i, seam[i]);
            if (!isOutOfBoundary(i, seam[i] - 1)) energy[i][seam[i] - 1] = getEnergy(i, seam[i] - 1);
        }
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        checkSeam(seam);
        if (seam.length != height())
            throw new IllegalArgumentException("Expected seam with length " + height());

        if (width() <= 1)
            throw new IllegalArgumentException("Cannot remove vertical seam on width <= 1");

        // Create new picture and energy matrices
        Picture np = new Picture(width() - 1, height());
        double[][] newEnergy = new double[width() - 1][height()];
        for (int j = 0; j < height(); j++) {
            int k = 0;
            for (int i = 0; i < width(); i++) {
                if (i != seam[j]) {
                    np.setRGB(k, j, picture.getRGB(i, j));
                    newEnergy[k][j] = energy[i][j];
                    k++;
                }
            }
        }
        this.energy = newEnergy;
        this.picture = np;
        // Adjust energy values that lie on the boundary of removed seam
        for (int j = 0; j < seam.length; j++) {
            if (!isOutOfBoundary(seam[j], j)) energy[seam[j]][j] = getEnergy(seam[j], j);
            if (!isOutOfBoundary(seam[j] - 1, j)) energy[seam[j] - 1][j] = getEnergy(seam[j] - 1, j);
        }
    }

    // // Utility method to convert to RGB from Hex
    // private static Color hex2Rgb(String colorStr) {
    //     return new Color(
    //             Integer.valueOf(colorStr.substring(1, 3), 16),
    //             Integer.valueOf(colorStr.substring(3, 5), 16),
    //             Integer.valueOf(colorStr.substring(5, 7), 16));
    // }
    //
    // public static void main(String[] args) {
    //     Picture pic = new Picture(5, 5);
    //     Scanner in = new Scanner(System.in);
    //     for (int i = 0; i < 5; i++) {
    //         for (int j = 0; j < 5; j++) {
    //             pic.set(j, i, hex2Rgb(in.next()));
    //         }
    //     }
    //     SeamCarver sc = new SeamCarver(pic);
    //     System.out.println("Initial Energy");
    //     for (int i = 0; i < 5; i++) {
    //         for (int j = 0; j < 5; j++) {
    //             System.out.printf("%5.2f ", sc.energy[j][i]);
    //         }
    //         System.out.println();
    //     }
    //     int[] arr = {0, 0, 1, 2, 2};
    //     sc.removeHorizontalSeam(arr);
    //     int[] arr2 = {0, 0, 0, 1};
    //     sc.removeVerticalSeam(arr2);
    //     System.out.println("New Energy");
    //     for (int i = 0; i < 4; i++) {
    //         for (int j = 0; j < 4; j++) {
    //             System.out.printf("%5.2f ", sc.energy[j][i]);
    //         }
    //         System.out.println();
    //     }
    // }
}