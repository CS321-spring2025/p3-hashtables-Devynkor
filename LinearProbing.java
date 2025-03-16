public class LinearProbing<T> extends CustomHashtable<T> {

    /**
     * Constructor: Initializes a hash table with the given size using linear probing.
     */
    public LinearProbing(int size) {
        super(size);
    }

    /**
     * Computes the probe index using linear probing.
     * Formula: h1(key) = key.hashCode() % tableSize
     * Linear probing: next index = (index + 1) % tableSize
     */
    @Override
    protected int getProbeIndex(T key, int probeCount) {
        int hash = positiveMod(key.hashCode(), tableSize);
        return (hash + probeCount) % tableSize; // Linear probing logic
    }
}
