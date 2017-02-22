/**
 * Created by Raymond on 22/2/2017.
 */

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * This class generates strings for generating visibility graphs.
 * Results are sent to stdout.
 */
public class VisGraphGenerator {
    public static void generate(Instance instance) {
        // Prepare robot coords for visgraph
        List<String> robotListArr = new ArrayList<>();
        for (int i = 0; i < instance.getRobots().size(); i++) {
            Robot r = instance.getRobots().get(i);
            robotListArr.add("vg.Point(" + r.getPosition().getX() + "," + r.getPosition().getY() + "," + i + ")");
        }
        String robotStr = "robots = [" + String.join(", ", robotListArr) + "]";

        // Prepare obstacle coords for visgraph
        List<String> obstacleListArr = new ArrayList<>();

        ArrayList<Obstacle> obstacles = instance.getObstacles();
        for (int i = 0; i < obstacles.size(); i++) {
            List<String> obstacleStrArr = new ArrayList<>();

            ArrayList<Point2D> coors = obstacles.get(i).getCoordinates();
            for (int j = 0; j < coors.size(); j++) {
                Point2D c = coors.get(j);
                obstacleStrArr.add("vg.Point(" + c.getX() + "," + c.getY() + "," + i + "," + j + ")");
            }

            String out = String.join(", ", obstacleStrArr);
            obstacleListArr.add("[" + out + "]");
        }
        String out = String.join(", ", obstacleListArr);


        String finalStr = "polys = [" + out + "]";

        System.out.println("==== VISGRAPH GENERATOR START ====");
        System.out.println("Number of robots: " + instance.getRobots().size());
        System.out.println("Number of obstacles: " + obstacles.size());
        System.out.println("Result:");
        System.out.println(robotStr);
        System.out.println(finalStr);
        System.out.println("==== VISGRAPH GENERATOR END ====");
    }
}
