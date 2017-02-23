import java.awt.geom.Point2D;

/**
 * Created by lmy60 on 21/2/2017.
 */
public class GreedyRobot extends Robot {
    public Double distanceAcquiredSinceLastJump;
    public Double remainingDistanceToClosestTarget;
    public int closest;
    public int currentPositionIndex;    // This holds the 'current' position (during the algorithm) of the robot. Represents which position (of another bot) it has taken.

    public GreedyRobot(Point2D position) {
       super(position);
    }
}
