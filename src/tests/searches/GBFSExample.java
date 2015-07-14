package tests.searches;

import java.util.List;
import core.components.Edge;
import algorithms.search.GreedyBestFirstSearch;
import algorithms.search.tools.Heuristic;
import algorithms.search.tools.State;
import edu.uci.ics.jung.graph.SparseMultigraph;

public class GBFSExample {

	public void runExample() {
		
		SparseMultigraph<SimpleState, Edge> graph = new SparseMultigraph<>();
		SimpleState[] states = new SimpleState[8]; 
		
		for (int i = 0; i < 8; i++) {
			int x = (int) Math.random()*10;
			int y = (int) Math.random()*10;
			SimpleState state = new SimpleState("a", x, y);
			graph.addVertex(state);
			states[i] = state;
		}

		for (int i = 0; i < 16; i++) {
			
			int source = (int)(Math.random()*10) % 8;
			int target = (int)(Math.random()*10) % 8;
			
			if (source == target) {
				--i;
				continue;
			}
		
			graph.addEdge(new Edge(), states[source], states[target]);
		
		}
	
		GreedyBestFirstSearch<SimpleState, Edge> gbfs = new GreedyBestFirstSearch<>(graph, new SimpleHeuristic());
		List<SimpleState> result = gbfs.search(states[0], states[4]);
	
		for (SimpleState s : result) {
			System.out.println(s);
		}
		
		gbfs.visualizeSearch(graph);
	}
	
	public class SimpleState extends State {
		
		String id;
		int x;
		int y;
		
		public SimpleState(String id, int x, int y) {
			this.id = id;
			this.x = x;
			this.y = y;
		}
		
		public int getDistance() {
			return (int)Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
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
