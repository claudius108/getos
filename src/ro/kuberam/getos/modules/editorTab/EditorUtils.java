package ro.kuberam.getos.modules.editorTab;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.Alert.AlertType;
import ro.kuberam.getos.Getos;
import ro.kuberam.getos.controller.factory.EditorController;
import ro.kuberam.getos.modules.main.MainWindowController;
import ro.kuberam.getos.utils.Utils;

public class EditorUtils {

	private final static String TAG = EditorUtils.class.getSimpleName();

	public static void createNewEditorTab2(EditorController controller, File file) {
		try {
			EditorTab newTab = new EditorTab(file);
			newTab.setClosable(true);
			newTab.setContent(controller.getRoot());
			
			Logger.getLogger(TAG).log(Level.INFO, "controller.getRoot() = " + file);

			newTab.setOnCloseRequest(event -> {
				// todo: show yes/no save dialog
				if (controller.isEdited()) {
					controller.saveContent();
				}
				
				Getos.tabControllers.remove(controller);
				
				controller.shutDown();
				if (MainWindowController.getTabPane().getTabs().size() == 1) {
					MainWindowController.getStatusLabel().setText("");
				}
			});
			newTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
				if (newValue) {
					EditorController tabController = Getos.tabControllers
							.get(MainWindowController.getTabPane().getTabs().indexOf(newTab));
					tabController.onEditorTabSelected();
				}
			});

			controller.setEditorPane(newTab);
			// newTabController.setStatusLabel(statusLabel);
			Getos.tabControllers.add(controller);
			
			Logger.getLogger(TAG).log(Level.INFO, "tabs = " + MainWindowController.getTabPane().getTabs().add(newTab));
			
			MainWindowController.getTabPane().getTabs().add(newTab);
			MainWindowController.getTabPane().getSelectionModel().select(newTab);
		} catch (Exception ex) {
			Utils.showAlert(AlertType.ERROR, ex);
		}
	}

}
