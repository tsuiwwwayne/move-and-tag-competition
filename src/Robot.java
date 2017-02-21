import java.awt.geom.Point2D;

/**
 * Created by waynetsui on 20/2/17.
 */
public class Robot {

    public enum Status {
        AWAKE, ASLEEP, STOPPED
    }

    private Point2D position;
    public Status status;

    public Robot(Point2D position) {
        this.position = position;
        this.status = Status.ASLEEP;
    }

    public Point2D getPosition() {
        return position;
    }

}
