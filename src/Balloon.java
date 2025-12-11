/**
 * Balloon class.
 * 
 * @author akashp
 * @author karthikyella
 * @version fall25
 */
public class Balloon extends AirObject {
    private String type;
    private int ascentRate;

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
     * @param type       Type
     * @param ascentRate Ascent Rate
     */
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

    /** @return type */
    public String getType() {
        return type;
    }

    /** @return ascent rate */
    public int getAscentRate() {
        return ascentRate;
    }

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