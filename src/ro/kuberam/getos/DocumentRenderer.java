package ro.kuberam.getos;

import javafx.scene.control.ScrollPane;

/**
 * A document renderer allowing various operation on the document.
 */
public interface DocumentRenderer {
	
	/**
	 * Get the root.
	 *
	 */
	ScrollPane root();

	/**
	 * Render one page back.
	 *
	 */
	void pageBack();
	
	/**
	 * Render one page back.
	 *
	 */
	void pageForward();	

}
