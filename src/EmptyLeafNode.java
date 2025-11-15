/**
 * Implements the Flyweight pattern for empty leaf nodes[cite: 44].
 * This is a Singleton.
 */
public class EmptyLeafNode implements BinNode {

    private static final EmptyLeafNode INSTANCE = new EmptyLeafNode();

    /**
     * Private constructor for Singleton.
     */
    private EmptyLeafNode() {
        // Nothing to do
    }


    /**
     * Get the single instance of the empty leaf node.
     * @return The instance.
     */
    public static EmptyLeafNode getInstance() {
        return INSTANCE;
    }


    /**
     * Returns the string for an empty leaf node, matching testEmpty().
     */
    @Override
    public StringBuilder print(int level, int x, int y, int z, int size) {
        StringBuilder sb = new StringBuilder();
        // This is the exact string from testEmpty()
        sb.append(String.format(
            "E (%d, %d, %d, %d, %d, %d) %d\r\n",
            x, y, z, size, size, size, level));
        return sb;
    }


    /**
     * An empty node is just 1 node.
     */
    @Override
    public int countNodes() {
        return 1;
    }
}