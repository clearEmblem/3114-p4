/**
 * Rocket class.
 * 
 * @author akashp
 * @author karthikyella
 * @version fall25
 */
public class Rocket extends AirObject {
    private int ascentRate;
    private double trajectory; // [cite: 28]

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
     * @param ascentRate Ascent
     * @param trajectory Trajectory
     */
    public Rocket(
            String name,
            int x,
            int y,
            int z,
            int xwid,
            int ywid,
            int zwid,
            int ascentRate,
            double trajectory) {
        super(name, x, y, z, xwid, ywid, zwid);
        this.ascentRate = ascentRate;
        this.trajectory = trajectory;
    }

    /** @return ascent rate */
    public int getAscentRate() {
        return ascentRate;
    }

    /** @return trajectory */
    public double getTrajectory() {
        return trajectory;
    }

    @Override
    public String toString() {
        return String.format(
                "Rocket %s %d %d %d %d %d %d %d %.2f",
                getName(),
                getXorig(),
                getYorig(),
                getZorig(),
                getXwidth(),
                getYwidth(),
                getZwidth(),
                ascentRate,
                trajectory);
    }
}