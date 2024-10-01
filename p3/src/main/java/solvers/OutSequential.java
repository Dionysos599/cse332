package solvers;

import cse332.interfaces.BellmanFordSolver;
import main.Parser;
import cse332.graph.GraphUtil;

import java.util.List;
import java.util.Map;

public class OutSequential implements BellmanFordSolver {

    @SuppressWarnings("ManualArrayCopy")
    public List<Integer> solve(int[][] adjMatrix, int source) {
        List<Map<Integer, Integer>> g = Parser.parse(adjMatrix);
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
            int[] dist_copy = new int[n];
            for (int v = 0; v < n; v++) {
                dist_copy[v] = dist[v];
            }

            for (int v = 0; v < n; v++) {
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

        // Check for negative-cost cycles
        return GraphUtil.getCycle(pred);
    }

}
