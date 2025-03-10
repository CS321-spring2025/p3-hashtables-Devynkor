public class HashObject<T> {
    private final T key;       // The key (generic type)
    private int frequency;     // Frequency count (tracks duplicates)
    private int probeCount;    // Number of probes required for insertion

    /**
     * Constructor initializes the HashObject with a key.
     * Frequency starts at 1 because the object is inserted once.
     * Probe count starts at 0.
     */
    public HashObject(T key) {
        this.key = key;
        this.frequency = 1;
        this.probeCount = 0;
    }

    /**
     * Returns the key stored in this object.
     */
    public T getKey() {
        return key;
    }

    /**
     * Returns the frequency count of this object.
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * Increments the frequency count when a duplicate key is inserted.
     */
    public void incrementFrequency() {
        this.frequency++;
    }

    /**
     * Returns the probe count (number of probes needed to insert).
     */
    public int getProbeCount() {
        return probeCount;
    }

    /**
     * Sets the probe count for this object.
     */
    public void setProbeCount(int probeCount) {
        this.probeCount = probeCount;
    }

    /**
     * Overrides equals() to compare HashObjects by key value.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        HashObject<?> other = (HashObject<?>) obj;
        return key.equals(other.key);
    }

    /**
     * Overrides hashCode() to ensure consistent hashing behavior.
     */
    @Override
    public int hashCode() {
        return key.hashCode();
    }

    /**
     * Returns a string representation: "key frequency probeCount"
     */
    @Override
    public String toString() {
        return key + " " + frequency + " " + probeCount;
    }
}
