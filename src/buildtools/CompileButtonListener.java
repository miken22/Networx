package buildtools;

import ide.Properties;

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
	private Properties properties;

	private String script = "";
	private boolean buildFailed = false;
	
	/**
	 * All libraries to be included on the build path.
	 */
	private final String libraries = "libraries/networxlib.jar;libraries/collections-generic-4.01.jar;libraries/colt-1.2.0.jar;libraries/concurrent-1.3.4.jar;libraries/j3d-core-1.3.1.jar"
										+ ";libraries/jung-algorithms-2.0.1.jar;libraries/jung-api-2.0.1.jar;libraries/jung-graph-impl-2.0.1.jar;libraries/jung-io.2.0.1.jar;libraries/jung-jai-2.0.1.jar"
										+ ";libraries/jung-visualization-2.0.1.jar;libraries/stax-api-1.0.1.jar;libraries/vecmath-1.3.1.jar;libraries/wstx-asl-3.2.6.jar ";

	public CompileButtonListener(JTextPane worksheet, JTextArea buildlog, Properties properties) {
		this.worksheet = worksheet;
		this.buildlog = buildlog;
		this.properties = properties;
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
		userFile.deleteOnExit();
		String fileName = userFile.getName();
		String className = fileName.substring(0, fileName.lastIndexOf('.'));
		
		try {
						
			// Compile code
			buildlog.append("Building script...\r\n");
			runProcess("javac -classpath .;" + libraries + fileName);
			
			if (buildFailed) {
				buildlog.append("Script could not be compiled.");
				return;
			}

			buildlog.append("Complete.\r\n");
			buildlog.append("---------------------------------------\r\n");
		
			File application = new File("UserScript.class");
			application.deleteOnExit();
			
			// Execute.
			runProcess("java -classpath .;" + libraries +  className);
			
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

			// TODO: Move methods out of main method.
		
			// Import needed files from JARs
			addJUNGPackageImports(outputStream);
			addNetworxPackages(outputStream);
	
			findUserMethods(theScript);
			
			// User class
			outputStream.write("public class UserScript { \r\n");
			// Main entry
			outputStream.write("	public static void main(String[] args) { \r\n");
			
			// TODO: Need to extract methods
			outputStream.write("		" + theScript + "\r\n	}\r\n}");
			outputStream.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void findUserMethods(String theScript) {
		
		
		
	}

	private void addNetworxPackages(Writer outputStream) throws IOException {
		
		outputStream.write("import components.Vertex;\r\n");
		
	}

	private void addJUNGPackageImports(Writer outputStream) throws IOException {

		for (String packageToImport : properties.getPackagesToImport()) {
			outputStream.write("import " + packageToImport + ".*;\r\n");
		}
		
	}
}
