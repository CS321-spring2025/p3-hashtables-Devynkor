import java.lang.reflect.Array;
import java.io.PrintWriter;
import java.io.IOException;

public abstract class CustomHashtable<T> {
    protected int tableSize;
    protected HashObject<T>[] table;
    protected int numElements;
    protected int totalProbes;
    private int duplicateCount;  // ✅ New field for duplicate tracking

    /**
     * Constructor: Initializes hash table with given size.
     */
    @SuppressWarnings("unchecked")
    public CustomHashtable(int size) {
        this.tableSize = size;
        this.table = (HashObject<T>[]) Array.newInstance(HashObject.class, size);
        this.numElements = 0;
        this.totalProbes = 0;
        this.duplicateCount = 0; // ✅ Initialize duplicates count
    }

    /**
     * Inserts a key into the hash table using open addressing.
     */
    public boolean insert(T key) {
        if (numElements >= tableSize) return false; // Table is full

        int probeCount = 0;
        int currentIndex = getProbeIndex(key, probeCount); // Subclass defines probing logic

        while (table[currentIndex] != null) {
            if (table[currentIndex].getKey().equals(key)) {
                table[currentIndex].incrementFrequency(); // Duplicate detected
                duplicateCount++; // ✅ Track duplicates
                return false;
            }
            probeCount++;
            currentIndex = getProbeIndex(key, probeCount);
            if (probeCount >= tableSize) return false; // Prevent infinite loop
        }

        // Insert new HashObject
        table[currentIndex] = new HashObject<>(key);
        table[currentIndex].setProbeCount(probeCount);
        numElements++;
        totalProbes += probeCount;
        return true;
    }

    /**
     * Getter for duplicate count.
     */
    public int getDuplicateCount() {
        return duplicateCount;
    }

    /**
     * Setter for duplicate count (for tracking in experiments).
     */
    public void setDuplicateCount(int count) {
        this.duplicateCount = count;
    }

    /**
     * Searches for a key in the hash table.
     */
    public boolean search(T key) {
        int probeCount = 0;
        int currentIndex = getProbeIndex(key, probeCount);

        while (table[currentIndex] != null) {
            if (table[currentIndex].getKey().equals(key)) {
                return true;
            }
            probeCount++;
            currentIndex = getProbeIndex(key, probeCount);
            if (probeCount >= tableSize) return false;
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
        try (PrintWriter out = new PrintWriter(fileName)) {
            for (int i = 0; i < tableSize; i++) {
                if (table[i] != null) {
                    out.printf("table[%d]: %s%n", i, table[i]); // Ensures no extra spaces
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
