package tests.components;

import core.components.Edge;
import core.components.Vertex;
import algorithms.connectivity.PathBasedComponents;
import algorithms.graphloader.GraphMLReader;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;

/**
 * Simple example using the connected component search
 * algorithm.
 * 
 * @author Mike Nowicki
 *
 */
public class ConnectComponentExample {
	
	public void runExample() {

		// Load graph from GraphML file
		Graph<Vertex, Edge> graph = new SparseGraph<>();
		GraphMLReader graphBuilder = new GraphMLReader();
		graphBuilder.loadGraphFile(graph);
		
		PathBasedComponents<Vertex, Edge> pbc = new PathBasedComponents<>();
		pbc.findComponents(graph);
		pbc.visualizeSearch();
		
	}
}
