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
        Instance testInstance = instances.get(4);
        ArrayList<Robot> robots = testInstance.getRobots();
        Graph g = new Graph(testInstance);
        ArrayList<Point2D> path = g.getPath(robots.get(0), robots.get(1));
        for (Point2D p: path) {
            System.out.print("VISITED NODES IN ORDER: " + p);
        }
        System.out.println();
        double distance = g.getDistance(robots.get(0), robots.get(1));
        System.out.println("DISTANCE:  " + distance);

    }
}
