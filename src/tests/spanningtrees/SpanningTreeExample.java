package tests.spanningtrees;

import algorithms.graphloader.GraphMLReader;
import algorithms.spanningtree.KruskallMinimalSpanningTree;
import core.components.Edge;
import core.components.Vertex;
import core.visualizer.Visualizer;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;

/**
 * Simple example illustrating how to use the spanning tree
 * algorithm.
 *  
 * @author Mike Nowicki
 *
 */
public class SpanningTreeExample {

	public void runExample() {
		
		// Load graph from GraphML file
		Graph<Vertex, Edge> graph = new SparseGraph<>();
		GraphMLReader graphBuilder = new GraphMLReader();
		graphBuilder.loadGraphFile(graph);
		
		KruskallMinimalSpanningTree<Vertex, Edge> mst = new KruskallMinimalSpanningTree<>();
		// Use JUNG2 generic Forest, in case graph has multiple components
		Forest<Vertex, Edge> tree = mst.findMinimalSpanningTree(graph);
		
		// Visualize tree
		Visualizer.viewTree(tree);
		
	}
	
}
