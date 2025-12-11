/**
 * Bird class.
 * 
 * @author akashp
 * @author karthikyella
 * @version fall25
 */
public class Bird extends AirObject {
    private String type;
    private int number;

    /**
     * Constructor.
     * 
     * @param name   Name
     * @param x      X
     * @param y      Y
     * @param z      Z
     * @param xwid   W
     * @param ywid   H
     * @param zwid   D
     * @param type   Type
     * @param number Number
     */
    public Bird(
            String name,
            int x,
            int y,
            int z,
            int xwid,
            int ywid,
            int zwid,
            String type,
            int number) {
        super(name, x, y, z, xwid, ywid, zwid);
        this.type = type;
        this.number = number;
    }

    /** @return type */
    public String getType() {
        return type;
    }

    /** @return number */
    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return String.format(
                "Bird %s %d %d %d %d %d %d %s %d",
                getName(),
                getXorig(),
                getYorig(),
                getZorig(),
                getXwidth(),
                getYwidth(),
                getZwidth(),
                type,
                number);
    }
}