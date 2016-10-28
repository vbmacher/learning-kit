#!/bin/python3

import sys


a0,a1,a2 = input().strip().split(' ')
a0,a1,a2 = [int(a0),int(a1),int(a2)]
b0,b1,b2 = input().strip().split(' ')
b0,b1,b2 = [int(b0),int(b1),int(b2)]

def p(a,b):
    if a == b:
        return (0,0)
    return (0,1) if (a < b) else (1,0)

a,b = p(a0,b0)
c,d = p(a1,b1)
e,f = p(a2,b2)

print(a+c+e, b+d+f)



