/**
 * Drone class.
 * 
 * @author akashp
 * @author karthikyella
 * @version fall25
 */
public class Drone extends AirObject {
    private String brand;
    private int numEngines;

    /**
     * Constructor.
     * 
     * @param name       Name
     * @param x          X
     * @param y          Y
     * @param z          Z
     * @param xwid       W
     * @param ywid       H
     * @param zwid       D
     * @param brand      Brand
     * @param numEngines # Engines
     */
    public Drone(
            String name,
            int x,
            int y,
            int z,
            int xwid,
            int ywid,
            int zwid,
            String brand,
            int numEngines) {
        super(name, x, y, z, xwid, ywid, zwid);
        this.brand = brand;
        this.numEngines = numEngines;
    }

    /** @return brand */
    public String getBrand() {
        return brand;
    }

    /** @return number of engines */
    public int getNumEngines() {
        return numEngines;
    }

    @Override
    public String toString() {
        return String.format(
                "Drone %s %d %d %d %d %d %d %s %d",
                getName(),
                getXorig(),
                getYorig(),
                getZorig(),
                getXwidth(),
                getYwidth(),
                getZwidth(),
                brand,
                numEngines);
    }
}