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

import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

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
	private String scriptName = "";
	private String className = "";

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
			script = worksheet.getText();
			buildScript(script);
			compileScript();

		}
	}

	private void compileScript() {

		if (scriptName == "") {
			buildlog.setText("No file specified to compile!");
			return;
		}

		try {
			runProcess("javac " + scriptName);
			runProcess("java " + className);
		} catch (Exception e) {
			String error = e.getMessage();
			buildlog.setText(error);
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
		printLines(pro.getErrorStream());
		pro.waitFor();
	}

	// Uses the file chooser to save the file
	private void buildScript(String theScript) {

		try {

			JFileChooser fileChooser = new JFileChooser();
			if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
				
				File userScript = fileChooser.getSelectedFile();
				Writer outputStream = new FileWriter(userScript);

				scriptName = userScript.getName();
				className = scriptName.substring(0, scriptName.lastIndexOf('.'));
				
				// TODO: More complex parsing of the script to add necessary imports, move methods out of main method.
				
				outputStream.write("public class " + className + " { \r\n");
				outputStream.write("public static void main(String[] args) { \r\n");
				outputStream.write(theScript + "\r\n}\r\n}");
				outputStream.close();

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
