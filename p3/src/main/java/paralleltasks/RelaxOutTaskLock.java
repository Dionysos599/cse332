package paralleltasks;

import cse332.graph.GraphUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.locks.ReentrantLock;

public class RelaxOutTaskLock extends RecursiveAction {

    public static final ForkJoinPool pool = new ForkJoinPool();
    public static final int CUTOFF = 1;

    private final List<Map<Integer, Integer>> g;
    private final int[] dist;
    private final int[] dist_copy;
    private final int[] pred;
    private final int lo, hi;
    private final ReentrantLock[] locks;

    public RelaxOutTaskLock(List<Map<Integer, Integer>> g, int[] dist, int[] dist_copy, int[] pred, int lo, int hi, ReentrantLock[] locks) {
        this.g = g;
        this.dist = dist;
        this.dist_copy = dist_copy;
        this.pred = pred;
        this.lo = lo;
        this.hi = hi;
        this.locks = locks;
    }

    protected void compute() {
        if (hi - lo <= CUTOFF) {
            sequential(lo, hi, g, dist, dist_copy, pred, locks);
        } else {
            int mid = lo + (hi - lo) / 2;
            RelaxOutTaskLock left = new RelaxOutTaskLock(g, dist, dist_copy, pred, lo, mid, locks);
            RelaxOutTaskLock right = new RelaxOutTaskLock(g, dist, dist_copy, pred, mid, hi, locks);

            left.fork();
            right.compute();
            left.join();
        }
    }

    private static void sequential(int lo, int hi, List<Map<Integer, Integer>> g,
                                   int[] dist, int[] dist_copy, int[] pred, ReentrantLock[] locks) {
        for (int v = lo; v < hi; v++) {
            Map<Integer, Integer> edges = g.get(v);
            for (Integer w : edges.keySet()) {
                int cost = edges.get(w);
                locks[w].lock();
                try {
                    if (dist_copy[v] != GraphUtil.INF && dist_copy[v] + cost < dist[w]) {
                        // Found a shorter path to w
                        dist[w] = dist_copy[v] + cost;
                        pred[w] = v;
                    }
                } finally {
                    locks[w].unlock();
                }
            }
        }
    }

    public static void parallel(List<Map<Integer, Integer>> g, int[] dist, int[] dist_copy, int[] pred, ReentrantLock[] locks) {
        pool.invoke(new RelaxOutTaskLock(g, dist, dist_copy, pred, 0, g.size(), locks));
    }

}
