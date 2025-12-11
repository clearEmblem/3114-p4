/**
 * Collects collision results from the Bintree.
 * 
 * @author akashp
 * @author karthikyella
 * @version fall25
 */
public class CollisionResult {
    private int nodesVisited;
    private StringBuilder output;

    /** Constructor. */
    public CollisionResult() {
        nodesVisited = 0;
        output = new StringBuilder();
    }

    /** Increments visited node count. */
    public void incrementVisited() {
        nodesVisited++;
    }

    /** @return visited count. */
    public int getNodesVisited() {
        return nodesVisited;
    }

    /**
     * Adds a header for a leaf node visit.
     * 
     * @param x     Region x
     * @param y     Region y
     * @param z     Region z
     * @param sx    Region width
     * @param sy    Region height
     * @param sz    Region depth
     * @param level Node level
     */
    public void addHeader(
            int x, int y, int z, int sx, int sy, int sz, int level) {
        output.append(String.format(
                "In leaf node (%d, %d, %d, %d, %d, %d) %d\n",
                x, y, z, sx, sy, sz, level));
    }

    /**
     * Records a collision.
     * 
     * @param a First object
     * @param b Second object
     */
    public void addCollision(AirObject a, AirObject b) {
        output.append(String.format(
                "%s and %s\n",
                a.toString(), b.toString()));
    }

    /** @return The formatted output string. */
    public String getOutput() {
        return output.toString();
    }
}
