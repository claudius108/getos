package ro.kuberam.getos;

import java.io.File;
import java.util.Calendar;

import javafx.scene.image.Image;

/**
 * Metadata about a document.
 * 
 * @param <T>
 */
public interface DocumentModel {

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
	 * Document type's fxml path.
	 *
	 */
	String fxml();

	/**
	 * Document type's recource bundle path.
	 *
	 */
	String bundle();

	/**
	 * Get a page from document.
	 *
	 */
	Image goToPage(int pageNumber);
	
	/**
	 * Closing of document.
	 *
	 */
	void shutdown();
}
