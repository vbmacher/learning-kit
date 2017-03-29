# python 3

N = int(input())
strings = {}

for i in range(0,N):
    s = input()
    if s in strings:
        strings[s] += 1
    else:
        strings[s] = 1

Q = int(input())
for i in range(0, Q):
    q = input()
    if q in strings:
        print(strings[q])
    else:
        print('0')

