class MyTestingClass {
    private int id;
    private String name;

    public MyTestingClass(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "MyTestingClass{id=" + id + ", name='" + name + "'}";
    }

    // Custom hashCode method (not using Objects.hash())
    @Override
    public int hashCode() {
        // Using prime numbers to create better distribution
        final int prime = 31;
        int result = 17;
        result = prime * result + id;
        result = prime * result + (name != null ? name.length() : 0);
        if (name != null) {
            for (int i = 0; i < name.length(); i++) {
                result = prime * result + name.charAt(i);
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        MyTestingClass other = (MyTestingClass) obj;
        if (id != other.id) return false;
        return name != null ? name.equals(other.name) : other.name == null;
    }
}

class Student {
    private String name;
    private int grade;

    public Student(String name, int grade) {
        this.name = name;
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Student{name='" + name + "', grade=" + grade + "}";
    }
}

public class HashTableTest {
    public static void main(String[] args) {
        // Choose a prime number for table size to improve distribution
        int tableSize = 997; // Prime number close to 1000
        MyHashTable<MyTestingClass, Student> table = new MyHashTable<>(tableSize);

        // Add random 10000 elements
        for (int i = 0; i < 10000; i++) {
            MyTestingClass key = new MyTestingClass(i, "Student" + i);
            Student value = new Student("Student" + i, (int)(Math.random() * 100));
            table.put(key, value);
        }

        // Count elements in each bucket to analyze distribution
        int[] bucketCounts = new int[tableSize];

        // We'll need to recreate the keys and calculate their hash values
        for (int i = 0; i < 10000; i++) {
            MyTestingClass key = new MyTestingClass(i, "Student" + i);
            int hashValue = key.hashCode();
            int bucketIndex = Math.abs(hashValue) % tableSize;
            bucketCounts[bucketIndex]++;
        }

        // Print statistics about bucket distribution
        int emptyCounts = 0;
        int maxItems = 0;
        int totalChains = 0;

        System.out.println("\nBucket Distribution Analysis:");
        System.out.println("---------------------------");

        for (int i = 0; i < tableSize; i++) {
            if (bucketCounts[i] == 0) {
                emptyCounts++;
            }
            if (bucketCounts[i] > maxItems) {
                maxItems = bucketCounts[i];
            }
            if (bucketCounts[i] > 0) {
                totalChains++;
            }
        }

        // Calculate standard deviation to measure uniformity
        double mean = 10000.0 / tableSize;
        double sumSquaredDiff = 0;

        for (int i = 0; i < tableSize; i++) {
            double diff = bucketCounts[i] - mean;
            sumSquaredDiff += diff * diff;
        }

        double standardDeviation = Math.sqrt(sumSquaredDiff / tableSize);

        System.out.println("Total elements: 10000");
        System.out.println("Table size: " + tableSize);
        System.out.println("Empty buckets: " + emptyCounts + " (" + (emptyCounts * 100.0 / tableSize) + "%)");
        System.out.println("Maximum items in a bucket: " + maxItems);
        System.out.println("Average chain length: " + (10000.0 / totalChains));
        System.out.println("Expected average (if uniform): " + mean);
        System.out.println("Standard deviation: " + standardDeviation);
        System.out.println("Uniformity coefficient (lower is better): " + (standardDeviation / mean));

        // Print histogram of bucket sizes (optional - gives visual representation)
        printHistogram(bucketCounts, tableSize);

        // Test other hash table methods
        MyTestingClass testKey = new MyTestingClass(500, "Student500");
        Student retrievedStudent = table.get(testKey);
        System.out.println("\nTesting get(): " + retrievedStudent);

        System.out.println("Testing contains(): " + table.contains(retrievedStudent));

        Student removedStudent = table.remove(testKey);
        System.out.println("Testing remove(): " + removedStudent);

        System.out.println("Testing get() after remove: " + table.get(testKey));
    }

    // Helper method to print a histogram of bucket sizes
    private static void printHistogram(int[] bucketCounts, int tableSize) {
        System.out.println("\nBucket Size Distribution:");
        System.out.println("------------------------");

        // Define ranges for histogram
        int[] ranges = {0, 1, 2, 3, 4, 5, 10, 15, 20, 25, 30, Integer.MAX_VALUE};
        int[] counts = new int[ranges.length - 1];

        // Count buckets in each range
        for (int i = 0; i < tableSize; i++) {
            int size = bucketCounts[i];
            for (int j = 0; j < ranges.length - 1; j++) {
                if (size >= ranges[j] && size < ranges[j + 1]) {
                    counts[j]++;
                    break;
                }
            }
        }

        // Print histogram
        for (int i = 0; i < counts.length; i++) {
            String range;
            if (i == counts.length - 1) {
                range = ranges[i] + "+";
            } else {
                range = ranges[i] == ranges[i + 1] - 1 ?
                        String.valueOf(ranges[i]) :
                        ranges[i] + "-" + (ranges[i + 1] - 1);
            }

            System.out.printf("%-6s: %5d (%5.2f%%) ",
                    range, counts[i], (counts[i] * 100.0 / tableSize));

            // Print visual bar
            int barLength = counts[i] * 50 / tableSize;
            for (int j = 0; j < barLength; j++) {
                System.out.print("■");
            }
            System.out.println();
        }
    }
}