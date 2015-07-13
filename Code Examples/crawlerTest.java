package core.webcrawler;

import algorithms.search.BreadthFirstSearch;
import algorithms.search.UninformedSearch;
import core.stocks.StockEdge;
import core.stocks.StockVertex;
import edu.uci.ics.jung.graph.Graph;


public class crawlerTest {

	public static void main(String[] args) {
		
		YahooCrawler crawler = new YahooCrawler();
		crawler.crawl();
	
		StockGraphBuilder sgb = new StockGraphBuilder();
		
		Graph<StockVertex, StockEdge> graph = sgb.buildGraph();
		
		System.out.println(graph.getVertexCount());
		System.out.println(graph.getEdgeCount());
		
		UninformedSearch<StockVertex, StockEdge> bfs = new BreadthFirstSearch<>(graph);
		bfs.visualizeSearch(graph.getVertices().iterator().next());
		
	}
}
