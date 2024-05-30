package main;

import cse332.exceptions.NotYetImplementedException;
import cse332.graph.GraphUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {

    /**
     * Parse an adjacency matrix into an adjacency list.
     * @param adjMatrix Adjacency matrix
     * @return Adjacency list of maps from node to weight
     */
    public static List<Map<Integer, Integer>> parse(int[][] adjMatrix) {
        int numVert = adjMatrix.length;
        List<Map<Integer, Integer>> adjList = new ArrayList<>();

        for (int i = 0; i < numVert; i++) {
            adjList.add(new HashMap<>());
        }

        for (int i = 0; i < numVert; i++) {
            for (int j = 0; j < numVert; j++) {
                int weight = adjMatrix[i][j];
                if (weight != GraphUtil.INF) {
                    adjList.get(i).put(j, weight);
                }
            }
        }

        return adjList;
    }

    /**
     * Parse an adjacency matrix into an adjacency list with incoming edges instead of outgoing edges.
     * @param adjMatrix Adjacency matrix
     * @return Adjacency list of maps from node to weight with incoming edges
     */
    public static List<Map<Integer, Integer>> parseInverse(int[][] adjMatrix) {
        int numVert = adjMatrix.length;
        List<Map<Integer, Integer>> adjList = new ArrayList<>();

        for (int i = 0; i < numVert; i++) {
            adjList.add(new HashMap<>());
        }

        for (int j = 0; j < numVert; j++) {
            for (int i = 0; i < numVert; i++) {
                int weight = adjMatrix[i][j];
                if (weight != GraphUtil.INF) {
                    adjList.get(j).put(i, weight);
                }
            }
        }

        return adjList;
    }

}
