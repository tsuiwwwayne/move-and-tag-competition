import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by waynetsui on 22/2/17.
 */
public class Graph {

    private ArrayList<Obstacle> obstacles;
    private ArrayList<Node> nodes;
    private ArrayList<Point2D[]> allCoordinatePairs;

    public Graph(Instance instance) {
        this.obstacles = instance.getObstacles();
        this.nodes = createNodes(obstacles);
        this.allCoordinatePairs = generateAllCoordinatesPairs(obstacles);
        populateNodes(nodes, obstacles);
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Point2D[]> getAllCoordinatePairs() {
        return allCoordinatePairs;
    }

    private ArrayList<Node> createNodes(ArrayList<Obstacle> obstacles) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (Obstacle obs: obstacles) {
            ArrayList<Point2D> coordinates = obs.getCoordinates();
            for (Point2D c: coordinates) {
                Node n = createNode(c);
                nodes.add(n);
            }
        }
        return nodes;
    }

    private Node createNode(Point2D c) {
        ArrayList<Edge> e = new ArrayList<>();
        return new Node(c, e);
    }

    // Allows addition of Start Robot Node and End Robot Node
    public void addRobotNodes(Point2D robotStartPosition, Point2D robotEndPosition) {
        Node rs = createNode(robotStartPosition);
        Node re = createNode(robotEndPosition);
        nodes.add(rs);
        nodes.add(re);
        populateRobotNodes(rs, re, nodes, obstacles);
    }

    private void populateRobotNodes(Node rs, Node re, ArrayList<Node> nodes, ArrayList<Obstacle> obstacles) {
        // TODO: Function body
    }

    public void removeRobotNodes() {
        // TODO: Add parameters and function body
    }

    private ArrayList<Point2D[]> generateAllCoordinatesPairs(ArrayList<Obstacle> obstacles) {
        ArrayList<Point2D[]> acp = new ArrayList<>();
        for (Obstacle obs: obstacles) {
            ArrayList<Point2D> coordinates = obs.getCoordinates();
            for (int i = 0; i < coordinates.size(); i++) {
                Point2D[] cp = new Point2D[2];
                cp[0] = coordinates.get(i);
                if (i+1 == coordinates.size()) {
                    cp[1] = coordinates.get(0);
                } else {
                    cp[1] = coordinates.get(i+1);
                }
                acp.add(cp);
            }
        }
        return acp;
    }

    private void populateNodes(ArrayList<Node> nodes, ArrayList<Obstacle> obstacles) {
        for (Node n: nodes) {
            populateNode(n, nodes, obstacles);
        }
    }

    /*
        Purpose: fill the ArrayList of Edges
        Key: Determine which is a valid edge to add to the list
    */
    private void populateNode(Node n, ArrayList<Node> nodes, ArrayList<Obstacle> obstacles) {
        ArrayList<Edge> edges = n.getEdges();
        for (Obstacle obs: obstacles) {
            ArrayList<Point2D> coordinates = obs.getCoordinates();
            for (Point2D c: coordinates) {

            }
        }
    }

}
