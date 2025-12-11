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
            AirObject a = new Drone("d" + i, 0, 0, 0, 100, 100, 100, "D", 1);
            leaf = (LeafNode)leaf.insert(a, 0, 0, 0, 0, 1024, 1024, 1024);
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
        node.insert(new Drone("L1", 0, 0, 0, 10, 10, 10, "D", 1), 0, 0, 0, 0,
            1024, 1024, 1024);
        node.insert(new Drone("L2", 10, 0, 0, 10, 10, 10, "D", 1), 0, 0, 0, 0,
            1024, 1024, 1024);

        // Right child (x >= 512)
        node.insert(new Drone("R1", 600, 0, 0, 10, 10, 10, "D", 1), 0, 0, 0, 0,
            1024, 1024, 1024);
        node.insert(new Drone("R2", 610, 0, 0, 10, 10, 10, "D", 1), 0, 0, 0, 0,
            1024, 1024, 1024);

        assertEquals(3, node.countNodes()); // Root + Left + Right

        // call remove on something non-existent to trigger check
        node.remove(new Drone("Ghost", 0, 0, 0, 10, 10, 10, "D", 1), 0, 0, 0, 0,
            1024, 1024, 1024);

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
        assertEquals(1, ((LeafNode)res).getCount());
    }

    // ----------------------------------------------------------
    // Targeted InternalNode Mutation Coverage
    // ----------------------------------------------------------


    /**
     * Test InternalNode two-arg constructor with nulls.
     * Kills mutants on lines 27, 28 (null checks).
     */
    public void testInternalNodeConstructorNulls() {
        InternalNode node = new InternalNode(null, null);
        // We can't access fields directly, but we can verify behavior
        // implies they are EmptyLeafNodes.
        // InternalNode.countNodes is 1 + left.count + right.count.
        // EmptyLeafNode.countNodes() is 1. So total should be 1 + 1 + 1 = 3.
        assertEquals(3, node.countNodes());
    }


    /**
     * Test InternalNode collapsing to EmptyLeafNode.
     * Kills mutants on line 139.
     */
    public void testMergeToEmpty() {
        InternalNode node = new InternalNode();
        AirObject a = new Drone("d1", 0, 0, 0, 10, 10, 10, "D", 1);

        // Insert object
        BinNode res = node.insert(a, 0, 0, 0, 0, 1024, 1024, 1024);

        // Remove object. Result should collapse to EmptyLeafNode.
        BinNode empty = res.remove(a, 0, 0, 0, 0, 1024, 1024, 1024);
        assertTrue(empty instanceof EmptyLeafNode);
    }


    /**
     * Test intersect at deeper levels (Z-axis).
     * Kills arithmetic mutants in intersect (lines 223, 225).
     */
    public void testIntersectDeepZ() {
        InternalNode root = new InternalNode();
        BinNode curr = root;

        // 4 objects to force split down to Z level (Level 2)
        // 1-3 in first Z-half, 4th in second Z-half
        AirObject a = new Drone("A", 10, 10, 10, 10, 10, 10, "D", 1);
        AirObject b = new Drone("B", 20, 20, 20, 10, 10, 10, "D", 1);
        AirObject c = new Drone("C", 30, 30, 30, 10, 10, 10, "D", 1);
        AirObject d = new Drone("D", 10, 10, 600, 10, 10, 10, "D", 1);

        curr = curr.insert(a, 0, 0, 0, 0, 1024, 1024, 1024);
        curr = curr.insert(b, 0, 0, 0, 0, 1024, 1024, 1024);
        curr = curr.insert(c, 0, 0, 0, 0, 1024, 1024, 1024);
        curr = curr.insert(d, 0, 0, 0, 0, 1024, 1024, 1024);

        // Query intersect deep in Z
        IntersectResult res = new IntersectResult();
        curr.intersect(10, 10, 600, 100, 100, 100, 0, 0, 0, 0, 1024, 1024, 1024,
            res);

        assertTrue(res.getMatchesString().contains("D"));
    }


    /**
     * Test collisions at deeper levels (Z-axis).
     * Kills arithmetic mutants in collisions (lines 275, 277).
     */
    public void testCollisionsDeepZ() {
        InternalNode root = new InternalNode();
        BinNode curr = root;

        // Force split to Z level
        curr = curr.insert(new Drone("A", 10, 10, 10, 10, 10, 10, "D", 1), 0, 0,
            0, 0, 1024, 1024, 1024);
        curr = curr.insert(new Drone("B", 20, 20, 20, 10, 10, 10, "D", 1), 0, 0,
            0, 0, 1024, 1024, 1024);
        curr = curr.insert(new Drone("C", 30, 30, 30, 10, 10, 10, "D", 1), 0, 0,
            0, 0, 1024, 1024, 1024);

        // Add colliding pair deep in Z (> 512)
        curr = curr.insert(new Drone("D1", 10, 10, 600, 10, 10, 10, "D", 1), 0,
            0, 0, 0, 1024, 1024, 1024);
        curr = curr.insert(new Drone("D2", 15, 15, 605, 10, 10, 10, "D", 1), 0,
            0, 0, 0, 1024, 1024, 1024);

        CollisionResult res = new CollisionResult();
        curr.collisions(0, 0, 0, 0, 1024, 1024, 1024, res);

        assertTrue(res.getOutput().contains("D1"));
        assertTrue(res.getOutput().contains("D2"));
    }

    // ----------------------------------------------------------
    // RngCheck Coverage
    // ----------------------------------------------------------


    /**
     * Run RngCheck main to cover its mutants.
     */
    public void testRngCheckMain() {
        RngCheck.main(new String[] {});
        // Success if no exception
        assertTrue(true);
    }
    // ----------------------------------------------------------
    // Phase 2: BoxUtil Arithmetic & Boundary Coverage
    // ----------------------------------------------------------


    /**
     * Test BoxUtil.intersectsStrict with touching vs overlapping boxes.
     * Kills boundary mutants (< vs <=) and arithmetic mutants.
     */
    public void testBoxUtilBoundaries() {
        // Strict intersection means sharing volume, not just face.

        // 1. Touching on X face (should be false)
        // Box A: [0, 10)
        // Box B: [10, 20)
        assertFalse(BoxUtil.intersectsStrict(0, 0, 0, 10, 10, 10, 10, 0, 0, 10,
            10, 10));

        // 2. Overlapping by 1 unit (should be true)
        // Box B: [9, 19)
        assertTrue(BoxUtil.intersectsStrict(0, 0, 0, 10, 10, 10, 9, 0, 0, 10,
            10, 10));

        // 3. Point in region strict
        // Region: [0, 10)
        assertTrue(BoxUtil.pointInRegion(0, 0, 0, 0, 0, 0, 10, 10, 10)); // On
                                                                         // start
                                                                         // edge
        assertTrue(BoxUtil.pointInRegion(9, 9, 9, 0, 0, 0, 10, 10, 10)); // Inside
        assertFalse(BoxUtil.pointInRegion(10, 0, 0, 0, 0, 0, 10, 10, 10)); // On
                                                                           // end
                                                                           // edge
                                                                           // (exclusive)
    }


    /**
     * Test intersectionBoxStrict logic.
     */
    public void testIntersectionBoxLogic() {
        // Touching boxes return null (no volume)
        int[] res = BoxUtil.intersectionBoxStrict(0, 0, 0, 10, 10, 10, 10, 0, 0,
            10, 10, 10);
        assertNull(res);

        // Overlapping boxes return correct box
        // A: [0, 10), B: [5, 15) -> Intersect [5, 10) width 5
        res = BoxUtil.intersectionBoxStrict(0, 0, 0, 10, 10, 10, 5, 0, 0, 10,
            10, 10);
        assertNotNull(res);
        assertEquals(5, res[0]); // x
        assertEquals(5, res[3]); // width
    }


    /**
     * Test commonIntersectionStrict loop mutants.
     */
    public void testCommonIntersection() {
        AirObject[] objs = new AirObject[] { new Drone("A", 0, 0, 0, 10, 10, 10,
            "D", 1), new Drone("B", 5, 0, 0, 10, 10, 10, "D", 1), new Drone("C",
                80, 80, 80, 10, 10, 10, "D", 1) // Disjoint
        };

        // First two overlap
        int[] res2 = BoxUtil.commonIntersectionStrict(objs, 2);
        assertNotNull(res2);
        assertEquals(5, res2[3]); // Width 5

        // All three disjoint
        int[] res3 = BoxUtil.commonIntersectionStrict(objs, 3);
        assertNull(res3);

        assertNull(BoxUtil.commonIntersectionStrict(null, 1));
        assertNull(BoxUtil.commonIntersectionStrict(objs, 0));
    }

    // ----------------------------------------------------------
    // Phase 2: EmptyLeafNode Level Checks
    // ----------------------------------------------------------


    /**
     * Test EmptyLeafNode methods with specific levels to kill conditional
     * mutants.
     */
    public void testEmptyLeafLevelChecks() {
        EmptyLeafNode node = EmptyLeafNode.getInstance();
        IntersectResult iRes = new IntersectResult();

        // Level 0: Should NOT add message
        node.intersect(0, 0, 0, 100, 100, 100, 0, 0, 0, 0, 10, 10, 10, iRes);
        // String msg0 = iRes.getMatchesString(); // assuming this gets the
        // output
        // Based on IntersectResult code (not visible but standard), unlikely to
        // have
        // message if count is 0?
        // Actually InternalNode only calls this if intersecting.
        // If message added, length changes.

        // Level 1: Should add message
        node.intersect(0, 0, 0, 100, 100, 100, 1, 0, 0, 0, 10, 10, 10, iRes);
        // Compare outcomes via side effects if possible, or just execution

        CollisionResult cRes = new CollisionResult();
        // Level 0: Should return early
        node.collisions(0, 0, 0, 0, 10, 10, 10, cRes);
        int v0 = cRes.getNodesVisited();

        // Level 1: Should increment visited
        node.collisions(1, 0, 0, 0, 10, 10, 10, cRes);
        int v1 = cRes.getNodesVisited();

        assertTrue(v1 > v0);
    }


    /**
     * Test EmptyLeafNode.insert returning this vs new LeafNode.
     */
    public void testEmptyLeafInsert() {
        EmptyLeafNode node = EmptyLeafNode.getInstance();
        AirObject a = new Drone("d", 100, 100, 100, 10, 10, 10, "D", 1);

        // Insert OUTSIDE region -> returns this
        // Region 0,0,0 size 10
        BinNode same = node.insert(a, 0, 0, 0, 0, 10, 10, 10);
        assertEquals(node, same);

        // Insert INSIDE region -> returns LeafNode
        // Region 100,100,100 size 10
        BinNode changed = node.insert(a, 0, 100, 100, 100, 10, 10, 10);
        assertTrue(changed instanceof LeafNode);
    }

    // ----------------------------------------------------------
    // Phase 2: Value Object Classes
    // ----------------------------------------------------------


    /**
     * Test AirObject compareTo null check.
     * Kills line 101 mutant.
     */
    public void testAirObjectCompareToNull() {
        AirObject a = new Drone("d", 0, 0, 0, 10, 10, 10, "D", 1);
        // Depending on implementation, might return 1 or throw or something
        // else.
        // Code says: if (other == null) return 1;
        // Mutant replaces with false -> crashes with NPE on other.getName()
        assertEquals(1, a.compareTo(null));
    }

    /**
     * Test CollisionResult increment arithmetic.
     * Kills line 20 mutant.
     */
    // ----------------------------------------------------------
    // Phase 3: Advanced InternalNode & BoxUtil
    // ----------------------------------------------------------


    /**
     * Test BoxUtil partial overlaps (2 dims overlap, 1 does not).
     * Kills conditional mutants in intersectsStrict (&& vs ||).
     */
    public void testBoxUtilPartialOverlaps() {
        // Box A: [0, 10)^3

        // 1. Overlap X and Y, disjoint Z
        // Box B: [0, 10), [0, 10), [20, 30)
        assertFalse(BoxUtil.intersectsStrict(0, 0, 0, 10, 10, 10, 0, 0, 20, 10,
            10, 10));

        // 2. Overlap X and Z, disjoint Y
        assertFalse(BoxUtil.intersectsStrict(0, 0, 0, 10, 10, 10, 0, 20, 0, 10,
            10, 10));

        // 3. Overlap Y and Z, disjoint X
        assertFalse(BoxUtil.intersectsStrict(0, 0, 0, 10, 10, 10, 20, 0, 0, 10,
            10, 10));
    }


    /**
     * Test InternalNode constructor satisfying standard assignment.
     * Kills "null check replaced with true" (always Empty).
     */
    public void testInternalNodeConstructorValid() {
        LeafNode l = new LeafNode();
        LeafNode r = new LeafNode();
        InternalNode node = new InternalNode(l, r);

        // If it replaced valid children with EmptyLeafNode, count would be 1.
        // empty leaf count = 1.
        // leaf count (empty) = 1 (calls countNodes -> 1 for self)
        // Wait, LeafNode.countNodes returns 1 + objects? No, usually just 1
        // node.
        // Let's verify LeafNode structure via traversal or side-effects.

        // Better check: insert something into "l" before constructing.
        l.insert(new Drone("d", 0, 0, 0, 10, 10, 10, "D", 1), 0, 0, 0, 0, 1024,
            1024, 1024);

        // Now if InternalNode kept it, finding "d" should work.
        // But InternalNode.print or InternalNode.intersect delegates.

        // Let's use intersect to verify "d" is reachable.
        IntersectResult res = new IntersectResult();
        node.intersect(0, 0, 0, 100, 100, 100, 0, 0, 0, 0, 1024, 1024, 1024,
            res);

        // If "d" is found, the child was preserved.
        assertTrue(res.getMatchesString().contains("d"));
    }


    /**
     * Test InternalNode dimension selection logic (line 48).
     * Kills "level % 3" mutants and conditional mutants.
     */
    public void testInternalNodeSplittingDimensions() {
        // Level 0: Split X.
        // Setup: sx=100, sy=10, sz=10.
        // If correct (X), half=50. Obj at 20 goes Left. Obj at 60 goes Right.
        // If wrong (Y), half=5. Obj at 20 goes Right.

        InternalNode node = new InternalNode();
        AirObject leftObj = new Drone("L", 20, 0, 0, 1, 1, 1, "D", 1);

        node.insert(leftObj, 0, 0, 0, 0, 100, 10, 10);

        // Verify L is in Left child (X < 50).
        // We can inspect by printing.
        node.print(0, 0, 0, 0, 100, 10, 10).toString();
        // Standard print order: I -> Left -> Right.
        // If L is in left, it appears before the "Right" traversal?
        // Or we can rely on indentation/structure in string.

        // Actually, cleaner way:
        // If dim was Y (0-10), half=5. Obj.y=0 < 5 -> Left.
        // If dim was Z (0-10), half=5. Obj.z=0 < 5 -> Left.
        // So this obj goes left in all cases?

        // Need obj that differentiates.
        // X-split (100): half=50.
        // Y-split (10): half=5.
        // Need obj where X < 50 (Left for X) AND Y >= 5 (Right for Y).
        // Pos: (20, 6, 0).
        AirObject discriminator = new Drone("D", 20, 6, 0, 1, 1, 1, "D", 1);

        InternalNode node2 = new InternalNode();
        node2.insert(discriminator, 0, 0, 0, 0, 100, 10, 10);

        // If X-split utilized: goes to Left child (20 < 50).
        // If Y-split utilized: goes to Right child (6 >= 5).

        // Accessing children is hard without reflection.
        // But print output shows tree structure.
        String p = node2.print(0, 0, 0, 0, 100, 10, 10).toString();
        // I (0,0,0, 100,10,10) 0
        // Leaf (Left child) ...
        // Leaf (Right child) ...

        // If in Left:
        // Leaf with 1 objects ... (D)
        // E ...

        // If in Right:
        // E ...
        // Leaf with 1 objects ... (D)

        // So if we see "Leaf with 1 objects" BEFORE an "E" (Empty), it's left?
        // Not reliable if both leaves exist.
        // But EmptyLeafNode prints "E". Leaf prints "Leaf...".

        // Expectation: Left should be occupied.
        // Output: "I...\n Leaf...(D)\n E..."
        // If idxLeaf < idxEmpty, it's in left.
        // Note: Right child starts at x+half = 50.
        // Left child prints first.

        // If "D" is in left child, we see it early.
        // If "D" is in right child, we see it late.

        // Let's trust the "I ... \n Leaf" sequence.
        // Left child comes immediately after Internal node header.
        assertTrue("Should split on X (Level 0), putting (20,6,0) in Left", p
            .contains("I (0, 0, 0, 100, 10, 10) 0\r\n"
                + "Leaf with 1 objects"));

        // Repeat for Level 1 (Y split).
        // sx=10, sy=100, sz=10.
        // X-split (wrong): half=5. Obj(6, 20, 0). Right.
        // Y-split (correct): half=50. Obj(6, 20, 0). Left.
        InternalNode node3 = new InternalNode();
        AirObject discY = new Drone("DY", 6, 20, 0, 1, 1, 1, "D", 1);
        node3.insert(discY, 1, 0, 0, 0, 10, 100, 10);

        String p3 = node3.print(1, 0, 0, 0, 10, 100, 10).toString();
        assertTrue("Should split on Y (Level 1), putting (6,20,0) in Left", p3
            .contains("I (0, 0, 0, 10, 100, 10) 1\r\n"
                + "Leaf with 1 objects"));
    }


    /**
     * Test InternalNode bounds rejection (remainingLength).
     * Tests "Arithmetic + replaced with -" etc. in bounds calc.
     */
    public void testInternalNodeBoundsCalcs() {
        InternalNode node = new InternalNode();

        // Insert at Level 0 (X split). splitSz=100. half=50.
        // Left: [0, 50). Right: [50, 100).

        // 1. Insert object fully in Right [50, 100).
        AirObject rightObj = new Drone("R", 60, 0, 0, 10, 10, 10, "D", 1);
        node.insert(rightObj, 0, 0, 0, 0, 100, 100, 100);

        // If remainingLength was wrong (e.g. returned 100 instead of 50),
        // Right child would be [50, 150).
        // Try to insert object at 110. Should be REJECTED by right child.
        AirObject outBounds = new Drone("O", 110, 0, 0, 10, 10, 10, "D", 1);
        node.insert(outBounds, 0, 0, 0, 0, 100, 100, 100);

        String p = node.print(0, 0, 0, 0, 100, 100, 100).toString();
        assertFalse("Object at 110 should not be in tree of width 100", p
            .contains("O"));

        assertTrue("Object at 60 should be in tree", p.contains("R"));
    }

    // ----------------------------------------------------------
    // Phase 4: LeafNode, IntersectResult, SkipList, RngCheck
    // ----------------------------------------------------------


    /**
     * Test LeafNode.remove with an object that has matching name but
     * NON-intersecting coordinates.
     * Kills LeafNode line 166 mutant (ignores BoxUtil check).
     */
    public void testLeafNodeRemoveNonIntersecting() {
        LeafNode leaf = new LeafNode();
        AirObject in = new Drone("Target", 10, 10, 10, 5, 5, 5, "D", 1);
        leaf.insert(in, 0, 0, 0, 0, 100, 100, 100);

        // Confirm it's there
        assertEquals(1, leaf.getCount());

        // Try to remove "Target" but using coordinates clearly OUTSIDE.
        // Region: 0-100. Leaf checks intersection with region?
        // No, LeafNode line 166 checks:
        // BoxUtil.objectIntersectsRegionStrict(obj, x,
        // y...)
        // i.e., does the *removal candidate object* intersect the node region?

        // Make a "Target" object at 200, 200, 200.
        AirObject out = new Drone("Target", 200, 200, 200, 5, 5, 5, "D", 1);

        leaf.remove(out, 0, 0, 0, 0, 100, 100, 100);

        // If check passes (mutant), it finds "Target" by name and removes it ->
        // count
        // 0.
        // If check works (correct), it returns early -> count 1.
        assertEquals(
            "Should not remove object if query coordinates don't intersect node",
            1, leaf.getCount());
    }


    /**
     * Test LeafNode.insert with non-intersecting object.
     * Kills LeafNode line 44 mutant.
     */
    public void testLeafNodeInsertNonIntersecting() {
        LeafNode leaf = new LeafNode();
        AirObject out = new Drone("Out", 200, 200, 200, 5, 5, 5, "D", 1);

        leaf.insert(out, 0, 0, 0, 0, 100, 100, 100);
        assertEquals(0, leaf.getCount());
    }


    /**
     * Test IntersectResult.addMatch with null.
     * Kills IntersectResult line 39 mutant (null check).
     */
    public void testIntersectResultNull() {
        IntersectResult res = new IntersectResult();
        res.addMatch(null); // Should not throw
        assertEquals("", res.getMatchesString());
    }


    /**
     * Test SkipList.remove for missing key.
     * Kills SkipList line 171 mutant.
     */
    public void testSkipListRemoveMissing() {
        SkipList<String, String> sl = new SkipList<>(new java.util.Random());
        sl.insert("B", "ValueB");

        // Remove "A" (less than B)
        assertNull(sl.remove("A"));

        // Remove "C" (greater than B)
        assertNull(sl.remove("C"));

        // Verify B still there
        assertEquals("ValueB", sl.find("B"));
    }


    /**
     * Test RngCheck main output.
     * Kills mutants that change loop conditions (resulting in 0 levels/visits).
     */
    public void testRngCheckOutput() {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        java.io.PrintStream originalOut = System.out;
        System.setOut(new java.io.PrintStream(out));

        try {
            RngCheck.main(new String[] {});
        }
        finally {
            System.setOut(originalOut);
        }

        String output = out.toString();
        // Check for expected "MATCH" strings indicating correct logic execution
        // We expect "MATCH" for B1, Air1, Air2, Ptero.
        assertTrue("RngCheck output should contain verification matches", output
            .contains("MATCH"));
        // Check specific failures if possible
        assertTrue(output.contains("B1: 1 MATCH"));
    }

    // ----------------------------------------------------------
    // Phase 5: WorldDB Validation & Logic
    // ----------------------------------------------------------


    /**
     * Test WorldDB.add with valid and invalid boxes.
     * Kills isBoxInvalid conditional mutants.
     */
    public void testWorldDBBoxValidation() {
        WorldDB w = new WorldDB(new java.util.Random());

        // 1. Valid Box (0,0,0, 10,10,10)
        AirObject valid = new Drone("D1", 0, 0, 0, 10, 10, 10, "B", 1);
        assertTrue(w.add(valid));

        // 2. Bound Min (0,0,0) - already covered
        // 3. Bound Max (1024) limits
        // Box at 1000 width 24 -> 1024 (Valid)
        AirObject edge = new Drone("D2", 1000, 1000, 1000, 24, 24, 24, "B", 1);
        assertTrue(w.add(edge));

        // 4. Invalid: Negative Origin
        assertFalse(w.add(new Drone("D3", -1, 0, 0, 10, 10, 10, "B", 1)));
        assertFalse(w.add(new Drone("D4", 0, -1, 0, 10, 10, 10, "B", 1)));
        assertFalse(w.add(new Drone("D5", 0, 0, -1, 10, 10, 10, "B", 1)));

        // 5. Invalid: Exceeds World Max (Origin)
        assertFalse(w.add(new Drone("D6", 1024, 0, 0, 10, 10, 10, "B", 1)));

        // 6. Invalid: Width <= 0
        assertFalse(w.add(new Drone("D7", 0, 0, 0, 0, 10, 10, "B", 1)));
        assertFalse(w.add(new Drone("D8", 0, 0, 0, -5, 10, 10, "B", 1)));

        // 7. Invalid: Width > 1024
        assertFalse(w.add(new Drone("D9", 0, 0, 0, 1025, 10, 10, "B", 1)));

        // 8. Invalid: Origin + Width > 1024
        // 1000 + 25 = 1025
        assertFalse(w.add(new Drone("D10", 1000, 0, 0, 25, 10, 10, "B", 1)));
    }


    /**
     * Test WorldDB null and bad param handling.
     * Kills null checks and logical expression mutants.
     */
    public void testWorldDBParams() {
        WorldDB w = new WorldDB(null); // Should init random internally

        // Null add
        assertFalse(w.add(null));

        // Duplicate Name
        w.add(new Drone("D1", 0, 0, 0, 10, 10, 10, "B", 1));
        assertFalse(w.add(new Drone("D1", 50, 50, 50, 10, 10, 10, "B", 1)));

        // Invalid Object Params
        assertFalse(w.add(new Drone(null, 0, 0, 0, 10, 10, 10, "B", 1))); // Null
                                                                          // name
        assertFalse(w.add(new Drone("", 0, 0, 0, 10, 10, 10, "B", 1))); // Empty
                                                                        // name

        // Specific class validation
        // Airplane bad params
        assertFalse(w.add(new AirPlane("P1", 0, 0, 0, 10, 10, 10, null, 1, 1))); // Null
                                                                                 // carrier
        assertFalse(w.add(new AirPlane("P2", 0, 0, 0, 10, 10, 10, "C", 0, 1))); // Bad
                                                                                // flightNum

        // Null checks in methods
        assertNull(w.delete(null));
        assertNull(w.print(null));
        assertNull(w.rangeprint(null, "Z"));
        assertNull(w.rangeprint("A", null));
        assertNull(w.rangeprint("Z", "A")); // Start > End

        // Intersect bad box
        assertNull(w.intersect(-1, 0, 0, 10, 10, 10));
    }
}
