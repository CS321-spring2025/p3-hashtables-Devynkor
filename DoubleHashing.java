public class DoubleHashing<T> extends Hashtable<T> {

    /**
     * Constructor: Initializes a hash table with the given size using double hashing.
     */
    public DoubleHashing(int size) {
        super(size);
    }

    /**
     * Computes the probe index using double hashing.
     * Formula: h1(key) = key.hashCode() % tableSize
     *          h2(key) = 1 + (key.hashCode() % (tableSize - 2))
     * Double hashing: next index = (h1 + i * h2) % tableSize
     */
    @Override
    protected int getProbeIndex(T key, int probeCount) {
        int h1 = positiveMod(key.hashCode(), tableSize);
        int h2 = 1 + positiveMod(key.hashCode(), tableSize - 2); // Secondary hash function

        return (h1 + probeCount * h2) % tableSize; // Double hashing logic
    }
}
