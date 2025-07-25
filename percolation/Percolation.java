import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
public class Percolation {
    private static final int[][] dirs = { { -1, 0 }, { 1, 0 }, { 0, 1 }, { 0, -1 } };
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF uf2;
    private int N;
    private boolean[] open;
    private int sum;

    public Percolation(int n){
        if (n <= 0) throw new IllegalArgumentException("n should be larger than 0");
        N = n;
        uf = new WeightedQuickUnionUF(n * n + 2);
        uf2 = new WeightedQuickUnionUF(n * n + 1);
        open = new boolean[n*n+2];
        sum = 0;
        open[0] = true;
        open[N * N + 1] = true;
    }

    private int index(int row,int col){
        if(row == 0)return 0;
        if (row == N + 1) return N*N+1;
        return (row - 1) * N + col;
    }

    private void validate(int p, String desc) {
        if (p < 1 || p > N) {
            throw new IllegalArgumentException(desc + " index i out of bounds");
        }
    }

    private boolean isInGrid(int row, int col) {
        return row >= 1 && row <= N && col >= 1 && col <= N;
    }

    public void open(int row, int col){
        validate(row, "row");
        validate(col, "col");
        int idx = index(row,col);
        if(open[ index(row,col) ] ) {
            return;
        }
        sum++;
        open[index(row,col)] = true;
        for (int i = 0; i < dirs.length; i++) {
            int neighborRow = row + dirs[i][0];
            int neighborCol = col + dirs[i][1];
            if (neighborRow == N + 1)
                uf.union(index(neighborRow, neighborCol), index(row, col));
            if (neighborRow == 0 || isInGrid(neighborRow, neighborCol) && isOpen(
                    neighborRow, neighborCol)) {
                uf2.union(index(neighborRow, neighborCol), index(row, col));
                uf.union(index(neighborRow, neighborCol), index(row, col));
            }
        }
    }

    public boolean isOpen(int row, int col){
        validate(row, "row");
        validate(col, "col");
        return open[index(row,col)];
    }

    public boolean isFull(int row, int col){
        validate(row, "row");
        validate(col, "col");
        if (!isOpen(row, col)) {
            return false;
        }
        return uf2.find(index(row,col)) == uf2.find(0);
    }

    public int numberOfOpenSites(){
        return sum;
    }

    public boolean percolates(){
        return uf.find(0) == uf.find(N *  N + 1);
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        Percolation p = new Percolation(n);
        while (!p.percolates()) {
            for(int i = 1; i <= n; i++){
                for(int j = 1; j <= n; j++){
                    double temp = (n * n - p.numberOfOpenSites() == 0)? 1 : 1.0 / (n * n - p.numberOfOpenSites());
                    if(StdRandom.bernoulli(temp))
                        p.open(i,j);
                }
            }
        }
        StdOut.println("Percolation threshold is " + p.numberOfOpenSites() / Math.pow(n, 2));
    }
}
