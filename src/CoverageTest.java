import student.TestCase;
import java.util.Random;

/**
 * Targeted coverage tests for edge cases.
 * 
 * @author akashp
 * @author karthikyella
 * @version fall25
 */
public class CoverageTest extends TestCase {

    // ----------------------------------------------------------
    // LeafNode specific coverage
    // ----------------------------------------------------------

    /** Tests LeafNode array growth. */
    public void testLeafNodeArrayGrowth() {
        // Create a leaf
        LeafNode leaf = new LeafNode();
        // Insert 6 objects that all overlap perfectly -> prevents split
        // Leaf capacity is 5 init. So 6th insert triggers grow.
        for (int i = 0; i < 6; i++) {
            AirObject a = new Drone(
                    "d" + i, 0, 0, 0, 100, 100, 100, "D", 1);
            leaf = (LeafNode) leaf.insert(
                    a, 0, 0, 0, 0, 1024, 1024, 1024);
        }
        assertEquals(1, leaf.countNodes());
        assertEquals(6, leaf.getCount());
    }

    /** Tests removing non-existent object from LeafNode. */
    public void testLeafRemoveNonExistent() {
        LeafNode leaf = new LeafNode();
        AirObject a = new Drone("d1", 0, 0, 0, 10, 10, 10, "D", 1);
        leaf.insert(a, 0, 0, 0, 0, 1024, 1024, 1024);

        // Remove different object in same region
        AirObject b = new Drone("d2", 0, 0, 0, 10, 10, 10, "D", 1);
        leaf.remove(b, 0, 0, 0, 0, 1024, 1024, 1024);

        assertEquals(1, leaf.getCount());
        assertEquals("d1", leaf.getObjects()[0].getName());
    }

    /** Tests removing object outside LeafNode region. */
    public void testLeafRemoveOutsideRegion() {
        LeafNode leaf = new LeafNode();
        AirObject a = new Drone("d1", 0, 0, 0, 10, 10, 10, "D", 1);
        leaf.insert(a, 0, 0, 0, 0, 1024, 1024, 1024);

        // Try removing object completely outside
        AirObject b = new Drone("d2", 5000, 5000, 5000, 10, 10, 10, "D", 1);
        leaf.remove(b, 0, 0, 0, 0, 1024, 1024, 1024);

        assertEquals(1, leaf.getCount());
    }

    // ----------------------------------------------------------
    // InternalNode specific coverage
    // ----------------------------------------------------------

    /** Tests InternalNode merge suppression when count > 3. */
    public void testInternalMergeFailsTooMany() {
        InternalNode node = new InternalNode();
        // Insert 2 objects left, 2 objects right (unique) -> total 4.
        // Should NOT merge.

        // Left child (x < 512)
        node.insert(new Drone(
                "L1", 0, 0, 0, 10, 10, 10, "D", 1),
                0, 0, 0, 0, 1024, 1024, 1024);
        node.insert(new Drone(
                "L2", 10, 0, 0, 10, 10, 10, "D", 1),
                0, 0, 0, 0, 1024, 1024, 1024);

        // Right child (x >= 512)
        node.insert(new Drone(
                "R1", 600, 0, 0, 10, 10, 10, "D", 1),
                0, 0, 0, 0, 1024, 1024, 1024);
        node.insert(new Drone(
                "R2", 610, 0, 0, 10, 10, 10, "D", 1),
                0, 0, 0, 0, 1024, 1024, 1024);

        assertEquals(3, node.countNodes()); // Root + Left + Right

        // call remove on something non-existent to trigger check
        node.remove(new Drone(
                "Ghost", 0, 0, 0, 10, 10, 10, "D", 1),
                0, 0, 0, 0, 1024, 1024, 1024);

        assertEquals(3, node.countNodes()); // Still 3, merge rejected
    }

    // ----------------------------------------------------------
    // Algorithm coverage (SkipList)
    // ----------------------------------------------------------

    /** Tests bad range search params in SkipList. */
    public void testSkipListRangeBad() {
        SkipList<String, String> sl = new SkipList<>(new Random(1));
        sl.insert("A", "ValA");
        sl.insert("C", "ValC");

        String res = sl.rangeSearch("B", "B");
        assertEquals("", res); // Empty range result

        res = sl.rangeSearch("D", "Z");
        assertEquals("", res);
    }

    // ----------------------------------------------------------
    // InternalNode safety
    // ----------------------------------------------------------

    /** Tests InternalNode safety check for small split size. */
    public void testInternalSplitSzSmall() {
        // Force deeply nested insert
        InternalNode node = new InternalNode();

        // If we call insert with tiny dimensions, should safeguard
        AirObject a = new Drone("a", 0, 0, 0, 1, 1, 1, "D", 1);

        // Level 0, but size 1
        node.insert(a, 0, 0, 0, 0, 1, 1, 1);
        // Should insert into child via safe path (Leaf creation)

        // Let's verify we get a LeafNode back
        BinNode res = node.insert(a, 0, 0, 0, 0, 1, 1, 1);
        assertTrue(res instanceof LeafNode);
        assertEquals(1, ((LeafNode) res).getCount());
    }
}
