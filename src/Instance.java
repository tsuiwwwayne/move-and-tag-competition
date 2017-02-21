import java.util.ArrayList;

/**
 * Created by waynetsui on 20/2/17.
 */
public class Instance {

    private ArrayList<Robot> robots;
    private ArrayList<Obstacle> obstacles;

    public Instance(ArrayList<Robot> robots, ArrayList<Obstacle> obstacles) {
        this.robots = robots;
        this.obstacles = obstacles;
    }

    public ArrayList<Robot> getRobots() {
        return robots;
    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

}
