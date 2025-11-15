public class Rocket extends AirObject {
    private int ascentRate;
    private double trajectory; // [cite: 28]

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


    public int getAscentRate() { return ascentRate; }
    public double getTrajectory() { return trajectory; }


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