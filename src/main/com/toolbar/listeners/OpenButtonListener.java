package main.com.toolbar.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.com.ide.Properties;
import main.com.ide.texteditor.TextEditorDocument;

/**
 * Class that loads user files and places the code into the text
 * editor
 * 
 * @author Mike Nowicki
 *
 */
public class OpenButtonListener implements ActionListener {

	private JTextPane worksheet;
	private Properties properties;
	
	public OpenButtonListener(JTextPane worksheet, Properties properties) {
		this.worksheet = worksheet;
		this.properties = properties;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String fileText = "";

		try {

			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

			FileNameExtensionFilter scriptFilter = new FileNameExtensionFilter("Networx Scripts (*.scrt)", "scrt");
			// add filters
			fileChooser.addChoosableFileFilter(scriptFilter);
			fileChooser.setFileFilter(scriptFilter);

			if (fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {

				// Clear old settings (if any)
				properties.clearSettings();
				worksheet.setText("");

				File userScript = fileChooser.getSelectedFile();	
				FileReader reader = new FileReader(userScript);
				BufferedReader bufferedReader = new BufferedReader(reader);
				TextEditorDocument doc = (TextEditorDocument)worksheet.getStyledDocument();
				
				fileText = bufferedReader.readLine();
				
				while (fileText != null && !fileText.equals("#Imports")) {
					doc.insertString(doc.getLength(), fileText + "\r\n", null);
					fileText = bufferedReader.readLine();
				}
				
				// Check for saved preferences, 
				if (fileText != null && fileText.equals("#Imports")) {
					fileText = bufferedReader.readLine();
					while (fileText != null && !fileText.equals("#Arguments")) {
						properties.addPackage(fileText);
						fileText = bufferedReader.readLine();
					}
				}
				
				// Add command line arguments if needed
				if (fileText != null && fileText.equals("#Arguments")) {
					fileText = bufferedReader.readLine();
					properties.addCommandLineArgument(fileText);
				}
				
				bufferedReader.close();
				doc.isSaved();
				worksheet.requestFocus();				
			
			}
			
		} catch (Exception exp) {
			exp.printStackTrace();
			actionPerformed(e);
		}
	}
}
