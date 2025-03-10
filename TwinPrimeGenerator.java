public class TwinPrimeGenerator {

    /**
     * Generates the largest twin prime in the given range [min, max].
     * @param min The lower bound of the search range.
     * @param max The upper bound of the search range.
     * @return The larger twin prime in the pair, or -1 if none found.
     */
    public static int generateTwinPrime(int min, int max) {
        for (int n = min; n <= max - 2; n++) {
            if (isPrime(n) && isPrime(n + 2)) {
                return n + 2; // Return the larger of the twin primes
            }
        }
        return -1; // No twin primes found in the range
    }

    /**
     * Checks if a number is prime.
     * @param num The number to check.
     * @return True if the number is prime, false otherwise.
     */
    private static boolean isPrime(int num) {
        if (num < 2) return false;
        if (num == 2 || num == 3) return true;
        if (num % 2 == 0 || num % 3 == 0) return false;

        // Check divisibility from 5 to sqrt(num) using 6k ± 1 optimization
        for (int i = 5; i * i <= num; i += 6) {
            if (num % i == 0 || num % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }
}

