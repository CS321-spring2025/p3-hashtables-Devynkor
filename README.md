Project 3: Experiments with Hashing
Author: Devyn Korbers
Course: CS321 Section 002, Spring 25

Overview
This project implements a custom hash table using open addressing to study how the load factor affects the average number of probes required for collision resolution. The hash table is designed as an abstract class with two concrete subclasses: one for linear probing and one for double hashing. A twin prime generator is used to determine a consistent table capacity across experiments. The program supports three data sources—random numbers, date values, and a large word list—and simulates the insertion of many HashObject instances to analyze performance metrics such as duplicate counts and average probes.

Reflection
Working on this project deepened my understanding of hashing techniques, especially the trade-offs between linear probing and double hashing. One challenge was ensuring that duplicate keys were not reinserted but had their frequency counts updated instead. Another was accurately tracking probe counts—every unique insertion is counted as having used at least one probe even if there’s no collision. Implementing a twin prime generator to standardize the hash table size provided additional insights into performance tuning. Overall, this project improved my skills in object-oriented design, debugging, and performance measurement in Java.

Compiling and Using
Compilation
Clone the repository:

sh
Copy
git clone <repository-url>
cd Project-3-DevynKor
Compile the Java files:

sh
Copy
javac *.java
Running the Program
The main class is HashtableExperiment. Run it with:

sh
Copy
java HashtableExperiment <dataSource> <loadFactor> [<debugLevel>]
dataSource:
1 — Random numbers
2 — Date values
3 — Word list
loadFactor:
The ratio 
𝛼 = 𝑛𝑚
​
  (where n is the number of unique keys inserted and m is the table capacity).
debugLevel:
0 — Summary output
1 — Summary output and dump hash tables to files
2 — Detailed debugging output for each insertion
Example:

java HashtableExperiment 3 0.5 0
This command runs the experiment using the word list at a load factor of 0.50 and prints a summary of the results.

Results
The experiments produced the following outcomes:

Twin Prime Generation:
The hash table capacity was set to 95791, and the effective table size is printed as 47896 (i.e., 95791/2, rounded appropriately).

Insertion and Duplicate Counts:
For a load factor of 0.50, the program attempted approximately 1,305,930 insertions, of which about 1,258,034 were duplicates. This confirms that the word list contains many repeated words.

Average Probes:
The average number of probes per unique insertion was:

1.60 for linear probing
1.39 for double hashing
Performance tests confirm that both collision resolution strategies work efficiently, with double hashing slightly outperforming linear probing in terms of probe count.

AWS Execution
I ran the project on an Amazon AWS EC2 instance using an Amazon Linux AMI free tier micro instance. After setting up my SSH keys and importing them into AWS, I launched the instance, installed Git and Java, and cloned my project repository. I compiled the project and executed the experiments on AWS. A screenshot of the output is included as AWS-Screenshot.jpg.

Submission
For submission, the following files are included in the GitHub repository:

All Java source files
 (CustomHashtable.java, LinearProbing.java, DoubleHashing.java, HashObject.java, TwinPrimeGenerator.java, HashtableExperiment.java) temp testing deleted.
p3-rubric.txt
generate-results.sh
run-tests.sh
README.md
AWS-Screenshot.jpg (or .png)
Do not include:

The test-cases/ subfolder (this folder is in the .gitignore)
The large word-list.txt file
Generated .class files or IDE configuration files (such as .idea/, .iml, .project, etc.)
