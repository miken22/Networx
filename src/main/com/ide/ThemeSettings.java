package main.com.ide;

import java.awt.Color;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class ThemeSettings {
	
	private Color editorColour = new Color(252, 252, 252);

	private SimpleAttributeSet reservedWords;
	private SimpleAttributeSet defaultColour;
	private SimpleAttributeSet quotations;
	private SimpleAttributeSet comments;

	public ThemeSettings() {

		reservedWords = new SimpleAttributeSet();
		defaultColour = new SimpleAttributeSet();
		quotations = new SimpleAttributeSet();
		comments = new SimpleAttributeSet();

		StyleConstants.setForeground(reservedWords, Color.RED);
		StyleConstants.setForeground(defaultColour, Color.BLACK);
		StyleConstants.setForeground(quotations, Color.BLUE);
		StyleConstants.setForeground(comments, Color.GREEN);
		
	}
	
	public void setDarkTheme() {
		StyleConstants.setForeground(reservedWords, Color.RED);
		StyleConstants.setForeground(defaultColour, Color.WHITE);
		StyleConstants.setForeground(quotations, Color.ORANGE);
		StyleConstants.setForeground(comments, Color.GREEN);
		
		editorColour = new Color(72,72,72);
	}
	
	public void setDefaultTheme() {
		StyleConstants.setForeground(reservedWords, Color.RED);
		StyleConstants.setForeground(defaultColour, Color.BLACK);
		StyleConstants.setForeground(quotations, Color.BLUE);
		StyleConstants.setForeground(comments, Color.GREEN);
	
		editorColour = new Color(252,252,252);
	}

	public Color getBackgroundColour() {
		return editorColour;
	}
	
	public SimpleAttributeSet getReservedWords() {
		return reservedWords;
	}

	public SimpleAttributeSet getDefaultColour() {
		return defaultColour;
	}

	public SimpleAttributeSet getQuotations() {
		return quotations;
	}

	public SimpleAttributeSet getComments() {
		return comments;
	}

}
