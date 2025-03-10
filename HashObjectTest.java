import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HashObjectTest {

    @Test
    public void testConstructorAndGetKey() {
        HashObject<Integer> obj = new HashObject<>(42);
        assertEquals(42, obj.getKey());
    }

    @Test
    public void testInitialFrequencyAndProbeCount() {
        HashObject<String> obj = new HashObject<>("hello");
        assertEquals(1, obj.getFrequency());  // Initial frequency should be 1
        assertEquals(0, obj.getProbeCount()); // Initial probe count should be 0
    }

    @Test
    public void testIncrementFrequency() {
        HashObject<Double> obj = new HashObject<>(3.14);
        obj.incrementFrequency();
        obj.incrementFrequency();
        assertEquals(3, obj.getFrequency());  // Initial count is 1, so 1 + 2 = 3
    }

    @Test
    public void testSetAndGetProbeCount() {
        HashObject<Integer> obj = new HashObject<>(99);
        obj.setProbeCount(5);
        assertEquals(5, obj.getProbeCount());

        obj.setProbeCount(10);
        assertEquals(10, obj.getProbeCount());
    }

    @Test
    public void testEqualsMethod() {
        HashObject<String> obj1 = new HashObject<>("test");
        HashObject<String> obj2 = new HashObject<>("test");
        HashObject<String> obj3 = new HashObject<>("different");

        assertTrue(obj1.equals(obj2));  // Same key → should be equal
        assertFalse(obj1.equals(obj3)); // Different key → should not be equal
    }

    @Test
    public void testHashCodeConsistency() {
        HashObject<Integer> obj1 = new HashObject<>(100);
        HashObject<Integer> obj2 = new HashObject<>(100);
        assertEquals(obj1.hashCode(), obj2.hashCode()); // Same key → same hash code
    }

    @Test
    public void testToStringOutput() {
        HashObject<String> obj = new HashObject<>("example");
        obj.setProbeCount(3);
        obj.incrementFrequency();
        String expected = "example 2 3"; // key, frequency, probe count
        assertEquals(expected, obj.toString());
    }
}

