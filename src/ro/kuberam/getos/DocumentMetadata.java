package ro.kuberam.getos;

import java.io.File;
import java.util.Calendar;

/**
 * Metadata about a document.
 */
public interface DocumentMetadata {

	/**
	 * Document's title.
	 *
	 */
	String title();

	/**
	 * Document's subject.
	 *
	 */
	String subject();

	/**
	 * Document's author.
	 *
	 */
	String author();

	/**
	 * Document's keywords.
	 *
	 */
	String keywords();

	/**
	 * Document's producer.
	 *
	 */
	String producer();

	/**
	 * Document's creator.
	 *
	 */
	String creator();

	/**
	 * When the document was created.
	 *
	 */
	Calendar created();

	/**
	 * When the document was modified.
	 *
	 */
	Calendar modified();

	/**
	 * Document's format.
	 *
	 */
	float format();

	/**
	 * Document's number of pages.
	 *
	 */
	int numberOfPages();

	/**
	 * Document is optimized.
	 *
	 */
	String optimized();

	/**
	 * Document's security.
	 *
	 */
	String security();

	/**
	 * Document's paper-size.
	 *
	 */
	String paperSize();

	/**
	 * Document's fonts.
	 *
	 */
	String fonts();

	/**
	 * Document's path.
	 *
	 */
	String path();
	
	/**
	 * Document's file.
	 *
	 */
	File file();
	
	/**
	 * Document's type.
	 *
	 */
	File type();
}
