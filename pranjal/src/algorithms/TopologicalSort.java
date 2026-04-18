package algorithms;

import graph.Graph;
import step.Step;
import step.StepType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TopologicalSort {

    /**
     * DFS-based Topological Sort.
     * Only valid for directed acyclic graphs (DAGs).
     */
    public static List<Step> run(Graph graph) {
        List<Step> steps = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();

        for (int v : graph.getVertices()) {
            if (!visited.contains(v)) {
                dfs(graph, v, visited, steps);
            }
        }

        return steps;
    }

    private static void dfs(Graph graph, int current,
            Set<Integer> visited, List<Step> steps) {

        visited.add(current);
        steps.add(Step.nodeStep(StepType.VISIT_NODE, current));

        for (int[] neighbor : graph.getNeighbors(current)) {
            int next = neighbor[0];

            steps.add(Step.edgeStep(StepType.EXPLORE_EDGE, current, next));

            if (!visited.contains(next)) {
                dfs(graph, next, visited, steps);
            }
        }

        // All descendants processed — push to topological order
        steps.add(Step.nodeStep(StepType.TOPO_PUSH_STACK, current));
        steps.add(Step.nodeStep(StepType.PROCESS_NODE, current));
    }
}
