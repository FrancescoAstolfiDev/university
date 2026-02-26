import json
import os

class Graph:
    _instance=None
    def __init__(self, file_path):
        if not hasattr(self, "initialized"):
            self.initialized = True
            self.file_path = file_path
            self.graph = self.load_graph()
    def __new__(cls, *args, **kwargs):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
        return cls._instance
    def load_graph(self):
        """Load graph from file or return empty graph."""
        if os.path.exists(self.file_path):
            try:
                with open(self.file_path, "r") as f:
                    content = f.read().strip()
                    return json.loads(content) if content else {}
            except json.JSONDecodeError:
                return {}
        return {}

    def save_graph(self):
        """Save graph to file."""
        with open(self.file_path, "w") as f:
            json.dump(self.graph, f, indent=4)

    def add_node(self, node):
        """Add a node if it doesn't exist."""
        if node not in self.graph:
            self.graph[node] = []
            print(f"Node '{node}' added.")
        else:
            print(f"Node '{node}' already exists.")

    def add_edge(self, node1, node2):
        """Add an edge between two nodes (undirected)."""
        self.add_node(node1)
        self.add_node(node2)
        if node2 not in self.graph[node1]:
            self.graph[node1].append(node2)
        if node1 not in self.graph[node2]:
            self.graph[node2].append(node1)

    def display(self):
        """Print the graph in a readable format."""
        print("\nCurrent Graph:")
        for node, neighbors in self.graph.items():
            print(f"{node}: {neighbors}")
        print()
    def get_graph(self):
        return self.graph

if __name__ == "__main__":
    PROJECT_ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
    FILE_GRAPH = os.path.join(PROJECT_ROOT, "data", "graph.json")
    graph_obj = Graph(FILE_GRAPH)
    graph1_obj = Graph("diff address")
    # graph_obj.add_edge("(0,0)", "(0,1)")
    # graph_obj.save_graph()
    graph1_obj.display()
