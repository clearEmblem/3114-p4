/**
 * Abstract base class for all objects in the air.
 * Implements Comparable to be stored in the Skip List by name[cite: 31].
 */
public abstract class AirObject implements Comparable<AirObject> {

    private String name;
    private int x, y, z, xwid, ywid, zwid;

    /**
     * Constructor for AirObject.
     * @param name Name of the object
     * @param x X-coordinate
     * @param y Y-coordinate
     * @param z Z-coordinate
     * @param xwid X-width
     * @param ywid Y-width
     * @param zwid Z-width
     */
    public AirObject(
        String name,
        int x,
        int y,
        int z,
        int xwid,
        int ywid,
        int zwid) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.xwid = xwid;
        this.ywid = ywid;
        this.zwid = zwid;
    }


    // --- Getters ---
    public int getXorig() { return x; }
    public int getYorig() { return y; }
    public int getZorig() { return z; }
    public int getXwidth() { return xwid; }
    public int getYwidth() { return ywid; }
    public int getZwidth() { return zwid; }
    public String getName() { return name; }

    /**
     * Compares AirObjects based on their name[cite: 31].
     * @param other The other AirObject to compare to.
     * @return Standard compareTo values (<0, 0, >0)
     */
    @Override
    public int compareTo(AirObject other) {
        if (other == null) {
            return 1;
        }
        return this.name.compareTo(other.getName());
    }


    /**
     * Abstract method to be implemented by all subclasses.
     * @return String representation of the object.
     */
    @Override
    public abstract String toString();
}