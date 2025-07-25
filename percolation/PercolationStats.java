import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
public class PercolationStats {
    private static final double const_second = 1.96;
    private double[] thresholds;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("n and trials should be larger than 0");
        thresholds = new double[trials];
        for (int w = 0; w < trials; w++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int row, col;
                do {
                    row = StdRandom.uniformInt(1, n + 1);
                    col = StdRandom.uniformInt(1, n + 1);
                } while (p.isOpen(row, col));
                p.open(row, col);
            }
            thresholds[w] = 1.0 * p.numberOfOpenSites() / Math.pow(n, 2);
        }
    }

    public double mean(){
        return StdStats.mean(thresholds);
    }

    public double stddev(){
        return thresholds.length == 1 ? Double.NaN : StdStats.stddev(thresholds);
    }

    public double confidenceLo(){
        return mean() - const_second * stddev() / Math.sqrt(1.0 * thresholds.length);
    }

    public double confidenceHi(){
        return mean() + const_second * stddev() / Math.sqrt(1.0 * thresholds.length);
    }

    public static void main(String[] args){
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(n, trials);
        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println(
                "95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}