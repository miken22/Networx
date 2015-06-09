package toolbar.listeners;

import ide.MessageHandler;
import ide.Properties;
import ide.texteditor.ClassHandler;
import ide.texteditor.UserDefinitionHandler;

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
import java.util.ArrayList;

import javax.swing.JOptionPane;
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

	private ArrayList<String> userMethods;

	/* Global set of program files and classes to auto add to command line
	   once use of custom classes added. */
	private String programFiles = "UserFiles/UserScript.java";
	private String programClasses = "UserScript";
	private String OS = "";

	/**
	 * All libraries to be included on the build path using windows file separators
	 */
	private final String windowsLibraries = "UserFiles/;libraries/networxlib.jar;libraries/collections-generic-4.01.jar;libraries/colt-1.2.0.jar;libraries/concurrent-1.3.4.jar;libraries/j3d-core-1.3.1.jar"
			+ ";libraries/jung-algorithms-2.0.1.jar;libraries/jung-api-2.0.1.jar;libraries/jung-graph-impl-2.0.1.jar;libraries/jung-io.2.0.1.jar;libraries/jung-jai-2.0.1.jar"
			+ ";libraries/jung-visualization-2.0.1.jar;libraries/stax-api-1.0.1.jar;libraries/vecmath-1.3.1.jar;libraries/wstx-asl-3.2.6.jar ";

	/**
	 * All libraries to be included on the build path using Unix file separators
	 */
	private final String linuxLibraries = "UserFiles/:libraries/networxlib.jar:libraries/collections-generic-4.01.jar:libraries/colt-1.2.0.jar:libraries/concurrent-1.3.4.jar:libraries/j3d-core-1.3.1.jar"
			+ ":libraries/jung-algorithms-2.0.1.jar:libraries/jung-api-2.0.1.jar:libraries/jung-graph-impl-2.0.1.jar:libraries/jung-io.2.0.1.jar:libraries/jung-jai-2.0.1.jar"
			+ ":libraries/jung-visualization-2.0.1.jar:libraries/stax-api-1.0.1.jar:libraries/vecmath-1.3.1.jar:libraries/wstx-asl-3.2.6.jar ";


	public CompileButtonListener(JTextPane worksheet, JTextArea buildlog, Properties properties) {
		this.worksheet = worksheet;
		this.buildlog = buildlog;
		this.properties = properties;
		userMethods = new ArrayList<>();	

		// Get OS on startup,
		OS = getOperatingSystem();

		if (OS.indexOf("mac") >= 0) {
			JOptionPane.showMessageDialog(null, "Sorry, only Windows and Linux currently supported.");
			System.exit(-1);			
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		// Don't compile anything if the script is blank
		if (worksheet.getText().length() == 0) {
			buildlog.setText("Nothing to compile!");
			return;
		} else {
			// Clear build log
			buildlog.setText("");
			// Get text and copy to the script variable
			try {
				script = worksheet.getStyledDocument().getText(0, worksheet.getStyledDocument().getLength());
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			// Extract code, separate methods/classes, and compile
			try {
				buildScript();
				compileScript(programFiles, programClasses);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 *  Calls files necessary to write/compile user classes
	 * 
	 * @param files - List of files to compile
	 * @param classes - List of classes for execution
	 * @throws Exception - Any possible error to arise from compiling/execution
	 */
	public void compileScript(String files, String classes) throws Exception {

		String libraries = "";

		if (OS.startsWith("Windows")) {
			libraries = windowsLibraries;
		} else if (OS.startsWith("Linux")) {
			libraries = linuxLibraries;
		}

		// Compile code
		buildlog.append("Building script...\r\n");
		if (OS.startsWith("Windows")){
			runProcess("javac -cp .;" + libraries + files);
		} else {
			runProcess("javac -cp .:" + libraries + files);
		}
		if (buildFailed) {
			buildlog.append("Script could not be compiled.");
			return;
		}

		buildlog.append("Complete.\r\n");
		buildlog.append("---------------------------------------\r\n");

		// Iterate over class files, set to remove on exit.
		String[] userClasses = programClasses.split(" ");
		File userFolder = new File("UserFiles");
		for (String userClass : userClasses) {
			File classFile = new File(userFolder, userClass + ".class");
			classFile.deleteOnExit();
		}
		userFolder.deleteOnExit();

		// Execute.
		if (OS.startsWith("Windows")){
			runProcess("java -cp .;" + libraries +  classes);
		} else {		
			runProcess("java -cp .:" + libraries +  classes);
		}

		buildlog.append("---------------------------------------\r\n");
		buildlog.append("Process complete");

	}

	// Generates the process and executes it
	private void runProcess(String command) throws Exception {

		Process runtimeProcess = Runtime.getRuntime().exec(command);
		MessageHandler.handleProgramMessaage(runtimeProcess.getInputStream(), buildlog);

		InputStream errorStream = runtimeProcess.getErrorStream();
		BufferedReader errorLines = new BufferedReader(new InputStreamReader(errorStream));
		String errorMessage = errorLines.readLine();

		if (errorMessage != null) {
			MessageHandler.handleErrorMessage(errorMessage, runtimeProcess, buildlog);
			buildFailed = true;
			return;
		}
		buildFailed = false;
		runtimeProcess.waitFor();
	}

	// Uses the file chooser to save the file
	private void buildScript() {

		File userFolder = new File("UserFiles");
		userFolder.mkdir();
		userFolder.deleteOnExit();
		
		try {

			File userFile = new File(userFolder, "UserScript.java");
			userFile.deleteOnExit();

			Writer outputStream = new FileWriter(userFile);

			// Import needed files from JARs
			addPackageImports(outputStream);
			userMethods.clear();
			UserDefinitionHandler findUserDefinitions = new UserDefinitionHandler();
			// Get all user declared methods and classes
			userMethods = findUserDefinitions.findUserDefinitions(script);
			// Get the script with all user defined methods/classes
			// removed
			script = findUserDefinitions.getCleanedScript();

			// User class and open user class bracket
			outputStream.write("public class UserScript { \r\n");
			// Main method for program entry, open method bracket
			outputStream.write("	public static void main(String[] args) { \r\n");
			// Write user code for main method and close main method bracket
			outputStream.write("		" + script + "\r\n	} \r\n");

			// Add supporting user methods
			insertUserMethods(outputStream);

			// Close class bracket
			outputStream.write("\r\n}");
			outputStream.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addPackageImports(Writer outputStream) throws IOException {
		for (String packageToImport : properties.getPackagesToImport()) {
			outputStream.write("import " + packageToImport + ".*;\r\n");
		}
	}

	

	private void insertUserMethods(Writer outputStream) throws IOException {
		
		ClassHandler classWriter = new ClassHandler(properties, programFiles, programClasses);
		
		for (String userMethod : userMethods) {
			if (userMethod.contains("class")) {
				classWriter.generateUserClass(userMethod);
				continue;
			}
			outputStream.write(userMethod + "\r\n");
		}
	}
	
	private String getOperatingSystem() {
		return System.getProperty("os.name");
	}

}
