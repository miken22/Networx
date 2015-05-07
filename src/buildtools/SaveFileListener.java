package buildtools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

public class SaveFileListener  implements ActionListener {

	private JTextPane worksheet;
	
	public SaveFileListener(JTextPane worksheet) {
		this.worksheet = worksheet;
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
	        // add filters
			fileChooser.addChoosableFileFilter(scriptFilter);
			fileChooser.setFileFilter(scriptFilter);
			
			if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
				
				File userScript = fileChooser.getSelectedFile();
				
				Writer outputStream = new FileWriter(userScript);
				outputStream.write(theScript);
				outputStream.close();

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
