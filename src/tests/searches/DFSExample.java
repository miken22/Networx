package tests.searches;

import java.util.List;

import algorithms.graphloader.GraphMLReader;
import algorithms.search.DepthFirstSearch;
import core.components.Edge;
import core.components.Vertex;
import core.visualizer.SimpleVisualizer;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;

public class DFSExample {

	public void runExample() {
		
		// Load graph from GraphML file
		Graph<Vertex, Edge> graph = new SparseGraph<>();
		GraphMLReader graphBuilder = new GraphMLReader();
		graphBuilder.loadGraphFile(graph);
		
		DepthFirstSearch<Vertex, Edge> dfs = new DepthFirstSearch<>(graph);
		
		Vertex root = graph.getVertices().iterator().next();
		List<Vertex> result = dfs.search(root);
		
		System.out.println("The vertices were visitted in the following order:");
		for (Vertex v : result) {
			System.out.println(v);
		}
		
		SimpleVisualizer<Vertex, Edge> visualizer = new SimpleVisualizer<>();
		visualizer.viewGraph(graph, root, false);
		
	}	
	
}
