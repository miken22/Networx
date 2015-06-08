package ide.texteditor;

import ide.Properties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class ClassHandler {
	
	private Properties properties;
	private String programFiles;
	private String programClasses;
	
	public ClassHandler(Properties props, String programFiles, String programClasses) {
		this.properties = props;
		this.programFiles = programFiles;
		this.programClasses = programClasses;
	}

	public void generateUserClass(String userClass) {

		String userClassName = "";

		String[] classTextByWords = userClass.split(" ");
		
		// This is a hack, probably should loop below until "class" is found,
		// then take the String from the next index as the class name. However,
		// there shouldn't be any abstract/final classes or interfaces/enums
		// as that's not what the system is meant for.

		// Find class name by splitting the text on whitespaces, if the third word
		// is 'static' then the class name is the 4th word (ie public static class ExampleClass {}) 
		if (classTextByWords[2].equalsIgnoreCase("static")) {
			userClassName = classTextByWords[3];
		} else {
			userClassName = classTextByWords[2]; // class name is third word in declaration
		}

		try {
			File userFolder = new File("UserFiles");
			File userFile = new File(userFolder, userClassName + ".java");
			userFile.deleteOnExit();	
			userFolder.deleteOnExit();

			Writer outputStream = new FileWriter(userFile);

			// Import needed files from JARs, will be same as what is imported in main method,
			// may need a better way to handle this as complexity grows.
			addPackageImports(outputStream);

			// User class and open user class bracket
			outputStream.write(userClass);
			outputStream.close();

			programFiles = programFiles + " UserFiles/" + userClassName + ".java";
			programClasses = programClasses + " "  + userClassName;

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
	
}
