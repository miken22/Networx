package toolbar.listeners;

import ide.MessageHandler;
import ide.Properties;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import builder.Libraries;
import builder.ScriptBuilder;

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
	
	// For control if compiler fails
	private boolean buildFailed = false;

	/**
	 * Class that extracts the text from the worksheet and build
	 * class(es) as needed that can be compiled and executed.
	 */
	private ScriptBuilder sb;
	
	private Properties properties;
	
	/* Global set of program files and classes to auto add to command line
	   once use of custom classes added. */
	private String programFiles = "";
	private String programClasses = "UserScript";
	private String OS = "";
	private String libraries = "";

	public CompileButtonListener(JTextPane worksheet, JTextArea buildlog, Properties properties) {
		this.worksheet = worksheet;
		this.buildlog = buildlog;	
		this.properties = properties;
		sb = new ScriptBuilder(properties);

		// Get OS on startup,
		OS = getOperatingSystem();
		
		if (OS.startsWith("Windows")) {
			programFiles += "UserFiles/UserScript.java";
			libraries = Libraries.windowsList;
		} else if (OS.startsWith("Linux")) {
			programFiles += "UserFiles/UserScript.java";
			libraries = Libraries.linuxList;
		}

		if (OS.indexOf("mac") >= 0) {
			JOptionPane.showMessageDialog(null, "Sorry, only Windows and Linux currently supported.");
			System.exit(-1);			
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		// Don't compile anything if the script is blank
		if (worksheet.getText().length() == 0) {
			buildlog.setText("Nothing to compile!");
			return;
		} else {
			// Clear build log
			buildlog.setText("");
			
			// Extract code, separate methods/classes, and compile
			try {
				sb.buildScript(worksheet, programFiles, programClasses);
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

		String arguments = properties.getCommandArguments();
		
		// Compile code
		buildlog.append("Building script...\r\n");
		runProcess("javac -cp " + libraries + files);
		
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
		runProcess("java " + arguments + " -cp " + libraries +  classes);

		buildlog.append("---------------------------------------\r\n");
		buildlog.append("Process complete");

	}

	// Generates the process and executes it
	private void runProcess(String command) throws Exception {

		Process runtimeProcess = Runtime.getRuntime().exec(command);
		// Print any console outputs in the buildlog
		MessageHandler.handleProgramMessaage(runtimeProcess.getInputStream(), buildlog);

		// Check for any error messages
		InputStream errorStream = runtimeProcess.getErrorStream();
		BufferedReader errorLines = new BufferedReader(new InputStreamReader(errorStream));
		String errorMessage = errorLines.readLine();

		if (errorMessage != null) {
			// If there is an error, print the message and signal build failure
			MessageHandler.handleErrorMessage(errorMessage, worksheet, buildlog);
			buildFailed = true;
			runtimeProcess.waitFor();
			return;
		}
		// Otherwise command succeed and wait for process to execute.
		buildFailed = false;
		runtimeProcess.waitFor();
	}
	
	/**
	 * Get users operating system
	 * 
	 * @return - A string that states the OS.
	 */
	private String getOperatingSystem() {
		return System.getProperty("os.name");
	}
}
