import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Raymond on 23/2/2017.
 */
public class GreedyCRDAlgo {
    public static Map<Integer, List<Integer>> doAlgo(List<Robot> robotList, Graph pathFinder) {
        ArrayList<GreedyRobot> robots = new ArrayList<>();
        // Make a copy of robots
        for (Robot r : robotList){
            double x = r.getPosition().getX();
            double y = r.getPosition().getY();
            Point2D point = new Point2D.Double(x,y);
            GreedyRobot greedyRobot = new GreedyRobot(point);
            greedyRobot.distanceAcquiredSinceLastJump = 0.0;
            greedyRobot.remainingDistanceToClosestTarget = Double.MAX_VALUE;
            greedyRobot.closest = -1;
            greedyRobot.status = r.status;
            robots.add(greedyRobot);
        }

        // Initialize movement schedule
        Map<Integer, List<Integer>> movementSchedule = new HashMap<>();
        for (int i=0; i<robots.size(); i++) {
            movementSchedule.put(i, new ArrayList<Integer>());
        }

        // Populate target list
        for (int x = 1; x < robots.size(); x++) {
            // Calculate distance to nearest robot for all awake robots
            for(int i = 0; i < robots.size(); i++){
                if(robots.get(i).status == Robot.Status.AWAKE){
                    robots.get(i).closest = -1;
                    robots.get(i).remainingDistanceToClosestTarget = Double.MAX_VALUE;

                    // Count distance of all sleeping robots from the current awake robot
                    for(int j = 0; j < robots.size(); j++){
                        if (robots.get(j).status == Robot.Status.ASLEEP) {
                            double d = pathFinder.getDistance(i, j) - robots.get(i).distanceAcquiredSinceLastJump;
                            // Only update dist if its shorter path
                            if(d < robots.get(i).remainingDistanceToClosestTarget){
                                robots.get(i).remainingDistanceToClosestTarget = d;
                                robots.get(i).closest = j; // The target robot
                            }
                        }
                    }
                }
            }

            // This is the robot which will reach its target first
            int nextRobotToReachTarget = -1;
            double d = Double.MAX_VALUE;
            for(int i = 0; i < robots.size(); i++){
                if(robots.get(i).status == Robot.Status.AWAKE && robots.get(i).remainingDistanceToClosestTarget < d){
                    d = robots.get(i).remainingDistanceToClosestTarget;
                    nextRobotToReachTarget = i;
                }
            }
            if(d == Double.MAX_VALUE){break;}

            // update distance for all awake robots
            for (int i = 0; i<robots.size(); i++) {
                if (robots.get(i).status == Robot.Status.AWAKE) {
                    robots.get(i).distanceAcquiredSinceLastJump += d;
                }
            }

            //reset robot that made the jump
            robots.get(nextRobotToReachTarget).distanceAcquiredSinceLastJump = 0.0;
            double new_X = robots.get(robots.get(nextRobotToReachTarget).closest).getPosition().getX();
            double new_Y = robots.get(robots.get(nextRobotToReachTarget).closest).getPosition().getY();
            robots.get(nextRobotToReachTarget).getPosition().setLocation(new_X, new_Y);

            // Add robot to movement schedule
            movementSchedule.get(nextRobotToReachTarget).add(robots.get(nextRobotToReachTarget).closest);

            // update newly awakened robot
            robots.get(robots.get(nextRobotToReachTarget).closest).status = Robot.Status.AWAKE;
        }
        return movementSchedule;
    }
}
