package tests.examples;

import algorithms.connectivity.PathBasedComponents;
import edu.uci.ics.jung.graph.SparseMultigraph;


public class ConnectComponentExample {

	
	public void runExample() {
		
		SparseMultigraph<Integer, Integer> graph = new SparseMultigraph<>();

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
		
		graph.addEdge(new Integer(1), v1, v2);
		graph.addEdge(new Integer(2), v1, v3);
		graph.addEdge(new Integer(3), v3, v2);
		
		graph.addEdge(new Integer(4), v4, v5);
		
		graph.addEdge(new Integer(5), v6, v7);
		graph.addEdge(new Integer(6), v7, v12);
		graph.addEdge(new Integer(7), v8, v11);
		graph.addEdge(new Integer(8), v11, v9);
		graph.addEdge(new Integer(9), v10, v12);
		graph.addEdge(new Integer(10), v7, v10);
		graph.addEdge(new Integer(11), v6, v9);
		graph.addEdge(new Integer(12), v6, v11);
		
		PathBasedComponents<Integer, Integer> pbc = new PathBasedComponents<>();
		pbc.findComponents(graph);
		pbc.visualizeSearch();
		
//		LoadTGF tgf = new LoadTGF();
//		Graph<Integer, Edge> graph = tgf.loadTrivialGraphFile(0, false);
//		PathBasedComponents<Integer, Edge> pbc = new PathBasedComponents<>();
//		pbc.findComponents(graph);
//		pbc.visualizeSearch();
		
	}
}
