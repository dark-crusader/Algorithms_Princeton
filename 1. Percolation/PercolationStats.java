
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] trials;
    private int gridSide;
    private double stdDev;
    private double mean;
    private double coeff;
    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Arguments should be greater than 0.");
        }
        gridSide = n;
        this.trials = new double[trials];
        performOperations();
        this.mean = StdStats.mean(this.trials);
        this.stdDev = StdStats.stddev(this.trials);
        calcConfidenceCoeff();
    }

    private void performOperations() {
        for (int i = 0; i < trials.length; i++) {
            Percolation grid = new Percolation(gridSide);
            int iteration = 0;
            while (!grid.percolates()) {
                int x = StdRandom.uniform(gridSide) + 1;
                int y = StdRandom.uniform(gridSide) + 1;
                if (!grid.isOpen(x, y)) {
                    grid.open(x, y);
                    iteration++;
                }
            }
            trials[i] = (double) iteration / (gridSide * gridSide);
        }
    }

    private void calcConfidenceCoeff() {
        this.coeff = (1.96 * stdDev) / Math.sqrt(trials.length);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stdDev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - coeff;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + coeff;
    }

    // test client
    public static void main(String[] args) {
        PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.printf("mean                     = %.16f %n", ps.mean());
        System.out.printf("stddev                   = %.16f %n", ps.stddev());
        System.out.printf("95%% confidence interval = [%.16f, %.16f] %n", ps.confidenceLo(), ps.confidenceLo());
    }

}