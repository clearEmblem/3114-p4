public class Drone extends AirObject {
    private String brand;
    private int numEngines;

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


    public String getBrand() { return brand; }
    public int getNumEngines() { return numEngines; }


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