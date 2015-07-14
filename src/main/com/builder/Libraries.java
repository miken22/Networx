package main.com.builder;

import java.io.File;

/**
 * List of libraries to add for compiling, formatted for linux or windows
 * @author Mike Nowicki
 *
 */
public final class Libraries {

	/**
	 * Gets all libraries that need to be added to the classpath
	 * when the user application is compiled
	 * 
	 * @param OS String indicating the operating system. From System.getProperty("os");
	 * @return The list of libraries to add to the compilers classpath.
	 */
	public static String getLibraries(String OS) {
		
		String libraries = "";
		
		if (OS.startsWith("Windows")) {
			libraries = loadWindowsLibaries(libraries);
		} else if (OS.startsWith("Linux")) {
			libraries = loadLinuxLibaries(libraries);
		}		
		return libraries;
	}
	
	private static String loadWindowsLibaries(String libraries) {
		
		libraries += ".;UserFiles/";
		
		File libFolder = new File("lib");
		if (!libFolder.isDirectory()) {
			try {
				throw new LibraryLoadingError("Could not find library directory");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		for (File subFile : libFolder.listFiles()) {
			
			if (subFile.isDirectory()) {
				// Only look one level into sub-folders, does not support >2 levels
				// of library files
				for (File nestedLib : subFile.listFiles()) {
					if (nestedLib.isDirectory()) {
						continue;
					}
					// Skip source and doc files
					if (isDocOrSource(nestedLib)) {
						continue;
					}
					
					libraries += ";" + nestedLib.toString();
				}	
			} else {
				if (!isDocOrSource(subFile)) {
					libraries += ";" + subFile.toString();
				}
			}
		}
		return libraries;

	}
	
	private static String loadLinuxLibaries(String libraries) {
		
		libraries += ".:UserFiles/";
		
		File libFolder = new File("lib");
		if (!libFolder.isDirectory()) {
			try {
				throw new LibraryLoadingError("Could not find library directory");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		for (File subFile : libFolder.listFiles()) {
			
			if (subFile.isDirectory()) {
				
				for (File nestedLib : subFile.listFiles()) {
					if (nestedLib.isDirectory()) {
						continue;
					}
					// Skip source and doc files
					if (isDocOrSource(nestedLib)) {
						continue;
					}
					
					libraries += ":" + nestedLib.toString();
				}	
			} else {
				if (!isDocOrSource(subFile)) {
					libraries += ":" + subFile.toString();
				}
			}
		}
		return libraries;
	}
	
	private static boolean isDocOrSource(File file) {
		if (file.toString().contains("javadoc")) {
			return true;
		}
		if (file.toString().contains("source")) {
			return true;
		}
		if (!file.toString().contains(".jar")) {
			return true;
		}
		return false;
	}
	
}
