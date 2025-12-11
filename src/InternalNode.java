/**
 * Internal node for the 3D Bintree.
 * Uses Composite pattern: delegates work to children.
 * 
 * @author akashp
 * @author karthikyella
 * @version fall25
 */
public class InternalNode implements BinNode {

    private BinNode left;
    private BinNode right;

    /** Constructor. */
    public InternalNode() {
        left = EmptyLeafNode.getInstance();
        right = EmptyLeafNode.getInstance();
    }

    /**
     * Constructor with children.
     * 
     * @param left  Left child
     * @param right Right child
     */
    public InternalNode(BinNode left, BinNode right) {
        this.left = (left == null) ? EmptyLeafNode.getInstance() : left;
        this.right = (right == null) ? EmptyLeafNode.getInstance() : right;
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

        if (!BoxUtil.objectIntersectsRegionStrict(obj, x, y, z, sx, sy, sz)) {
            return this;
        }

        // Determine splitting dimension
        int dim = level % 3;
        int splitSz = (dim == 0) ? sx : (dim == 1) ? sy : sz;

        if (splitSz <= 1) { // Defensive check against infinite recursion
            LeafNode leaf = new LeafNode();
            return leaf.insert(obj, level, x, y, z, sx, sy, sz);
        }

        int half = splitSz / 2;
        int lx = x, ly = y, lz = z, lsx = sx, lsy = sy, lsz = sz;
        int rx = x, ry = y, rz = z, rsx = sx, rsy = sy, rsz = sz;

        if (dim == 0) {
            rx = x + half;
            lsx = half;
            rsx = remainingLength(sx, half);
        } else if (dim == 1) {
            ry = y + half;
            lsy = half;
            rsy = remainingLength(sy, half);
        } else {
            rz = z + half;
            lsz = half;
            rsz = remainingLength(sz, half);
        }

        if (BoxUtil.objectIntersectsRegionStrict(
                obj, lx, ly, lz, lsx, lsy, lsz)) {
            left = left.insert(obj, level + 1, lx, ly, lz, lsx, lsy, lsz);
        }
        if (BoxUtil.objectIntersectsRegionStrict(
                obj, rx, ry, rz, rsx, rsy, rsz)) {
            right = right.insert(obj, level + 1, rx, ry, rz, rsx, rsy, rsz);
        }
        return this;
    }

    private int remainingLength(int total, int firstHalf) {
        return total - firstHalf;
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

        if (!BoxUtil.objectIntersectsRegionStrict(obj, x, y, z, sx, sy, sz)) {
            return this;
        }

        int dim = level % 3;
        int splitSz = (dim == 0) ? sx : (dim == 1) ? sy : sz;

        if (splitSz <= 1) {
            return this;
        }

        int half = splitSz / 2;
        int lx = x, ly = y, lz = z, lsx = sx, lsy = sy, lsz = sz;
        int rx = x, ry = y, rz = z, rsx = sx, rsy = sy, rsz = sz;

        if (dim == 0) {
            rx = x + half;
            lsx = half;
            rsx = sx - half;
        } else if (dim == 1) {
            ry = y + half;
            lsy = half;
            rsy = sy - half;
        } else {
            rz = z + half;
            lsz = half;
            rsz = sz - half;
        }

        if (BoxUtil.objectIntersectsRegionStrict(
                obj, lx, ly, lz, lsx, lsy, lsz)) {
            left = left.remove(obj, level + 1, lx, ly, lz, lsx, lsy, lsz);
        }
        if (BoxUtil.objectIntersectsRegionStrict(
                obj, rx, ry, rz, rsx, rsy, rsz)) {
            right = right.remove(obj, level + 1, rx, ry, rz, rsx, rsy, rsz);
        }

        // Merge logic if both children are leaves and total objects <= 3
        if (left == EmptyLeafNode.getInstance()
                && right == EmptyLeafNode.getInstance()) {
            return EmptyLeafNode.getInstance();
        }

        boolean lLeaf = (left instanceof LeafNode)
                || (left instanceof EmptyLeafNode);
        boolean rLeaf = (right instanceof LeafNode)
                || (right instanceof EmptyLeafNode);

        if (lLeaf && rLeaf) {
            AirObject[] unique = new AirObject[10];
            int uCount = 0;

            if (left instanceof LeafNode) {
                LeafNode l = (LeafNode) left;
                for (int i = 0; i < l.getCount(); i++) {
                    unique[uCount++] = l.getObjects()[i];
                }
            }
            if (right instanceof LeafNode) {
                LeafNode r = (LeafNode) right;
                AirObject[] rObjs = r.getObjects();
                for (int i = 0; i < r.getCount(); i++) {
                    AirObject candidate = rObjs[i];
                    boolean exists = false;
                    for (int j = 0; j < uCount; j++) {
                        if (unique[j].getName().equals(candidate.getName())) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        unique[uCount++] = candidate;
                    }
                }
            }

            if (uCount <= 3) {
                LeafNode merged = new LeafNode();
                for (int i = 0; i < uCount; i++) {
                    merged.insert(unique[i], level, x, y, z, sx, sy, sz);
                }
                return merged;
            }
        }
        return this;
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
                "In Internal node (%d, %d, %d, %d, %d, %d) %d\n",
                x, y, z, sx, sy, sz, level));

        int dim = level % 3;
        int splitSz = (dim == 0) ? sx : (dim == 1) ? sy : sz;

        if (splitSz <= 1) {
            return;
        }
        int half = splitSz / 2;

        int lx = x, ly = y, lz = z, lsx = sx, lsy = sy, lsz = sz;
        int rx = x, ry = y, rz = z, rsx = sx, rsy = sy, rsz = sz;

        if (dim == 0) {
            rx = x + half;
            lsx = half;
            rsx = sx - half;
        } else if (dim == 1) {
            ry = y + half;
            lsy = half;
            rsy = sy - half;
        } else {
            rz = z + half;
            lsz = half;
            rsz = sz - half;
        }

        if (BoxUtil.regionIntersectsBoxStrict(
                lx, ly, lz, lsx, lsy, lsz, qx, qy, qz, qxw, qyw, qzw)) {
            left.intersect(
                    qx, qy, qz, qxw, qyw, qzw,
                    level + 1, lx, ly, lz, lsx, lsy, lsz, res);
        }
        if (BoxUtil.regionIntersectsBoxStrict(
                rx, ry, rz, rsx, rsy, rsz, qx, qy, qz, qxw, qyw, qzw)) {
            right.intersect(
                    qx, qy, qz, qxw, qyw, qzw,
                    level + 1, rx, ry, rz, rsx, rsy, rsz, res);
        }
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

        res.incrementVisited(); // Internal nodes count as visited

        int dim = level % 3;
        int splitSz = (dim == 0) ? sx : (dim == 1) ? sy : sz;

        if (splitSz <= 1) {
            return;
        }
        int half = splitSz / 2;

        int lx = x, ly = y, lz = z, lsx = sx, lsy = sy, lsz = sz;
        int rx = x, ry = y, rz = z, rsx = sx, rsy = sy, rsz = sz;

        if (dim == 0) {
            rx = x + half;
            lsx = half;
            rsx = sx - half;
        } else if (dim == 1) {
            ry = y + half;
            lsy = half;
            rsy = sy - half;
        } else {
            rz = z + half;
            lsz = half;
            rsz = sz - half;
        }

        left.collisions(level + 1, lx, ly, lz, lsx, lsy, lsz, res);
        right.collisions(level + 1, rx, ry, rz, rsx, rsy, rsz, res);
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
        sb.append(String.format("I (%d, %d, %d, %d, %d, %d) %d\r\n",
                x, y, z, sx, sy, sz, level));

        int dim = level % 3;
        int splitSz = (dim == 0) ? sx : (dim == 1) ? sy : sz;
        int half = splitSz / 2;
        int remainder = splitSz - half;

        int lx = x, ly = y, lz = z, lsx = sx, lsy = sy, lsz = sz;
        int rx = x, ry = y, rz = z, rsx = sx, rsy = sy, rsz = sz;

        if (dim == 0) {
            rx = x + half;
            lsx = half;
            rsx = remainder;
        } else if (dim == 1) {
            ry = y + half;
            lsy = half;
            rsy = remainder;
        } else {
            rz = z + half;
            lsz = half;
            rsz = remainder;
        }

        sb.append(left.print(level + 1, lx, ly, lz, lsx, lsy, lsz));
        sb.append(right.print(level + 1, rx, ry, rz, rsx, rsy, rsz));
        return sb;
    }

    @Override
    public int countNodes() {
        return 1 + left.countNodes() + right.countNodes();
    }
}
