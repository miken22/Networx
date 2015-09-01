
package main.com.ide;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple class to store the list of packages a user wants to include in the script
 * 
 * @author Michael Nowicki
 *
 */
public class Properties {

	private List<String> packagesToImport;
	private String commandArguments;
	
	public Properties() {
		packagesToImport = new ArrayList<>();
		commandArguments = "";
	}
	
	public void addCommandLineArgument(String argument) {
		commandArguments += " " + argument;
	}
	
	public String getCommandArguments() {
		return commandArguments.trim();
	}

	public void addPackage(String requiredPackage) {
		packagesToImport.add(requiredPackage);
	}
	
	public List<String> getPackagesToImport() {
		return packagesToImport;
	}
	
	public void clearImports() {
		packagesToImport.clear();
	}
	public void clearArguments() {
		commandArguments = "";
	}
}
