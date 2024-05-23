package solvers;

import cse332.interfaces.BellmanFordSolver;
import main.Parser;
import cse332.graph.GraphUtil;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OutSequential implements BellmanFordSolver {

    /**
     * Determine if the graph contains any negative-cost cycles by constructing a predecessor array
     * and using GraphUtil.getCycle to generate the resulting List (see GraphUtil.java)
     * @param adjMatrix adjacency matrix
     * @param source starting node
     * @return cycle, as a list, or empty list if there is no cycle
     */
    public List<Integer> solve(int[][] adjMatrix, int source) {
        List<Map<Integer, Integer>> g = Parser.parse(adjMatrix);
        int n = g.size();
        int[] dist = new int[n];
        int[] prev = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;
        Arrays.fill(prev, -1);

        // Bellman-Ford
        for (int k = 0; k < n - 1; k++) {
            for (int i = 0; i < n; i++) {
                for (Map.Entry<Integer, Integer> edge : g.get(i).entrySet()) {
                    int v = edge.getKey();
                    int weight = edge.getValue();
                    if (dist[i] != Integer.MAX_VALUE && dist[i] + weight < dist[v]) {
                        dist[v] = dist[i] + weight;
                        prev[v] = i;
                    }
                }
            }
        }

        // Check for negative cycles
        for (int i = 0; i < n; i++) {
            for (Map.Entry<Integer, Integer> edge : g.get(i).entrySet()) {
                int v = edge.getKey();
                int weight = edge.getValue();
                if (dist[i] != Integer.MAX_VALUE && dist[i] + weight < dist[v]) {
                    // negative cycle
                    return GraphUtil.getCycle(prev);
                }
            }
        }
        return new LinkedList<>(); // No cycle found
    }

}
