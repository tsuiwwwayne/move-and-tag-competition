import java.awt.geom.Point2D;

/**
 * Created by waynetsui on 20/2/17.
 */
public class Robot {

    private Point2D position;

    public Robot(Point2D position) {
        this.position = position;
    }

    public Point2D getPosition() {
        return position;
    }

}
