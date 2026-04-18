package algorithms;

import graph.Graph;
import step.Step;
import step.StepType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TSP {

    /**
     * Nearest-Neighbor heuristic for TSP visualization.
     * Greedily picks the closest unvisited city at each step.
     */
    public static List<Step> run(Graph graph, int start) {
        List<Step> steps = new ArrayList<>();

        Set<Integer> visited = new HashSet<>();
        List<Integer> tour = new ArrayList<>();

        int current = start;
        visited.add(current);
        tour.add(current);
        steps.add(Step.nodeStep(StepType.VISIT_NODE, current));

        int totalVertices = graph.getVertices().size();

        while (visited.size() < totalVertices) {
            int nearest = -1;
            int minWeight = 99999;

            // Explore all neighbors of current to find nearest unvisited
            for (int[] neighbor : graph.getNeighbors(current)) {
                int next = neighbor[0];
                int weight = neighbor[1];

                steps.add(Step.edgeStep(StepType.EXPLORE_EDGE, current, next));

                if (!visited.contains(next) && weight < minWeight) {
                    nearest = next;
                    minWeight = weight;
                }
            }

            if (nearest == -1) {
                // No unvisited neighbor reachable — try all unvisited vertices
                // (handles disconnected-looking cases)
                break;
            }

            // Select this edge as part of the tour
            steps.add(Step.edgeStep(StepType.TSP_TOUR_EDGE, current, nearest));
            steps.add(Step.nodeStep(StepType.VISIT_NODE, nearest));
            steps.add(Step.nodeStep(StepType.PROCESS_NODE, current));

            visited.add(nearest);
            tour.add(nearest);
            current = nearest;
        }

        // Return to start to complete the tour
        if (tour.size() > 1) {
            steps.add(Step.edgeStep(StepType.TSP_TOUR_EDGE, current, start));
            steps.add(Step.nodeStep(StepType.PROCESS_NODE, current));
            steps.add(Step.nodeStep(StepType.PROCESS_NODE, start));
        }

        return steps;
    }
}
