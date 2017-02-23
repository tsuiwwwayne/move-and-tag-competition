import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    final static String USERNAME = "griffin";
    final static String PASSWORD = "usrdoupjm8q94i7td9qctue39k";
    final static String INPUT_FILE = "robots.mat.txt";

    // Fill in the problem number to attempt here.
    static int INSTANCE_NUMBER = 8;

    public static void main(String[] args) {
        if (args.length > 0) {
            // If present, use the first argument as the instance number to solve.
            INSTANCE_NUMBER = Integer.parseInt(args[0]);
        }

        ArrayList<Instance> instances = new Instantiator().getInstances(INPUT_FILE);
        Instance instance = instances.get(INSTANCE_NUMBER - 1);

        ArrayList<Robot> robots = instance.getRobots();
        ArrayList<Obstacle> obstacles = instance.getObstacles();

        // ----------------
        // START
        // ----------------
        System.out.println("==== START ====");
        System.out.println("Instance number: " + INSTANCE_NUMBER);

        // ----------------
        // PATHFINDING
        // ----------------
        //PathFinder pf = new PathFinder(INSTANCE_NUMBER, robots, obstacles);
        Graph g = new Graph(instance);

        // Wake up the first robot.
        robots.get(0).status = Robot.Status.AWAKE;

        // ----------------
        // ALGORITHM
        // ----------------
        Map<Integer, List<Integer>> movementSchedule = GreedyCRDAlgo.doAlgo(robots, g);

        // ----------------
        // OUTPUT
        // ----------------
        System.out.println("==== RESULT ====");
        String output = INSTANCE_NUMBER + ":" + Output.generateOutputString(g, robots, obstacles, movementSchedule);
        System.out.println(output);

        // ----------------
        // DEBUG METHODS
        // ----------------
        // Generate visibility graph (comment out unless required)
        //VisGraphGenerator.generate(instance);

        // ----------------
        // Wayne's Testing Corner
        // ----------------
//        Graph g = new Graph(instance);
//        ArrayList<Point2D> path = g.getPath(0, 1);
//        for (Point2D p: path) {
//            System.out.print("VISITED NODES IN ORDER: " + p);
//        }
//        System.out.println();
//        double distance = g.getDistance(0, 1);
//        System.out.println("DISTANCE:  " + distance);

        // Check Nodes that have line of sight via Coordinates
//        int index = g.getNodeIndexViaDoubles(9.693092833894978, -2.1132276868488913);
//        int index = g.getNodeIndexViaDoubles(11.160112254556559, 4.731321532741966);
//        int index = g.getNodeIndexViaDoubles(0.0, 0.0);
//        Node n = g.getNodes().get(index);
//        System.out.println("host node is: " + n.getCoordinates());
//        ArrayList<Edge> edges3 = n.getEdges();
//        for (Edge e: edges3) {
//        System.out.println(e.getEnd().getCoordinates());
//        System.out.println(e.getWeight());
//        }
    }
}
