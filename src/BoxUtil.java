/**
 * Utility methods for 3D axis-aligned bounding boxes used by the Bintree.
 * Boxes are defined by origin (x, y, z) and dimensions (width, height, depth).
 *
 * IMPORTANT: Two boxes that only touch on a face/edge/point are NOT
 * considered intersecting. We require strict overlap in all 3 dimensions.
 * 
 * @author akashp
 * @author karthikyella
 * @version fall25
 */
public final class BoxUtil {

    private BoxUtil() {
        // Utility class
    }

    /**
     * Returns true if two boxes have a non-empty 3D intersection volume.
     * Adjacent-only contact (sharing a face) returns false.
     * 
     * @param ax  Box A x-origin
     * @param ay  Box A y-origin
     * @param az  Box A z-origin
     * @param axw Box A x-width
     * @param ayw Box A y-width
     * @param azw Box A z-width
     * @param bx  Box B x-origin
     * @param by  Box B y-origin
     * @param bz  Box B z-origin
     * @param bxw Box B x-width
     * @param byw Box B y-width
     * @param bzw Box B z-width
     * @return true if they distinctively overlap.
     */
    public static boolean intersectsStrict(
            int ax, int ay, int az, int axw, int ayw, int azw,
            int bx, int by, int bz, int bxw, int byw, int bzw) {

        // Compute open-interval overlap in each dimension:
        // [a, a+aw) and [b, b+bw)
        int aMaxX = ax + axw;
        int aMaxY = ay + ayw;
        int aMaxZ = az + azw;

        int bMaxX = bx + bxw;
        int bMaxY = by + byw;
        int bMaxZ = bz + bzw;

        boolean xOverlap = Math.max(ax, bx) < Math.min(aMaxX, bMaxX);
        boolean yOverlap = Math.max(ay, by) < Math.min(aMaxY, bMaxY);
        boolean zOverlap = Math.max(az, bz) < Math.min(aMaxZ, bMaxZ);

        return xOverlap && yOverlap && zOverlap;
    }

    /**
     * Check if a point (px, py, pz) is strictly inside a region defined by
     * (x, y, z) and size (sx, sy, sz).
     * Strict: [x, x+sx), etc.
     * 
     * @param px Point x
     * @param py Point y
     * @param pz Point z
     * @param x  Region x
     * @param y  Region y
     * @param z  Region z
     * @param sx Region width
     * @param sy Region height
     * @param sz Region depth
     * @return true if point is inside.
     */
    public static boolean pointInRegion(
            int px,
            int py,
            int pz,
            int x,
            int y,
            int z,
            int sx,
            int sy,
            int sz) {

        return (px >= x && px < x + sx)
                && (py >= y && py < y + sy)
                && (pz >= z && pz < z + sz);
    }

    /**
     * Returns true if a point lies within the region cube defined by size.
     * 
     * @param px   Point x
     * @param py   Point y
     * @param pz   Point z
     * @param x    Region x
     * @param y    Region y
     * @param z    Region z
     * @param size Region size (all dims)
     * @return true if inside
     */
    public static boolean pointInRegion(
            int px, int py, int pz,
            int x, int y, int z, int size) {
        return pointInRegion(px, py, pz, x, y, z, size, size, size);
    }

    // ----------------------------------------------------------
    // Intersection checks
    // ----------------------------------------------------------

    /**
     * Check if a region (x,y,z, sx, sy, sz) strictly intersects a box
     * defined by (bx, by, bz, bxw, byw, bzw).
     * 
     * @param x   Region x
     * @param y   Region y
     * @param z   Region z
     * @param sx  Region width
     * @param sy  Region height
     * @param sz  Region depth
     * @param bx  Box x
     * @param by  Box y
     * @param bz  Box z
     * @param bxw Box width
     * @param byw Box height
     * @param bzw Box depth
     * @return true if intersecting
     */
    public static boolean regionIntersectsBoxStrict(
            int x, int y, int z, int sx, int sy, int sz,
            int bx, int by, int bz, int bxw, int byw, int bzw) {

        return intersectsStrict(
                x, y, z, sx, sy, sz,
                bx, by, bz, bxw, byw, bzw);
    }

    /**
     * Helper for cubic region.
     * 
     * @param x    Region x
     * @param y    Region y
     * @param z    Region z
     * @param size Region size
     * @param bx   Box x
     * @param by   Box y
     * @param bz   Box z
     * @param bxw  Box width
     * @param byw  Box height
     * @param bzw  Box depth
     * @return true if intersecting
     */
    public static boolean regionIntersectsBoxStrict(
            int x, int y, int z, int size,
            int bx, int by, int bz, int bxw, int byw, int bzw) {
        return regionIntersectsBoxStrict(
                x, y, z, size, size, size, bx, by, bz, bxw, byw, bzw);
    }

    /**
     * Convenience overload for AirObjects.
     * 
     * @param a Object a
     * @param b Object b
     * @return true if intersecting
     */
    public static boolean intersectsStrict(AirObject a, AirObject b) {
        return intersectsStrict(
                a.getXorig(), a.getYorig(), a.getZorig(),
                a.getXwidth(), a.getYwidth(), a.getZwidth(),
                b.getXorig(), b.getYorig(), b.getZorig(),
                b.getXwidth(), b.getYwidth(), b.getZwidth());
    }

    /**
     * Compute the intersection box of two boxes.
     * Returns null if the intersection is empty (including adjacency-only).
     *
     * Returned int[] = {x, y, z, xwid, ywid, zwid}
     * 
     * @param ax  Box A x
     * @param ay  Box A y
     * @param az  Box A z
     * @param axw Box A width
     * @param ayw Box A height
     * @param azw Box A depth
     * @param bx  Box B x
     * @param by  Box B y
     * @param bz  Box B z
     * @param bxw Box B width
     * @param byw Box B height
     * @param bzw Box B depth
     * @return array of 6 ints or null
     */
    public static int[] intersectionBoxStrict(
            int ax, int ay, int az, int axw, int ayw, int azw,
            int bx, int by, int bz, int bxw, int byw, int bzw) {

        int ix = Math.max(ax, bx);
        int iy = Math.max(ay, by);
        int iz = Math.max(az, bz);

        int iMaxX = Math.min(ax + axw, bx + bxw);
        int iMaxY = Math.min(ay + ayw, by + byw);
        int iMaxZ = Math.min(az + azw, bz + bzw);

        int ixw = iMaxX - ix;
        int iyw = iMaxY - iy;
        int izw = iMaxZ - iz;

        // Strictly positive widths required
        if (ixw <= 0 || iyw <= 0 || izw <= 0) {
            return null;
        }

        return new int[] { ix, iy, iz, ixw, iyw, izw };
    }

    /**
     * Compute the non-empty common intersection box of a small array.
     * Returns null if there is no shared non-empty intersection.
     * 
     * @param objs  Array of objects
     * @param count Number of objects to check
     * @return Common intersection box or null
     */
    public static int[] commonIntersectionStrict(AirObject[] objs, int count) {
        if (objs == null || count <= 0) {
            return null;
        }

        // Start with first object's box as candidate intersection
        int ix = objs[0].getXorig();
        int iy = objs[0].getYorig();
        int iz = objs[0].getZorig();
        int iMaxX = ix + objs[0].getXwidth();
        int iMaxY = iy + objs[0].getYwidth();
        int iMaxZ = iz + objs[0].getZwidth();

        for (int i = 1; i < count; i++) {
            AirObject o = objs[i];
            int ox = o.getXorig();
            int oy = o.getYorig();
            int oz = o.getZorig();
            int oMaxX = ox + o.getXwidth();
            int oMaxY = oy + o.getYwidth();
            int oMaxZ = oz + o.getZwidth();

            ix = Math.max(ix, ox);
            iy = Math.max(iy, oy);
            iz = Math.max(iz, oz);
            iMaxX = Math.min(iMaxX, oMaxX);
            iMaxY = Math.min(iMaxY, oMaxY);
            iMaxZ = Math.min(iMaxZ, oMaxZ);

            int ixw = iMaxX - ix;
            int iyw = iMaxY - iy;
            int izw = iMaxZ - iz;

            if (ixw <= 0 || iyw <= 0 || izw <= 0) {
                return null;
            }
        }

        return new int[] { ix, iy, iz, iMaxX - ix, iMaxY - iy, iMaxZ - iz };
    }

    /**
     * Returns true if an AirObject intersects a node-region cube.
     * 
     * @param obj The object
     * @param x   Region x
     * @param y   Region y
     * @param z   Region z
     * @param sx  Region width
     * @param sy  Region height
     * @param sz  Region depth
     * @return true if intersecting
     */
    public static boolean objectIntersectsRegionStrict(
            AirObject obj, int x, int y, int z, int sx, int sy, int sz) {

        return intersectsStrict(
                obj.getXorig(), obj.getYorig(), obj.getZorig(),
                obj.getXwidth(), obj.getYwidth(), obj.getZwidth(),
                x, y, z, sx, sy, sz);
    }

    /**
     * Returns true if an AirObject intersects a node-region cube.
     * 
     * @param obj  The object
     * @param x    Region x
     * @param y    Region y
     * @param z    Region z
     * @param size Region size
     * @return true if intersecting
     */
    public static boolean objectIntersectsRegionStrict(
            AirObject obj, int x, int y, int z, int size) {
        return objectIntersectsRegionStrict(obj, x, y, z, size, size, size);
    }
}
