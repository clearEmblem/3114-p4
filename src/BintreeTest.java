import student.TestCase;

/**
 * Tests for the Bintree class.
 * 
 * @author akashp
 * @author karthikyella
 * @version fall25
 */
public class BintreeTest extends TestCase {

    /** Tests empty node becoming leaf. */
    public void testEmptyBecomesLeaf() {
        BinNode root = EmptyLeafNode.getInstance();
        AirObject d = new Drone("d1", 0, 0, 0, 10, 10, 10, "DJI", 4);

        root = root.insert(d, 0, 0, 0, 0, 1024, 1024, 1024);

        assertTrue(root instanceof LeafNode);
        assertEquals(1, root.countNodes());
    }

    /** Tests split behavior. */
    public void testLeafSplitsAfterFourthWithoutCommonIntersection() {
        BinNode root = EmptyLeafNode.getInstance();

        AirObject a = new Drone("a", 0, 0, 0, 10, 10, 10, "B", 1);
        AirObject b = new Drone("b", 100, 0, 0, 10, 10, 10, "B", 1);
        AirObject c = new Drone("c", 0, 100, 0, 10, 10, 10, "B", 1);
        AirObject d = new Drone("d", 0, 0, 100, 10, 10, 10, "B", 1);

        root = root.insert(a, 0, 0, 0, 0, 1024, 1024, 1024);
        root = root.insert(b, 0, 0, 0, 0, 1024, 1024, 1024);
        root = root.insert(c, 0, 0, 0, 0, 1024, 1024, 1024);
        root = root.insert(d, 0, 0, 0, 0, 1024, 1024, 1024);

        assertTrue(root instanceof InternalNode);
        assertTrue(root.countNodes() >= 3);
    }

    /** Tests common intersection suppression of split. */
    public void testNoSplitWhenCommonIntersectionExists() {
        BinNode root = EmptyLeafNode.getInstance();

        AirObject a = new Drone("a", 0, 0, 0, 50, 50, 50, "B", 1);
        AirObject b = new Drone("b", 10, 10, 10, 50, 50, 50, "B", 1);
        AirObject c = new Drone("c", 20, 20, 20, 50, 50, 50, "B", 1);
        AirObject d = new Drone("d", 30, 30, 30, 50, 50, 50, "B", 1);

        root = root.insert(a, 0, 0, 0, 0, 1024, 1024, 1024);
        root = root.insert(b, 0, 0, 0, 0, 1024, 1024, 1024);
        root = root.insert(c, 0, 0, 0, 0, 1024, 1024, 1024);
        root = root.insert(d, 0, 0, 0, 0, 1024, 1024, 1024);

        assertTrue(root instanceof LeafNode); // exception rule
    }

    /** Remove behavior. */
    public void testRemoveFromLeafToEmpty() {
        Bintree t = new Bintree();

        AirObject a = new Drone("Alpha", 0, 0, 0, 10, 10, 10, "B", 1);
        t.insert(a);

        assertTrue(t.print().toString().contains("Alpha"));

        t.remove(a);

        String out = t.print().toString();
        assertFalse(out.contains("Alpha"));
    }

    /** Remove integrity. */
    public void testRemoveDoesNotBreakTree() {
        Bintree t = new Bintree();

        AirObject a = new Drone("Alpha", 0, 0, 0, 10, 10, 10, "B", 1);
        AirObject b = new Drone("Beta", 100, 0, 0, 10, 10, 10, "B", 1);

        t.insert(a);
        t.insert(b);

        t.remove(a);

        String out = t.print().toString();
        assertFalse(out.contains("Alpha"));
        assertTrue(out.contains("Beta"));

    }

    /** Intersect test. */
    public void testIntersectFindsObject() {
        Bintree t = new Bintree();

        AirObject a = new Drone("Alpha", 0, 0, 0, 10, 10, 10, "B", 1);
        AirObject b = new Drone("Beta", 100, 100, 100, 10, 10, 10, "B", 1);

        t.insert(a);
        t.insert(b);

        IntersectResult res = t.intersect(0, 0, 0, 20, 20, 20);

        assertTrue(res.getMatchesString().contains("Alpha"));
        assertFalse(res.getMatchesString().contains("Beta"));
        assertTrue(res.getNodesVisited() > 0);
    }

    /** Adjacency test (should not intersect). */
    public void testIntersectAdjacencyNotCounted() {
        Bintree t = new Bintree();

        AirObject a = new Drone("Alpha", 0, 0, 0, 10, 10, 10, "B", 1);
        t.insert(a);

        // Query touches face at x=10 only
        IntersectResult res = t.intersect(10, 0, 0, 5, 10, 10);

        assertFalse(res.getMatchesString().contains("Alpha"));
    }

    /** Collision test. */
    public void testCollisionFound() {
        Bintree t = new Bintree();

        AirObject a = new Drone("Alpha", 0, 0, 0, 10, 10, 10, "B", 1);
        AirObject b = new Drone("Beta", 5, 5, 5, 10, 10, 10, "B", 1);

        t.insert(a);
        t.insert(b);

        CollisionResult res = t.collisions();
        assertTrue(res.getOutput().contains("Alpha"));
        assertTrue(res.getOutput().contains("Beta"));
        assertTrue(res.getNodesVisited() > 0);
    }

    /** Adjacency collision test (should not collide). */
    public void testAdjacencyNotCollision() {
        Bintree t = new Bintree();

        AirObject a = new Drone("Alpha", 0, 0, 0, 10, 10, 10, "B", 1);
        AirObject b = new Drone("Beta", 10, 0, 0, 5, 10, 10, "B", 1);

        t.insert(a);
        t.insert(b);

        CollisionResult res = t.collisions();
        assertFalse(res.getOutput().contains("collide"));
    }

    /** Duplicate intersect report avoidance. */
    public void testIntersectDoesNotDuplicate() {
        Bintree t = new Bintree();

        // Big object likely overlaps multiple leaves
        AirObject a = new Drone("Alpha", 0, 0, 0, 600, 600, 600, "B", 1);
        t.insert(a);

        IntersectResult res = t.intersect(0, 0, 0, 700, 700, 700);

        String out = res.getMatchesString();

        // Should contain once
        int first = out.indexOf("Alpha");
        int last = out.lastIndexOf("Alpha");
        assertTrue(first != -1);
        assertEquals(first, last);
    }

    /** Deep split test. */
    public void testDeepSplit() {
        Bintree t = new Bintree();
        // Insert objects to force split past level 2 (Z axis)
        // Level 0: X split (512)
        // Level 1: Y split (512)
        // Level 2: Z split (512)

        // All in top-left-front octant
        t.insert(new Drone("A", 0, 0, 0, 10, 10, 10, "D", 1));
        t.insert(new Drone("B", 10, 0, 0, 10, 10, 10, "D", 1));
        t.insert(new Drone("C", 0, 10, 0, 10, 10, 10, "D", 1));
        t.insert(new Drone("D", 0, 0, 10, 10, 10, 10, "D", 1));
        // 4 objects -> split.
        // If no common intersection (A, B, C, D don't all overlap), splits.
        // Verify output shows level 3 nodes or higher.
        // In current print format, level is the last integer on Internal/Leaf.
        String out = t.print().toString();
        // Check for level 3
        assertTrue(out.contains(" 3\r\n") || out.contains(" 3\n"));
    }

    /** Common intersection split suppression logic. */
    public void testCommonIntersectionpreventsSplit() {
        Bintree t = new Bintree();
        // 4 objects all overlapping at (10,10,10)
        t.insert(new Drone("A", 0, 0, 0, 20, 20, 20, "D", 1));
        t.insert(new Drone("B", 0, 0, 0, 20, 20, 20, "D", 1));
        t.insert(new Drone("C", 0, 0, 0, 20, 20, 20, "D", 1));
        t.insert(new Drone("D", 0, 0, 0, 20, 20, 20, "D", 1));

        // Limit is 3. But common intersection exists. Should remain 1 leaf.
        assertEquals(1, t.countNodes());
    }

    /** Merge logic with duplicates. */
    public void testMergeWithDuplicates() {
        Bintree t = new Bintree();
        // Create a scenario where an object spans the split plane
        // Level 0 (X split at 512).
        // Obj S spans 512.
        AirObject s = new Drone("Spanner", 500, 0, 0, 24, 10, 10, "D", 1);

        t.insert(s); // In Left and Right children of Root

        // Add minimal objects to keep it split if we add more
        t.insert(new Drone("L1", 0, 0, 0, 10, 10, 10, "D", 1));
        t.insert(new Drone("L2", 0, 0, 0, 10, 10, 10, "D", 1));
        t.insert(new Drone("R1", 600, 0, 0, 10, 10, 10, "D", 1));

        // Total unique: Spanner, L1, L2, R1 = 4 objects.
        // Root splits.
        // Left has: Spanner, L1, L2 (3 objects)
        // Right has: Spanner, R1 (2 objects)
        // Total nodes: Root + Left + Right = 3 nodes.
        assertEquals(3, t.countNodes());

        // Remove L1. Unique count becomes 3 (Spanner, L2, R1).
        // Should merge into 1 leaf.
        t.remove(new Drone("L1", 0, 0, 0, 10, 10, 10, "D", 1));

        assertEquals(1, t.countNodes());

        // Verify Spanner is present exactly once in output list of leaf
        String out = t.print().toString();
        // Count occurrences of "Spanner"
        int first = out.indexOf("Spanner");
        int last = out.lastIndexOf("Spanner");
        assertTrue(first != -1);
        assertEquals(first, last);
    }
}
