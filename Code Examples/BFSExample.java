package tests.examples;

import java.util.List;

import algorithms.search.BreadthFirstSearch;
import algorithms.search.UninformedSearch;
import edu.uci.ics.jung.graph.SparseMultigraph;

/**
 * Simple example illustrating the performace of the BFS algorithm.
 * 
 * @author Mike Nowicki
 *
 */
public class BFSExample {

	public void runExample() {
		SparseMultigraph<SimpleState, Integer> graph = new SparseMultigraph<>();
		UninformedSearch<SimpleState, Integer> bfs = new BreadthFirstSearch<>(graph);
		
		SimpleState v1 = new SimpleState("a");
		SimpleState v2 = new SimpleState("b");
		SimpleState v3 = new SimpleState("c");
		SimpleState v4 = new SimpleState("d");
		SimpleState v5 = new SimpleState("e");
		SimpleState v6 = new SimpleState("f");
		SimpleState v7 = new SimpleState("g");
		SimpleState v8 = new SimpleState("h");
		
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
//		graph.addEdge(new Integer(8), v2, v3);
		
		List<SimpleState> result = bfs.search(v2);
		
		bfs.visualizeSearch(v2);
		
		System.out.println("The vertices were visitted in the following order:");
		for (SimpleState v : result) {
			System.out.println(v);
		}
//		
//		SimpleVisualizer<Vertex, Integer> visualizer = new SimpleVisualizer<>();
//		visualizer.viewGraph(graph);
		
	}
	
	public static class SimpleState {
		
		String id;
		
		public SimpleState(String id) {
			this.id = id;
		}
		
		public String toString() {
			return id;
		}
		
	}	
}
