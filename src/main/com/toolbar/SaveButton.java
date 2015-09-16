package main.com.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

import main.com.ide.Properties;
import main.com.ide.texteditor.TextEditorDocument;

/**
 * Default button for the save functionality, is basic
 * now, meant for future development
 * 
 * @author Mike Nowicki
 *
 */
public class SaveButton extends JButton implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextPane worksheet;
	private TextEditorDocument document;
	private Properties properties;
	
	public SaveButton(JTextPane worksheet, Properties properties) {
		super();
		this.worksheet = worksheet;
		this.properties = properties;
		this.document = (TextEditorDocument) worksheet.getDocument();
		addActionListener(this);
	}
	
	public SaveButton(JTextPane worksheet, Properties properties, String name) {
		super(name);
		this.worksheet = worksheet;
		this.properties = properties;
		this.document = (TextEditorDocument) worksheet.getDocument();
		addActionListener(this);
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
			
			// Set and add filter
			FileNameExtensionFilter scriptFilter = new FileNameExtensionFilter("Networx Script files (*.scrt)", "scrt");
			fileChooser.setFileFilter(scriptFilter);

			if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {				

				File userScript = fileChooser.getSelectedFile();
				Writer outputStream = null;

				if (userScript.getName().contains(".scrt")) {
					outputStream = new FileWriter(userScript);
				} else {
					// Otherwise we need to add correct fileExtention
					outputStream = new FileWriter(userScript + ".scrt");
				}

				outputStream.write(theScript);
				
				// If there are set properties add them to the file
				if (properties.getPackagesToImport().size() != 0) {

					outputStream.write("\r\n#Imports\r\n");
					
					for (String toImport : properties.getPackagesToImport()) {
						outputStream.write(toImport + "\r\n");
					}
				}
				
				// Write any saved command line args to the file
				if (properties.getCommandArguments().length() != 0) {
					outputStream.write("#Arguments\r\n");
					outputStream.write(properties.getCommandArguments());
				}
				
				outputStream.close();

				document.isSaved();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
