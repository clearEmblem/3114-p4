public class AirPlane extends AirObject {
    private String carrier;
    private int flightNum;
    private int numEngines;

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


    public String getCarrier() { return carrier; }
    public int getFlightNum() { return flightNum; }
    public int getNumEngines() { return numEngines; }


    @Override
    public String toString() {
        // Format based on AirControlTest.java
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