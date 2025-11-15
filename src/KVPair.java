/**
 * A simple KVPair class to store key-value pairs in the SkipList.
 * @param <K> The key (must be Comparable)
 * @param <V> The value
 */
public class KVPair<K extends Comparable<K>, V> implements Comparable<KVPair<K, V>> {
    private K key;
    private V value;

    public KVPair(K key, V value) {
        this.key = key;
        this.value = value;
    }


    public K getKey() { return key; }
    public V getValue() { return value; }


    @Override
    public int compareTo(KVPair<K, V> other) {
        return this.key.compareTo(other.getKey());
    }
}