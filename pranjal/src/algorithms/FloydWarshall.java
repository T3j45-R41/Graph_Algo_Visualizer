package algorithms;

import graph.Graph;
import step.Step;
import step.StepType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FloydWarshall {

    public static List<Step> run(Graph graph) {
        List<Step> steps = new ArrayList<>();

        List<Integer> vertices = new ArrayList<>(graph.getVertices());
        int V = vertices.size();

        
        Map<Integer, Integer> idToIndex = new HashMap<>();
        for (int i = 0; i < V; i++) {
            idToIndex.put(vertices.get(i), i);
        }

        
        int[][] dist = new int[V][V];
        int INF = 99999;

        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                dist[i][j] = (i == j) ? 0 : INF;
            }
        }

        
        for (int u : vertices) {
            int ui = idToIndex.get(u);
            for (int[] neighbor : graph.getNeighbors(u)) {
                int vi = idToIndex.get(neighbor[0]);
                int w = neighbor[1];
                dist[ui][vi] = w;
            }
        }

        
        for (int k = 0; k < V; k++) {
            int kVertex = vertices.get(k);
            steps.add(Step.nodeStep(StepType.VISIT_NODE, kVertex));

            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (i == j || i == k || j == k) continue;

                    int iVertex = vertices.get(i);
                    int jVertex = vertices.get(j);

                    steps.add(Step.edgeStep(StepType.EXPLORE_EDGE, iVertex, jVertex));

                    if (dist[i][k] != INF && dist[k][j] != INF
                            && dist[i][k] + dist[k][j] < dist[i][j]) {

                        dist[i][j] = dist[i][k] + dist[k][j];
                        steps.add(Step.edgeStep(StepType.RELAX_EDGE, iVertex, jVertex));
                    } else {
                        steps.add(Step.edgeStep(StepType.NO_UPDATE, iVertex, jVertex));
                    }
                }
            }

            steps.add(Step.nodeStep(StepType.PROCESS_NODE, kVertex));
        }

        return steps;
    }
}
