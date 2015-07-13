package tests.examples;

import java.util.List;
import java.util.Random;

import algorithms.search.GreedyBestFirstSearch;
import algorithms.search.tools.Heuristic;
import algorithms.search.tools.State;
import edu.uci.ics.jung.graph.SparseMultigraph;

public class GBFSExample {

	public void runExample() {
		
		SparseMultigraph<SimpleState, Integer> graph = new SparseMultigraph<>();
		
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
		
//		graph.addEdge(new Integer(1), v1, v2);
//		graph.addEdge(new Integer(2), v1, v3);
//		graph.addEdge(new Integer(3), v3, v2);
//		graph.addEdge(new Integer(4), v4, v5);		
//		graph.addEdge(new Integer(5), v6, v7);
//		graph.addEdge(new Integer(6), v7, v12);
//		graph.addEdge(new Integer(7), v8, v11);
//		graph.addEdge(new Integer(8), v11, v9);
//		graph.addEdge(new Integer(9), v10, v12);
//		graph.addEdge(new Integer(10), v7, v10);
//		graph.addEdge(new Integer(11), v6, v9);
//		graph.addEdge(new Integer(12), v6, v11);
//		graph.addEdge(new Integer(13), v1, v4);
//		graph.addEdge(new Integer(14), v1, v11);
//		graph.addEdge(new Integer(15), v5, v8);
//		graph.addEdge(new Integer(16), v5, v1);
//		graph.addEdge(new Integer(17), v2, v4);
//		graph.addEdge(new Integer(18), v2, v9);
//		graph.addEdge(new Integer(19), v3, v8);
//		graph.addEdge(new Integer(20), v3, v4);

		graph.addEdge(new Integer(1), v1, v2);
		graph.addEdge(new Integer(2), v1, v3);
		graph.addEdge(new Integer(3), v2, v4);
		graph.addEdge(new Integer(4), v2, v5);
		graph.addEdge(new Integer(5), v3, v6);
		graph.addEdge(new Integer(6), v3, v7);
		graph.addEdge(new Integer(7), v5, v8);
		graph.addEdge(new Integer(8), v2, v3);
		graph.addEdge(new Integer(9), v2, v6);
		graph.addEdge(new Integer(10), v2, v7);
		graph.addEdge(new Integer(11), v2, v8);
		graph.addEdge(new Integer(12), v5, v3);
		graph.addEdge(new Integer(13), v5, v4);
		graph.addEdge(new Integer(14), v5, v6);
		graph.addEdge(new Integer(15), v1, v8);
		graph.addEdge(new Integer(16), v5, v7);
		graph.addEdge(new Integer(17), v1, v7);
	
		GreedyBestFirstSearch<SimpleState, Integer> gbfs = new GreedyBestFirstSearch<>(graph, new SimpleHeuristic());
		List<SimpleState> result = gbfs.search(v1, v5);
	
		for (SimpleState s : result) {
			System.out.println(s);
		}
		
		gbfs.visualizeSearch(graph);
	}
	
	public class SimpleState extends State {
		
		String id;
		Random r;
		int distance;
		
		public SimpleState(String id) {
			this.id = id;
			r = new Random();
			distance = r.nextInt(10) + 1;
		}
		
		public int getDistance() {
			return distance;
		}
		
		@Override
		public String toString() {
			if (isStart()) {
				return "Start " + getH(); 
			}
			if (isGoal()) {
				return "Goal" + getH();
			}
			return id + " " + getH();
		}
		
	}
	public class SimpleHeuristic implements Heuristic<SimpleState> {
		@Override
		public int evaluate(SimpleState vertex) {
			return vertex.getDistance();
		}
	}
}
