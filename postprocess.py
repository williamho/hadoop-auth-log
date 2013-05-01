#!/usr/bin/python2
from __future__ import print_function
import hashlib
import sys

f = open(sys.argv[1], "r")
for line in f.readlines():
    parts = line.split("\t")
    parts2 = parts[0].split(",")
    parts2.extend(parts[1].split(","))
    #print(parts2)
    parts = [part.strip() for part in parts2]
    #print(hashlib.md5(parts[0]).hexdigest())
    print(str(int(hashlib.md5(parts[0]).hexdigest(),16)),
            str(int(hashlib.md5(parts[1]).hexdigest(),16)),
            parts[2],parts[3],parts[4], sep=" ")
