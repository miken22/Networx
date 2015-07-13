package builder;

import java.io.File;

/**
 * List of libraries to add for compiling, formatted for linux or windows
 * @author Mike Nowicki
 *
 */
public final class Libraries {

	/**
	 * All libraries to be included on the build path using Unix file separators
	 */
	public final static String linuxList = ".:UserFiles/";//:lib/networxlib.jar:lib/collections-generic-4.01.jar:lib/colt-1.2.0.jar:lib/concurrent-1.3.4.jar:lib/j3d-core-1.3.1.jar"
			//+ ":lib/jung-algorithms-2.0.1.jar:lib/jung-api-2.0.1.jar:lib/jung-graph-impl-2.0.1.jar:lib/jung-io.2.0.1.jar:lib/jung-jai-2.0.1.jar"
			//+ ":lib/jung-visualization-2.0.1.jar:lib/stax-api-1.0.1.jar:lib/vecmath-1.3.1.jar:lib/wstx-asl-3.2.6.jar ";

	
	/**
	 * All libraries to be included on the build path using windows file separators
	 */
	public final static String windowsList = ".;UserFiles/";//;lib/networxlib.jar;lib/collections-generic-4.01.jar;lib/colt-1.2.0.jar;lib/concurrent-1.3.4.jar;lib/j3d-core-1.3.1.jar"
			//+ ";lib/jung-algorithms-2.0.1.jar;lib/jung-api-2.0.1.jar;lib/jung-graph-impl-2.0.1.jar;lib/jung-io.2.0.1.jar;lib/jung-jai-2.0.1.jar"
			//+ ";lib/jung-visualization-2.0.1.jar;lib/stax-api-1.0.1.jar;lib/vecmath-1.3.1.jar;lib/wstx-asl-3.2.6.jar ";
	
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
	
	public static String getLibraries(String OS) {
		
		String libraries = "";
		
		if (OS.startsWith("Windows")) {
			libraries = loadWindowsLibaries(libraries);
		} else if (OS.startsWith("Linux")) {
			libraries = loadLinuxLibaries(libraries);
		}
		System.out.println(libraries);
		
		return libraries;
	}
	
}
