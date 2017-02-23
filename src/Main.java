import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Main {

    final static String USERNAME = "griffin";
    final static String PASSWORD = "usrdoupjm8q94i7td9qctue39k";
    final static String INPUT_FILE = "robots.mat.txt";

    // Fill in the problem number to attempt here.
    final static int INSTANCE_NUMBER = 2;

    public static void main(String[] args) {
        ArrayList<Instance> instances = new Instantiator().getInstances(INPUT_FILE);

        Instance instance = instances.get(INSTANCE_NUMBER - 1);

        ArrayList<Robot> robots = instance.getRobots();
        ArrayList<Obstacle> obstacles = instance.getObstacles();

        // Create PathFinder
        PathFinder pf = new PathFinder(INSTANCE_NUMBER, robots, obstacles);

        // Wake up the first robot.
        instance.getRobots().get(0).status = Robot.Status.AWAKE;

        // Do algorithm
        // TODO - Place the call to do the algorithm here!

        // --- Do other debug things here ---
        // Generate visibility graph (comment out unless required)
        //VisGraphGenerator.generate(instance);

        // Wayne's Testing Corner;
        Graph g = new Graph(instance);
        ArrayList<Point2D> path = g.getPath(0, 1);
        for (Point2D p: path) {
            System.out.print("VISITED NODES IN ORDER: " + p);
        }
        System.out.println();
        double distance = g.getDistance(0, 1);
        System.out.println("DISTANCE:  " + distance);
    }
}
