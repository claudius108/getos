package ro.kuberam.getos.modules.editorTab;

import java.io.File;

import javafx.scene.control.Alert.AlertType;
import ro.kuberam.getos.Getos;
import ro.kuberam.getos.controller.factory.EditorController;
import ro.kuberam.getos.utils.Utils;

public class EditorUtils {
	
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
				Getos.tabControllers.remove(controller);
				controller.shutDown();
				if (Getos.tabPane.getTabs().size() == 1) {
					Getos.statusLabel.setText("");
				}
			});
			newTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
				if (newValue) {
					EditorController tabController = Getos.tabControllers.get(Getos.tabPane.getTabs().indexOf(newTab));
					tabController.onEditorTabSelected();
				}
			});

			controller.setEditorPane(newTab);
			// newTabController.setStatusLabel(statusLabel);
			Getos.tabControllers.add(controller);
			Getos.tabPane.getTabs().add(newTab);
			Getos.tabPane.getSelectionModel().select(newTab);
		} catch (Exception ex) {
			Utils.showAlert(AlertType.ERROR, ex);
		}
	}

}
