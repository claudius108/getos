package ro.kuberam.getos;

/**
 * Metadata about the document.
 */
public interface DocumentMetadata {
	
	/**
	 * Path to the document.
	 *
	 */
	String path();

	/**
	 * Render one page back.
	 *
	 */
	void pageForward();

}
