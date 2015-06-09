package toolbar.compiler;

import ide.Properties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import javax.swing.JTextPane;

public class ScriptBuilder {

	private Properties properties;
	private ArrayList<String> userMethods;

	public ScriptBuilder(Properties properties) {
		this.properties = properties;
		userMethods = new ArrayList<>();
	}

	// Uses the file chooser to save the file
	public void buildScript(JTextPane worksheet, String programFiles, String programClasses) {

		String script = worksheet.getText();
		
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
			insertUserMethods(outputStream, programFiles, programClasses);

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

	

	private void insertUserMethods(Writer outputStream, String programFiles, String programClasses) throws IOException {
		
		ClassHandler classWriter = new ClassHandler(properties, programFiles, programClasses);
		
		for (String userMethod : userMethods) {
			if (userMethod.contains("class")) {
				classWriter.generateUserClass(userMethod);
				continue;
			}
			outputStream.write(userMethod + "\r\n");
		}
	}
	
}
