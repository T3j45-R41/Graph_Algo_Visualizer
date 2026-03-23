package step;

public class Step {

    private final StepType type;
    private final int node; // primary node
    private final int fromNode; // source
    private final int toNode; // destination

    public Step(StepType type, int node, int fromNode, int toNode) {
        this.type = type;
        this.node = node;
        this.fromNode = fromNode;
        this.toNode = toNode;
    }

    // visit node, process node
    public static Step nodeStep(StepType type, int node) {
        return new Step(type, node, -1, -1);
    }

    // explore the edge
    public static Step edgeStep(StepType type, int from, int to) {
        return new Step(type, -1, from, to);
    }

    public StepType getType() {
        return type;
    }

    public int getNode() {
        return node;
    }

    public int getFromNode() {
        return fromNode;
    }

    public int getToNode() {
        return toNode;
    }

    @Override
    public String toString() {
        switch (type) {
            case VISIT_NODE:
            case PROCESS_NODE:
            case ADD_TO_QUEUE:
            case REMOVE_FROM_QUEUE:
                return type + "(node=" + node + ")";
            case EXPLORE_EDGE:
            case EDGE_CONSIDERED:
            case EDGE_SELECTED:
            case EDGE_REJECTED:
                return type + "(edge " + fromNode + " -> " + toNode + ")";
            default:
                return type + "(node=" + node
                        + ", from=" + fromNode + ", to=" + toNode + ")";
        }
    }
}
