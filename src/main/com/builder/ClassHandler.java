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
import java.util.List;

import main.com.ide.Properties;

/**
 * This class handles the user code to identify any user defined classes.
 * Any classes are extracted and written to a separate user file
 * 
 * @author Mike Nowicki
 *
 */
public class ClassHandler {

	private Properties properties;
	
	private List<String> fileList;
	
	public ClassHandler(Properties props) {
		this.properties = props;
		fileList = new ArrayList<>();
	}

	/**
	 * Takes a string, representing all the code in the user defined class,
	 * and writes it to a seperate file
	 * 
	 * @param userClass - A string composed on the user class code
	 */
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
			
			File userFolder = new File(".UserFiles");
			
			if (System.getProperty("os.name").contains("Windows")) {			
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
			
			fileList.add(userClassName);

		// catches should not be triggered
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	/**
	 * Add the necessary packages to import as defined by the properties
	 * 
	 * @param outputStream - The file writer
	 * @throws IOException
	 */
	private void addPackageImports(Writer outputStream) throws IOException {
		for (String packageToImport : properties.getPackagesToImport()) {
			outputStream.write("import " + packageToImport + ".*;\r\n");
		}
	}
	
	public String updateSourceFileList(String programFiles) {
		
		for (String fileName : fileList) {
			programFiles += fileName + ".java ";
		}
		
		return programFiles;
	}
	
	public String updateClassFileList(String classFiles) {
		for (String fileName : fileList) {
			classFiles += fileName + " ";
		}
		return classFiles;
	}
}
