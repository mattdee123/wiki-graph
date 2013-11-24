if [ $# -ne 1 ];
then
    echo "Usage : ./parse.sh [WIKIPEDIA_DUMP.xml]"
else
    ./run-wikigraph.sh slice $1 data/
    echo "SORTING"
    sort -nk 2 -t "," data/links.csv > data/in.csv
    echo "DONE SORTING"
    ./run-wikigraph.sh index data/ index/
    echo "ALL DONE"
fi