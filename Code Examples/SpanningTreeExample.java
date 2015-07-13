package tests.examples;

import algorithms.search.KruskallMinimalSpanningTree;
import core.components.Edge;
import core.visualizer.SimpleVisualizer;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;

public class SpanningTreeExample {

	public void runExample() {
		
		Graph<Integer, Edge> graph = new SparseGraph<>();

		Integer v1 = new Integer(1);
		Integer v2 = new Integer(2);
		Integer v3 = new Integer(3);
		Integer v4 = new Integer(4);
		Integer v5 = new Integer(5);
		Integer v6 = new Integer(6);
		Integer v7 = new Integer(7);
		Integer v8 = new Integer(8);
		Integer v9 = new Integer(9);
		Integer v10 = new Integer(10);
		Integer v11 = new Integer(11);
		Integer v12 = new Integer(12);
		
		graph.addVertex(v1);
		graph.addVertex(v2);
		graph.addVertex(v3);
		graph.addVertex(v4);
		graph.addVertex(v5);
		graph.addVertex(v6);
		graph.addVertex(v7);
		graph.addVertex(v8);
		graph.addVertex(v9);
		graph.addVertex(v10);
		graph.addVertex(v11);
		graph.addVertex(v12);
		
		graph.addEdge(new Edge(13), v1, v2);
		graph.addEdge(new Edge(20), v1, v3);
		graph.addEdge(new Edge(33), v3, v2);
		graph.addEdge(new Edge(40), v3, v4);
		graph.addEdge(new Edge(14), v4, v5);
		graph.addEdge(new Edge(31), v4, v7);
		graph.addEdge(new Edge(73), v4, v6);
		graph.addEdge(new Edge(16), v6, v7);
		graph.addEdge(new Edge(60), v7, v12);
		graph.addEdge(new Edge(17), v8, v11);
		graph.addEdge(new Edge(90), v11, v9);
		graph.addEdge(new Edge(72), v10, v12);
		graph.addEdge(new Edge(10), v7, v10);
		graph.addEdge(new Edge(81), v6, v9);
		graph.addEdge(new Edge(22), v6, v11);
		
		KruskallMinimalSpanningTree<Integer, Edge> mst = new KruskallMinimalSpanningTree<>();
		Forest<Integer, Edge> tree = mst.findMinimalSpanningTree(graph);
		
		SimpleVisualizer<Integer, Edge> viewer = new SimpleVisualizer<>();
		viewer.viewTree(tree);
		
	}
	
}
