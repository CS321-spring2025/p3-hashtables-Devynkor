import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HashtableTest {
    private LinearProbing<Integer> linearTable;
    private DoubleHashing<Integer> doubleTable;

    @BeforeEach
    void setUp() {
        // Initialize hash tables with a small size for easier testing
        linearTable = new LinearProbing<>(7);
        doubleTable = new DoubleHashing<>(7);
    }

    @Test
    void testInsertAndSearch() {
        assertTrue(linearTable.insert(10));
        assertTrue(linearTable.insert(20));
        assertTrue(linearTable.insert(30));

        assertTrue(linearTable.search(10));
        assertTrue(linearTable.search(20));
        assertTrue(linearTable.search(30));
        assertFalse(linearTable.search(40)); // Should not be found
    }

    @Test
    void testDuplicateInsertion() {
        assertTrue(linearTable.insert(15));
        assertFalse(linearTable.insert(15)); // Duplicate should not insert

        HashObject<Integer> obj = linearTable.table[linearTable.getProbeIndex(15, 0)];
        assertEquals(2, obj.getFrequency()); // Should have frequency count of 2
    }

    @Test
    void testProbing() {
        linearTable.insert(5);
        linearTable.insert(12); // Will cause a collision (if tableSize is 7)

        int probeCount = linearTable.table[linearTable.getProbeIndex(12, 0)].getProbeCount();
        assertTrue(probeCount > 0, "Probing should have occurred");
    }

    @Test
    void testFullTableInsertion() {
        linearTable.insert(1);
        linearTable.insert(2);
        linearTable.insert(3);
        linearTable.insert(4);
        linearTable.insert(5);
        linearTable.insert(6);
        linearTable.insert(7);

        assertFalse(linearTable.insert(8)); // Table is full
    }

    @Test
    void testAverageProbes() {
        linearTable.insert(1);
        linearTable.insert(8); // Causes at least 1 probe

        assertTrue(linearTable.getAverageProbes() > 0);
    }
}

