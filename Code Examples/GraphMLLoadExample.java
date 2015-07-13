package algorithms.graphloader;

import algorithms.search.BreadthFirstSearch;
import algorithms.search.UninformedSearch;
import core.components.Edge;
import core.components.Vertex;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;

public class GraphMPLoadExample {
	
	public static void main(String[] args) {
		
		Graph<Vertex, Edge> graph = new SparseGraph<>();
		GraphMLReader graphBuilder = new GraphMLReader();
		graphBuilder.loadGraphFile(graph);
		
		UninformedSearch<Vertex, Edge> bfs = new BreadthFirstSearch<Vertex, Edge>(graph);
		bfs.visualizeSearch(graph.getVertices().iterator().next());
		
		
	}
}
