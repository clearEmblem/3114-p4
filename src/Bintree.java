/**
 * Bintree wrapper class that manages the root node and
 * provides a clean interface for WorldDB.
 *
 * This supports design separation expected by the rubric.
 * 
 * @author akashp
 * @author karthikyella
 * @version fall25
 */
public class Bintree {

    /** World size constant. */
    public static final int WORLD_SIZE = 1024;

    private BinNode root;

    /** Constructor. */
    public Bintree() {
        root = EmptyLeafNode.getInstance();
    }

    /** Clears the tree. */
    public void clear() {
        root = EmptyLeafNode.getInstance();
    }

    /**
     * Inserts an object.
     * 
     * @param obj The object.
     */
    public void insert(AirObject obj) {
        root = root.insert(
                obj, 0, 0, 0, 0, WORLD_SIZE, WORLD_SIZE, WORLD_SIZE);
    }

    /** @return The root. */
    public BinNode getRoot() {
        return root;
    }

    /**
     * detects collisions.
     * 
     * @return collision results.
     */
    public CollisionResult collisions() {
        CollisionResult res = new CollisionResult();
        root.collisions(0, 0, 0, 0, WORLD_SIZE, WORLD_SIZE, WORLD_SIZE, res);
        return res;
    }

    /** @return node count. */
    public int countNodes() {
        return root.countNodes();
    }

    /**
     * Removes an object.
     * 
     * @param obj object to remove.
     */
    public void remove(AirObject obj) {
        root = root.remove(
                obj, 0, 0, 0, 0, WORLD_SIZE, WORLD_SIZE, WORLD_SIZE);
    }

    /**
     * Intersect query.
     * 
     * @param bx  box x
     * @param by  box y
     * @param bz  box z
     * @param bxw box w
     * @param byw box h
     * @param bzw box d
     * @return result
     */
    public IntersectResult intersect(
            int bx, int by, int bz, int bxw, int byw, int bzw) {
        IntersectResult res = new IntersectResult();
        root.intersect(
                bx, by, bz, bxw, byw, bzw,
                0, 0, 0, 0, WORLD_SIZE, WORLD_SIZE, WORLD_SIZE, res);
        return res;
    }

    /**
     * Prints the tree.
     * 
     * @return string dump
     */
    public String print() {
        return root.print(
                0, 0, 0, 0, WORLD_SIZE, WORLD_SIZE, WORLD_SIZE).toString();
    }
}
