package tests.loaders;

import algorithms.graphloader.GraphMLReader;
import algorithms.search.BreadthFirstSearch;
import core.components.Edge;
import core.components.Vertex;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;

public class GraphMLLoadExample {
	
	public void runExample() {
		
		Graph<Vertex, Edge> graph = new SparseGraph<>();
		GraphMLReader graphBuilder = new GraphMLReader();
		graphBuilder.loadGraph(graph);
		
		BreadthFirstSearch<Vertex, Edge> bfs = new BreadthFirstSearch<>();
		bfs.visualizeSearch(graph, graph.getVertices().iterator().next());
		
		
	}
}
