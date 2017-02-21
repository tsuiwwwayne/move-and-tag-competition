import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by waynetsui on 21/2/17.
 */
public class Instantiator {

    public Instantiator() {
    }

    public ArrayList<Instance> getInstances(String fileName) {
        /*
        * Read file and store each line in an ArrayList
        * */
        ArrayList<Instance> instances = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            try {
                String line = br.readLine();
                while (line != null) {
                    Instance i = getInstance(line);
                    instances.add(i);
                    line = br.readLine();
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return instances;
    }

    private Instance getInstance(String line) {

        ArrayList<Robot> r = new ArrayList<>();
        ArrayList<Obstacle> o = new ArrayList<>();

        // Remove instance numbering (eg. "1:", "12:")
        line = line.substring(line.lastIndexOf(':')+1).trim();

        // Split into robots section and obstacles section
        String[] sections = line.split("#");
        if (sections.length == 1) { // No obstacles
            String s = sections[0];
            String[] parts = s.split("(?<=\\))(,\\s*)(?=\\()");
            for (String part : parts) {
                part = part.substring(1, part.length() - 1); // Get rid of parentheses.
                String[] coordinates = part.split(",\\s*");
                Point2D p = new Point2D.Double(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]));
                r.add(new Robot(p));
            }
        } else if (sections.length == 2) { // Robots and Obstacles
            String s = sections[0];
            String[] parts = s.split("(?<=\\))(,\\s*)(?=\\()");
            for (String part : parts) {
                part = part.substring(1, part.length() - 1); // Get rid of parentheses.
                String[] coordinates = part.split(",\\s*");
                Point2D p = new Point2D.Double(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]));
                r.add(new Robot(p));
            }
            String[] obs = sections[1].split(";");
            for (String ob: obs) {
                ArrayList<Point2D> point2Ds = new ArrayList<>();
                String[] obsParts = ob.split("(?<=\\))(,\\s*)(?=\\()");
                for (String obsPart : obsParts) {
                    obsPart = obsPart.substring(1, obsPart.length() - 1); // Get rid of parentheses.
                    String[] coordinates = obsPart.split(",\\s*");
                    Point2D p = new Point2D.Double(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]));
                    point2Ds.add(p);
                }
                o.add(new Obstacle(point2Ds));
            }
        } else {
            System.out.println("Unable to split instance into robots and obstacles!");
        }

        return new Instance(r, o);
    }

}
