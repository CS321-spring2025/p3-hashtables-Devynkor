import java.lang.reflect.Array;

public abstract class Hashtable<T> {
    protected int tableSize;               // Size of the hash table
    protected HashObject<T>[] table;       // Array to store hash objects
    protected int numElements;             // Number of elements inserted
    protected int totalProbes;             // Total probes across all insertions

    /**
     * Constructor: Initializes hash table with given size.
     */
    @SuppressWarnings("unchecked")
    public Hashtable(int size) {
        this.tableSize = size;
        this.table = (HashObject<T>[]) Array.newInstance(HashObject.class, size);
        this.numElements = 0;
        this.totalProbes = 0;
    }

    /**
     * Inserts a key into the hash table using open addressing.
     */
    public boolean insert(T key) {
        if (numElements >= tableSize) return false; // Table is full

        int probeCount = 0;
        int index = getProbeIndex(key, probeCount); // Subclass defines probing logic

        while (table[index] != null) {
            if (table[index].getKey().equals(key)) {
                table[index].incrementFrequency(); // Duplicate detected
                return false;
            }
            probeCount++;
            index = getProbeIndex(key, probeCount); // Get next probe index
        }

        // Insert new HashObject
        table[index] = new HashObject<>(key);
        table[index].setProbeCount(probeCount);
        numElements++;
        totalProbes += probeCount;
        return true;
    }

    /**
     * Searches for a key in the hash table.
     */
    public boolean search(T key) {
        int probeCount = 0;
        int index = getProbeIndex(key, probeCount);

        while (table[index] != null) {
            if (table[index].getKey().equals(key)) {
                return true;
            }
            probeCount++;
            index = getProbeIndex(key, probeCount);
        }
        return false;
    }

    /**
     * Computes a positive modulus to avoid negative hash indices.
     */
    protected int positiveMod(int dividend, int divisor) {
        int quotient = dividend % divisor;
        return (quotient < 0) ? quotient + divisor : quotient;
    }

    /**
     * Abstract method to be implemented by subclasses for different probing strategies.
     */
    protected abstract int getProbeIndex(T key, int probeCount);

    /**
     * Returns the average number of probes per insertion.
     */
    public double getAverageProbes() {
        return (numElements == 0) ? 0 : (double) totalProbes / numElements;
    }

    /**
     * Dumps the hash table contents.
     */
    public void dumpToFile(String fileName) {
        try (java.io.PrintWriter out = new java.io.PrintWriter(fileName)) {
            for (int i = 0; i < tableSize; i++) {
                if (table[i] != null) {
                    out.println("table[" + i + "]: " + table[i]);
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
