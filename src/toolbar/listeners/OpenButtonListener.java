package toolbar.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Document;

public class OpenButtonListener implements ActionListener {

	private JTextPane worksheet;
	
	public OpenButtonListener(JTextPane worksheet) {
		this.worksheet = worksheet;
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
				
				worksheet.setText("");
				
				File userScript = fileChooser.getSelectedFile();	
			
				FileReader reader = new FileReader(userScript);
				BufferedReader bufferedReader = new BufferedReader(reader);
				
				Document doc = worksheet.getStyledDocument();
				fileText = bufferedReader.readLine();
				while (fileText != null) {
					doc.insertString(doc.getLength(), fileText + "\r\n", null);
					fileText = bufferedReader.readLine();
				}
		        bufferedReader.close();
                worksheet.requestFocus();				
			}
			
		} catch (Exception exp) {
			exp.printStackTrace();
			actionPerformed(e);
		}
	}

}
