package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphVisualData {

    private final Map<Integer, VisualNode> nodeMap; // vertex id -> visual node
    private final List<VisualEdge> edges; // all visual edges

    public GraphVisualData() {
        this.nodeMap = new HashMap<>();
        this.edges = new ArrayList<>();
    }

    public void addNode(int id, double x, double y) {
        nodeMap.put(id, new VisualNode(id, x, y));
    }

    public VisualNode getNode(int id) {
        return nodeMap.get(id);
    }

    public Map<Integer, VisualNode> getAllNodes() {
        return nodeMap;
    }

    public void addEdge(int u, int v, int weight) {
        edges.add(new VisualEdge(u, v, weight));
    }

    public void addEdge(int u, int v) {
        edges.add(new VisualEdge(u, v));
    }

    public List<VisualEdge> getAllEdges() {
        return edges;
    }

    public void buildFromGraph(Graph graph, double centerX, double centerY, double radius) {
        // Clear any previous data
        nodeMap.clear();
        edges.clear();

        // Collect vertices into a list for indexed access
        List<Integer> vertices = new ArrayList<>(graph.getVertices());
        int n = vertices.size();

        // Place nodes in a circle
        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            addNode(vertices.get(i), x, y);
        }

        // Add edges
        for (int v : vertices) {
            for (int[] neighbor : graph.getNeighbors(v)) {
                int dest = neighbor[0];
                int weight = neighbor[1];

                if (graph.isDirected()) {
                    addEdge(v, dest, weight);
                } else {

                    if (v < dest) {
                        addEdge(v, dest, weight);
                    }
                }
            }
        }
    }

    // FOr CLI printing
    public void print() {
        System.out.println("Nodes:");
        for (VisualNode node : nodeMap.values()) {
            System.out.println("  " + node);
        }
        System.out.println("Edges:");
        for (VisualEdge edge : edges) {
            System.out.println("  " + edge);
        }
    }
}
