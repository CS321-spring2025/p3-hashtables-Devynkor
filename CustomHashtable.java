import java.lang.reflect.Array;
import java.io.PrintWriter;
import java.io.IOException;

public abstract class CustomHashtable<T> {
    protected int tableSize;
    protected HashObject<T>[] table;
    protected int numElements;
    protected int totalProbes;
    private int duplicateCount;  // Track duplicate insertions

    /**
     * Constructor: Initializes hash table with given size.
     */
    @SuppressWarnings("unchecked")
    public CustomHashtable(int size) {
        this.tableSize = size;
        this.table = (HashObject<T>[]) Array.newInstance(HashObject.class, size);
        this.numElements = 0;
        this.totalProbes = 0;
        this.duplicateCount = 0; // Initialize duplicate count
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
                duplicateCount++; // Track duplicates
                return false;
            }
            probeCount++;
            currentIndex = getProbeIndex(key, probeCount);
            if (probeCount >= tableSize) return false; // Prevent infinite loop
        }

        // Count at least one probe for a successful insertion.
        int effectiveProbe = probeCount + 1;
        table[currentIndex] = new HashObject<>(key);
        table[currentIndex].setProbeCount(effectiveProbe);
        numElements++;
        totalProbes += effectiveProbe;
        return true;
    }

    /**
     * Getter for duplicate count.
     */
    public int getDuplicateCount() {
        return duplicateCount;
    }

    /**
     * Setter for duplicate count.
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
     * Dumps the hash table contents to a file.
     * Each line is formatted as:
     * "table[index]:  key frequency probeCount"
     * (with exactly two spaces after the colon and one space between fields).
     */
    public void dumpToFile(String fileName) {
        try (PrintWriter out = new PrintWriter(fileName)) {
            for (int i = 0; i < tableSize; i++) {
                if (table[i] != null) {
                    out.printf("table[%d]:  %s %d %d%n", 
                               i, 
                               table[i].getKey(), 
                               table[i].getFrequency(), 
                               table[i].getProbeCount());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        
    /**
     * Public getter for the number of unique elements inserted.
     */
    public int getNumElements() {
        return numElements;
    }
    
    /**
     * Public getter for the total number of probes used.
     */
    public int getTotalProbes() {
        return totalProbes;
    }
}
