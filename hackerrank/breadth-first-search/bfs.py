T = int(input())

def twoint():
    return [int(x) for x in input().strip('\r').strip('\n').split(' ')]

def readgraph(M, N):
    graph = {}
    for x in range(0,M):
        graph[x] = []
    for x in range(0,N):
        I,J = twoint()
        graph[I-1] += [J-1]
        graph[J-1] += [I-1]
    return graph

def bfs(graph, s):
    distances = {}
    for x in range(0, len(graph)):
        distances[x] = -1
    
    queue = [s]
    distances[s] = 0
    while queue:
        v = queue[0]
        del queue[0]
        
        for x in graph[v]:
            if distances[x] == -1:
                queue = queue + [x]
                distances[x] = distances[v] + 6
        
    del distances[s]
    
    print(' '.join(map(str,distances.values())))
        
        

for t in range(0, T):
    M, N = twoint()
    graph = readgraph(M,N)
    s = int(input()) - 1
    bfs(graph, s)


