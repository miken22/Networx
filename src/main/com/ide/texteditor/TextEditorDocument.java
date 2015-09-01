package main.com.ide.texteditor;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;

/**
 * The document style for the scripts, this class defines the
 * colouring of key words, quotes, and comments within the text.
 * 
 * @author Mike Nowicki
 *
 */
public class TextEditorDocument extends DefaultStyledDocument {

	private static final long serialVersionUID = 1L;
	private SimpleAttributeSet reservedColour;
	private SimpleAttributeSet defaultColour;
	private SimpleAttributeSet quotations;
	private SimpleAttributeSet comments;

	private boolean hasChanged;

	/**
	 * Since these words are reserved an enum type cannot be used, instead string
	 * comparisons must be done
	 */
	public static final String[] reservedWords = {
			"private","public","protected","final","super","if","while","do","void"+
			"true","null","false","else","System","static","throws","int","double" +
			"float","byte","interface","new","boolean","class","for" 
			};
	
	public TextEditorDocument() {

		// Initialize all the different styles for text highlighting.
		reservedColour = new SimpleAttributeSet();
		defaultColour = new SimpleAttributeSet();
		quotations = new SimpleAttributeSet();
		comments = new SimpleAttributeSet();

		StyleConstants.setForeground(reservedColour, Color.RED);
		StyleConstants.setForeground(defaultColour, Color.BLACK);
		StyleConstants.setForeground(quotations, Color.BLUE);
		StyleConstants.setForeground(comments, Color.GREEN);

		hasChanged = false;

	}

	/**
	 * Inserts the string and performs necessary decorations
	 */
	public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
		if (str.equals("\t")){
			str = "    ";
		}

		super.insertString(offset, str, a);
		
		updateTextStyles();
		colourQuotes();
		colourComments();

	}

	private void colourQuotes() {
		String text = "";
		try {
			text = getText(0, this.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		StringReader strReader = new StringReader(text);
		BufferedReader reader = new BufferedReader(strReader);

		// Iterate through each line, find those that contain "//" to color.
		String line = null;
		try {
			line = reader.readLine();
			int lineLengthCounter = 0;
			while (line != null) {
				if (line.contains("\"")) {
					// Find the index of the quotation mark
					int offset = lineLengthCounter + line.indexOf("\"");
					// Find the end of the quote
					int endOfQuote = lineLengthCounter + line.lastIndexOf("\"");
					setCharacterAttributes(offset, endOfQuote - offset + 1, quotations, false);
				}
				// Keep track of length of each line so offset lines up, add +1 for each
				// new line character not including in line length.
				lineLengthCounter += line.length() + 1;
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Does not currently function 100%, deleting comment markers
	 * causes issues if leaving the line behind.
	 */
	private void colourComments() {

		String text = "";
		try {
			text = this.getText(0, this.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		defaultColour.addAttribute(StyleConstants.CharacterConstants.Bold, Boolean.FALSE);

		StringReader strReader = new StringReader(text);
		BufferedReader reader = new BufferedReader(strReader);

		// Iterate through each line, find those that contain "//" to color.
		String line = null;
		try {
			int position = 0;
			line = reader.readLine();
			while (line != null) {
				if (line.startsWith("//")) {
					int length = line.length();
					setCharacterAttributes(position, length, comments, false);     
				} else if (line.contains("//")) {
					int offset = line.indexOf("//");
					// Otherwise only colour from the // to the end of the line
					setCharacterAttributes(offset, line.length() - offset, comments, false);
				}
				position += line.length()+1;
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Removes the string and performs necessary decorations
	 */
	public void remove(int offset, int length) throws BadLocationException {
		super.remove(offset, length);

		updateTextStyles();
		colourQuotes();
		colourComments();
	}

	private void updateTextStyles() throws BadLocationException {

		// Clear existing styles
		this.setCharacterAttributes(0, getText(0, getLength()).length(), defaultColour, true);
		
		Pattern pattern = buildPattern();

		// Look for tokens and highlight them
		Matcher matcher = pattern.matcher(getText(0, getLength()));
		while (matcher.find()) {
			// Change the color of recognized tokens
			this.setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), reservedColour, false);
		}
	}
	
	/**
	 * Builds a regex pattern of all words to search for
	 * @return The regex experession
	 */
	private Pattern buildPattern() {

		StringBuilder sb = new StringBuilder();
		for (String token : reservedWords) {
			sb.append("\\b"); // Start of word boundary
			sb.append(token);
			sb.append("\\b|"); // End of word boundary and an or for the next word
		}
		// Remove the trailing "|"
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}

		return Pattern.compile(sb.toString());

	}

	/*
	 * The following are used to update which style to use
	 */

	public void setReservedWords(SimpleAttributeSet reservedWords) {
		this.reservedColour = reservedWords;
	}

	public void setDefaultColour(SimpleAttributeSet defaultColour) {
		this.defaultColour = defaultColour;
	}

	public void setQuotations(SimpleAttributeSet quotations) {
		this.quotations = quotations;
	}

	public void setComments(SimpleAttributeSet comments) {
		this.comments = comments;
	}

	/**
	 * 
	 * @return - True if the document was modified, False otherwise
	 */
	public boolean documentHasChanged() {
		return hasChanged;
	}

	/**
	 * Mark the document as saved
	 */
	public void isSaved() {
		hasChanged = false;
	}

}
