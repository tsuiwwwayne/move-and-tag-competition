import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    final static String USERNAME = "griffin";
    final static String PASSWORD = "usrdoupjm8q94i7td9qctue39k";
    final static String INPUT_FILE = "robots.mat.txt";

    // Fill in the problem number to attempt here.
    static int INSTANCE_NUMBER = 1;

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
    }
}
