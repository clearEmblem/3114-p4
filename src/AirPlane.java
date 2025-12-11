/**
 * AirPlane class.
 * 
 * @author akashp
 * @author karthikyella
 * @version fall25
 */
public class AirPlane extends AirObject {
    private String carrier;
    private int flightNum;
    private int numEngines;

    /**
     * Constructor for AirPlane.
     * 
     * @param name       Name
     * @param x          X
     * @param y          Y
     * @param z          Z
     * @param xwid       W
     * @param ywid       H
     * @param zwid       D
     * @param carrier    Carrier
     * @param flightNum  Flight Num
     * @param numEngines # Engines
     */
    public AirPlane(
            String name,
            int x,
            int y,
            int z,
            int xwid,
            int ywid,
            int zwid,
            String carrier,
            int flightNum,
            int numEngines) {
        super(name, x, y, z, xwid, ywid, zwid);
        this.carrier = carrier;
        this.flightNum = flightNum;
        this.numEngines = numEngines;
    }

    /** @return carrier */
    public String getCarrier() {
        return carrier;
    }

    /** @return flight number */
    public int getFlightNum() {
        return flightNum;
    }

    /** @return number of engines */
    public int getNumEngines() {
        return numEngines;
    }

    @Override
    public String toString() {
        return String.format(
                "Airplane %s %d %d %d %d %d %d %s %d %d",
                getName(),
                getXorig(),
                getYorig(),
                getZorig(),
                getXwidth(),
                getYwidth(),
                getZwidth(),
                carrier,
                flightNum,
                numEngines);
    }
}