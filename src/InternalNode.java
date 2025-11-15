/**
 * Stub class for Bintree Internal Nodes.
 * You will implement this fully in the next milestone.
 */
public class InternalNode implements BinNode {

    // You will add children here (BinNode left, BinNode right)
    
    public InternalNode() {
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
        // Stub: 1 (for itself) + left.countNodes() + right.countNodes()
        return 1; 
    }
}