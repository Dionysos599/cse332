package solvers;

import cse332.graph.GraphUtil;
import cse332.interfaces.BellmanFordSolver;
import main.Parser;
import paralleltasks.ArrayCopyTask;
import paralleltasks.RelaxInTask;

import java.util.List;
import java.util.Map;

public class InParallel implements BellmanFordSolver {

    public List<Integer> solve(int[][] adjMatrix, int source) {
        List<Map<Integer, Integer>> g = Parser.parseInverse(adjMatrix);
        int n = adjMatrix.length;

        // Initialization
        int[] dist = new int[n];
        int[] pred = new int[n];
        for (int v = 0; v < n; v++) {
            dist[v] = GraphUtil.INF;
            pred[v] = -1;
        }
        dist[source] = 0;

        // Main algorithm
        for (int i = 0; i < n; i++) {
            int[] dist_copy = ArrayCopyTask.copy(dist);

            RelaxInTask.parallel(g, dist, dist_copy, pred);
        }

        // Check for negative-cost cycles
        return GraphUtil.getCycle(pred);
    }

}