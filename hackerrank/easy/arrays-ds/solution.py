#!/bin/python3

import sys

n = int(input().strip())

arr = []
for t in input().strip().split(' '):
  arr = [t] + arr

print(' '.join(arr))


