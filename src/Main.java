import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Main {

    final static String USERNAME = "griffin";
    final static String PASSWORD = "usrdoupjm8q94i7td9qctue39k";
    final static String INPUT_FILE = "robots.mat.txt";

    private static String removeTrailingZeros(double d) {
        return String.valueOf(d).replaceAll("[0]*$", "").replaceAll("\\.$", "");
    }

    public static void main(String[] args) {

        /*
        * Trying it out
        * */
        ArrayList<Instance> instances = new Instantiator().getInstances(INPUT_FILE);
        Instance testInstance = instances.get(2);
        ArrayList<Robot> robots = testInstance.getRobots();
        System.out.println("--------------------Robot Coordinates--------------------");
        for (Robot r: robots) {
            System.out.print(r.getPosition().getX());
            System.out.print(" ");
            System.out.print(r.getPosition().getY());
            System.out.println();
        }
        if ((testInstance.getObstacles()).isEmpty()) {
            System.out.println("No obstacles in instance");
        } else {
            ArrayList<Obstacle> obstacles = testInstance.getObstacles();
            System.out.println("--------------------Obstacle Coordinates--------------------");
            for (int i = 0; i < obstacles.size(); i++) {
                ArrayList<Point2D> coors = obstacles.get(i).getCoordinates();
                System.out.println("--------------------Obstacle " + String.valueOf(i+1) + "--------------------");
                for (Point2D c: coors) {
                    System.out.print(c.getX());
                    System.out.print(" ");
                    System.out.print(c.getY());
                    System.out.println();
                }

            }
        }

        Graph g = new Graph(testInstance);
        if (g.getNodes().isEmpty()) {
            System.out.println("empty graph");
        } else {
            for (Node n: g.getNodes()) {
                System.out.println(n.getCoordinates());
            }
        }
        ArrayList<Point2D[]> acp = g.getAllCoordinatePairs();
        for (Point2D[] arr: acp) {
            System.out.print(arr[0] + "------" + arr[1]);
            System.out.println();
        }

//        // Able to compare Point2D type by its values
//        Point2D pd = new Point2D.Double(5.5,5);
//        Point2D pdd = new Point2D.Double(5,5);
//        if (pd.equals(pdd)) {
//            System.out.println("Point2Ds are equal");
//        } else {
//            System.out.println("Point2Ds are NOT equal");
//        }

//        // L-shape line considered intersect
//        System.out.println(Line2D.linesIntersect(0,10,0,0,0,0,10,0));

        g.addRobotNodes(new Point2D.Double(1.1,1.0), new Point2D.Double(1.51,1.51));
        Node nn = g.getNodes().get(0);
        System.out.println("host node is: " + nn.getCoordinates());
        ArrayList<Edge> edges = nn.getEdges();
        for (Edge e: edges) {
            System.out.println(e.getEnd().getCoordinates());
            System.out.println(e.getWeight());
        }
        g.removeRobotNodes();
        g.addRobotNodes(new Point2D.Double(1.9,1.9), new Point2D.Double(1.5,1.5));
        Node nnn = g.getNodes().get(0);
        System.out.println("host node is: " + nnn.getCoordinates());
        ArrayList<Edge> edges2 = nnn.getEdges();
        for (Edge e: edges2) {
            System.out.println(e.getEnd().getCoordinates());
            System.out.println(e.getWeight());
        }
        Node nnnn = g.getNodes().get(2);
        System.out.println("host node is: " + nnnn.getCoordinates());
        ArrayList<Edge> edges3 = nnnn.getEdges();
        for (Edge e: edges3) {
            System.out.println(e.getEnd().getCoordinates());
            System.out.println(e.getWeight());
        }
        System.out.println("--------------------");
        ArrayList<Node> yoyo = g.getNodes();
        for (Node y: yoyo) {
            System.out.println(y.getCoordinates());
        }
        System.out.println("--------------------");
        g.removeRobotNodes();



    }
}
