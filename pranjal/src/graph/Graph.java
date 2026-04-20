package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph {

    
    private final Map<Integer, List<int[]>> adjacencyList;
    private final boolean directed;

    public Graph(boolean directed) {
        this.directed = directed;
        this.adjacencyList = new HashMap<>();
    }

    public void addVertex(int v) {
        adjacencyList.putIfAbsent(v, new ArrayList<>());
    }

    public Set<Integer> getVertices() {
        return adjacencyList.keySet();
    }

    public boolean hasVertex(int v) {
        return adjacencyList.containsKey(v);
    }

    public void addEdge(int u, int v) {
        addWeightedEdge(u, v, 1);
    }

    public void addWeightedEdge(int u, int v, int w) {

        addVertex(u);
        addVertex(v);

        
        adjacencyList.get(u).add(new int[] { v, w });

        
        if (!directed) {
            adjacencyList.get(v).add(new int[] { u, w });
        }
    }

    public List<int[]> getNeighbors(int v) {
        return adjacencyList.getOrDefault(v, new ArrayList<>());
    }

    public boolean isDirected() {
        return directed;
    }

    
    public int getEdgeCount() {
        int total = 0;
        for (List<int[]> neighbors : adjacencyList.values()) {
            total += neighbors.size();
        }
        return directed ? total : total / 2;
    }

    public void printGraph() {
        for (Map.Entry<Integer, List<int[]>> entry : adjacencyList.entrySet()) {
            StringBuilder sb = new StringBuilder();
            sb.append(entry.getKey()).append(" -> ");
            for (int[] edge : entry.getValue()) {
                sb.append("[").append(edge[0]).append(", w=").append(edge[1]).append("] ");
            }
            System.out.println(sb.toString().trim());
        }
    }
}
