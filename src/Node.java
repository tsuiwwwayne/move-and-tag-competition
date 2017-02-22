import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by waynetsui on 22/2/17.
 */
public class Node {

    private Point2D coordinates;
    private ArrayList<Edge> edges;

    public Node(Point2D coordinates, ArrayList<Edge> edges) {
        this.coordinates = coordinates;
        this.edges = edges;
    }

    public Point2D getCoordinates() {
        return coordinates;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

}
