package tests.searches;

import java.util.List;
import java.util.Random;

import algorithms.search.AStarSearch;
import algorithms.search.tools.Heuristic;
import algorithms.search.tools.State;
import algorithms.search.tools.WeightedEdge;
import edu.uci.ics.jung.graph.SparseMultigraph;

/**
 * Simple example of how the AStarSearch class works. Heuristic and edge values
 * are random to vary the behaviour of the search.
 * 
 * @author Mike Nowicki
 *
 */
public class AStarExample {
	
	public void runExample() {

		Random r = new Random();
		SparseMultigraph<SimpleState, SimpleEdge> graph = new SparseMultigraph<>();
		
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

		graph.addEdge(new SimpleEdge(r.nextInt(10)), v1, v2);
		graph.addEdge(new SimpleEdge(r.nextInt(10)), v1, v3);
		graph.addEdge(new SimpleEdge(r.nextInt(10)), v2, v4);
		graph.addEdge(new SimpleEdge(r.nextInt(10)), v2, v5);
		graph.addEdge(new SimpleEdge(r.nextInt(10)), v3, v6);
		graph.addEdge(new SimpleEdge(r.nextInt(10)), v3, v7);
		graph.addEdge(new SimpleEdge(r.nextInt(10)), v5, v8);
		graph.addEdge(new SimpleEdge(r.nextInt(10)), v2, v3);
		graph.addEdge(new SimpleEdge(r.nextInt(10)), v2, v6);
		graph.addEdge(new SimpleEdge(r.nextInt(10)), v2, v7);
		graph.addEdge(new SimpleEdge(r.nextInt(10)), v2, v8);
		graph.addEdge(new SimpleEdge(r.nextInt(10)), v5, v3);
		graph.addEdge(new SimpleEdge(r.nextInt(10)), v5, v4);
		graph.addEdge(new SimpleEdge(r.nextInt(10)), v5, v6);
		graph.addEdge(new SimpleEdge(r.nextInt(10)), v1, v8);
		graph.addEdge(new SimpleEdge(r.nextInt(10)), v5, v7);
		graph.addEdge(new SimpleEdge(r.nextInt(10)), v1, v7);
		
//		graph.addEdge(new Integer(8), v2, v3);
		
		
		
		AStarSearch<SimpleState, SimpleEdge> astar = new AStarSearch<>(graph, new SimpleHeuristic());		
		List<SimpleState> result = astar.search(v8, v7);
		
		for (SimpleState s : result) {
			System.out.println(s);
		}
		
		astar.visualizeSearch(graph);
		
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
				return "Start " + getG(); 
			}
			if (isGoal()) {
				return "Goal" + getG();
			}
			return id + " " + getG();
		}
		
	}
	public class SimpleEdge extends WeightedEdge {
		
		public SimpleEdge(int weight) {
			super(++weight);
		}
		
		public String toString() {
			return "" + getEdgeWeight();
		}
				
	}
	
	public class SimpleHeuristic implements Heuristic<SimpleState> {
		@Override
		public int evaluate(SimpleState vertex) {
			return vertex.getDistance();
		}
	}
}
