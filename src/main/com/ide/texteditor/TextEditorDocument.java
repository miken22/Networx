package main.com.ide.texteditor;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;

/**
 * The document that the user edits to create their scripts
 * 
 * @author Mike Nowicki
 *
 */
public class TextEditorDocument extends DefaultStyledDocument {

	private static final long serialVersionUID = 1L;
	private SimpleAttributeSet reservedWords;
	private SimpleAttributeSet defaultColour;
	private SimpleAttributeSet quotations;
	private SimpleAttributeSet comments;

	private boolean hasChanged;

	public TextEditorDocument() {

		// Initialize all the different styles for text highlighting.
		reservedWords = new SimpleAttributeSet();
		defaultColour = new SimpleAttributeSet();
		quotations = new SimpleAttributeSet();
		comments = new SimpleAttributeSet();

		StyleConstants.setForeground(reservedWords, Color.RED);
		StyleConstants.setForeground(defaultColour, Color.BLACK);
		StyleConstants.setForeground(quotations, Color.BLUE);
		StyleConstants.setForeground(comments, Color.GREEN);

		hasChanged = false;

	}
	
	// TODO: Auto formatting, indenting when user is already etc

	/**
	 * Inserts the string and performs necessary decorations
	 */
	public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
		if (str.equals("\t")){
			str = "    ";
		}	
		super.insertString(offset, str, a);

		hasChanged = true;

		String text = getText(0, getLength());
		int before = findLastNonWordChar(text, offset);
		if (before < 0) {
			before = 0;
		}
		int after = findFirstNonWordChar(text, offset + str.length());
		int wordL = before;
		int wordR = before;

		while (wordR <= after) {
			
			if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
				if (text.substring(wordL, wordR).matches("(\\W)*(" + ReservedWords.reservedWords + ")")) {
					// Only colour the word itself, not brackets, other words, etc
					while (!String.valueOf(text.charAt(wordL)).matches("[a-zA-Z]")) {
						wordL++;
					}
					setCharacterAttributes(wordL, wordR - wordL, reservedWords, false);             	    	            	
				} else {
					setCharacterAttributes(wordL, wordR - wordL, defaultColour, false);
				}
				wordL = wordR;
			}
			wordR++;
		}       
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
					int offset = lineLengthCounter + line.indexOf("\"");
					int endOfQuote = lineLengthCounter + line.lastIndexOf("\"");
					// TODO: Support multi-line quotes
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
	 * Does not currently function 100%
	 */
	private void colourComments() {

		String text = "";
		try {
			text = this.getText(0, this.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

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

		hasChanged = true;

		String text = this.getText(0, getLength());
		int before = findLastNonWordChar(text, offset);
		if (before < 0) before = 0;
		int after = findFirstNonWordChar(text, offset);

		if (text.substring(before, after).matches("(\\W)*(" + ReservedWords.reservedWords + ")")) {
			while (!String.valueOf(text.charAt(before)).matches("[a-zA-Z]")) {
				before++;
			}
			setCharacterAttributes(before, after - before, reservedWords, false);
		} else {
			setCharacterAttributes(before, after - before, defaultColour, false);
		}

		colourComments();

	}

	private int findLastNonWordChar(String text, int index) {
		while (--index >= 0) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
		}
		return index;
	}

	private int findFirstNonWordChar(String text, int index) {
		while (index < text.length()) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
			index++;
		}
		return index;
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
