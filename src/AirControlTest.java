import java.util.Random;
import student.TestCase;

/**
 * Tests for the AirControl main logic.
 * 
 * @author akashp
 * @author karthikyella
 * @version fall25
 */
public class AirControlTest extends TestCase {

    /**
     * Sets up the tests that follow. In general, used for initialization
     */
    public void setUp() {
        // Nothing here
    }

    /**
     * Get code coverage of the class declaration.
     */
    public void testRInit() {
        AirControl recstore = new AirControl();
        assertNotNull(recstore);
    }

    // ----------------------------------------------------------
    /**
     * Test syntax: Sample Input/Output
     *
     * @throws Exception if something goes wrong
     */
    public void testSampleInput() throws Exception {
        Random rnd = new Random();
        rnd.setSeed(0xCAFEBEEF);
        WorldDB w = new WorldDB(rnd);

        assertTrue(w.add(new Balloon("B1",
                10, 11, 11, 21, 12, 31, "hot_air", 15)));
        assertTrue(w.add(new AirPlane("Air1",
                0, 10, 1, 20, 2, 30, "USAir", 717, 4)));
        assertTrue(w.add(new Drone("Air2",
                100, 1010, 101, 924, 2, 900, "Droners", 3)));
        assertTrue(w.add(new Bird("pterodactyl",
                0, 100, 20, 10, 50, 50, "Dinosaur", 1)));
        assertFalse(w.add(new Bird("pterodactyl",
                0, 100, 20, 10, 50, 50, "Dinosaur", 1)));
        assertTrue(w.add(new Rocket("Enterprise",
                0, 100, 20, 10, 50, 50, 5000, 99.29)));

        assertFuzzyEquals(
                "Rocket Enterprise 0 100 20 10 50 50 5000 99.29",
                w.delete("Enterprise"));

        assertFuzzyEquals("Airplane Air1 0 10 1 20 2 30 USAir 717 4",
                w.print("Air1"));
        assertNull(w.print("air1"));

        assertFuzzyEquals(
                "I (0, 0, 0, 1024, 1024, 1024) 0\r\n"
                        + "  I (0, 0, 0, 512, 1024, 1024) 1\r\n"
                        + "    Leaf with 3 objects (0, 0, 0, 512, 512, 1024) 2\r\n"
                        + "    (Airplane Air1 0 10 1 20 2 30 USAir 717 4)\r\n"
                        + "    (Balloon B1 10 11 11 21 12 31 hot_air 15)\r\n"
                        + "    (Bird pterodactyl 0 100 20 10 50 50 Dinosaur 1)\r\n"
                        + "    Leaf with 1 objects (0, 512, 0, 512, 512, 1024) 2\r\n"
                        + "    (Drone Air2 100 1010 101 924 2 900 Droners 3)\r\n"
                        + "  Leaf with 1 objects (512, 0, 0, 512, 1024, 1024) 1\r\n"
                        + "  (Drone Air2 100 1010 101 924 2 900 Droners 3)\r\n"
                        + "5 Bintree nodes printed\r\n",
                w.printbintree());

        assertFuzzyEquals(
                "Node has depth 3, Value (null)\r\n"
                        + "node has depth 3, "
                        + "Value (Airplane Air1 0 10 1 20 2 30 USAir 717 4)\r\n"
                        + "Node has depth 1, "
                        + "Value (Drone Air2 100 1010 101 924 2 900 Droners 3)\r\n"
                        + "Node has depth 2, "
                        + "Value (Balloon B1 10 11 11 21 12 31 hot_air 15)\r\n"
                        + "Node has depth 2, "
                        + "Value (Bird pterodactyl 0 100 20 10 50 50 Dinosaur 1)\r\n"
                        + "4 skiplist nodes printed",
                w.printskiplist());

        assertFuzzyEquals(
                "Found these records in the range a to z\r\n"
                        + "Bird pterodactyl 0 100 20 10 50 50 Dinosaur 1\r\n",
                w.rangeprint("a", "z"));
        assertFuzzyEquals(
                "Found these records in the range a to l\r\n",
                w.rangeprint("a", "l"));
        assertNull(w.rangeprint("z", "a"));

        assertFuzzyEquals(
                "The following collisions exist in the database:\r\n"
                        + "In leaf node (0, 0, 0, 512, 512, 1024) 2\r\n"
                        + "(Airplane Air1 0 10 1 20 2 30 USAir 717 4) "
                        + "and (Balloon B1 10 11 11 21 12 31 hot_air 15)\r\n"
                        + "In leaf node (0, 512, 0, 512, 512, 1024) 2\r\n"
                        + "In leaf node (512, 0, 0, 512, 1024, 1024) 1\r\n",
                w.collisions());

        assertFuzzyEquals(
                "The following objects intersect (0 0 0 1024 1024 1024):\r\n"
                        + "In Internal node (0, 0, 0, 1024, 1024, 1024) 0\r\n"
                        + "In Internal node (0, 0, 0, 512, 1024, 1024) 1\r\n"
                        + "In leaf node (0, 0, 0, 512, 512, 1024) 2\r\n"
                        + "Airplane Air1 0 10 1 20 2 30 USAir 717 4\r\n"
                        + "Balloon B1 10 11 11 21 12 31 hot_air 15\r\n"
                        + "Bird pterodactyl 0 100 20 10 50 50 Dinosaur 1\r\n"
                        + "In leaf node (0, 512, 0, 512, 512, 1024) 2\r\n"
                        + "Drone Air2 100 1010 101 924 2 900 Droners 3\r\n"
                        + "In leaf node (512, 0, 0, 512, 1024, 1024) 1\r\n"
                        + "5 nodes were visited in the bintree\r\n",
                w.intersect(0, 0, 0, 1024, 1024, 1024));
    }

    // ----------------------------------------------------------
    /**
     * Test syntax: Check various forms of bad input parameters
     *
     * @throws Exception if something goes wrong
     */
    public void testBadInput() throws Exception {
        Random rnd = new Random();
        rnd.setSeed(0xCAFEBEEF);
        WorldDB w = new WorldDB(rnd);
        assertFalse(w.add(new AirPlane("a", 1, 1, 1, 1, 1, 1, null, 1, 1)));
        assertFalse(w.add(new AirPlane("a", 1, 1, 1, 1, 1, 1, "A", 0, 1)));
        assertFalse(w.add(new AirPlane("a", 1, 1, 1, 1, 1, 1, "A", 1, 0)));
        assertFalse(w.add(new Balloon(null, 1, 1, 1, 1, 1, 1, "hot", 5)));
        assertFalse(w.add(new Balloon("b", -1, 1, 1, 1, 1, 1, "hot", 5)));
        assertFalse(w.add(new Balloon("b", 1, -1, 1, 1, 1, 1, "hot", 5)));
        assertFalse(w.add(new Balloon("b", 1, 1, -1, 1, 1, 1, "hot", 5)));
        assertFalse(w.add(new Balloon("b", 1, 1, 1, 0, 1, 1, "hot", 5)));
        assertFalse(w.add(new Balloon("b", 1, 1, 1, 1, 0, 1, "hot", 5)));
        assertFalse(w.add(new Balloon("b", 1, 1, 1, 1, 1, 0, "hot", 5)));
        assertFalse(w.add(new Balloon("b", 1, 1, 1, 1, 1, 1, null, 5)));
        assertFalse(w.add(new Balloon("b", 1, 1, 1, 1, 1, 1, "hot", -1)));
        assertFalse(w.add(new Bird("b", 1, 1, 1, 1, 1, 1, null, 5)));
        assertFalse(w.add(new Bird("b", 1, 1, 1, 1, 1, 1, "Ostrich", 0)));
        assertFalse(w.add(new Drone("d", 1, 1, 1, 1, 1, 1, null, 5)));
        assertFalse(w.add(new Drone("d", 1, 1, 1, 1, 1, 1, "Droner", 0)));
        assertFalse(w.add(new Rocket("r", 1, 1, 1, 1, 1, 1, -1, 1.1)));
        assertFalse(w.add(new Rocket("r", 1, 1, 1, 1, 1, 1, 1, -1.1)));
        assertFalse(w.add(
                new AirPlane("a", 2000, 1, 1, 1, 1, 1, "Alaska", 1, 1)));
        assertFalse(w.add(
                new AirPlane("a", 1, 2000, 1, 1, 1, 1, "Alaska", 1, 1)));
        assertFalse(w.add(
                new AirPlane("a", 1, 1, 2000, 1, 1, 1, "Alaska", 1, 1)));
        assertFalse(w.add(
                new AirPlane("a", 1, 1, 1, 2000, 1, 1, "Alaska", 1, 1)));
        assertFalse(w.add(
                new AirPlane("a", 1, 1, 1, 1, 2000, 1, "Alaska", 1, 1)));
        assertFalse(w.add(
                new AirPlane("a", 1, 1, 1, 1, 1, 2000, "Alaska", 1, 1)));
        assertFalse(w.add(
                new AirPlane("a", 1000, 1, 1, 1000, 1, 1, "Alaska", 1, 1)));
        assertFalse(w.add(
                new AirPlane("a", 1, 1000, 1, 1, 1000, 1, "Alaska", 1, 1)));
        assertFalse(w.add(
                new AirPlane("a", 1, 1, 1000, 1, 1, 1000, "Alaska", 1, 1)));
        assertNull(w.delete(null));
        assertNull(w.print(null));
        assertNull(w.rangeprint(null, "a"));
        assertNull(w.rangeprint("a", null));
        assertNull(w.intersect(-1, 1, 1, 1, 1, 1));
        assertNull(w.intersect(1, -1, 1, 1, 1, 1));
        assertNull(w.intersect(1, 1, -1, 1, 1, 1));
        assertNull(w.intersect(1, 1, 1, -1, 1, 1));
        assertNull(w.intersect(1, 1, 1, 1, -1, 1));
        assertNull(w.intersect(1, 1, 1, 1, 1, -1));
        assertNull(w.intersect(2000, 1, 1, 1, 1, 1));
        assertNull(w.intersect(1, 2000, 1, 1, 1, 1));
        assertNull(w.intersect(1, 1, 2000, 1, 1, 1));
        assertNull(w.intersect(1, 1, 1, 2000, 1, 1));
        assertNull(w.intersect(1, 1, 1, 1, 2000, 1));
        assertNull(w.intersect(1, 1, 1, 1, 1, 2000));
        assertNull(w.intersect(1000, 1, 1, 1000, 1, 1));
        assertNull(w.intersect(1, 1000, 1, 1, 1000, 1));
        assertNull(w.intersect(1, 1, 1000, 1, 1, 1000));
    }

    // ----------------------------------------------------------
    /**
     * Test empty: Check various returns from commands on empty database
     *
     * @throws Exception if something goes wrong
     */
    public void testEmpty() throws Exception {
        WorldDB w = new WorldDB(null);
        assertNull(w.delete("hello"));
        assertFuzzyEquals("SkipList is empty", w.printskiplist());
        assertFuzzyEquals(
                "E (0, 0, 0, 1024, 1024, 1024) 0\r\n"
                        + "1 Bintree nodes printed\r\n",
                w.printbintree());
        assertNull(w.print("hello"));
        assertFuzzyEquals("Found these records in the range begin to end\n",
                w.rangeprint("begin", "end"));
        assertFuzzyEquals("The following collisions exist in the database:\n",
                w.collisions());
        assertFuzzyEquals(
                "The following objects intersect (1, 1, 1, 1, 1, 1)\n" +
                        "1 nodes were visited in the bintree\n",
                w.intersect(1, 1, 1, 1, 1, 1));
    }

    /**
     * Test SkipList edge cases for remove()
     */
    public void testSkipListRemove() {
        WorldDB w = new WorldDB(new Random(12345)); // Use a fixed seed

        w.add(new Balloon("B1", 10, 10, 10, 10, 10, 10, "hot", 5));
        w.add(new Balloon("A1", 10, 10, 10, 10, 10, 10, "hot", 5));
        w.add(new Balloon("C1", 10, 10, 10, 10, 10, 10, "hot", 5));

        // Test removing the head
        w.delete("A1");
        String skiplist = w.printskiplist();
        assertFalse(skiplist.contains("A1")); // A1 should be gone
        assertTrue(skiplist.contains("B1")); // B1 should still be there

        // Test removing the tail
        w.delete("C1");
        skiplist = w.printskiplist();
        assertFalse(skiplist.contains("C1")); // C1 should be gone

        // Test removing the last item
        w.delete("B1");
        assertFuzzyEquals("SkipList is empty", w.printskiplist());

        // Test removing from empty
        assertNull(w.delete("A1"));
    }

    /** Test delete missing. */
    public void testDeleteMissing() {
        WorldDB db = new WorldDB(new Random(1));

        String msg = db.delete("ghost");
        assertNull(msg);
    }

    /**
     * Test SkipList rangeprint() edge cases
     */
    public void testRangePrintEdges() {
        WorldDB w = new WorldDB(new Random(54321)); // Use a fixed seed

        w.add(new Bird("BIRD", 10, 10, 10, 10, 10, 10, "type", 1));
        w.add(new Bird("FALCON", 10, 10, 10, 10, 10, 10, "type", 1));
        w.add(new Bird("HAWK", 10, 10, 10, 10, 10, 10, "type", 1));
        w.add(new Bird("ZEBRA", 10, 10, 10, 10, 10, 10, "type", 1));

        String header, result;

        // Test a range that finds nothing (in the middle)
        header = "Found these records in the range C to E\r\n";
        result = w.rangeprint("C", "E");
        assertFuzzyEquals(header, result); // Should be empty

        // Test a range that finds nothing (at the end)
        header = "Found these records in the range ZZZ to ZZZZ\r\n";
        result = w.rangeprint("ZZZ", "ZZZZ");
        assertFuzzyEquals(header, result);

        // Test a range that finds only the first item
        header = "Found these records in the range A to C\r\n";
        result = w.rangeprint("A", "C");
        assertTrue(result.contains("BIRD"));
        assertFalse(result.contains("FALCON"));

        // Test a range that finds only the last item
        header = "Found these records in the range Y to ZZZ\r\n";
        result = w.rangeprint("Y", "ZZZ");
        assertTrue(result.contains("ZEBRA"));
        assertFalse(result.contains("HAWK"));

        // Test a range that finds exactly one item
        header = "Found these records in the range FALCON to FALCON\r\n";
        result = w.rangeprint("FALCON", "FALCON");
        assertTrue(result.contains("FALCON"));
        assertFalse(result.contains("BIRD"));
    }

    // Helper to make a DB with deterministic RNG if your constructor accepts
    // Random.
    // If your WorldDB constructor has no Random parameter,
    // just use new WorldDB().
    private WorldDB makeDB() {
        return new WorldDB(new Random(1));
    }

    // -------------------------
    // Empty-state tests
    // -------------------------

    /** Test printskiplist on empty DB. */
    public void testPrintSkipListEmpty() {
        WorldDB db = new WorldDB(new Random(1));
        String out = db.printskiplist();
        assertNotNull(out);
        assertTrue(out.length() > 0);
    }

    /** Test printbintree on empty DB. */
    public void testPrintBintreeEmptyFormat() {
        WorldDB db = makeDB();
        String out = db.printbintree();
        assertNotNull(out);

        // Robust checks that won't break on minor formatting differences
        assertTrue("Actual output was:\n" + out,
                out.toLowerCase().contains("bintree nodes printed"));

        // Optional: if you want a tiny bit more certainty
        assertTrue("Actual output was:\n" + out,
                out.contains("1"));

        assertTrue(out.toLowerCase().contains("bintree nodes printed"));
    }

    // -------------------------
    // Add validation + duplicates
    // -------------------------

    /** Test adding duplicate name. */
    public void testAddValidDroneSucceeds() {
        WorldDB db = makeDB();
        AirObject a = new Drone("a", 0, 0, 0, 10, 10, 10, "DJI", 4);
        assertTrue(db.add(a));
    }

    /** Test duplicate name failure. */
    public void testAddDuplicateNameFails() {
        WorldDB db = makeDB();

        AirObject a1 = new Drone("a", 0, 0, 0, 10, 10, 10, "DJI", 4);
        AirObject a2 = new Drone("a", 20, 20, 20, 10, 10, 10, "DJI", 4);

        assertTrue(db.add(a1));
        assertFalse(db.add(a2));
    }

    /** Test bad box failure. */
    public void testAddBadBoxFails() {
        WorldDB db = makeDB();

        // x out of bounds
        AirObject bad = new Drone("bad", -1, 0, 0, 10, 10, 10, "DJI", 4);
        assertFalse(db.add(bad));
    }

    /** Test bad drone params. */
    public void testAddBadDroneParamsFails() {
        WorldDB db = makeDB();

        // invalid engines (assuming your validation rejects <= 0)
        AirObject bad = new Drone("bad", 0, 0, 0, 10, 10, 10, "DJI", 0);
        assertFalse(db.add(bad));
    }

    // -------------------------
    // print(name) + delete(name)
    // -------------------------

    /** Test print missing. */
    public void testPrintMissingReturnsNull() {
        WorldDB db = makeDB();
        assertNull(db.print("ghost"));
    }

    /** Test print existing. */
    public void testPrintExistingReturnsString() {
        WorldDB db = makeDB();

        AirObject a = new Drone("a", 0, 0, 0, 10, 10, 10, "DJI", 4);
        assertTrue(db.add(a));

        String out = db.print("a");
        assertNotNull(out);
        assertTrue(out.contains("a"));
    }

    /** Test delete missing. */
    public void testDeleteMissingReturnsNull() {
        WorldDB db = makeDB();
        assertNull(db.delete("ghost"));
    }

    /** Test delete existing. */
    public void testDeleteExistingRemovesRecord() {
        WorldDB db = makeDB();

        AirObject a = new Drone("a", 0, 0, 0, 10, 10, 10, "DJI", 4);
        assertTrue(db.add(a));

        String out = db.delete("a");
        assertNotNull(out);
        assertTrue(out.contains("a"));

        assertNull(db.print("a"));
    }

    // -------------------------
    // rangeprint
    // -------------------------

    /** Test range print bad params. */
    public void testRangePrintBadParams() {
        WorldDB db = makeDB();

        assertNull(db.rangeprint(null, "z"));
        assertNull(db.rangeprint("z", null));
        assertNull(db.rangeprint("z", "a")); // start > end
    }

    /** Test range print basic. */
    public void testRangePrintBasic() {
        WorldDB db = makeDB();

        assertTrue(db.add(
                new Drone("alpha", 0, 0, 0, 1, 1, 1, "DJI", 1)));
        assertTrue(db.add(
                new Drone("bravo", 0, 0, 0, 1, 1, 1, "DJI", 1)));
        assertTrue(db.add(
                new Drone("charlie", 0, 0, 0, 1, 1, 1, "DJI", 1)));

        String out = db.rangeprint("alpha", "bravo");
        assertNotNull(out);
        assertTrue(out.contains("alpha"));
        assertTrue(out.contains("bravo"));
        assertFalse(out.contains("charlie"));
    }

    // -------------------------
    // intersect (lightweight)
    // -------------------------

    /** Test intersect bad params. */
    public void testIntersectBadParamsReturnsNull() {
        WorldDB db = makeDB();

        // width 0 should be invalid in your style
        assertNull(db.intersect(0, 0, 0, 0, 10, 10));
    }

    /** Test intersection empty still reports visited. */
    public void testIntersectEmptyStillReportsVisited() {
        WorldDB db = makeDB();

        String out = db.intersect(0, 0, 0, 10, 10, 10);
        assertNotNull(out);
        assertTrue(out.toLowerCase().contains("nodes were visited"));
    }

    /** Test intersection invalid params. */
    public void testIntersectInvalidReturnsNull() {
        WorldDB db = makeDB();
        // Negative width should be invalid per your style
        assertNull(db.intersect(0, 0, 0, -1, 10, 10));
    }

}