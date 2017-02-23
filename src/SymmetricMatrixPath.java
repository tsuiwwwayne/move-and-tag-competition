import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Raymond on 22/2/2017.
 */

/**
 * Holds paths. Indexed by ArrayList<Point2D> = matrix[a][b], which is a list of waypoints
 */
public class SymmetricMatrixPath {

    // A triangular 3D matrix to hold the data.
    // 1: Robot A
    // 2: Robot B
    // 3: ArrayList<Point2D> (This is the List of waypoints, in sequence)
    private ArrayList<ArrayList<ArrayList<Point2D>>> matrix;

    /**
     * Creates an n-by-n symmetric matrix with an empty path.
     */
    public SymmetricMatrixPath(int n) {
        matrix = new ArrayList<>(n);
        // Create columns
        for (int i=0; i<n; i++) {
            matrix.add(i, new ArrayList<>(n));

            // Create the path array
            for (int j=0; j<n; j++) {
                matrix.get(i).add(null);
            }
        }
    }

    public void set( int row, int col, ArrayList<Point2D> path) {
        if (row < col) {
            matrix.get(row).set(col, path);
        } else {
            matrix.get(col).set(row, path);
        }
    }

    /**
     * Returns the path at position (row,col).
     */
    public ArrayList<Point2D> get( int row, int col ) {
        return matrix.get(row).get(col);
    }

    /**
     * Returns the path at position (row,col).
     * Returns null if not found.
     */
    public ArrayList<Point2D> getPath( int row, int col ) {
        ArrayList<Point2D> path;

        if (row < col) {
            path = matrix.get(row).get(col);
            return path;
        } else {
            path = matrix.get(col).get(row);
            if (path == null) return null;
            // Shallow clone array and reverse the path.
            ArrayList<Point2D> reversePath = (ArrayList<Point2D>) path.clone();
            Collections.reverse(reversePath);
            return reversePath;
        }
    }
}
