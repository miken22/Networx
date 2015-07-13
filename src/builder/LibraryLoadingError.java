package builder;

public class LibraryLoadingError extends Exception {

	private static final long serialVersionUID = 1L;

	public LibraryLoadingError(String error) {
		super(error);
	}
}
