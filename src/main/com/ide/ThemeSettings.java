package main.com.ide;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * Class that sets the attributes for the text editor for the
 * theme picking environment. The settings are relatively trivial
 * right now, only dark or default, and is stored as a string of
 * text in a .settings text file
 * 
 * @author Michael Nowicki
 *
 */
public class ThemeSettings {
	
	private Color editorColour = new Color(252, 252, 252);
	
	private Color lineNumberColour = new Color(255, 255, 255);

	private Color buildLogFontColour = new Color(0, 0, 0);
	
	private Color environmentColour = new Color(217, 217, 217);
		
	private SimpleAttributeSet reservedWords;
	private SimpleAttributeSet defaultColour;
	private SimpleAttributeSet quotations;
	private SimpleAttributeSet comments;
	
	private int fontSize = 14;
	
	private boolean isDefault;

	public ThemeSettings() {

		reservedWords = new SimpleAttributeSet();
		defaultColour = new SimpleAttributeSet();
		quotations = new SimpleAttributeSet();
		comments = new SimpleAttributeSet();

		StyleConstants.setForeground(reservedWords, Color.RED);
		StyleConstants.setForeground(defaultColour, Color.BLACK);
		StyleConstants.setForeground(quotations, Color.BLUE);
		StyleConstants.setForeground(comments, Color.GREEN);
		
		reservedWords.addAttribute(StyleConstants.CharacterConstants.Bold, Boolean.TRUE);
		
		isDefault = true;
		
	}
	
	public void setDarkTheme() {
		StyleConstants.setForeground(reservedWords, Color.RED);
		StyleConstants.setForeground(defaultColour, Color.WHITE);
		StyleConstants.setForeground(quotations, Color.ORANGE);
		StyleConstants.setForeground(comments, Color.GREEN);
		
		editorColour = new Color(72, 72, 72);
		environmentColour = new Color(72, 72, 72);
		lineNumberColour = new Color(255, 255, 255);
		buildLogFontColour = new Color(255, 255, 255);
		
		isDefault = false;
	}
	
	public void setDefaultTheme() {
		StyleConstants.setForeground(reservedWords, Color.RED);
		StyleConstants.setForeground(defaultColour, Color.BLACK);
		StyleConstants.setForeground(quotations, Color.BLUE);
		StyleConstants.setForeground(comments, Color.GREEN);
	
		editorColour = new Color(252, 252, 252);
		environmentColour = new Color(217, 217, 217);
		lineNumberColour = new Color(0, 0, 0);
		buildLogFontColour = new Color(0, 0, 0);

		isDefault = true;
	}

	public void setFontSize(int size) {
		this.fontSize = size;
	}

	public int getFontSize() { return fontSize; }
	
	public Color getLineNumberColour() {
		return lineNumberColour;
	}

	/**
	 * 
	 * @return The colour for the background of the text panes
	 */
	public Color getEditorColour() {
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
	/**
	 * 
	 * @return The colour for the build log
	 */
	public Color getBuildLogColour() {
		return buildLogFontColour;
	}

	public boolean isDefaultTheme() {
		return isDefault;
	}

	/**
	 * 
	 * @return The colour for the main window
	 */
	public Color getEnvironmentColour() {
		return environmentColour;
	}
	
	/**
	 * When the editor loads this is called to read the settings
	 * for the environment and is applied when created.
	 */
	public void loadEnvironmentSettings() {

		File settingFile = new File(".settings.txt");
		
		// On first launch it won't exist so create it
		if(!settingFile.exists()) {
			try {
				
				FileWriter outputStream = new FileWriter(settingFile);
				outputStream.write("default\r\n");		
				outputStream.write("14");
				outputStream.close();
				
				setDefaultTheme();
				return;
					
			} catch (IOException e) {
				JOptionPane.showMessageDialog(new JFrame(), "Could not load settings.", 
						"Error",
				        JOptionPane.ERROR_MESSAGE);
			}
			
		}
	
		try {
			FileReader reader = new FileReader(settingFile);
			BufferedReader bufferedReader = new BufferedReader(reader);	

			String fileText = bufferedReader.readLine();
			
			if (fileText == null || fileText.equals("default")) {
				setDefaultTheme();
			} else {
				setDarkTheme();
			}
			
			fileText = bufferedReader.readLine();
			fontSize = Integer.valueOf(fileText);
			
			bufferedReader.close();
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Could not load settings.", 
					"Error",
			        JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	/**
	 * Writes the modified settings back to a file
	 */
	public void updateEnvironmentSettings() {
		
		try {
			
			File settingFile = new File(".settings.txt");		
			FileWriter outputStream = new FileWriter(settingFile);
			
			if (isDefault) {
				outputStream.write("default");		
			} else {
				outputStream.write("dark");		
			}
			
			outputStream.write("\r\n" + fontSize);
			
			outputStream.close();
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Could not load settings.", 
					"Error",
			        JOptionPane.ERROR_MESSAGE);
		}
	}

}
