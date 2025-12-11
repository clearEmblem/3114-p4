/**
 * A simple KVPair class to store key-value pairs in the SkipList.
 * 
 * @param <K> The key (must be Comparable)
 * @param <V> The value
 * 
 * @author akashp
 * @author karthikyella
 * @version fall25
 */
public class KVPair<K extends Comparable<K>, V>
        implements Comparable<KVPair<K, V>> {

    private K key;
    private V value;

    /**
     * Creates a pair.
     * 
     * @param key   The key
     * @param value The value
     */
    public KVPair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Gets the key.
     * 
     * @return The key
     */
    public K getKey() {
        return key;
    }

    /**
     * Gets the value.
     * 
     * @return The value
     */
    public V getValue() {
        return value;
    }

    /**
     * Compares to another KVPair based on key.
     * 
     * @param other The other pair
     * @return Comparison result
     */
    @Override
    public int compareTo(KVPair<K, V> other) {
        return this.key.compareTo(other.getKey());
    }
}