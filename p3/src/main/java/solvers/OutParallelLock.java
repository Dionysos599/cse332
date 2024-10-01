package solvers;

import cse332.graph.GraphUtil;
import cse332.interfaces.BellmanFordSolver;
import main.Parser;
import paralleltasks.ArrayCopyTask;
import paralleltasks.RelaxOutTaskLock;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class OutParallelLock implements BellmanFordSolver {

    public List<Integer> solve(int[][] adjMatrix, int source) {
        List<Map<Integer, Integer>> g = Parser.parse(adjMatrix);
        int n = adjMatrix.length;

        // Initialization
        int[] dist = new int[n];
        int[] pred = new int[n];
        ReentrantLock[] locks = new ReentrantLock[n];
        for (int v = 0; v < n; v++) {
            dist[v] = GraphUtil.INF;
            pred[v] = -1;
            locks[v] = new ReentrantLock();
        }
        dist[source] = 0;

        // Main algorithm
        for (int i = 0; i < n; i++) {
            int[] dist_copy = ArrayCopyTask.copy(dist);

            RelaxOutTaskLock.parallel(g, dist, dist_copy, pred, locks);
        }

        // Check for negative-cost cycles
        return GraphUtil.getCycle(pred);
    }

}
