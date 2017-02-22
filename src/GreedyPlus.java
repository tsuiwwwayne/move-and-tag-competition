import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lmy60 on 21/2/2017.
 */
public class GreedyPlus {
    public Double getLineDist(Double x1, Double x2, Double y1, Double y2, Double distanceSinceLastJump){
        // Distance
        Double d = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1)) - distanceSinceLastJump;
        return d;
    }

    // Creates copies of sequences to store in Map
    public List<Integer> getCopyOfList(List<Integer> original){
        List<Integer> copy = new ArrayList<Integer>();
        for(Integer i : original){
            copy.add(i);
        }
        return copy;
    }

    // Main Algorithm
    public Map<Integer, List<Integer>> greedyPlus(ArrayList<Robot> robot){
        ArrayList<GreedyRobot> robots = new ArrayList<GreedyRobot>();
        // Make a copy of robots
        for(Robot r : robot){
            Double x = r.getPosition().getX();
            Double y = r.getPosition().getY();
            Point2D point = new Point2D.Double(x,y);
            GreedyRobot greedyRobot = new GreedyRobot(point);
            greedyRobot.distanceAcquiredSinceLastJump = 0.0;
            greedyRobot.remainingDistanceToClosestTarget = 10000.0;
            greedyRobot.closest = -1;
            robots.add(greedyRobot);
        }

//        System.out.println("------------BEFORE UPDATING-------------");
//        for(GreedyRobot r : robots){
//            System.out.println("X: "+r.getPosition().getX() + " Y: " + r.getPosition().getY());
//        }

        // First robot awake
        robots.get(0).status = Robot.Status.AWAKE;

        // Initialize target list
        ArrayList<Target> targets = new ArrayList<Target>();
        targets.add(new Target(0,-1));

        // Populate target list
        for(int x = 1; x < robots.size(); x++){

            // Calculate distance to nearest robot for all awake robots
            for(int i = 0; i < robots.size(); i++){
                if(robots.get(i).status == Robot.Status.AWAKE){
                    robots.get(i).closest = -1;
                    robots.get(i).remainingDistanceToClosestTarget = 10000.0;

                    // Count distance of all sleeping robots from the current awake robot
                    for(int j = 0; j < robots.size(); j++){
                        if(robots.get(j).status == Robot.Status.ASLEEP){
                            Double x1 = robots.get(j).getPosition().getX();
                            Double y1 = robots.get(j).getPosition().getY();
                            Double x2 = robots.get(i).getPosition().getX();
                            Double y2 = robots.get(i).getPosition().getY();
                            Double d = getLineDist(x1,x2,y1,y2,robots.get(i).distanceAcquiredSinceLastJump);
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
            Double d = 100000.0;
            for(int i = 0; i < robots.size(); i++){
                if(robots.get(i).status == Robot.Status.AWAKE && robots.get(i).remainingDistanceToClosestTarget < d){
                    d = robots.get(i).remainingDistanceToClosestTarget;
                    nextRobotToReachTarget = i;
                }
            }
            if(d == 100000.0){break;}

            // update distance for all awake robots
            for (int i = 0; i<robots.size(); i++) {
                if (robots.get(i).status == Robot.Status.AWAKE) {
                    robots.get(i).distanceAcquiredSinceLastJump += d;
                }
            }

            //reset robot that made the jump
            robots.get(nextRobotToReachTarget).distanceAcquiredSinceLastJump = 0.0;
            Double new_X = robots.get(robots.get(nextRobotToReachTarget).closest).getPosition().getX();
            Double new_Y = robots.get(robots.get(nextRobotToReachTarget).closest).getPosition().getY();
            robots.get(nextRobotToReachTarget).getPosition().setLocation(new_X,new_Y);

            //add the two robots to the target list in numerical order
            if (robots.get(nextRobotToReachTarget).closest < nextRobotToReachTarget) {
                targets.add(new Target(robots.get(nextRobotToReachTarget).closest,-1));
                targets.add(new Target(nextRobotToReachTarget,-1));
            }
            else {
                targets.add(new Target(nextRobotToReachTarget,-1));
                targets.add(new Target(robots.get(nextRobotToReachTarget).closest,-1));
            }

            // update newly awakened robot
            robots.get(robots.get(nextRobotToReachTarget).closest).status = Robot.Status.AWAKE;

            //update the target list
            boolean updated = false;
            for (int i = 0; i < targets.size(); i++) {
                if (targets.get(i).getX() == nextRobotToReachTarget && targets.get(i).getY() == -1 && !updated) {
                    targets.get(i).setY(robots.get(nextRobotToReachTarget).closest);
                    updated = true;
                }
            }
        }

        ArrayList<Integer> s = new ArrayList<>();
        for (int i = 0; i < targets.size(); i++) {
            s.add(targets.get(i).getY());
        }

        Map<Integer, List<Integer>> movementSchedule = new HashMap<>();

        // Start from robot zero
        int currentMovingRobot = 0;
        int currentRobot = 0;
        List<Integer> seq = new ArrayList<Integer>();

        // Based on seq array create a movement schedule
        for(int i = 0; i < targets.size(); i++){
            // Ignore negative values
            if(s.get(i) < 0){continue;}
            // Add to seq if asc order
            if(currentRobot < s.get(i)) {
                currentRobot = s.get(i);
                seq.add(s.get(i));
                //System.out.println("Robot: " + currentMovingRobot + " goes to " + seq);
                List<Integer> paths = getCopyOfList(seq);
                movementSchedule.put(currentMovingRobot, paths);
            }else{
                // The next moving robot is the first robot of the seq
                currentRobot = s.get(i);
                currentMovingRobot = seq.get(0);
                //System.out.println("Clear and Reset");
                seq.clear();
                seq.add(s.get(i));
                //System.out.println("Robot: " + currentMovingRobot + " goes to " + seq);
                List<Integer> paths = getCopyOfList(seq);
                movementSchedule.put(currentMovingRobot, paths);
            }
        }

        System.out.println("Printing Map");
        for (Integer key : movementSchedule.keySet()) {
            System.out.println("Robot: " + key + " goes to " + movementSchedule.get(key));
        }

//        System.out.println("------------AFTER UPDATING-------------");
//
//        for(GreedyRobot r : robots){
//            System.out.println("X: "+r.getPosition().getX() + " Y: " + r.getPosition().getY());
//        }

        for(Integer i : s){
            System.out.print(i + " ");
        }

        return movementSchedule;
    }
}
