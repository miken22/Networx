package main.com.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.com.ide.Properties;
import main.com.ide.texteditor.TextEditorDocument;

/**
 * Default button for the open functionality, is basic
 * now, meant for future development
 * 
 * @author Mike Nowicki
 *
 */
public class OpenButton extends JButton implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextPane worksheet;
	private Properties properties;
	
	public OpenButton(JTextPane worksheet, Properties properties) {
		super();
		this.worksheet = worksheet;
		this.properties = properties;
		addActionListener(this);
	}
	
	
	public OpenButton(JTextPane worksheet, Properties properties, String name) {
		super(name);
		this.worksheet = worksheet;
		this.properties = properties;
		addActionListener(this);
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

				// System dependent newline character, cannot insert both at the
				// end of a line because it messes up the highlighting logic.
				String newLineChar = "";
				if (System.getProperty("os.name").contains("Windows")) {
					newLineChar = "\n";
				} else {
					newLineChar = "\r";
				}
				
				// Clear old settings (if any)
				properties.clearImports();
				properties.clearArguments();
				worksheet.setText("");

				File userScript = fileChooser.getSelectedFile();	
				FileReader reader = new FileReader(userScript);
				BufferedReader bufferedReader = new BufferedReader(reader);
				TextEditorDocument doc = (TextEditorDocument)worksheet.getStyledDocument();
				
				fileText = bufferedReader.readLine();
				
				while (fileText != null && !fileText.equals("#Imports")) {
					doc.insertString(doc.getLength(), fileText + newLineChar, null);
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
