/**
 * Interface for the Bintree nodes (Composite Pattern [cite: 45]).
 */
public interface BinNode {

    /**
     * Print the Bintree nodes in preorder.
     * @param level The depth of the current node.
     * @param x X-coordinate of the node's box
     * @param y Y-coordinate of the node's box
     * @param z Z-coordinate of the node's box
     * @param size The size (width/height/depth) of the node's box
     * @return A StringBuilder containing the preorder traversal string.
     */
    public StringBuilder print(int level, int x, int y, int z, int size);


    /**
     * Counts the total number of nodes in this subtree.
     * @return The count.
     */
    public int countNodes();
    
    // You will add insert, remove, intersect, etc., here for later milestones.
}