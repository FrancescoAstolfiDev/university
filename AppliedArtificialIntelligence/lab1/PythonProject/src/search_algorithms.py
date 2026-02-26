import os
from collections import deque
from src.definition_graph import Graph

PROJECT_ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
FILE_GRAPH = os.path.join(PROJECT_ROOT, "data", "graph.json")
graph_obj = Graph(FILE_GRAPH)

def dfs_path(node, target, visited=None, path=None):
    if visited is None:
        visited = set()
    if path is None:
        path = []
    visited.add(node)
    path.append(node)
    if node == target:
        print(path)
        return True  # stop further recursion
    for neighbor in graph_obj.graph[node]:
        if neighbor not in visited:
            if dfs_path(neighbor, target, visited, path):
                return True
    path.pop()  # backtrack
    return False

def bfs_path(start, goal):
    queue = deque([start])
    visited = set([start])
    parent = {start: None}  # mappa:  each node points to its parent
    while queue:
        node = queue.popleft()
        if node == goal:
            # reconstruction of the path using parent dict
            path = []
            while node is not None:
                path.append(node)
                node = parent[node]
            print (list(reversed(path)))
            return list(reversed(path))

        for neighbor in graph_obj.graph[node]:
            if neighbor not in visited:
                visited.add(neighbor)
                parent[neighbor] = node
                queue.append(neighbor)
    return None

if __name__ == "__main__":
    print("DFS path from (0,1) to (9,18):")
    dfs_path("(0,1)", "(9,18)")
    print("BFS path from (0,1) to (9,18):")
    bfs_path("(0,1)", "(9,18)")
