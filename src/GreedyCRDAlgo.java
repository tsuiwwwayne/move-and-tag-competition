import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Raymond on 23/2/2017.
 */
public class GreedyCRDAlgo {
    /*
    * Greedy Heuristic Algorithm
    * Incorporates the following:
    * - Claim
    * - Refresh
    * - Delayed-target
    * */

    public static Map<Integer, List<Integer>> doAlgo(List<Robot> robotList, Graph pathFinder) {
        ArrayList<GreedyRobot> robots = new ArrayList<>();

        for (int i = 0; i < robotList.size(); i++) {
            Robot r = robotList.get(i);
            double x = r.getPosition().getX();
            double y = r.getPosition().getY();
            Point2D point = new Point2D.Double(x,y);
            GreedyRobot greedyRobot = new GreedyRobot(point);
            greedyRobot.distanceAcquiredSinceLastJump = 0.0;
            greedyRobot.remainingDistanceToClosestTarget = Double.MAX_VALUE;
            greedyRobot.closest = -1;
            greedyRobot.status = r.status;
            greedyRobot.currentPositionIndex = i;
            robots.add(greedyRobot);
        }

        // Initialize movement schedule
        Map<Integer, List<Integer>> movementSchedule = new HashMap<>();
        for (int i=0; i<robots.size(); i++) {
            movementSchedule.put(i, new ArrayList<Integer>());
        }

        // ---- Manual Optimisation Counter ----
        int[] manualOptimizationList = new int[5];

        // Populate target list
        int awokenRobots = 1;
        System.out.println("Initialising robot 1 to be awake...");
        for (int x = 1; x < robots.size(); x++) {
            // Calculate distance to nearest robot for all awake robots
            for(int i = 0; i < robots.size(); i++){
                if(robots.get(i).status == Robot.Status.AWAKE){
                    robots.get(i).closest = -1;
                    robots.get(i).remainingDistanceToClosestTarget = Double.MAX_VALUE;

                    // Count distance of all sleeping robots from the current awake robot
                    for(int j = 0; j < robots.size(); j++){
                        if (robots.get(j).status == Robot.Status.ASLEEP) {
                            double d = pathFinder.getDistance(robots.get(i).currentPositionIndex, robots.get(j).currentPositionIndex) - robots.get(i).distanceAcquiredSinceLastJump;
                            // Only update dist if its shorter path
                            if(d < robots.get(i).remainingDistanceToClosestTarget){
                                robots.get(i).remainingDistanceToClosestTarget = d;
                                robots.get(i).closest = j; // We have a new closest robot!
                                // TODO - should we check if any other robots have earlier 'claimed' this robot (and unset it?)
                            }
                        }
                    }
                }
            }

            // Find the robot we should move for this iteration.
            // (This is the robot which will reach its target first.)
            int nextRobotToReachTarget = -1;
            double d = Double.MAX_VALUE;
            for(int i = 0; i < robots.size(); i++){
                if(robots.get(i).status == Robot.Status.AWAKE && robots.get(i).remainingDistanceToClosestTarget < d){
                    // Delayed target choice:
                    // Avoid committing to the direction a robot is heading until that robot has travelled far enough
                    // to awaken its target, at which point its target is fully determined.
                    d = robots.get(i).remainingDistanceToClosestTarget;
                    nextRobotToReachTarget = i;
                }
            }
            if(d == Double.MAX_VALUE){break;}   // No more robots need to move, we're done.

            // Update distance for all awake robots
            for (int i = 0; i<robots.size(); i++) {
                if (robots.get(i).status == Robot.Status.AWAKE) {
                    robots.get(i).distanceAcquiredSinceLastJump += d;

                    // ==== Manual Optimisation Section ====
                    // (for action on all robots)
                    /*if (i == 0) {

                    }*/
                }
            }

            // ==== Manual Optimisation Section ====
            /*if (nextRobotToReachTarget == 0) {
                System.out.println("robot encountered");
                switch (manualOptimizationList[0]++) {
                    case 0:
                        robots.get(nextRobotToReachTarget).closest = 191;
                        System.out.println("MADE CHANGE");
                        break;
                    *//*case 1:
                        robots.get(nextRobotToReachTarget).closest = 198;
                        System.out.println("MADE CHANGE 2");
                        break;*//*
                }
            }*/
            /*if (nextRobotToReachTarget == 199) {
                switch (manualOptimizationList[1]++) {
                    case 0:
                        robots.get(nextRobotToReachTarget).closest = 157;
                        System.out.println("MADE CHANGE");
                        break;
                }
            }*/
            /*if (nextRobotToReachTarget == 138) {
                switch (manualOptimizationList[2]++) {
                    case 0:
                        robots.get(nextRobotToReachTarget).closest = 127;
                        break;
                }
            }*/

            // Set the location of the robot that moved.
            // Do this by setting the index of the robot that it moved to.
            robots.get(nextRobotToReachTarget).distanceAcquiredSinceLastJump = 0.0;
            robots.get(nextRobotToReachTarget).currentPositionIndex = robots.get(robots.get(nextRobotToReachTarget).closest).currentPositionIndex;

            // Add robot to movement schedule
            movementSchedule.get(nextRobotToReachTarget).add(robots.get(nextRobotToReachTarget).closest);

            // update newly awakened robot
            robots.get(robots.get(nextRobotToReachTarget).closest).status = Robot.Status.AWAKE;

            awokenRobots++;
            System.out.println("Number of robots awoken: " + awokenRobots + " out of " + robots.size() + " robots.");

            // A robot has been awakened - the next iteration will check whether we have a new closest robot to robots that have been claimed but not reached.
            // If there's a new closest robot, reset the original robot to its last position (not necessarily the start!),
            // Unset its 'closest' and 'remainingDistanceToClosestTarget' parameters,
            // Then set the above parameters for the new closest robot.
        }
        return movementSchedule;
    }
}
