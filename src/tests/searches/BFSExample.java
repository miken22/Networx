package tests.searches;

import java.util.List;

import core.components.Edge;
import core.components.Vertex;
import algorithms.graphloader.GraphMLReader;
import algorithms.search.BreadthFirstSearch;
import algorithms.search.UninformedSearch;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;

/**
 * Simple example illustrating the performace of the BFS algorithm.
 * 
 * @author Mike Nowicki
 *
 */
public class BFSExample {

	public void runExample() {
		
		// Load graph from GraphML file
		Graph<Vertex, Edge> graph = new SparseGraph<>();
		GraphMLReader graphBuilder = new GraphMLReader();
		graphBuilder.loadGraphFile(graph);

		// Instantiate search algorithms class
		UninformedSearch<Vertex, Edge> bfs = new BreadthFirstSearch<>(graph);
				
		// Use first node pointed to by iterator as root for example
		Vertex root = graph.getVertices().iterator().next();
		List<Vertex> result = bfs.search(graph, root);
		
		// Show the result of the search
		bfs.visualizeSearch(graph, root);
		
		// Print the nodes
		System.out.println("The vertices were visitted in the following order:");
		for (Vertex v : result) {
			System.out.println(v);
		}
	}
}
