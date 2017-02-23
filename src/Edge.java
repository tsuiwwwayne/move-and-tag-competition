/**
 * Created by waynetsui on 22/2/17.
 */
public class Edge {

    private Node end;
    private double weight;

    public Edge(Node end, double weight) {
        this.end = end;
        this.weight = weight;
    }

    public Node getEnd() {
        return end;
    }

    public double getWeight() {
        return weight;
    }
}
