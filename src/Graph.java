import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by waynetsui on 22/2/17.
 */
public class Graph {

    private  ArrayList<Robot> robots;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Node> nodes;
    private ArrayList<Point2D[]> allCoordinatePairs;
    private ArrayList<Path2D> paths;
    private Node robotStart;
    private Node robotEnd;

    public Graph(Instance instance) {
        this.robots = instance.getRobots();
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
        robotStart = createNode(robotStartPosition);
        robotEnd = createNode(robotEndPosition);
        nodes.add(0, robotStart);
        nodes.add(1, robotEnd);
        populateRobotNodes(robotStart, robotEnd, nodes, obstacles);
    }

    private void populateRobotNodes(Node rs, Node re, ArrayList<Node> nodes, ArrayList<Obstacle> obstacles) {
        // Populate two single nodes, rs and re.
        populateNode(rs, nodes, obstacles);
        populateNode(re, nodes, obstacles);
        // For each single node, inside its list of edges, get those edges.
        // Populate single node as an edge for those edges.
        ArrayList<Edge> rsEdges = rs.getEdges();
        for (Edge e: rsEdges) {
            Node n = e.getEnd();
            n.getEdges().add(new Edge(rs, e.getWeight()));
        }
        ArrayList<Edge> reEdges = re.getEdges();
        for (Edge e: reEdges) {
            Node n = e.getEnd();
            n.getEdges().add(new Edge(re, e.getWeight()));
        }
    }

    public void removeRobotNodes() {
        // TODO: Add parameters and function body
        // Remove rs and re from arraylist of nodes.
        nodes.remove(robotStart);
        nodes.remove(robotEnd);
        // For all nodes, if node contains rs or re node as its edges, remove it
        ArrayList<Edge> rsEdges = robotStart.getEdges();
        for (Edge e: rsEdges) {
            Node n = e.getEnd();
            removeEdgeByEndNode(n, robotStart);
        }
        ArrayList<Edge> reEdges = robotEnd.getEdges();
        for (Edge e: reEdges) {
            Node n = e.getEnd();
            removeEdgeByEndNode(n, robotEnd);
        }

    }

    private void removeEdgeByEndNode(Node n, Node n1) {
        ArrayList<Edge> edges = n.getEdges();
        for (Iterator<Edge> iterator = edges.iterator(); iterator.hasNext(); ) {
            Edge e = iterator.next();
            if (e.getEnd().equals(n1)) {
                iterator.remove();
            }
        }
//        for (Edge e: edges) {
//            if (e.getEnd().equals(n1)) {
//                edges.remove(e);
//            }
//        }
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

    private Point2D[] getCoordinatePairByValues(Point2D p1, Point2D p2) {
        for (Point2D[] cp: allCoordinatePairs) {
            if ((cp[0].equals(p1) && cp[1].equals(p2)) || (cp[0].equals(p2) && cp[1].equals(p1))) {
                return cp;
            }
        }
        return null;
    }

    private boolean isEdge(Node n, Node c, ArrayList<Point2D[]> allCoordinatePairs) {
        Point2D s = n.getCoordinates();
        Point2D e = c.getCoordinates();
        if (s.equals(e)) { // Same point. ie. Same Node
            return false;
        }
        for (Path2D p: paths) {
            if (p.contains(midpoint(s, e)) && (!(allCoordinatePairs.contains(getCoordinatePairByValues(s, e))))) {
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

    private ArrayList<Graph_Dijkstra.Edge> generateDijkstraGraph() {
        // Create ArrayList<Graph_Dijkstra.Edge> by iterating over nodes and its edges with weights
        ArrayList<Graph_Dijkstra.Edge> dEdges = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            Node n = nodes.get(i);
            Point2D p = n.getCoordinates();
            // append to Edge[]
            ArrayList<Edge> edges = n.getEdges();
            for (Edge e: edges) {
                dEdges.add(new Graph_Dijkstra.Edge(i, nodes.indexOf(e.getEnd()), p, e.getEnd().getCoordinates(), e.getWeight()));
            }
        }
        return dEdges;
    }

    public double getDistance(int r1, int r2) {
        Robot start = robots.get(r1);
        Robot end = robots.get(r2);
        Point2D p1 = start.getPosition();
        Point2D p2 = end.getPosition();
        addRobotNodes(p1, p2);

        ArrayList<Graph_Dijkstra.Edge> dEdges = generateDijkstraGraph();

        Graph_Dijkstra g = new Graph_Dijkstra(dEdges);
        g.dijkstra(p1);
        g.printPath(p2);
        double distance = g.returnDistance(p2);

        removeRobotNodes();

        return distance;
    }

    public ArrayList<Point2D> getPath(int r1, int r2) {
        Robot start = robots.get(r1);
        Robot end = robots.get(r2);
        Point2D p1 = start.getPosition();
        Point2D p2 = end.getPosition();
        addRobotNodes(p1, p2);

        ArrayList<Graph_Dijkstra.Edge> dEdges = generateDijkstraGraph();

        Graph_Dijkstra g = new Graph_Dijkstra(dEdges);
        g.dijkstra(p1);
        g.printPath(p2);
        ArrayList<Point2D> path = g.returnPath(p2);

        removeRobotNodes();

        return path;
    }

}
