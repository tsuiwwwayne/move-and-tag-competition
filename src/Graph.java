import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by waynetsui on 22/2/17.
 */
public class Graph {

    private ArrayList<Obstacle> obstacles;
    private ArrayList<Node> nodes;
    private ArrayList<Point2D[]> allCoordinatePairs;

    private ArrayList<Path2D> paths;

    public Graph(Instance instance) {
        this.obstacles = instance.getObstacles();
        this.nodes = createNodes(obstacles);
        this.allCoordinatePairs = generateAllCoordinatesPairs(obstacles);
        this.paths = generatePaths(obstacles);
        populateNodes(nodes, obstacles);
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Point2D[]> getAllCoordinatePairs() {
        return allCoordinatePairs;
    }

    public ArrayList<Path2D> getPaths() {
        return paths;
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

    private ArrayList<Path2D> generatePaths(ArrayList<Obstacle> obstacles) {
        ArrayList<Path2D> paths = new ArrayList<>();

        for (Obstacle o: obstacles) {
            Path2D path = new Path2D.Double();
            ArrayList<Point2D> c = o.getCoordinates();
            path.moveTo(c.get(0).getX(), c.get(0).getY());
            for(int i = 1; i < c.size(); ++i) {
                path.lineTo(c.get(i).getX(), c.get(i).getY());
            }
            path.closePath();
            paths.add(path);
        }

        return paths;
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
//        for (Obstacle obs: obstacles) {
//            ArrayList<Point2D> coordinates = obs.getCoordinates();
//            for (Point2D c: coordinates) {
//                if (isEdge(n, c, allCoordinatePairs)) {
//                    Node end = getNodeViaCoordinates(c);
//                    double weight = calculateWeightBetweenNodes(n, end);
//                    edges.add(new Edge(end, weight));
//                }
//            }
//        }
        for (Node c: nodes) {
            if (isEdge(n, c, allCoordinatePairs)) {
                double weight = calculateWeightBetweenNodes(n, c);
                edges.add(new Edge(c, weight));
            }
        }
    }

    private Node getNodeViaCoordinates(Point2D p) {
        for (Node n: nodes) {
            if (n.getCoordinates().equals(p)) {
                return n;
            }
        }
        System.out.println("Could not find Node via Coordinates");
        return null;
    }

    private Point2D[] getCoordinatePairbyValues(Point2D p1, Point2D p2) {
        for (Point2D[] cp: allCoordinatePairs) {
            if ((cp[0].equals(p1) && cp[1].equals(p2)) || (cp[0].equals(p2) && cp[1].equals(p1))) {
                return cp;
            }
        }
        System.out.println("Cannot get coordinate via value");
        return null;
    }

    private boolean isEdge(Node n, Node c, ArrayList<Point2D[]> allCoordinatePairs) {
        Point2D s = n.getCoordinates();
        Point2D e = c.getCoordinates();
        if (s.equals(e)) { // Same point. ie. Same Node
            return false;
        }
        for (Path2D p: paths) {
            if (p.contains(midpoint(s, e)) && (!(allCoordinatePairs.contains(getCoordinatePairbyValues(s, e))))) {
                return false;
            }
        }
        for (Point2D[] arr: allCoordinatePairs) {
            if (lineSegmentsCross(s, e, arr[0], arr[1])) {
                return false;
            }
        }
        return true;
    }

    private boolean lineSegmentsCross(Point2D a, Point2D b, Point2D c, Point2D d) {
        if (a.equals(c) || a.equals(d) || b.equals(c) || b.equals(d)) {
            return false;
        }
        return Line2D.linesIntersect(a.getX(), a.getY(), b.getX(), b.getY(), c.getX(), c.getY(), d.getX(), d.getY());
//        double denominator = ((b.getX() - a.getX()) * (d.getY() - c.getY())) - ((b.getY() - a.getY()) * (d.getX() - c.getX()));
//        if (denominator == 0) {
//            return false;
//        }
//        double numerator1 = ((a.getY() - c.getY()) * (d.getX() - c.getX())) - ((a.getX() - c.getX()) * (d.getY() - c.getY()));
//        double numerator2 = ((a.getY() - c.getY()) * (b.getX() - a.getX())) - ((a.getX() - c.getX()) * (b.getY() - a.getY()));
//
//        if (numerator1 == 0 || numerator2 == 0) {
//            return false;
//        }
//
//        double r = numerator1/denominator;
//        double s = numerator2/denominator;
//
//        return (r > 0 && r < 1) && (s > 0 && s < 1);
    }

    private Point2D midpoint(Point2D p1, Point2D p2) {
        return new Point2D.Double((p1.getX() + p2.getX())/2.0, (p1.getY()+p2.getY())/2.0);
    }

    private double calculateWeightBetweenNodes(Node start, Node end) {
        Point2D s = start.getCoordinates();
        Point2D e = end.getCoordinates();
        return Point2D.distance(s.getX(), s.getY(), e.getX(), e.getY());
    }

}
