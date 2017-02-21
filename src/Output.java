import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Raymond on 21/2/2017.
 */
public class Output {
    public static String generateOutputString(ArrayList<Robot> robots, Map<Integer, List<Integer>> movementSchedule) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<movementSchedule.size(); i++) {

            List<Integer> visitList = movementSchedule.get(i);

            if (visitList.size() > 0) {
                if (i > 0) sb.append("; ");
                Robot bot = robots.get(i);
                sb.append(getCoords(bot.getInitialPosition().getX(), bot.getInitialPosition().getY()));

                for (int j = 0; j < visitList.size(); j++) {
                    sb.append(", ");
                    Robot bot2 = robots.get(visitList.get(j));
                    sb.append(getCoords(bot2.getInitialPosition().getX(), bot2.getInitialPosition().getY()));
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
}
