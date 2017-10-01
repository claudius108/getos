package ro.kuberam.getos.modules.viewers;

import java.util.List;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;

public interface Viewer<S, T> {

	/**
	 * Parse a String
	 * 
	 * @param code
	 *            The code to parse, as a string
	 * @return The root TreeItem than describe the parsed code
	 * @throws Exception
	 *             If an error occur during parsing
	 */
	TreeItem<S> parseCode(String code) throws Exception;

	/**
	 * Return a pretty print of code
	 * 
	 * @return A String that represent the same code as before, but pretty
	 *         printed
	 * @param code
	 *            A String representing the code
	 * @throws Exception
	 *             If an error occur during the operation
	 */
	String prettyPrint(String code) throws Exception;

	/**
	 * @return A list of column to display in the TreeTableView next to the
	 *         editor
	 */
	List<TreeTableColumn<S, T>> getTreeTableViewColumns();

}
