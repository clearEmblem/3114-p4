/**
 * Stub class for Bintree (non-empty) Leaf Nodes.
 * You will implement this fully in the next milestone.
 */
public class LeafNode implements BinNode {
    
    // You will add a list/array for AirObjects here.
    // Remember, no ArrayList! 

    public LeafNode() {
        // In M1, this should never be created,
        // but it needs to exist to compile.
    }

    @Override
    public StringBuilder print(int level, int x, int y, int z, int size) {
        // Stub
        return new StringBuilder();
    }


    @Override
    public int countNodes() {
        return 1; // Itself
    }
}