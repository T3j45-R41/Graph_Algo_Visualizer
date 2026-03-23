package algorithms;

import graph.Graph;
import step.Step;
import step.StepType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Kruskal {

    public static List<Step> run(Graph graph) {
        List<Step> steps = new ArrayList<>();

        List<int[]> edges = new ArrayList<>();
        for (int u : graph.getVertices()) {
            for (int[] neighbor : graph.getNeighbors(u)) {
                int v = neighbor[0];
                int w = neighbor[1];

                if (u < v) {
                    edges.add(new int[] { u, v, w });
                }
            }
        }

        // Sort edges by weigt
        Collections.sort(edges, Comparator.comparingInt(e -> e[2]));

        int maxVertex = 0;
        for (int v : graph.getVertices()) {
            maxVertex = Math.max(maxVertex, v);
        }
        DisjointSet ds = new DisjointSet(maxVertex + 1);

        int selectedCount = 0;
        int totalVertices = graph.getVertices().size();

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int w = edge[2];

            // Record of we are considering this edge
            steps.add(Step.edgeStep(StepType.EDGE_CONSIDERED, u, v));

            if (ds.union(u, v)) {
                // No cycle — edge is part of the MST
                steps.add(Step.edgeStep(StepType.EDGE_SELECTED, u, v));
                selectedCount++;

                if (selectedCount == totalVertices - 1) {
                    break;
                }
            } else {
                // Would form a cycle we reject
                steps.add(Step.edgeStep(StepType.EDGE_REJECTED, u, v));
            }
        }

        return steps;
    }
}
