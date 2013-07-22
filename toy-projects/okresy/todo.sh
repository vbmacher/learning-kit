#!/bin/bash

for file in `ls *.json`; do
  index=`echo $file | cut -d '-' -f 2`
  subindex=`echo $file | cut -d '-' -f 3 | cut -d '.' -f 1`

  python okresy2.py okresy.csv $index $subindex > $file

  echo Sleeping 10 seconds
  sleep 10
done

