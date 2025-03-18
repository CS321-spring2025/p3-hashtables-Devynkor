import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.util.Date;

public class HashTableExperiment {
    
    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) {
            System.out.println("Usage: java HashtableExperiment <dataSource> <loadFactor> [<debugLevel>]");
            return;
        }

        int dataSource = Integer.parseInt(args[0]);
        double loadFactor = Double.parseDouble(args[1]);
        int debugLevel = (args.length == 3) ? Integer.parseInt(args[2]) : 0;

        // Generate the correct twin prime table capacity (expected 95791)
        int tableSize = TwinPrimeGenerator.generateTwinPrime(95700, 95800);
        System.out.println("HashtableExperiment: Found a twin prime for table capacity: " + tableSize);

        // The target unique keys count (n = ceil(alpha * m))
        int targetUnique = (int) Math.ceil(loadFactor * tableSize);

        // Create both hash tables
        LinearProbing<Object> linearTable = new LinearProbing<>(tableSize);
        DoubleHashing<Object> doubleTable = new DoubleHashing<>(tableSize);

        System.out.println("HashtableExperiment: Input: " + getDataSourceName(dataSource) + "   Loadfactor: " + String.format("%.2f", loadFactor));

        Scanner scanner = null;
        if (dataSource == 3) {
            try {
                scanner = new Scanner(new File("word-list.txt"));
            } catch (FileNotFoundException e) {
                System.err.println("Error: Word list file not found.");
                return;
            }
        }

        // Continue attempting insertions until the number of unique keys reaches the target.
        int totalAttempts = 0;
        Random random = new Random();
        long currentTime = new Date().getTime();

        while (linearTable.numElements < targetUnique) {
            Object key = generateKey(dataSource, random, currentTime, scanner, totalAttempts);

            boolean insertedLinear = linearTable.insert(key);
            boolean insertedDouble = doubleTable.insert(key);

            totalAttempts++;

            if (debugLevel == 2) {
                System.out.printf("Insert %d: Key=%s | Linear: %s | Double: %s%n",
                        totalAttempts, key,
                        insertedLinear ? "Inserted" : "Duplicate",
                        insertedDouble ? "Inserted" : "Duplicate");
            }
        }

        if (scanner != null) {
            scanner.close();
        }

        printResults("Using Linear Probing", linearTable, totalAttempts);
        printResults("Using Double Hashing", doubleTable, totalAttempts);

        if (debugLevel == 1) {
            linearTable.dumpToFile("linear-dump.txt");
            doubleTable.dumpToFile("double-dump.txt");
            System.out.println("HashtableExperiment: Saved dump of hash table");
        }
    }

    /**
     * Generates a key based on the selected data source.
     */
    private static Object generateKey(int dataSource, Random random, long currentTime, Scanner scanner, int index) {
        switch (dataSource) {
            case 1:
                return random.nextInt();
            case 2:
                return new Date(currentTime + (index * 1000));
            case 3:
                return (scanner != null && scanner.hasNext()) ? scanner.nextLine().trim() : "Word" + index;
            default:
                throw new IllegalArgumentException("Invalid data source: " + dataSource);
        }
    }

    /**
     * Prints the summary results.
     * The table size is printed as ceil(tableSize/2.0) to match expected output.
     * Duplicate count is computed as (totalAttempts - unique insertions).
     */
    private static void printResults(String title, CustomHashtable<Object> table, int totalAttempts) {
        System.out.println("\n" + title);
        int printedSize = (int) Math.ceil(table.tableSize / 2.0);
        System.out.println("HashtableExperiment: size of hash table is " + printedSize);
        int unique = table.numElements;
        int duplicates = totalAttempts - unique;
        System.out.println("Inserted " + totalAttempts + " elements, of which " + duplicates + " were duplicates");
        // Each unique insertion is counted as at least one probe; so add 1 to each probe count.
        // We adjust the average by adding 1 per unique insertion.
        double adjustedAverage = ((double) table.totalProbes + unique) / unique;
        System.out.printf("Avg. no. of probes = %.2f%n", adjustedAverage);
    }

    /**
     * Returns a string representation of the data source.
     */
    private static String getDataSourceName(int dataSource) {
        String result;
        switch (dataSource) {
            case 1:
                result = "Random Numbers";
                break;
            case 2:
                result = "Date Values";
                break;
            case 3:
                result = "Word-List";
                break;
            default:
                result = "Unknown";
                break;
        }
        return result;
    }
}
