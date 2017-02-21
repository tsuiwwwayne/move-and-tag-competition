import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by waynetsui on 20/2/17.
 */
public class Obstacle {

    private ArrayList<Point2D> coordinates;

    public Obstacle(ArrayList<Point2D> coordinates) {
        this.coordinates = coordinates;
    }

    public ArrayList<Point2D> getCoordinates() {
        return coordinates;
    }

}
