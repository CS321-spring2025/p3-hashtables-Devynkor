import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.util.Date;

public class HashtableExperiment {
    
    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) {
            System.out.println("Usage: java HashtableExperiment <dataSource> <loadFactor> [<debugLevel>]");
            return;
        }

        int dataSource = Integer.parseInt(args[0]);
        double loadFactor = Double.parseDouble(args[1]);
        int debugLevel = (args.length == 3) ? Integer.parseInt(args[2]) : 0;

        // Generate the twin prime table size
        int tableSize = TwinPrimeGenerator.generateTwinPrime(95500, 96000);
        System.out.println("HashtableExperiment: Found a twin prime table capacity: " + tableSize);

        int numElements = (int) Math.ceil(loadFactor * tableSize);

        LinearProbing<Object> linearTable = new LinearProbing<>(tableSize);
        DoubleHashing<Object> doubleTable = new DoubleHashing<>(tableSize);

        System.out.println("HashtableExperiment: Input: " + getDataSourceName(dataSource) + "   Loadfactor: " + loadFactor);

        insertData(linearTable, doubleTable, dataSource, numElements, debugLevel);

        // Output summary results
        printResults("Using Linear Probing", linearTable);
        printResults("Using Double Hashing", doubleTable);

        // Dump files for debug level 1
        if (debugLevel == 1) {
            linearTable.dumpToFile("linear-dump.txt");
            doubleTable.dumpToFile("double-dump.txt");
            System.out.println("HashtableExperiment: Saved dump of hash table");
        }
    }

    /**
     * Inserts data into both hash tables based on the selected data source.
     */
    private static void insertData(LinearProbing<Object> linearTable, DoubleHashing<Object> doubleTable, int dataSource, int numElements, int debugLevel) {
        Random random = new Random();
        long currentTime = new Date().getTime();

        try (Scanner scanner = (dataSource == 3) ? new Scanner(new File("word-list.txt")) : null) {
            for (int i = 0; i < numElements; i++) {
                Object key = generateKey(dataSource, random, currentTime, scanner, i);

                boolean insertedLinear = linearTable.insert(key);
                boolean insertedDouble = doubleTable.insert(key);

                if (debugLevel == 2) {
                    System.out.printf("Insert %d: Key=%s | Linear: %s | Double: %s\n",
                            i + 1, key, insertedLinear ? "Inserted" : "Duplicate", insertedDouble ? "Inserted" : "Duplicate");
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: Word list file not found.");
        }
    }

    /**
     * Generates a key based on the selected data source.
     */
    private static Object generateKey(int dataSource, Random random, long currentTime, Scanner scanner, int index) {
        switch (dataSource) {
            case 1: return random.nextInt();
            case 2: return new Date(currentTime + (index * 1000)); // Increasing time values by 1 second
            case 3: return (scanner != null && scanner.hasNext()) ? scanner.nextLine() : "Word" + index;
            default: throw new IllegalArgumentException("Invalid data source: " + dataSource);
        }
    }

    /**
     * Prints the summary results.
     */
    private static void printResults(String title, CustomHashtable<Object> table) {
        System.out.println("\n" + title);
        System.out.println("HashtableExperiment: size of hash table is " + (table.tableSize / 2));
        System.out.println("Inserted " + table.numElements + " elements, of which " + (table.numElements - table.totalProbes) + " were duplicates");
        System.out.printf("Avg. no. of probes = %.2f\n", table.getAverageProbes());
    }

    /**
     * Returns a string representation of the data source.
     */
    private static String getDataSourceName(int dataSource) {
        return switch (dataSource) {
            case 1 -> "Random Numbers";
            case 2 -> "Date Values";
            case 3 -> "Word List";
            default -> "Unknown";
        };
    }
}
