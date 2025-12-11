import java.util.Random;

/**
 * The world for this project. We have a Skip List and a Bintree.
 * This is the M1 implementation.
 *
 * @author akashp
 * @author karthikyella
 * @version fall25
 */
public class WorldDB implements ATC {
    // World bounds [0, 1023]
    private final int WORLD_MIN = 0;
    private final int WORLD_MAX = 1024; // [cite: 30, 34]

    private Random rnd;
    private SkipList<String, AirObject> skipList;
    private Bintree bintree;

    /**
     * Create a brave new World.
     * 
     * @param r A random number generator to use
     */
    public WorldDB(Random r) {
        rnd = r;
        if (rnd == null) {
            rnd = new Random();
        }
        clear();
    }

    /**
     * Clear the world
     */
    public final void clear() {
        skipList = new SkipList<String, AirObject>(rnd);
        bintree = new Bintree();
    }

    // --- Validation Helpers ---

    /**
     * Helper to check if a generic box is invalid.
     * 
     * @return True if the box is outside world bounds, false otherwise.
     */
    private boolean isBoxInvalid(
            int x, int y, int z,
            int xwid, int ywid, int zwid) {

        return
        // Coords must be in [0, 1023]
        (x < WORLD_MIN || y < WORLD_MIN || z < WORLD_MIN) ||
                (x >= WORLD_MAX || y >= WORLD_MAX || z >= WORLD_MAX) ||

                // Widths must be in [1, 1024]
                (xwid < 1 || ywid < 1 || zwid < 1) ||
                (xwid > WORLD_MAX || ywid > WORLD_MAX || zwid > WORLD_MAX) ||

                // Check that the *entire box* is within the world [0, 1024]
                (x + xwid > WORLD_MAX
                        || y + ywid > WORLD_MAX
                        || z + zwid > WORLD_MAX);
    }

    /**
     * Helper to check all object-specific parameters from testBadInput.
     * 
     * @param a The AirObject to check.
     * @return True if any parameters are invalid, false otherwise.
     */
    private boolean isObjectParamsInvalid(AirObject a) {
        if (a.getName() == null || a.getName().isEmpty()) {
            return true; // [cite: 32]
        }

        if (a instanceof AirPlane) {
            AirPlane p = (AirPlane) a;
            if (p.getCarrier() == null || p.getFlightNum() <= 0
                    || p.getNumEngines() <= 0) {
                return true;
            }
        } else if (a instanceof Balloon) {
            Balloon b = (Balloon) a;
            if (b.getType() == null || b.getAscentRate() < 0) {
                return true;
            }
        } else if (a instanceof Bird) {
            Bird b = (Bird) a;
            if (b.getType() == null || b.getNumber() <= 0) {
                return true;
            }
        } else if (a instanceof Drone) {
            Drone d = (Drone) a;
            if (d.getBrand() == null || d.getNumEngines() <= 0) {
                return true;
            }
        } else if (a instanceof Rocket) {
            Rocket r = (Rocket) a;
            if (r.getAscentRate() < 0 || r.getTrajectory() < 0) {
                return true;
            }
        }
        return false;
    }

    // ----------------------------------------------------------
    /**
     * (Try to) insert an AirObject into the database
     * 
     * @param a An AirObject.
     * @return True iff the AirObject is successfully entered into the database
     */
    public boolean add(AirObject a) {
        // 1. Validate box
        if (a == null || isBoxInvalid(
                a.getXorig(), a.getYorig(), a.getZorig(),
                a.getXwidth(), a.getYwidth(), a.getZwidth())) {
            return false;
        }

        // 2. Validate object-specific params
        if (isObjectParamsInvalid(a)) {
            return false;
        }

        // 3. Check for duplicate name
        if (skipList.find(a.getName()) != null) {
            return false;
        }

        // 4. Insert into Skip List
        skipList.insert(a.getName(), a);

        // 5. Insert into Bintree
        bintree.insert(a);

        return true;
    }

    // ----------------------------------------------------------
    /**
     * The AirObject with this name is deleted from the database (if it exists).
     * 
     * @param name AirObject name.
     * @return A string representing the AirObject, or null if no such name.
     */
    public String delete(String name) {

        if (name == null) {
            return null; // keeping your minimal style
        }

        AirObject removed = (AirObject) skipList.remove(name);

        if (removed == null) {
            return null;
        }

        // Keep tree in sync
        bintree.remove(removed);

        return removed.toString();
    }

    // ----------------------------------------------------------
    /**
     * Return a listing of the Skiplist in alphabetical order on the names.
     * 
     * @return String listing the AirObjects in the Skiplist as specified.
     */
    public String printskiplist() {
        String slString = skipList.toString();

        if (slString.isEmpty()) {
            return "SkipList is empty"; // For testEmpty
        }

        return slString; // For testSampleInput
    }

    // ----------------------------------------------------------
    /**
     * Return a listing of the Bintree nodes in preorder.
     * 
     * @return String listing the Bintree nodes as specified.
     */
    public String printbintree() {
        StringBuilder sb = new StringBuilder();

        sb.append(bintree.print()); // already prints the node lines
        sb.append(String.format(
                "%d bintree nodes printed\n",
                bintree.countNodes()));

        return sb.toString();
    }

    // ----------------------------------------------------------
    /**
     * Print an AirObject with a given name if it exists
     * 
     * @param name The name of the AirObject to print
     * @return String showing the toString for the AirObject if it exists
     *         Return null if there is no such name
     */
    public String print(String name) {
        if (name == null) {
            return null; // For testBadInput
        }

        AirObject obj = skipList.find(name);

        return (obj == null) ? null : obj.toString();
    }

    // ----------------------------------------------------------
    /**
     * Return a listing of the AirObjects found in the database between the
     * min and max values for names.
     * 
     * @param start Minimum of range
     * @param end   Maximum of range
     * @return String listing the AirObjects in the range as specified.
     *         Null if the parameters are bad
     */
    public String rangeprint(String start, String end) {
        // Bad param check
        if (start == null || end == null || start.compareTo(end) > 0) {
            return null;
        }

        String results = skipList.rangeSearch(start, end);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(
                "Found these records in the range %s to %s\r\n", start, end));
        sb.append(results);

        return sb.toString();
    }

    // ----------------------------------------------------------
    /**
     * Return a listing of all collisions between AirObjects bounding boxes
     * that are found in the database.
     * 
     * @return String listing the AirObjects that participate in collisions.
     */
    public String collisions() {
        CollisionResult res = bintree.collisions();

        return String.format(
                "The following collisions exist in the database:\n%s",
                res.getOutput(),
                res.getNodesVisited());
    }

    // ----------------------------------------------------------
    /**
     * Return a listing of all AirObjects whose bounding boxes
     * that intersect the given bounding box.
     * 
     * @param x    Bounding box upper left x
     * @param y    Bounding box upper left y
     * @param z    Bounding box upper left z
     * @param xwid Width
     * @param ywid Height
     * @param zwid Depth
     * @return String listing the AirObjects that intersect the given box.
     *         Return null if any input parameters are bad
     */
    public String intersect(int x, int y, int z, int xwid, int ywid, int zwid) {
        // 1. Validate parameters
        if (isBoxInvalid(x, y, z, xwid, ywid, zwid)) {
            return null;
        }

        // 2. Call the real Bintree intersect
        IntersectResult res = bintree.intersect(x, y, z, xwid, ywid, zwid);

        // 3. Return formatted output (minimal, same style)
        return String.format(
                "The following objects intersect (%d %d %d %d %d %d)\n"
                        + "%s"
                        + "%d nodes were visited in the bintree\n",
                x, y, z, xwid, ywid, zwid,
                res.getMatchesString(),
                res.getNodesVisited());
    }

}