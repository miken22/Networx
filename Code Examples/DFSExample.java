package tests.examples;

import java.util.List;

import algorithms.search.DepthFirstSearch;
import core.visualizer.SimpleVisualizer;
import edu.uci.ics.jung.graph.SparseMultigraph;

public class DFSExample {

	public void runExample() {
		SparseMultigraph<Vertex, Integer> graph = new SparseMultigraph<>();
		DepthFirstSearch<Vertex, Integer> dfs = new DepthFirstSearch<>(graph);
		
		Vertex v1 = new Vertex("a");
		Vertex v2 = new Vertex("b");
		Vertex v3 = new Vertex("c");
		Vertex v4 = new Vertex("d");
		Vertex v5 = new Vertex("e");
		Vertex v6 = new Vertex("f");
		Vertex v7 = new Vertex("g");
		Vertex v8 = new Vertex("h");
		
		graph.addVertex(v1);
		graph.addVertex(v2);
		graph.addVertex(v3);
		graph.addVertex(v4);
		graph.addVertex(v5);
		graph.addVertex(v6);
		graph.addVertex(v7);
		graph.addVertex(v8);

		graph.addEdge(new Integer(1), v1, v2);
		graph.addEdge(new Integer(2), v1, v3);
		graph.addEdge(new Integer(3), v2, v4);
		graph.addEdge(new Integer(4), v2, v5);
		graph.addEdge(new Integer(5), v3, v6);
		graph.addEdge(new Integer(6), v3, v7);
		graph.addEdge(new Integer(7), v5, v8);
		
		List<Vertex> result = dfs.search(v1);
		
		System.out.println("The vertices were visitted in the following order:");
		for (Vertex v : result) {
			System.out.println(v);
		}
		
		SimpleVisualizer<Vertex, Integer> visualizer = new SimpleVisualizer<>();
		visualizer.viewGraph(graph, v1, false);
		
	}
	
	public static class Vertex {
		
		String id;
		
		public Vertex(String id) {
			this.id = id;
		}
		
		public String toString() {
			return id;
		}
		
	}	
	
}
