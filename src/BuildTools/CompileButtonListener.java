package BuildTools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;

/**
 * Class that implements the compiling functionality. Saves the script, calls javac
 * to compile the code and then uses java to execute the program. Text output is 
 * displayed in the build log text area.
 * 
 * @author Michael
 *
 */
public class CompileButtonListener implements ActionListener {

	// The IDE's notepad and outputs
	private JTextPane worksheet;
	private JTextArea buildlog;

	private String script = "";
	private boolean buildFailed = false;

	public CompileButtonListener(JTextPane worksheet, JTextArea buildlog) {
		this.worksheet = worksheet;
		this.buildlog = buildlog;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		// Don't compile anything if the script is blank
		if (worksheet.getText().length() == 0) {
			buildlog.setText("Nothing to compile!");
			return;
		} else {
			buildlog.setText("");
			try {
				script = worksheet.getStyledDocument().getText(0, worksheet.getStyledDocument().getLength());
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			buildScript(script);
			compileScript();

		}
	}

	private void compileScript() {

		File userFile = new File("UserScript.java");
		String fileName = userFile.getName();
		String className = fileName.substring(0, fileName.lastIndexOf('.'));
		
		try {
			
			// Compile code
			buildlog.append("Building script...\r\n");
			runProcess("javac " + fileName + " src/graphcomponents/vertex.java");
			
			if (buildFailed) {
				buildlog.append("Script could not be compiled.");
				return;
			}

			buildlog.append("Complete.\r\n");
			buildlog.append("---------------------------------------\r\n");
			
			// Execute. Need to find a way to skip if there is a build failure.
			runProcess("java " +  className);
			buildlog.append("---------------------------------------\r\n");
			buildlog.append("Process complete");
			
		} catch (Exception e) {
			String error = e.getMessage();
			buildlog.append(error);
		}

	}

	// Write any file outputs
	private void printLines(InputStream ins) throws Exception {
		String line = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(ins));
		while ((line = in.readLine()) != null) {
			buildlog.append(line + "\n");
		}
	}

	// Generates the process and executes it
	private void runProcess(String command) throws Exception {
		Process pro = Runtime.getRuntime().exec(command);
		printLines(pro.getInputStream());
		
		InputStream errorStream = pro.getErrorStream();
		BufferedReader errorLines = new BufferedReader(new InputStreamReader(errorStream));
		String errorMessage = errorLines.readLine();
		if (errorMessage != null) {
			buildFailed = true;	
			buildlog.append(errorMessage + "\n");
			pro.waitFor();
			return;
		}
		buildFailed = false;
		
		pro.waitFor();
	}

	// Uses the file chooser to save the file
	private void buildScript(String theScript) {

		try {
			
			File userFile = new File("UserScript.java");
			Writer outputStream = new FileWriter(userFile);

			// TODO: More complex parsing of the script to add necessary imports, move methods out of main method.
			outputStream.write("import graphcomponents.Vertex;\r\n");
			outputStream.write("public class UserScript { \r\n");
			outputStream.write("public static void main(String[] args) { \r\n");
			outputStream.write(theScript + "\r\n}\r\n}");
			outputStream.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
