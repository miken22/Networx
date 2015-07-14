package main.com.toolbar.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

import main.com.ide.Properties;
import main.com.ide.texteditor.TextEditorDocument;

/**
 * Class that saves the user script to a file
 * 
 * @author Mike Nowicki
 *
 */
public class SaveFileListener  implements ActionListener {

	private JTextPane worksheet;
	private TextEditorDocument document;
	private Properties properties;

	public SaveFileListener(JTextPane worksheet, Properties properties) {
		this.worksheet = worksheet;
		this.properties = properties;
		this.document = (TextEditorDocument) worksheet.getDocument();
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		String theScript = "";
		try {
			theScript = worksheet.getStyledDocument().getText(0, worksheet.getStyledDocument().getLength());
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}

		try {

			JFileChooser fileChooser = new JFileChooser();

			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

			FileNameExtensionFilter scriptFilter = new FileNameExtensionFilter("Networx Script files (*.scrt)", "scrt");
			// add filter
			fileChooser.setFileFilter(scriptFilter);

			if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {				

				File userScript = fileChooser.getSelectedFile();
				Writer outputStream = null;

				if (userScript.getName().contains(".scrt")) {
					outputStream = new FileWriter(userScript);
				} else { // Otherwise we need to add correct fileExtention
					outputStream = new FileWriter(userScript + ".scrt");
				}

				outputStream.write(theScript);
				
				// If there are set properties add them to the file
				if (properties.getPackagesToImport().size() != 0) {

					outputStream.write("\r\n");
					outputStream.write("#Imports");
					outputStream.write("\r\n");
					
					for (String toImport : properties.getPackagesToImport()) {
						outputStream.write(toImport + "\r\n");
					}
				}
				
				if (properties.getCommandArguments().length() != 0) {
					outputStream.write("#Arguments");
					outputStream.write("\r\n");
					outputStream.write(properties.getCommandArguments());
				}
				
				outputStream.close();

				document.isSaved();
			}
			
			// TODO: Save user properties (imports etc)
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
