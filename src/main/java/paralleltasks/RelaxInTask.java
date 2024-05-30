package paralleltasks;

import cse332.exceptions.NotYetImplementedException;
import cse332.graph.GraphUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class RelaxInTask extends RecursiveAction {

    public static final ForkJoinPool pool = new ForkJoinPool();
    public static final int CUTOFF = 1;

    final List<Map<Integer, Integer>> g;
    final int[] dist;
    final int[] dist_copy;
    final int[] pred;
    final int lo, hi;

    public RelaxInTask(List<Map<Integer, Integer>> g, int[] dist, int[] dist_copy, int[] pred, int lo, int hi) {
        this.g = g;
        this.dist = dist;
        this.dist_copy = dist_copy;
        this.pred = pred;
        this.lo = lo;
        this.hi = hi;
    }

    protected void compute() {
        if (hi - lo <= CUTOFF) {
            sequential(lo, hi, g, dist, dist_copy, pred);
        } else {
            int mid = lo + (hi - lo) / 2;
            RelaxInTask left = new RelaxInTask(g, dist, dist_copy, pred, lo, mid);
            RelaxInTask right = new RelaxInTask(g, dist, dist_copy, pred, mid, hi);

            left.fork();
            right.compute();
            left.join();
        }
    }

    public static void sequential(int lo, int hi, List<Map<Integer, Integer>> g,
                                  int[] dist, int[] dist_copy, int[] pred) {
        for (int w = lo; w < hi; w++) {
            Map<Integer, Integer> edges = g.get(w);
            for (Integer v : edges.keySet()) {
                int cost = edges.get(v);
                if (dist_copy[v] != GraphUtil.INF && dist_copy[v] + cost < dist[w]) {
                    // Found a shorter path to w
                    dist[w] = dist_copy[v] + cost;
                    pred[w] = v;
                }
            }
        }
    }

    public static void parallel(List<Map<Integer, Integer>> g, int[] dist, int[] dist_copy, int[] pred) {
        pool.invoke(new RelaxInTask(g, dist, dist_copy, pred, 0, g.size()));
    }

}
