/**
 * Interface for the Bintree nodes.
 * 
 * @author akashp
 * @author karthikyella
 * @version fall25
 */
public interface BinNode {

    /**
     * Inserts an object into the tree.
     * 
     * @param obj   The object to insert.
     * @param level The current level of the node.
     * @param x     The x-coordinate of the region.
     * @param y     The y-coordinate of the region.
     * @param z     The z-coordinate of the region.
     * @param sx    The width of the region.
     * @param sy    The height of the region.
     * @param sz    The depth of the region.
     * @return The new root of this subtree.
     */
    BinNode insert(
            AirObject obj,
            int level,
            int x,
            int y,
            int z,
            int sx,
            int sy,
            int sz);

    /**
     * Removes an object from the tree.
     * 
     * @param obj   The object to remove.
     * @param level The current level.
     * @param x     The x-coordinate of the region.
     * @param y     The y-coordinate of the region.
     * @param z     The z-coordinate of the region.
     * @param sx    The width of the region.
     * @param sy    The height of the region.
     * @param sz    The depth of the region.
     * @return The new root of this subtree.
     */
    BinNode remove(
            AirObject obj,
            int level,
            int x,
            int y,
            int z,
            int sx,
            int sy,
            int sz);

    /**
     * Reports all collisions within this node's region.
     * 
     * @param level The current level.
     * @param x     The x-coordinate of the region.
     * @param y     The y-coordinate of the region.
     * @param z     The z-coordinate of the region.
     * @param sx    The width of the region.
     * @param sy    The height of the region.
     * @param sz    The depth of the region.
     * @param res   The result accumulator.
     */
    void collisions(
            int level,
            int x,
            int y,
            int z,
            int sx,
            int sy,
            int sz,
            CollisionResult res);

    /**
     * Reports all intersections with a query box.
     * 
     * @param qx    Query x.
     * @param qy    Query y.
     * @param qz    Query z.
     * @param qxw   Query width.
     * @param qyw   Query height.
     * @param qzw   Query depth.
     * @param level Current level.
     * @param x     Region x.
     * @param y     Region y.
     * @param z     Region z.
     * @param sx    Region width.
     * @param sy    Region height.
     * @param sz    Region depth.
     * @param res   The result accumulator.
     */
    void intersect(
            int qx, int qy, int qz, int qxw, int qyw, int qzw,
            int level, int x, int y, int z, int sx, int sy, int sz,
            IntersectResult res);

    /**
     * Prints the tree structure.
     * 
     * @param level Current level.
     * @param x     Region x.
     * @param y     Region y.
     * @param z     Region z.
     * @param sx    Region width.
     * @param sy    Region height.
     * @param sz    Region depth.
     * @return The string builder containing the dump.
     */
    StringBuilder print(
            int level,
            int x,
            int y,
            int z,
            int sx,
            int sy,
            int sz);

    /**
     * Counts the number of nodes in the subtree.
     * 
     * @return The number of nodes.
     */
    int countNodes();
}
