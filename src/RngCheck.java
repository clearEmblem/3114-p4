import java.util.Random;

/**
 * Utility to check RNG behavior for SkipList levels.
 * 
 * @author akashp
 * @author karthikyella
 * @version fall25
 */
public class RngCheck {

    /**
     * Main method.
     * 
     * @param args Command line args.
     */
    public static void main(String[] args) {
        checkMethod("nextBoolean", (r) -> {
            int lvl = 0;
            while (r.nextBoolean()) {
                lvl++;
            }
            return lvl;
        });
        checkMethod("nextInt(2) == 0", (r) -> {
            int lvl = 0;
            while (r.nextInt(2) == 0) {
                lvl++;
            }
            return lvl;
        });
        checkMethod("nextInt() % 2 == 0", (r) -> {
            int lvl = 0;
            while (r.nextInt() % 2 == 0) {
                lvl++;
            }
            return lvl;
        });
    }

    interface RngOp {
        int run(Random r);
    }

    static void checkMethod(String name, RngOp op) {
        Random r = new Random();
        r.setSeed(0xCAFEBEEF);

        System.out.println("Method: " + name);
        // Sequence: B1, Air1, Air2, Ptero, Enterprise
        // Target Levels: 1, 2, 0, 1, ?
        // (Depths: 2, 3, 1, 2, ?)

        int b1 = op.run(r);
        int air1 = op.run(r);
        int air2 = op.run(r);
        int ptero = op.run(r);
        int ent = op.run(r);

        System.out.println("B1: " + b1 + (b1 == 1 ? " MATCH" : ""));
        System.out.println("Air1: " + air1 + (air1 == 2 ? " MATCH" : ""));
        System.out.println("Air2: " + air2 + (air2 == 0 ? " MATCH" : ""));
        System.out.println("Ptero: " + ptero + (ptero == 1 ? " MATCH" : ""));
        System.out.println("Ent: " + ent);
        System.out.println("----------------");
    }
}
