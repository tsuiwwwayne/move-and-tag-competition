import java.awt.geom.Point2D;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raymond on 22/2/2017.
 */
public class PathFinder {
    public static final String FILE_PREFIX = "./pathfinding/path_";

    private SymmetricMatrixPath paths;
    private List<Robot> robots;

    public PathFinder(int instanceNumber, List<Robot> robots, List<Obstacle> obstacles) {
        this.robots = robots;

        System.out.println("Pathfinder started for instance: " + instanceNumber);
        // Create new symmetric matrix of paths
        paths = new SymmetricMatrixPath(robots.size());
        String filepath = FILE_PREFIX + instanceNumber + ".txt";

        try {
            // Load the file and parse it
            List<String> file = Files.readAllLines(Paths.get(filepath), StandardCharsets.UTF_8);

            for (int i=0; i<file.size(); i++) {
                String[] line = file.get(i).split("#");
                String[] s_fromTo = line[0].split(",");

                int from = Integer.parseInt(s_fromTo[0]);
                int to = Integer.parseInt(s_fromTo[1]);

                String[] s_obstacles = line[1].split("\\|");

                for (int j=0; j<s_obstacles.length; j++) {
                    String[] s_obstacleVertexIndexes = s_obstacles[j].split(":");
                    int obstacleIdx = Integer.parseInt(s_obstacleVertexIndexes[0]);
                    int vertexIdx = Integer.parseInt(s_obstacleVertexIndexes[1]);

                    paths.get(from, to).add(obstacles.get(obstacleIdx).getCoordinates().get(vertexIdx));
                }

                //System.out.println("Pathfind parse complete.");
            }
        } catch (IOException e) {
            System.out.println("ERROR: Pathfinder: Couldn't open file " + filepath);
            //e.printStackTrace();
        }
    }

    public ArrayList<Point2D> getPath(int robotA, int robotB) {
        return paths.getPath(robotA, robotB);
    }

    public double getDistance(int robotA, int robotB) {
        // Shallow clone array so we don't modify it.
        ArrayList<Point2D> path = (ArrayList<Point2D>) paths.getPath(robotA, robotB).clone();

        // Add robot A and B's coords to it.
        path.add(0, robots.get(robotA).getPosition());
        path.add(robots.get(robotB).getPosition());

        double dist = 0;

        for (int i=0; i<path.size()-1; i++) {
            dist += getLinearDistance(path.get(i), path.get(i+1));
        }

        return dist;
    }

    private static double getLinearDistance(Point2D a, Point2D b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return Math.sqrt(dx*dx + dy*dy);
    }
}
