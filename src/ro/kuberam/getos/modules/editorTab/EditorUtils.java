package ro.kuberam.getos.modules.editorTab;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.Alert.AlertType;
import ro.kuberam.getos.controller.factory.EditorController;
import ro.kuberam.getos.utils.Utils;

public class EditorUtils {
	
	private final static String TAG = EditorUtils.class.getSimpleName();
	
	public static void createNewEditorTab2(EditorController controller, File file) {
		try {
			EditorTab newTab = new EditorTab(file);
			newTab.setClosable(true);
			newTab.setContent(controller.getRoot());

			newTab.setOnCloseRequest(event -> {
				// todo: show yes/no save dialog
				if (controller.isEdited()) {
					controller.saveContent();
				}
				tabControllers.remove(controller);
				controller.shutDown();
				if (tabPane.getTabs().size() == 1) {
					statusLabel.setText("");
				}
			});
			newTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
				if (newValue) {
					EditorController tabController = tabControllers.get(tabPane.getTabs().indexOf(newTab));
					tabController.onEditorTabSelected();
				}
			});

			controller.setEditorPane(newTab);
			// newTabController.setStatusLabel(statusLabel);
			tabControllers.add(controller);
			Logger.getLogger(TAG).log(Level.INFO, "tabPane = " + tabPane);
			tabPane.getTabs().add(newTab);
			tabPane.getSelectionModel().select(newTab);
		} catch (Exception ex) {
			Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
			if (ex.getCause() != null) {
				Utils.showAlert(AlertType.ERROR, null, ex.getCause().getLocalizedMessage());
			} else {
				Utils.showAlert(AlertType.ERROR, null, ex.getLocalizedMessage());
			}
		}
	}

}
