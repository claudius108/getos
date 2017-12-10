package ro.kuberam.getos.utils;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public final class Utils {

	private final static String TAG = Utils.class.getSimpleName();

	public static void showAlert(AlertType type, String header, String content) {
		Alert alert = new Alert(type);

		alert.setHeaderText(header);
		alert.setContentText(content);

		alert.showAndWait();
	}

	public static void showAlert(AlertType type, Exception ex) {
		String content;

		if (ex.getCause() != null) {
			content = ex.getCause().getLocalizedMessage();
		} else {
			content = ex.getLocalizedMessage();
		}

		Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
		showAlert(AlertType.ERROR, null, content);
	}

	public static String getExtension(File file) {
		String name = file.getName();
		int i = file.getName().lastIndexOf('.');
		if (i >= 0 && name.length() >= i + 1) {
			return file.getName().substring(i + 1).toLowerCase();
		} else {
			return "";
		}
	}

}
