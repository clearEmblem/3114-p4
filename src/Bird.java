public class Bird extends AirObject {
    private String type;
    private int number;

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


    public String getType() { return type; }
    public int getNumber() { return number; }


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