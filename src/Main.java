import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Instance testInstance = instances.get(7);
        ArrayList<Robot> robots = testInstance.getRobots();
        ArrayList<Obstacle> obstacles = testInstance.getObstacles();
        PathFinder pathFinder = new PathFinder(8,robots,obstacles);
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

        System.out.println("----------------Output-------------------");

        Map<Integer, List<Integer>> movementSchedule = new HashMap<>();
        GreedyPlus gp = new GreedyPlus();
        movementSchedule = gp.greedyPlus(robots,pathFinder);
        Output output = new Output();
        obstacles = testInstance.getObstacles();
        String output_str = output.generateOutputString(pathFinder,robots,obstacles,movementSchedule);
        System.out.println(output_str);

    }
}
