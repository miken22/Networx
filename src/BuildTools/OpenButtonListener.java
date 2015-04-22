package BuildTools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class OpenButtonListener implements ActionListener {

	private JTextPane worksheet;
	
	public OpenButtonListener(JTextPane worksheet) {
		this.worksheet = worksheet;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		try {

			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

	        FileNameExtensionFilter scriptFilter = new FileNameExtensionFilter("Networx Script files (*.scrt)", "scrt");
	        // add filters
			fileChooser.addChoosableFileFilter(scriptFilter);
			fileChooser.setFileFilter(scriptFilter);
			
			if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
				File userScript = fileChooser.getSelectedFile();	
			
				FileReader reader = new FileReader(userScript);
				BufferedReader bw = new BufferedReader(reader);
				
				// TODO: Need to find a way to only get code within the main method, or user defined
				// methods....
				
                worksheet.read(bw, userScript);
                bw.close();
                worksheet.requestFocus();				
			}
			
			
		} catch (Exception exp) {
			
		}
	}

}
