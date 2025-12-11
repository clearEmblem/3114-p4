/**
 * Leaf node for the 3D Bintree that stores AirObjects.
 * A leaf can hold more than 3 objects ONLY if all stored objects
 * share a non-empty common intersection box.
 * 
 * @author akashp
 * @author karthikyella
 * @version fall25
 */
public class LeafNode implements BinNode {

    private AirObject[] objects;
    private int count;

    /** @return count of objects */
    public int getCount() {
        return count;
    }

    /** @return objects array */
    public AirObject[] getObjects() {
        return objects;
    }

    /** Constructor. */
    public LeafNode() {
        // Initial capacity > 3 to avoid immediate resize
        objects = new AirObject[5];
        count = 0;
    }

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

        // Only store if this leaf's region intersects the object
        if (!BoxUtil.objectIntersectsRegionStrict(obj, x, y, z, sx, sy, sz)) {
            return this;
        }

        insertSorted(obj);

        // If within normal capacity, done
        if (count <= 3) {
            return this;
        }

        // Safety check for smallest granularity (prevent infinite recursion)
        int dim = level % 3;
        int checkSz = (dim == 0) ? sx : (dim == 1) ? sy : sz;

        // If region cannot split safely, keep as leaf
        if (checkSz <= 1) {
            return this;
        }

        // If all boxes share a non-empty common intersection, DO NOT split
        int[] common = BoxUtil.commonIntersectionStrict(objects, count);
        if (common != null) {
            return this;
        }

        // Otherwise, split into an internal node and reinsert
        InternalNode internal = new InternalNode();
        for (int i = 0; i < count; i++) {
            internal.insert(objects[i], level, x, y, z, sx, sy, sz);
        }
        return internal;
    }

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
        res.addMessage(String.format(
                "In leaf node (%d, %d, %d, %d, %d, %d) %d\n",
                x, y, z, sx, sy, sz, level));

        for (int i = 0; i < count; i++) {
            AirObject o = objects[i];
            int[] inter = BoxUtil.intersectionBoxStrict(
                    o.getXorig(), o.getYorig(), o.getZorig(),
                    o.getXwidth(), o.getYwidth(), o.getZwidth(),
                    qx, qy, qz, qxw, qyw, qzw);

            if (inter != null) {
                // Report only if this leaf contains the origin of intersection
                if (pointInRegionLoose(
                        inter[0], inter[1], inter[2],
                        x, y, z, sx, sy, sz)) {
                    res.addMatch(o);
                }
            }
        }
    }

    // Helper for pointInRegion with loose dims
    private boolean pointInRegionLoose(
            int px, int py, int pz,
            int x, int y, int z,
            int sx, int sy, int sz) {
        return px >= x && px < x + sx
                && py >= y && py < y + sy
                && pz >= z && pz < z + sz;
    }

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

        res.incrementVisited();
        res.addHeader(x, y, z, sx, sy, sz, level);

        // Pairwise check inside this leaf
        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++) {
                AirObject a = objects[i];
                AirObject b = objects[j];

                int[] inter = BoxUtil.intersectionBoxStrict(
                        a.getXorig(), a.getYorig(), a.getZorig(),
                        a.getXwidth(), a.getYwidth(), a.getZwidth(),
                        b.getXorig(), b.getYorig(), b.getZorig(),
                        b.getXwidth(), b.getYwidth(), b.getZwidth());

                if (inter != null) {
                    // Report only once: the leaf that contains the
                    // origin of the intersection box
                    if (pointInRegionLoose(
                            inter[0], inter[1], inter[2],
                            x, y, z, sx, sy, sz)) {
                        res.addCollision(a, b);
                    }
                }
            }
        }
    }

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

        // If the object doesn't intersect this region, nothing to remove here
        if (!BoxUtil.objectIntersectsRegionStrict(obj, x, y, z, sx, sy, sz)) {
            return this;
        }

        // Find by name (SkipList already ensures unique names)
        int idx = -1;
        for (int i = 0; i < count; i++) {
            if (objects[i].getName().equals(obj.getName())) {
                idx = i;
                break;
            }
        }

        if (idx == -1) {
            return this;
        }

        // Remove and shift
        for (int j = idx; j < count - 1; j++) {
            objects[j] = objects[j + 1];
        }
        objects[count - 1] = null;
        count--;

        if (count == 0) {
            return EmptyLeafNode.getInstance();
        }

        return this;
    }

    /**
     * Insert an object into the array in Comparable (name) order.
     * No ArrayList allowed.
     */
    private void insertSorted(AirObject obj) {
        if (count == objects.length) {
            // Grow array
            AirObject[] bigger = new AirObject[objects.length * 2 + 1];
            System.arraycopy(objects, 0, bigger, 0, objects.length);
            objects = bigger;
        }

        // Find insertion point
        int idx = 0;
        while (idx < count && objects[idx].compareTo(obj) < 0) {
            idx++;
        }

        // Shift to make room
        for (int j = count; j > idx; j--) {
            objects[j] = objects[j - 1];
        }

        objects[idx] = obj;
        count++;
    }

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
                "Leaf with %d objects (%d, %d, %d, %d, %d, %d) %d\r\n",
                count, x, y, z, sx, sy, sz, level));

        // Print stored objects in Comparable order
        for (int i = 0; i < count; i++) {
            sb.append(objects[i].toString()).append("\r\n");
        }
        return sb;
    }

    @Override
    public int countNodes() {
        return 1;
    }
}
