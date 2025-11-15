import java.util.Random;

/**
 * A Skip List implementation.
 * @param <K> Key (Comparable)
 * @param <V> Value
 */
public class SkipList<K extends Comparable<K>, V> {

    /**
     * Inner node class for SkipList.
     */
    private static class SLNode<K extends Comparable<K>, V> {
        public KVPair<K, V> pair;
        public SLNode<K, V>[] forward;
        public int level;

        @SuppressWarnings("unchecked")
        public SLNode(KVPair<K, V> pair, int level) {
            this.pair = pair;
            this.level = level;
            this.forward = (SLNode<K, V>[])new SLNode[level + 1];
        }
    }

    private SLNode<K, V> head;
    private int size;
    private Random rnd;
    private int maxLevel;

    /**
     * Constructor for SkipList.
     * @param r Random generator to use.
     */
    @SuppressWarnings("unchecked")
    public SkipList(Random r) {
        this.rnd = r;
        this.size = 0;
        this.maxLevel = 0;
        // Head node has null pair and initial level 0
        head = new SLNode<K, V>(null, 0);
    }


    /**
     * Picks a random level for a new node.
     * @return The random level.
     */
    private int randomLevel() {
        int level = 0;
        // rnd.nextInt() % 2 == 0 is a common way
        while (rnd.nextBoolean()) {
            level++;
        }
        return level;
    }


    /**
     * Adjusts the head node's level if the new node is taller.
     * @param newLevel The new level to grow to.
     */
    @SuppressWarnings("unchecked")
    private void adjustHead(int newLevel) {
        if (newLevel > maxLevel) {
            SLNode<K, V>[] oldForward = head.forward;
            head.level = newLevel;
            head.forward = (SLNode<K, V>[])new SLNode[newLevel + 1];
            
            // Copy old pointers
            System.arraycopy(oldForward, 0, head.forward, 0, oldForward.length);
            
            maxLevel = newLevel;
        }
    }


    /**
     * Insert a new KVPair into the SkipList.
     * @param key The key
     * @param value The value
     */
    @SuppressWarnings("unchecked")
    public void insert(K key, V value) {
        int newLevel = randomLevel();
        adjustHead(newLevel); // Make sure head is tall enough

        KVPair<K, V> newPair = new KVPair<>(key, value);
        SLNode<K, V> newNode = new SLNode<>(newPair, newLevel);
        
        // Find update points (predecessors)
        SLNode<K, V>[] update = (SLNode<K, V>[])new SLNode[maxLevel + 1];
        SLNode<K, V> curr = head;

        for (int i = maxLevel; i >= 0; i--) {
            while (curr.forward[i] != null
                && curr.forward[i].pair.compareTo(newPair) < 0) {
                curr = curr.forward[i];
            }
            update[i] = curr;
        }

        // Relink pointers
        for (int i = 0; i <= newLevel; i++) {
            newNode.forward[i] = update[i].forward[i];
            update[i].forward[i] = newNode;
        }
        size++;
    }


    /**
     * Find the value associated with a key.
     * @param key The key to search for.
     * @return The value, or null if not found.
     */
    public V find(K key) {
        SLNode<K, V> curr = head;
        for (int i = maxLevel; i >= 0; i--) {
            while (curr.forward[i] != null
                && curr.forward[i].pair.getKey().compareTo(key) < 0) {
                curr = curr.forward[i];
            }
        }
        
        curr = curr.forward[0]; // Move to potential match
        
        if (curr != null && curr.pair.getKey().compareTo(key) == 0) {
            return curr.pair.getValue();
        }
        return null; // Not found
    }


    /**
     * Remove a KVPair by its key.
     * @param key The key to remove.
     * @return The value of the removed pair, or null if not found.
     */
    @SuppressWarnings("unchecked")
    public V remove(K key) {
        SLNode<K, V>[] update = (SLNode<K, V>[])new SLNode[maxLevel + 1];
        SLNode<K, V> curr = head;

        // Find predecessors
        for (int i = maxLevel; i >= 0; i--) {
            while (curr.forward[i] != null
                && curr.forward[i].pair.getKey().compareTo(key) < 0) {
                curr = curr.forward[i];
            }
            update[i] = curr;
        }

        curr = curr.forward[0]; // The node to be removed

        // Check if node was found
        if (curr == null || curr.pair.getKey().compareTo(key) != 0) {
            return null; // Not found
        }

        // Relink
        for (int i = 0; i <= curr.level; i++) {
            if (update[i].forward[i] == curr) {
                update[i].forward[i] = curr.forward[i];
            }
        }
        
        size--;
        
        // Note: We don't shrink the head level, it's generally not required.
        
        return curr.pair.getValue();
    }


    /**
     * Generates a string representation of the SkipList.
     * @return The string.
     */
    @Override
    public String toString() {
        if (size == 0) {
            return ""; // WorldDB will handle "is empty"
        }
        
        StringBuilder sb = new StringBuilder();
        SLNode<K, V> curr = head.forward[0]; // Start at first real node
        
        int count = 0;
        while (curr != null) {
            sb.append(String.format("Node has depth %d, Value (%s)\r\n",
                curr.level + 1, // Test file seems to use 1-based depth
                curr.pair.getValue().toString()));
            curr = curr.forward[0];
            count++;
        }
        sb.append(String.format("%d skiplist nodes printed\r\n", count));
        return sb.toString();
    }


    /**
     * Finds all values with keys in the given range.
     * @param start Start key (inclusive)
     * @param end End key (inclusive)
     * @return A formatted string of results.
     */
    public String rangeSearch(K start, K end) {
        StringBuilder sb = new StringBuilder();
        SLNode<K, V> curr = head;

        // Find the node *at or after* start
        for (int i = maxLevel; i >= 0; i--) {
            while (curr.forward[i] != null
                && curr.forward[i].pair.getKey().compareTo(start) < 0) {
                curr = curr.forward[i];
            }
        }

        curr = curr.forward[0]; // First potential candidate

        // Iterate at level 0
        while (curr != null && curr.pair.getKey().compareTo(end) <= 0) {
            sb.append(curr.pair.getValue().toString());
            sb.append("\r\n");
            curr = curr.forward[0];
        }

        return sb.toString();
    }
}