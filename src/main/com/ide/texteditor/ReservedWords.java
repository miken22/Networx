package main.com.ide.texteditor;

/**
 * List of reserved java words for highlighting
 * 
 * @author Mike Nowicki
 *
 */
public final class ReservedWords {
	/**
	 * Since these words are reserved an enum type cannot be used, instead string
	 * comparisons must be done
	 */
	public static final String[] reservedWords = {
			"private","public","protected","final","super","if","while","do","void"+
			"true","null","false","else","System","static","throws","int","double" +
			"float","byte","interface","new","boolean","class","for" };
}
