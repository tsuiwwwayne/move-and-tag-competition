import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by waynetsui on 22/2/17.
 */
public class Graph {

    private ArrayList<Node> nodes;

    public Graph(Instance instance) {
        this.nodes = createNodes(instance.getObstacles());
//        populateNodes(nodes, instance.getObstacles());
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    private ArrayList<Node> createNodes(ArrayList<Obstacle> obstacles) {
        ArrayList<Node> n = new ArrayList<>();

        return n;
    }

    private ArrayList<Node> populateNodes(ArrayList<Obstacle> obstacles) {
        ArrayList<Node> n = new ArrayList<>();

        for (Obstacle obs: obstacles) {
            ArrayList<Point2D> coordinates = obs.getCoordinates();
            for (Point2D c: coordinates) {
                n.add(makeNode(c, obs, obstacles));
            }
        }
        return n;
    }

    private Node makeNode(final Point2D c, Obstacle parent, ArrayList<Obstacle> obstacles) {
        ArrayList<Edge> e = new ArrayList<>();




        return new Node(c, e);
    }

}
