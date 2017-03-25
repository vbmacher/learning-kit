#!/bin/bash

for index in {62..79}; do
  for subindex in {0..50}; do
    python okresy2.py data $index $subindex > "10-$index-$subindex.csv"

    WC=`cat 10-$index-$subindex.csv | wc -l`
    if [[ $WC -eq 0 ]]; then
      break
    fi

    echo "Sleeping 10 seconds"
    sleep 10
  done
done

