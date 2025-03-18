#!/bin/sh

# Optional helper function to print a header (currently not used)
function header(){
    output=$1
    for i in {1..80} 
    do
        echo -n "-" >> $output
    done
    echo >> $output
}

echo
echo "Compiling the source code"
echo
javac *.java

if ! test -f HashtableExperiment.class
then
    echo
    echo "HashtableExperiment.class not found! Not able to test!!"
    echo
    exit 1
fi

echo
echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
echo "Running tests for word-list for varying load factors"
echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
echo

# Convert test-case files to Unix format (if needed)
dos2unix test-cases/* >& /dev/null

# Set debug level to 1 to dump hash tables at the end of the run.
debugLevel=1

# Define the list of load factors to test
loadFactors="0.5 0.6 0.7 0.8 0.9 0.95 0.99"

# Loop over each load factor
for load in $loadFactors
do
    echo "Running java HashtableExperiment dataSource = 3 loadFactor = $load"
    java HashtableExperiment 3 $load $debugLevel  >> /dev/null
    
    # Normalize line endings in the output dump files
    dos2unix linear-dump.txt double-dump.txt  >& /dev/null

    # Compare linear probing dump
    diff -w -B  linear-dump.txt test-cases/word-list-$load-linear-dump.txt > diff-linear-$load.out
    if test "$?" = 0
    then
        echo "Test PASSED for linear probing and load = $load"
        /bin/rm -f diff-linear-$load.out
    else
        echo "==> Test FAILED for linear probing load = $load!!"
        echo "       Check the file diff-linear-$load.out for differences"
    fi

    # Compare double hashing dump
    diff -w -B  double-dump.txt test-cases/word-list-$load-double-dump.txt > diff-double-$load.out
    if test "$?" = 0
    then
        echo "Test PASSED for double hashing and load = $load"
        /bin/rm -f diff-double-$load.out
    else
        echo "==> Test FAILED for double hashing load = $load!!"
        echo "       Check the file diff-double-$load.out for differences"
    fi

    echo
done
