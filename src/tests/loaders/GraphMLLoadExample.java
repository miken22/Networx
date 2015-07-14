package tests.loaders;

import algorithms.graphloader.GraphMLReader;
import algorithms.search.BreadthFirstSearch;
import algorithms.search.UninformedSearch;
import core.components.Edge;
import core.components.Vertex;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;

public class GraphMLLoadExample {
	
	public void runExample() {
		
		Graph<Vertex, Edge> graph = new SparseGraph<>();
		GraphMLReader graphBuilder = new GraphMLReader();
		graphBuilder.loadGraphFile(graph);
		
		UninformedSearch<Vertex, Edge> bfs = new BreadthFirstSearch<Vertex, Edge>(graph);
		bfs.visualizeSearch(graph.getVertices().iterator().next());
		
		
	}
}
