package toolbar.listeners;

import ide.texteditor.TextEditorDocument;

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

/**
 * Class that saves the user script to a file
 * 
 * @author Mike Nowicki
 *
 */
public class SaveFileListener  implements ActionListener {

	private JTextPane worksheet;
	private TextEditorDocument document;
	private String filePath = "";

	public SaveFileListener(JTextPane worksheet) {
		this.worksheet = worksheet;
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

			if (filePath.equals("")){
				JFileChooser fileChooser = new JFileChooser();

				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

				FileNameExtensionFilter scriptFilter = new FileNameExtensionFilter("Networx Script files (*.scrt)", "scrt");
				// add filter
				fileChooser.setFileFilter(scriptFilter);

				if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {				
					File userScript = fileChooser.getSelectedFile();
					filePath = userScript.getAbsolutePath();
					if (userScript.getName().contains(".scrt")) {
						Writer outputStream = new FileWriter(userScript);
						outputStream.write(theScript);
						outputStream.close();
					} else { // Otherwise we need to add correct fileExtention
						Writer outputStream = new FileWriter(userScript + ".scrt");
						outputStream.write(theScript);
						outputStream.close();
					}
					document.isSaved();
				}
			} else {
				File userScript = new File(filePath);
				if (userScript.getName().contains(".scrt")) {
					Writer outputStream = new FileWriter(userScript);
					outputStream.write(theScript);
					outputStream.close();
				} else { // Otherwise we need to add correct fileExtention
					Writer outputStream = new FileWriter(userScript + ".scrt");
					outputStream.write(theScript);
					outputStream.close();
				}
				document.isSaved();
			}

			// TODO: Probably fix this to handle crashes without losing worksheet
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
