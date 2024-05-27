package paralleltasks;

import cse332.graph.GraphUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class RelaxOutTaskBad extends RecursiveAction {

    public static final ForkJoinPool pool = new ForkJoinPool();
    public static final int CUTOFF = 1000;

    private final List<Map<Integer, Integer>> g;
    private final int[] dist;
    private final int[] dist_copy;
    private final int[] pred;
    private final int lo, hi;

    public RelaxOutTaskBad(List<Map<Integer, Integer>> g, int[] dist, int[] dist_copy, int[] pred, int lo, int hi) {
        this.g = g;
        this.dist = dist;
        this.dist_copy = dist_copy;
        this.pred = pred;
        this.lo = lo;
        this.hi = hi;
    }

    protected void compute() {
        if (hi - lo <= CUTOFF) {
            sequential(lo, hi);
        } else {
            int mid = lo + (hi - lo) / 2;
            RelaxOutTaskBad left = new RelaxOutTaskBad(g, dist, dist_copy, pred, lo, mid);
            RelaxOutTaskBad right = new RelaxOutTaskBad(g, dist, dist_copy, pred, mid, hi);

            left.fork();
            right.compute();
            left.join();
        }
    }

    private void sequential(int lo, int hi) {
        for (int v = lo; v < hi; v++) {
            Map<Integer, Integer> edges = g.get(v);
            for (Integer w : edges.keySet()) {
                int cost = edges.get(w);
                if (dist_copy[v] != GraphUtil.INF && dist_copy[v] + cost < dist[w]) {
                    // Found a shorter path to w
                    dist[w] = dist_copy[v] + cost;
                    pred[w] = v;
                }
            }
        }
    }

    public static void parallel(List<Map<Integer, Integer>> g, int[] dist, int[] dist_copy, int[] pred) {
        pool.invoke(new RelaxOutTaskBad(g, dist, dist_copy, pred, 0, g.size()));
    }

}