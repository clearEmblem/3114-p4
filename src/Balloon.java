public class Balloon extends AirObject {
    private String type;
    private int ascentRate;

    public Balloon(
        String name,
        int x,
        int y,
        int z,
        int xwid,
        int ywid,
        int zwid,
        String type,
        int ascentRate) {
        super(name, x, y, z, xwid, ywid, zwid);
        this.type = type;
        this.ascentRate = ascentRate;
    }


    public String getType() { return type; }
    public int getAscentRate() { return ascentRate; }


    @Override
    public String toString() {
        return String.format(
            "Balloon %s %d %d %d %d %d %d %s %d",
            getName(),
            getXorig(),
            getYorig(),
            getZorig(),
            getXwidth(),
            getYwidth(),
            getZwidth(),
            type,
            ascentRate);
    }
}