package tests;

import tests.components.ConnectComponentExample;
import tests.loaders.GraphMLLoadExample;
import tests.searches.BFSExample;
import tests.searches.DFSExample;
import tests.searches.UniformCostExample;
import tests.spanningtrees.SpanningTreeExample;

@SuppressWarnings("unused")
public class RunTests {

	public static void main(String[] args) {


//		BFSExample example = new BFSExample();

//		DFSExample example = new DFSExample();

//		UniformCostExample example = new UniformCostExample();
		
//		GraphMLLoadExample example = new GraphMLLoadExample();
		
//		ConnectComponentExample example = new ConnectComponentExample();
		
		SpanningTreeExample example = new SpanningTreeExample();
		
		example.runExample();

	}

}
