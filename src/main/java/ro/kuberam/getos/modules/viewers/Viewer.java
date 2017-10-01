package ro.kuberam.getos.modules.viewers;

public interface Viewer<T> {

	/**
	 * Return a viewer to be included into the editor
	 * 
	 * @return A viewer
	 * @param filePath
	 *            A String representing the path to the file to be viewed
	 * @throws Exception
	 *             If an error occur during the operation
	 */
	String create(String filePath) throws Exception;

}
