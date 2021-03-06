package ro.kuberam.getos;

import java.nio.file.Path;
import java.util.LinkedHashMap;

import javafx.scene.image.Image;

/**
 * Metadata about a document.
 * 
 */
public interface DocumentModel {

	String TAG = DocumentModel.class.getSimpleName();

	/**
	 * Document's general metadata.
	 *
	 */
	LinkedHashMap<String, String> generalMetadata();

	/**
	 * Document's specific metadata.
	 *
	 */
	LinkedHashMap<String, String> specificMetadata();

	/**
	 * Document's path.
	 *
	 */
	Path path();

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
	 * Document's current page.
	 *
	 */
	int currentPage();
	void setCurrentPage(int currentPage);

	/**
	 * Get a page from document.
	 *
	 */
	Image goToPage(int pageNumber);
	
	/**
	 * Extract tables from page.
	 *
	 */
	Object extractTablesFromPage(int pageNumber);

	/**
	 * Closing of document.
	 *
	 */
	void shutdown();
}

// http://dublincore.org/documents/dcmi-terms/
