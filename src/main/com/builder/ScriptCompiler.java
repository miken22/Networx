package main.com.builder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JTextArea;
import javax.swing.JTextPane;

import main.com.ide.MessageHandler;
import main.com.ide.Properties;

/**
 * Class that takes the users script from the editor and generates
 * Java files, compiles and executes the users application.
 * 
 * @author Mike Nowicki
 *
 */
public class ScriptCompiler {
	
	private Properties properties;
	private JTextPane worksheet;
	private JTextArea buildlog;
	
	private boolean buildFailed = false;
	
	public ScriptCompiler(Properties properties, JTextPane worksheet, JTextArea buildlog) {
		this.properties = properties;
		this.worksheet = worksheet;
		this.buildlog = buildlog;
	}
	
	/**
	 * Calls files necessary to write/compile user classes
	 * 
	 * @throws Exception - Any possible error to arise from compiling/execution
	 */
	public void compileAndRun() throws Exception {

		String arguments = properties.getCommandArguments();
		String OS = System.getProperty("os.name");
		String libraries = Libraries.getLibraries(OS);
		
		// Compile code
		buildlog.append("Building script...\r\n");
		runProcess("javac -cp " + libraries + " .UserFiles/UserScript.java");
		
		if (buildFailed) {
			return;
		}

		buildlog.append("Complete.\r\n");
		buildlog.append("---------------------------------------\r\n");

		// Execute.
		runProcess("java " + arguments + " -cp " + libraries + " UserScript");

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

			while (errorMessage != null) {
				// If there is an error, print the message and signal build failure
				MessageHandler.handleErrorMessage(errorMessage, worksheet, buildlog);
				errorMessage = errorLines.readLine();
			}
			
			buildFailed = true;
			// Play error sound (Windows, need to test unix)
			java.awt.Toolkit.getDefaultToolkit().beep();
			runtimeProcess.waitFor();
			return;
		}
		// Otherwise command succeed and wait for process to execute.
		buildFailed = false;
		runtimeProcess.waitFor();
	}
}
