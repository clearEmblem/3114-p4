import student.TestCase;

/**
 * Tests for BoxUtil class.
 * 
 * @author akashp
 * @author karthikyella
 * @version fall25
 */
public class BoxUtilTest extends TestCase {

    /** Setup. */
    public void setUp() {
        // Nothing
    }

    // ----------------------------------------------------------
    // intersectsStrict
    // ----------------------------------------------------------

    /** Tests strict intersection logic (simple overlap). */
    public void testStrictIntersectsOverlap() {
        // Simple overlap
        assertTrue(BoxUtil.intersectsStrict(
                0, 0, 0, 10, 10, 10,
                5, 5, 5, 10, 10, 10));
    }

    /** Tests strict intersection logic (touching). */
    public void testStrictIntersectsTouchingIsNotIntersection() {
        // Touching X face: Box 1 [0,10), Box 2 [10, 20)
        assertFalse(BoxUtil.intersectsStrict(
                0, 0, 0, 10, 10, 10,
                10, 0, 0, 10, 10, 10));
        // Touching Y face
        assertFalse(BoxUtil.intersectsStrict(
                0, 0, 0, 10, 10, 10,
                0, 10, 0, 10, 10, 10));
        // Touching Z face
        assertFalse(BoxUtil.intersectsStrict(
                0, 0, 0, 10, 10, 10,
                0, 0, 10, 10, 10, 10));
    }

    /** Tests strict intersection logic (disjoint). */
    public void testStrictIntersectsDisjoint() {
        assertFalse(BoxUtil.intersectsStrict(
                0, 0, 0, 10, 10, 10,
                20, 20, 20, 10, 10, 10));
    }

    // ----------------------------------------------------------
    // regionIntersectsBoxStrict
    // ----------------------------------------------------------

    /** Tests region vs box strict intersection. */
    public void testRegionIntersectsBoxStrict() {
        // Region: [0, 0, 0] dims (100, 100, 100)
        // Box: [10, 10, 10] dims (10, 10, 10) -> true
        assertTrue(BoxUtil.regionIntersectsBoxStrict(
                0, 0, 0, 100, 100, 100,
                10, 10, 10, 10, 10, 10));

        // Touching region boundary -> false
        // Region x=[0, 100). Box starts at 100 -> false
        assertFalse(BoxUtil.regionIntersectsBoxStrict(
                0, 0, 0, 100, 100, 100,
                100, 10, 10, 10, 10, 10));
    }

    // ----------------------------------------------------------
    // pointInRegion
    // ----------------------------------------------------------

    /** Tests pointInRegion logic. */
    public void testPointInRegion() {
        assertTrue(BoxUtil.pointInRegion(
                10, 10, 10, 0, 0, 0, 100, 100, 100)); // Inside

        // Boundaries
        assertTrue(BoxUtil.pointInRegion(
                0, 0, 0, 0, 0, 0, 100, 100, 100)); // Inclusive Start
        assertFalse(BoxUtil.pointInRegion(
                100, 0, 0, 0, 0, 0, 100, 100, 100)); // Exclusive End (Size)
    }

    // ----------------------------------------------------------
    // commonIntersectionStrict
    // ----------------------------------------------------------

    /** Tests common intersection calculation. */
    public void testCommonIntersectionStrict() {
        AirObject a = new Drone("a", 0, 0, 0, 20, 20, 20, "D", 1);
        AirObject b = new Drone("b", 10, 10, 10, 20, 20, 20, "D", 1);
        AirObject c = new Drone("c", 15, 15, 15, 20, 20, 20, "D", 1);

        AirObject[] objs = { a, b, c };
        int[] res = BoxUtil.commonIntersectionStrict(objs, 3);
        assertNotNull(res);
        // Common box should be [15, 15, 15] to ...
        // Expected overlap starts: max(0, 10, 15) = 15
        // Expected overlap ends: min(20, 30, 35) = 20
        // Result dim = 20 - 15 = 5.
        assertEquals(15, res[0]);
        assertEquals(15, res[1]);
        assertEquals(15, res[2]);
        assertEquals(5, res[3]);
        assertEquals(5, res[4]);
        assertEquals(5, res[5]);
    }

    /** Tests no common intersection. */
    public void testNoCommonIntersection() {
        AirObject a = new Drone("a", 0, 0, 0, 10, 10, 10, "D", 1);
        AirObject b = new Drone("b", 20, 20, 20, 10, 10, 10, "D", 1);
        AirObject[] objs = { a, b };
        assertNull(BoxUtil.commonIntersectionStrict(objs, 2));
    }
}
