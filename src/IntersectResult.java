/**
 * Collects intersection results from the Bintree.
 * 
 * @author akashp
 * @author karthikyella
 * @version fall25
 */
public class IntersectResult {
    private int nodesVisited;
    private StringBuilder matches;

    /** Constructor. */
    public IntersectResult() {
        nodesVisited = 0;
        matches = new StringBuilder();
    }

    /** Increments visited node count. */
    public void incrementVisited() {
        nodesVisited++;
    }

    /** @return visited count. */
    public int getNodesVisited() {
        return nodesVisited;
    }

    /**
     * Adds a matching object if not already seen.
     * 
     * @param obj The finding.
     */
    /**
     * Adds a matching object.
     * 
     * @param obj The finding.
     */
    public void addMatch(AirObject obj) {
        if (obj == null) {
            return;
        }
        matches.append(obj.toString()).append("\n");
    }

    /**
     * Adds a generic message (like a header).
     * 
     * @param msg The message.
     */
    public void addMessage(String msg) {
        matches.append(msg);
    }

    /** @return The matches string. */
    public String getMatchesString() {
        return matches.toString();
    }
}
