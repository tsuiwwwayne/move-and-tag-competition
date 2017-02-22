import java.util.ArrayList;

public class Main {

    final static String USERNAME = "griffin";
    final static String PASSWORD = "usrdoupjm8q94i7td9qctue39k";
    final static String INPUT_FILE = "robots.mat.txt";

    // Fill in the problem number to attempt here.
    final static int INSTANCE_NUMBER = 1;

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
    }
}
