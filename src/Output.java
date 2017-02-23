import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Raymond on 21/2/2017.
 */
public class Output {
    public static String generateOutputString(Graph g, ArrayList<Robot> robots, ArrayList<Obstacle> obstacles ,Map<Integer, List<Integer>> movementSchedule) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<robots.size(); i++) {
            //Get array list in Map
            List<Integer> visitList = movementSchedule.get(i);

            if (visitList != null && visitList.size() > 0) {
                if (i > 0) sb.append("; ");
                Robot bot = robots.get(i); // Moving bot
                sb.append(getCoords(bot.getPosition().getX(), bot.getPosition().getY()));// append moving robot
                sb.append(", ");
                ArrayList<Point2D> obsPts = g.getPath(i,visitList.get(0));
                String obsPath = coordsToString(obsPts);// generate path string
                //System.out.println("Obstacles from robot " + i + " and " + visitList.get(0) + " are " + obsPts);
                sb.append(obsPath);
                if(visitList.size() == 1){
                    //System.out.println("one");
                    Robot bot2 = robots.get(visitList.get(0));
                    sb.append(getCoords(bot2.getPosition().getX(), bot2.getPosition().getY()));
                }else{
                    for (int j = 0; j < visitList.size()-1; j++) {
                        // getPath(j,j+1) and convert them to string
                        Robot bot2;
                        if(j < 1){
                            bot2 = robots.get(visitList.get(j));
                            sb.append(getCoords(bot2.getPosition().getX(), bot2.getPosition().getY()));
                            sb.append(", ");
                        }
                        obsPts = g.getPath(visitList.get(j),visitList.get(j+1));
                        //System.out.println("Obstacles from robot " + visitList.get(j) + " and " + visitList.get(j+1) + " are " + obsPts);
                        obsPath = coordsToString(obsPts);
                        sb.append(obsPath);
                        bot2 = robots.get(visitList.get(j+1));
                        sb.append(getCoords(bot2.getPosition().getX(), bot2.getPosition().getY()));
                        // append commas for the next iterations
                        if(j+1 < (visitList.size()-1)){
                            sb.append(", ");
                        }
                    }
                }
            }
        }
        return sb.toString();
    }


    public static String getCoords(double x, double y) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(x);
        sb.append(", ");
        sb.append(y);
        sb.append(")");
        return sb.toString();
    }

    public static String coordsToString(ArrayList<Point2D> coords){
        StringBuilder sb = new StringBuilder();
        for(Point2D p : coords){
            String s = getCoords(p.getX(),p.getY());
            sb.append(s);
            sb.append(", ");
        }
        return sb.toString();
    }
}
