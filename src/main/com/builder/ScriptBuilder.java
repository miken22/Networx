package main.com.builder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;

import javax.swing.JTextPane;

import main.com.ide.Properties;

/**
 * Class that takes the user script and generates the necessary
 * class files and adds imports to the java file
 * @author mike
 *
 */
public class ScriptBuilder {

	private Properties properties;
	private ArrayList<String> userMethods;

	public ScriptBuilder(Properties properties) {
		this.properties = properties;
		userMethods = new ArrayList<>();
	}

	// Uses the file chooser to save the file
	/**
	 * Takes the user script and generates the appropriate Java files for compiling
	 * 
	 * @param worksheet - The editor environment
	 */
	public void buildScript(JTextPane worksheet) {

		String script = worksheet.getText();
		String OS = System.getProperty("os.name");
		
		File userFolder = new File(".UserFiles");

		if (OS.contains("windows")) {
			// Convert to path, check if the folder is hidden, hide it if it is not.
			Path path = userFolder.toPath();
			Boolean hidden;
			try {
				// Check if folder is hidden
				hidden = (Boolean) Files.getAttribute(path, "dos:hidden", LinkOption.NOFOLLOW_LINKS);
				if (hidden != null && !hidden) {
					// Hide folder for Windows/Linux
				    Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
				}
			} catch (IOException e1) {}
		}

		if (!userFolder.exists()) {
			userFolder.mkdir();
		}
		
		try {

			File userFile = new File(userFolder, "UserScript.java");
			userFile.deleteOnExit();

			Writer outputStream = new FileWriter(userFile);

			// Import needed files from JARs
			addPackageImports(outputStream);
			userMethods.clear();
			UserDefinitionHandler userDefHandler = new UserDefinitionHandler();
			// Get all user declared methods and classes
			userMethods = userDefHandler.findUserDefinitions(script);
			// Get the script with all user defined methods/classes
			// removed
			script = userDefHandler.getCleanedScript();
			
			// TODO: Let user type in imports, find and move them to the top
			//       of the class file.

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

	/**
	 * Adds the packages from the properties to the top of the java file
	 * 
	 * @param outputStream
	 * @throws IOException
	 */
	private void addPackageImports(Writer outputStream) throws IOException {
		for (String packageToImport : properties.getPackagesToImport()) {
			outputStream.write("import " + packageToImport + ".*;\r\n");
		}
	}

	/**
	 * Moves user methods to the correct locations in the java code
	 * 
	 * @param outputStream
	 * @param programFiles
	 * @param programClasses
	 * @throws IOException
	 */
	private void insertUserMethods(Writer outputStream) throws IOException {
		
		ClassHandler classWriter = new ClassHandler(properties);
		
		for (String userMethod : userMethods) {
			if (userMethod.contains("class")) {
				classWriter.generateUserClass(userMethod);
				continue;
			}
			outputStream.write(userMethod + "\r\n");
		}
	}
	
}
