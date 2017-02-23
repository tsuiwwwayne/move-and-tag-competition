import java.awt.geom.Point2D;

/**
 * Created by lmy60 on 21/2/2017.
 */
public class GreedyRobot extends Robot {
    public Double distanceAcquiredSinceLastJump;
    public Double remainingDistanceToClosestTarget;
    public int closest;

    public GreedyRobot(Point2D position) {
       super(position);
    }
}
