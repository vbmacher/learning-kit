#!/usr/bin/python
# -*- coding: utf8 -*-

import csv
import googlemaps
import sys

DISTRICT = 'Názov okresu'
VILLAGE = 'Názov obce'
LAT = 'LAT'
LON = 'LON'


def load_districts(csv_file):
  dbv = {}
  g = {}

  with open(csv_file, 'rb') as c:
    reader = csv.DictReader(c, delimiter=',', quotechar='"')
    for row in reader:
      dist = row[DISTRICT][6:]
      vil = row[VILLAGE]

      dbv[vil] = dist
      g[vil] = (row[LAT], row[LON])
      if dist not in g.keys():
        g[dist] =(0,0)
  return dbv,g


if __name__ == "__main__":
  if len(sys.argv) < 3:
    print("Usage: okresy.py csvfile index")
    sys.exit(1)

  index = int(sys.argv[2])
  dbv,g = load_districts(sys.argv[1])
  districts = map(lambda (f,s):f,  filter(lambda (k, v): k == v, dbv.iteritems()))
  
  os = []
  ds = []

  origins = []
  dests = []

  for k,v in dbv.iteritems():
    if (g[k] == (0,0)):
      continue

    origins += (map(float, g[k]),)

    if g[v] == (0,0):
      dests += [v]
    else:
      dests += (map(float, g[v]),)
    if len(origins) >= 10:
      os += [origins]
      ds += [dests]
      origins = []
      dests = []

  os += [origins]
  ds += [dests]

  key = '' # your google client key
  client = googlemaps.Client(key)

  json = client.distance_matrix(os[index], ds[index])
  print(json)


