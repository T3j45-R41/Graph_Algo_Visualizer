package algorithms;

import graph.Graph;
import step.Step;
import step.StepType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Dijkstra {
    public static List<Step> run(Graph graph, int start) {
        List<Step> steps = new ArrayList<>();

        Map<Integer, Integer> dist = new HashMap<>(); // {start node, v distance}

        Set<Integer> finalized = new HashSet<>(); // processed nodes

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]); // {node,distance}

        for (int v : graph.getVertices()) {
            dist.put(v, 99999); // initializing distance to large
        }

        dist.put(start, 0);
        pq.add(new int[] { start, 0 }); // add start node to queue
        steps.add(Step.nodeStep(StepType.ADD_TO_QUEUE, start));

        while (!pq.isEmpty()) {
            // fetch node with minimum distance
            int[] curr = pq.poll();
            int u = curr[0]; // The node
            int Distu = curr[1]; // the distance

            steps.add(Step.nodeStep(StepType.REMOVE_FROM_QUEUE, u));

            if (finalized.contains(u)) {
                continue;
            } // skip this u already finalized

            // not visited then set visited true
            steps.add(Step.nodeStep(StepType.VISIT_NODE, u));
            finalized.add(u);

            // explore all nieghbours of u
            for (int[] neighbour : graph.getNeighbors(u)) {
                int v = neighbour[0]; // v aka neighbour
                int wt = neighbour[1]; // weight bw u&v

                steps.add(Step.edgeStep(StepType.EXPLORE_EDGE, u, v));

                if (!finalized.contains(v)) {
                    int newDist = Distu + wt;

                    if (newDist < dist.get(v)) {
                        dist.put(v, newDist); // added shorter path to v from u
                        pq.add(new int[] { v, newDist });
                        steps.add(Step.nodeStep(StepType.ADD_TO_QUEUE, v));
                    }
                }

            }
            steps.add(Step.nodeStep(StepType.PROCESS_NODE, u)); // processing of u is done

        }
        return steps;
    }
}
