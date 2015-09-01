package main.com.ide.texteditor;

/**
 * A static class that can be used to identify key words
 * that should be highlighted in the text editting pane.
 * 
 * NOT USED AT THE MOMENT (Sept 1)
 */
public final class KeyWords {
	/**
	 * List of key words we want highlighted, since the reserved words
	 * are found using string comparisons we'll do the same here.
	 */
	public static final String keyWords = 
			"String|Object|Integer|Double|Float|Byte|ArrayList|List|HashMap|HashSet|" +
			"DirectedGraph|Forest|Graph |Hypergraph|KPartiteGraph|MultiGraph|Tree|" + 
			"UndirectedGraph|AbstractGraph|AbstractTypedGraph|DelegateForest|DelegateTree|" + 
			"DirectedOrderedSparseMultigraph|DirectedSparseGraph|DirectedSparseMultigraph|" +
			"GraphDecorator|ObservableGraph|OrderedKAryTree|OrderedSparseMultigraph|" +
			"SetHypergraph|SortedSparseMultigraph|SparseGraph|SparseMultigraph|" + 
			"UndirectedOrderedSparseMultigraph|UndirectedSparseGraph|UndirectedSparseMultigraph";
	
}
