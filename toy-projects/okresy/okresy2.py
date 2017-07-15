#!/usr/bin/python
# -*- coding: utf8 -*-

from backports import csv
#import unicodecsv as csv
import googlemaps
import sys
import codecs
import io

DISTRICT = u'Názov okresu'
VILLAGE = u'Názov obce'
LAT = 'LAT'
LON = 'LON'


def load_districts(csv_file):
  vbd = {}
  g = {}

  with io.open(csv_file, encoding='utf-8') as c:
    reader = csv.DictReader(c) #,delimiter=',', quotechar='"')
    for row in reader:
      vil = row[VILLAGE]
      dis = row[DISTRICT][6:]

      if dis in vbd.keys():
        vbd[dis] += [vil]
      else:
        vbd[dis] = [vil]

      g[vil + dis] = (row[LAT], row[LON])

  return vbd,g

def names_to_geo(names,geo,dis):
  return [map(float, geo[n+dis]) if (n+dis) in geo else n for n in names]

def split_lists(lst, wanted_items):
  lenlst = len(lst)
  onelen = wanted_items

  if onelen == 0:
    return [lst]

  return [
    lst[i:max(0,min(lenlst,i + onelen))] for i in range(0,lenlst+1,onelen)
  ]

if __name__ == "__main__":
  if len(sys.argv) < 4:
    print("Usage: okresy.py csvfile index subindex")
    sys.exit(1)


  index = int(sys.argv[2])
  subindex = int(sys.argv[3])
  buckets = 20

  vbd,g = load_districts(sys.argv[1])

  districtAt = None
  i = 0
  for k in iter(vbd):
    if i == index:
      districtAt = k
      break
    i += 1

  if districtAt == None:
    sys.exit(0)

  district = names_to_geo([districtAt], g, districtAt)
  villages = names_to_geo(vbd[districtAt], g, districtAt)
  villages = split_lists(villages, buckets)
  vilnames = split_lists(vbd[districtAt], buckets)

  if len(villages) <= subindex:
    sys.exit(0)

  villages = villages[subindex]
  vilnames = vilnames[subindex]
  if len(villages) == 0:
    sys.exit(0)

  sys.stderr.write("\n%d.%d Villages: %d" % (index, subindex, len(villages)))
  sys.stderr.write('\nDistrict: %s\n' % districtAt)  

  key = '' # your google client key
  client = googlemaps.Client(key)

  json = client.distance_matrix(villages, district)

  if not 'rows' in json:
    sys.exit(0)

  zipped = zip(
    map(lambda b: b['elements'][0],
    json['rows']),
    json['origin_addresses'],
    vilnames,
    villages
  )
  gmapsdistrict = json['destination_addresses'][0]

  UTF8Writer = codecs.getwriter('utf-8')
  sys.stdout = UTF8Writer(sys.stdout)

  wr = csv.writer(sys.stdout)
#  wr.writerow([
#    VILLAGE, "GPS", VILLAGE + u' (google maps)',
#    DISTRICT, "GPS", DISTRICT + u' (google maps)',
#    u'Čas ľudsky (autom)', u'Čas sekundy (autom)',
#    u'Vzdialenosť ľudsky (autom)', u'Vzdialenosť metre (autom)'
#  ])

  unrec = ()
  for (road, origin, vill, vilgps) in zipped:
    durText = "N/A"
    durValue = "N/A"
    disText = "N/A"
    disValue = "N/A"

    if 'duration' in road:
      durText = road['duration']['text']
      durValue = road['duration']['value']
    else:
      unrec += (vill,)

    if 'distance' in road:
      disText = road['distance']['text']
      disValue = road['distance']['value']
    elif vill not in unrec:
      unrec += (vill,)

    wr.writerow([
      vill, vilgps, origin,
      districtAt, district[0], gmapsdistrict,
      durText, durValue,
      disText, disValue
    ])

  sys.stderr.write("\nUnrecognized villages: %s" % unrec)

