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
	private String programFiles = "UserScript.java";
	private String programClasses = "UserScript";
	
	/**
	 * All libraries to be included on the build path using windows file separators
	 */
	private final String windowsLibraries = "libraries/networxlib.jar;libraries/collections-generic-4.01.jar;libraries/colt-1.2.0.jar;libraries/concurrent-1.3.4.jar;libraries/j3d-core-1.3.1.jar"
			+ ";libraries/jung-algorithms-2.0.1.jar;libraries/jung-api-2.0.1.jar;libraries/jung-graph-impl-2.0.1.jar;libraries/jung-io.2.0.1.jar;libraries/jung-jai-2.0.1.jar"
			+ ";libraries/jung-visualization-2.0.1.jar;libraries/stax-api-1.0.1.jar;libraries/vecmath-1.3.1.jar;libraries/wstx-asl-3.2.6.jar ";

	/**
	 * All libraries to be included on the build path using Unix file separators
	 */
	private final String linuxLibraries = "libraries/networxlib.jar:libraries/collections-generic-4.01.jar:libraries/colt-1.2.0.jar:libraries/concurrent-1.3.4.jar:libraries/j3d-core-1.3.1.jar"
			+ ":libraries/jung-algorithms-2.0.1.jar:libraries/jung-api-2.0.1.jar:libraries/jung-graph-impl-2.0.1.jar:libraries/jung-io.2.0.1.jar:libraries/jung-jai-2.0.1.jar"
			+ ":libraries/jung-visualization-2.0.1.jar:libraries/stax-api-1.0.1.jar:libraries/vecmath-1.3.1.jar:libraries/wstx-asl-3.2.6.jar ";


	public CompileButtonListener(JTextPane worksheet, JTextArea buildlog, Properties properties) {
		this.worksheet = worksheet;
		this.buildlog = buildlog;
		this.properties = properties;
		userMethods = new ArrayList<>();
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
			buildScript();
			compileScript();
		}
	}

	private void compileScript() {

		File userFile = new File("UserScript.java");
		userFile.deleteOnExit();
		
		String OS = getOperatingSystem();

		try {

			// TODO: Determine OS at load and store in a variable instead of checking each compile

			if (OS.startsWith("Windows")) {
				windowsCompile(programFiles, programClasses);
			} else if (OS.startsWith("Linux")) {
				unixCompile(programFiles, programClasses);
			} else {
				JOptionPane.showMessageDialog(null, "Sorry, only Windows and Linux currently supported.");
				System.exit(-1);
			}

		} catch (Exception e) {
			String error = e.getMessage();
			buildlog.append(error);
		}

	}

	private void unixCompile(String files, String classes) throws Exception {
		// Compile code
		buildlog.append("Building script...\r\n");
		runProcess("javac -classpath .:" + linuxLibraries + files);

		if (buildFailed) {
			buildlog.append("Script could not be compiled.");
			return;
		}

		buildlog.append("Complete.\r\n");
		buildlog.append("---------------------------------------\r\n");

		File application = new File("UserScript.class");
		application.deleteOnExit();

		// Execute.
		runProcess("java -classpath .:" + linuxLibraries +  classes);

		buildlog.append("---------------------------------------\r\n");
		buildlog.append("Process complete");

	}

	private void windowsCompile(String fileName, String className) throws Exception {
		// Compile code
		buildlog.append("Building script...\r\n");
		runProcess("javac -classpath .;" + windowsLibraries + fileName);

		if (buildFailed) {
			buildlog.append("Script could not be compiled.");
			return;
		}

		buildlog.append("Complete.\r\n");
		buildlog.append("---------------------------------------\r\n");

		File application = new File("UserScript.class");
		application.deleteOnExit();

		// Execute.
		runProcess("java -classpath .;" + windowsLibraries +  className);

		buildlog.append("---------------------------------------\r\n");
		buildlog.append("Process complete");

	}

	private String getOperatingSystem() {
		return System.getProperty("os.name");
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
	private void buildScript() {
		try {

			File userFile = new File("UserScript.java");
			Writer outputStream = new FileWriter(userFile);

			// TODO: Move methods out of main method.

			// Import needed files from JARs
			addJUNGPackageImports(outputStream);
			addNetworxPackages(outputStream);

			userMethods.clear();
			findUserMethods(script);

			// User class and open user class bracket
			outputStream.write("public class UserScript { \r\n");
			// Main method for program entry, open method bracket
			outputStream.write("	public static void main(String[] args) { \r\n");
			// Write user code for main method and close main method bracket
			outputStream.write("		" + script + "\r\n	} \r\n");
			
			// Add supporting user methods
			insertUserMethods(outputStream);
			
			// Possible TODO: Handle custom user classes, write to separate file, add to command line args
			
			// Close class bracket
			outputStream.write("\r\n}");
			outputStream.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void addNetworxPackages(Writer outputStream) throws IOException {

		outputStream.write("import components.Vertex;\r\n");

	}

	private void addJUNGPackageImports(Writer outputStream) throws IOException {

		for (String packageToImport : properties.getPackagesToImport()) {
			outputStream.write("import " + packageToImport + ".*;\r\n");
		}

	}


	private void findUserMethods(String theScript) {

		int position = script.indexOf("public");
		this.script = theScript;		
		
		
		if (position != -1) {
			this.script = extractUserMethod(script, position);		// Global copy of trimmed String
			findUserMethods(script);	// Continue to search for more methods
		}
		
		position = script.indexOf("private");
		if (position != -1) {
			this.script = extractUserMethod(script, position);
			findUserMethods(script);
		}
		
		position = script.indexOf("protected");
		if (position != -1) {
			this.script = extractUserMethod(script, position);
			findUserMethods(script);
		}
		
	}
	
	private String extractUserMethod(String theScript, int position) {

		// Store starting index of method.
		int startLocation = position;
		int braceCounter = 0;
		// Find first occurence of open brace
		position = theScript.indexOf("{", position);
		
		// Update the counter, shift to next index in string
		braceCounter++;
		position++;
				
		while (braceCounter > 0) {
			
			for (int i = position; i < theScript.length(); i++) {
				char c = theScript.charAt(i);
			
				if (c == '{') {
					braceCounter++;
				} else if (c == '}') {
					braceCounter--;
				}					
				
				if (braceCounter == 0) {
					position = i;
					break;
				}
			}
		}
		
		position++;	// Shift to next index in String (if it exists)
		
		userMethods.add(theScript.substring(startLocation, position));
		
		String remainingCode = "";
		// Store remaining code (if any)
		try {
			remainingCode = theScript.substring(position);
		} catch (IndexOutOfBoundsException e) {
			// Means we are ok, reached the end of the script
			theScript = theScript.substring(0, startLocation);
			theScript += "\r\n" + remainingCode;
			return theScript;
		}
		
		theScript = theScript.substring(0, startLocation);
		theScript += "\r\n" + remainingCode;
		// Otherwise more code to examine.
		return theScript;
	}

	private void insertUserMethods(Writer outputStream) throws IOException {
	
		for (String userMethod : userMethods) {
			outputStream.write(userMethod + "\r\n");
		}
		
	}	
}
