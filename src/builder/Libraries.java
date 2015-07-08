package builder;

/**
 * List of libraries to add for compiling, formatted for linux or windows
 * @author Mike Nowicki
 *
 */
public final class Libraries {

	/**
	 * All libraries to be included on the build path using Unix file separators
	 */
	public final static String linuxList = ".:UserFiles/:libraries/networxlib.jar:libraries/collections-generic-4.01.jar:libraries/colt-1.2.0.jar:libraries/concurrent-1.3.4.jar:libraries/j3d-core-1.3.1.jar"
			+ ":libraries/jung-algorithms-2.0.1.jar:libraries/jung-api-2.0.1.jar:libraries/jung-graph-impl-2.0.1.jar:libraries/jung-io.2.0.1.jar:libraries/jung-jai-2.0.1.jar"
			+ ":libraries/jung-visualization-2.0.1.jar:libraries/stax-api-1.0.1.jar:libraries/vecmath-1.3.1.jar:libraries/wstx-asl-3.2.6.jar ";

	
	/**
	 * All libraries to be included on the build path using windows file separators
	 */
	public final static String windowsList = ".;UserFiles/;libraries/networxlib.jar;libraries/collections-generic-4.01.jar;libraries/colt-1.2.0.jar;libraries/concurrent-1.3.4.jar;libraries/j3d-core-1.3.1.jar"
			+ ";libraries/jung-algorithms-2.0.1.jar;libraries/jung-api-2.0.1.jar;libraries/jung-graph-impl-2.0.1.jar;libraries/jung-io.2.0.1.jar;libraries/jung-jai-2.0.1.jar"
			+ ";libraries/jung-visualization-2.0.1.jar;libraries/stax-api-1.0.1.jar;libraries/vecmath-1.3.1.jar;libraries/wstx-asl-3.2.6.jar ";

	
}
