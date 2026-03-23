package graph;

//stores edge for animation
public class VisualEdge {

    private final int u; // source vertex id
    private final int v; // destination vertex id
    private final int weight; // edge weight

    public VisualEdge(int u, int v, int weight) {
        this.u = u;
        this.v = v;
        this.weight = weight;
    }

    // for unweighted edges
    public VisualEdge(int u, int v) {
        this(u, v, 1);
    }

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Edge(" + u + " -> " + v + ", w=" + weight + ")";
    }
}
