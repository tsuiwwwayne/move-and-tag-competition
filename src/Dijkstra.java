/**
 * Created by Raymond on 22/2/2017.
 */
import java.awt.geom.Point2D;
import java.util.*;

/**
 * Algorithm is derived from Wikipedia section 'Using a priority queue'.
 * This implementation finds the single path from a source to all reachable vertices.
 * Building the graph from a set of edges takes O(E log V) for each pass.
 * Vertices are stored in a TreeSet (self-balancing binary search tree) instead of a PriorityQueue (a binary heap)
 * in order to get O(log n) performance for removal of any element, not just the head.
 * Decreasing the distance of a vertex is accomplished by removing it from the tree and later re-inserting it.
 *
 * Edge cost modified to be double-precision type instead of integer for match-and-tag problem.
 */
public class Dijkstra {
//    private static final Graph_Dijkstra.Edge[] GRAPH = {
//            new Graph_Dijkstra.Edge("a", "b", 7),
//            new Graph_Dijkstra.Edge("a", "c", 9),
//            new Graph_Dijkstra.Edge("a", "f", 14),
//            new Graph_Dijkstra.Edge("b", "c", 10),
//            new Graph_Dijkstra.Edge("b", "d", 15),
//            new Graph_Dijkstra.Edge("c", "d", 11),
//            new Graph_Dijkstra.Edge("c", "f", 2),
//            new Graph_Dijkstra.Edge("d", "e", 6),
//            new Graph_Dijkstra.Edge("e", "f", 9),
//    };
//    private static final String START = "a";
//    private static final String END = "e";

    public static void main(String[] args) {
//        Graph_Dijkstra g = new Graph_Dijkstra(GRAPH);
//        g.dijkstra(START);
//        g.printPath(END);
//        g.printAllPaths();
    }

    public static void fromObstacleList(ArrayList<Obstacle> obstacles) {
        for (int i=0; i<obstacles.size(); i++) {

        }
    }
}

class Graph_Dijkstra {
    private final Map<Point2D, Vertex> graph; // mapping of vertex names to Vertex objects, built from a set of Edges

    /** One edge of the graph (only used by Graph_Dijkstra constructor) */
    public static class Edge {
        public final int index1;
        public final int index2;
        public final Point2D v1, v2;
        public final double dist;
        public Edge(int index1, int index2, Point2D v1, Point2D v2, double dist) {
            this.index1 = index1;
            this.index2 = index2;
            this.v1 = v1;
            this.v2 = v2;
            this.dist = dist;
        }
    }

    /** One vertex of the graph, complete with mappings to neighbouring vertices */
    public static class Vertex implements Comparable<Vertex>{
        public final String name;
        public final Point2D coordinates;
        public double dist = Double.MAX_VALUE; // MAX_VALUE assumed to be infinity
        public Vertex previous = null;
        public final Map<Vertex, Double> neighbours = new HashMap<>();
//        public ArrayList<Point2D> path = new ArrayList<>();

        public Vertex(String name, Point2D coordinates)
        {
            this.name = name;
            this.coordinates = coordinates;
        }

        private void returnPath(ArrayList<Point2D> path) {
            if (this == this.previous) {
                path.add(this.coordinates);
            } else if (this.previous == null) {
                System.out.printf("%s(unreached)", this.coordinates.toString());
            } else {
                this.previous.returnPath(path);
                path.add(this.coordinates);
            }
        }

//        private ArrayList<Point2D> returnArray() {
//            return path;
//        }

        private double returnDistance() {
            return this.dist;
        }

        private void printPath()
        {
            if (this == this.previous)
            {
                System.out.printf("%s", this.name);
            }
            else if (this.previous == null)
            {
                System.out.printf("%s(unreached)", this.name);
            }
            else
            {
                this.previous.printPath();
                System.out.printf(" -> %s(%f)", this.name.toString(), this.dist);
            }
        }

        public int compareTo(Vertex other)
        {
            if (dist == other.dist)
                return name.compareTo(other.name);
//                return name.equals(other.name) ? 0 : 0;

            return Double.compare(dist, other.dist);
        }

//        @Override public String toString()
//        {
//            return "(" + name + ", " + dist + ")";
//        }
    }

    /** Builds a graph from a set of edges */
    public Graph_Dijkstra(ArrayList<Edge> edges) {
        graph = new HashMap<>(edges.size());

        //one pass to find all vertices
        for (Edge e : edges) {
            if (!graph.containsKey(e.v1)) graph.put(e.v1, new Vertex(String.valueOf(e.index1), e.v1));
            if (!graph.containsKey(e.v2)) graph.put(e.v2, new Vertex(String.valueOf(e.index2), e.v2));
        }

        //another pass to set neighbouring vertices
        for (Edge e : edges) {
            graph.get(e.v1).neighbours.put(graph.get(e.v2), e.dist);
            graph.get(e.v2).neighbours.put(graph.get(e.v1), e.dist); // also do this for an undirected graph NOTE: FOR UNDIRECTED GRAPHS ONLY!!!
        }
    }

    /** Runs dijkstra using a specified source vertex */
    public void dijkstra(Point2D startName) {
        if (!graph.containsKey(startName)) {
            System.err.printf("Graph_Dijkstra doesn't contain start vertex \"%s\"\n", startName.toString());
            return;
        }
        final Vertex source = graph.get(startName);
        NavigableSet<Vertex> q = new TreeSet<>();

        // set-up vertices
        for (Vertex v : graph.values()) {
            v.previous = v == source ? source : null;
            v.dist = v == source ? 0 : Double.MAX_VALUE;
            q.add(v);
        }

        dijkstra(q);
    }

    /** Implementation of dijkstra's algorithm using a binary heap. */
    private void dijkstra(final NavigableSet<Vertex> q) {
        Vertex u, v;
        while (!q.isEmpty()) {

            u = q.pollFirst(); // vertex with shortest distance (first iteration will return source)
            if (u.dist == Double.MAX_VALUE) break; // we can ignore u (and any other remaining vertices) since they are unreachable

            //look at distances to each neighbour
            for (Map.Entry<Vertex, Double> a : u.neighbours.entrySet()) {
                v = a.getKey(); //the neighbour in this iteration

                final double alternateDist = u.dist + a.getValue();
                if (alternateDist < v.dist) { // shorter path to neighbour found
                    q.remove(v);
                    v.dist = alternateDist;
                    v.previous = u;
                    q.add(v);
                }
            }
        }
    }

    /** Prints a path from the source to the specified vertex */
    public void printPath(Point2D endName) {
        if (!graph.containsKey(endName)) {
            System.err.printf("Graph_Dijkstra doesn't contain end vertex \"%s\"\n", endName.toString());
            return;
        }

        graph.get(endName).printPath();
        System.out.println();
    }

    public double returnDistance(Point2D endName) {
        if (!graph.containsKey(endName)) {
            System.err.printf("Graph_Dijkstra doesn't contain end vertex \"%s\"\n", endName.toString());
            return 0;
        }

        return graph.get(endName).returnDistance();
    }

    public ArrayList<Point2D> returnPath(Point2D endName) {
        if (!graph.containsKey(endName)) {
            System.err.printf("Graph_Dijkstra doesn't contain end vertex \"%s\"\n", endName.toString());
            return null;
        }
        ArrayList<Point2D> path = new ArrayList<>();
        graph.get(endName).returnPath(path);
        if (!path.isEmpty()) {
            path.remove(0);
            if (!path.isEmpty()) {
                path.remove(path.size()-1);
            }
        }
        return path;
    }

    /** Prints the path from the source to every vertex (output order is not guaranteed) */
    public void printAllPaths() {
        for (Vertex v : graph.values()) {
            v.printPath();
            System.out.println();
        }
    }
}