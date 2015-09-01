package main.com.builder;

/**
 * Error class for building list of libraries to compile with.
 *
 * @author Michael Nowicki
 *
 */
public class LibraryLoadingError extends Exception {

	private static final long serialVersionUID = 1L;

	public LibraryLoadingError(String error) {
		super(error);
	}
}
