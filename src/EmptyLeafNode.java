/**
 * Implements the Flyweight pattern for empty leaf nodes.
 * This is a Singleton.
 * 
 * @author akashp
 * @author karthikyella
 * @version fall25
 */
public class EmptyLeafNode implements BinNode {

    private static final EmptyLeafNode INSTANCE = new EmptyLeafNode();

    private EmptyLeafNode() {
        // Nothing to do
    }

    /**
     * @return The singleton instance.
     */
    public static EmptyLeafNode getInstance() {
        return INSTANCE;
    }

    /**
     * Inserts an AirObject into the quadtree.
     * If the object intersects this empty region, this node transforms into a
     * LeafNode
     * and the object is inserted into it. Otherwise, the node remains unchanged.
     *
     * @param obj   The AirObject to insert.
     * @param level The current level of the node in the quadtree.
     * @param x     The x-coordinate of the region's origin.
     * @param y     The y-coordinate of the region's origin.
     * @param z     The z-coordinate of the region's origin.
     * @param sx    The x-dimension of the region.
     * @param sy    The y-dimension of the region.
     * @param sz    The z-dimension of the region.
     * @return The BinNode after the insertion operation.
     */
    @Override
    public BinNode insert(
            AirObject obj,
            int level,
            int x,
            int y,
            int z,
            int sx,
            int sy,
            int sz) {
        // If the object doesn't intersect this region, nothing changes.
        if (!BoxUtil.objectIntersectsRegionStrict(obj, x, y, z, sx, sy, sz)) {
            return this;
        }

        // Composite behavior: empty leaf becomes a real leaf.
        LeafNode leaf = new LeafNode();
        return leaf.insert(obj, level, x, y, z, sx, sy, sz);
    }

    /**
     * Removes an AirObject from the quadtree.
     * Since this is an empty leaf node, no object can be removed, so it returns
     * itself.
     *
     * @param obj   The AirObject to remove.
     * @param level The current level of the node in the quadtree.
     * @param x     The x-coordinate of the region's origin.
     * @param y     The y-coordinate of the region's origin.
     * @param z     The z-coordinate of the region's origin.
     * @param sx    The x-dimension of the region.
     * @param sy    The y-dimension of the region.
     * @param sz    The z-dimension of the region.
     * @return The BinNode after the removal operation (always this
     *         EmptyLeafNode).
     */
    @Override
    public BinNode remove(
            AirObject obj,
            int level,
            int x,
            int y,
            int z,
            int sx,
            int sy,
            int sz) {
        return this;
    }

    /**
     * Prints the representation of this empty leaf node.
     *
     * @param level The current level of the node in the quadtree.
     * @param x     The x-coordinate of the region's origin.
     * @param y     The y-coordinate of the region's origin.
     * @param z     The z-coordinate of the region's origin.
     * @param sx    The x-dimension of the region.
     * @param sy    The y-dimension of the region.
     * @param sz    The z-dimension of the region.
     * @return A StringBuilder containing the formatted string representation.
     */
    @Override
    public StringBuilder print(
            int level,
            int x,
            int y,
            int z,
            int sx,
            int sy,
            int sz) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(
                "E (%d, %d, %d, %d, %d, %d) %d\r\n",
                x, y, z, sx, sy, sz, level));
        return sb;
    }

    /**
     * Checks for intersections between the node's region and a query box.
     * If the regions intersect, it increments the visited count and adds a
     * message if at a certain level.
     *
     * @param qx    The x-coordinate of the query box's origin.
     * @param qy    The y-coordinate of the query box's origin.
     * @param qz    The z-coordinate of the query box's origin.
     * @param qxw   The x-dimension of the query box.
     * @param qyw   The y-dimension of the query box.
     * @param qzw   The z-dimension of the query box.
     * @param level The current level of the node in the quadtree.
     * @param x     The x-coordinate of the node's region origin.
     * @param y     The y-coordinate of the node's region origin.
     * @param z     The z-coordinate of the node's region origin.
     * @param sx    The x-dimension of the node's region.
     * @param sy    The y-dimension of the node's region.
     * @param sz    The z-dimension of the node's region.
     * @param res   The IntersectResult object to store results.
     */
    @Override
    public void intersect(
            int qx, int qy, int qz, int qxw, int qyw, int qzw,
            int level, int x, int y, int z, int sx, int sy, int sz,
            IntersectResult res) {

        if (!BoxUtil.regionIntersectsBoxStrict(
                x, y, z, sx, sy, sz, qx, qy, qz, qxw, qyw, qzw)) {
            return;
        }

        res.incrementVisited();
        if (level > 0) {
            res.addMessage(String.format(
                    "In leaf node (%d, %d, %d, %d, %d, %d) %d\n",
                    x, y, z, sx, sy, sz, level));
        }
    }

    /**
     * Performs collision detection within this node's region.
     * For an empty leaf, it increments the visited count and adds a header if
     * the level is valid.
     *
     * @param level The current level of the node in the quadtree.
     * @param x     The x-coordinate of the region's origin.
     * @param y     The y-coordinate of the region's origin.
     * @param z     The z-coordinate of the region's origin.
     * @param sx    The x-dimension of the region.
     * @param sy    The y-dimension of the region.
     * @param sz    The z-dimension of the region.
     * @param res   The CollisionResult object to store collision information.
     */
    @Override
    public void collisions(
            int level,
            int x,
            int y,
            int z,
            int sx,
            int sy,
            int sz,
            CollisionResult res) {
        if (level <= 0) {
            return;
        }
        // Visiting an empty leaf counts as a node visit
        res.incrementVisited();
        res.addHeader(x, y, z, sx, sy, sz, level);
    }

    /**
     * Counts the number of nodes in this subtree.
     * An empty leaf node counts as one node.
     *
     * @return The number of nodes (always 1 for an EmptyLeafNode).
     */
    @Override
    public int countNodes() {
        return 1;
    }
}
