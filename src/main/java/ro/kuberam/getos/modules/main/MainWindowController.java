package ro.kuberam.getos.modules.main;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ro.kuberam.getos.App;
import ro.kuberam.getos.DocumentModel;
import ro.kuberam.getos.controller.factory.ControllerFactory;
import ro.kuberam.getos.controller.factory.EditorController;
import ro.kuberam.getos.controller.factory.StageController;
import ro.kuberam.getos.documentType.DocumentTypes;
import ro.kuberam.getos.events.UserInterfaceEvent;
import ro.kuberam.getos.modules.about.AboutDialogController;
import ro.kuberam.getos.modules.editorTab.EditorTab;
import ro.kuberam.getos.modules.editorTab.EditorTabController;
import ro.kuberam.getos.utils.Utils;

public final class MainWindowController extends StageController {

	@FXML
	private BorderPane root;

	@FXML
	private MenuItem mItemAbout;

	@FXML
	private Button closeAppButton;

	@FXML
	private Button openFileButton;

	@FXML
	private Button pdfButton;

	@FXML
	private Button saveEditorContentButton;

	@FXML
	private TabPane tabPane;

	@FXML
	private Label statusLabel;

	EditorController newTabController = null;
	private FileChooser fileChooser;

	public MainWindowController(Application application, Stage stage) {
		super(application, stage);
	}

	@FXML
	public void initialize() {

		App.eventBus.registerEvent("update-status-label",
				new UserInterfaceEvent(UserInterfaceEvent.UPDATE_STATUS_LABEL));

		App.eventBus.addEventHandler(UserInterfaceEvent.UPDATE_STATUS_LABEL, event -> {
			statusLabel.setText((String) ((Object[]) event.getData())[0]);

			event.consume();
		});

		openFileButton.setOnAction(event -> {

			// ResourceBundle resourceBundle = getResources();
			//
			// fileChooser.setTitle(resourceBundle.getString("open_file_dialog_title"));
			//
			// File file = fileChooser.showOpenDialog(getStage());
			// if (file != null) {
			// fileChooser.setInitialDirectory(file.getParentFile());
			// }

			Path path = Paths.get("/home/claudius/comune.pdf");

			createNewEditorTab(DocumentTypes.getDocumentModel(path, getResources()));

			event.consume();
		});

		saveEditorContentButton.setOnAction(event -> {
			ResourceBundle resourceBundle = getResources();

			fileChooser.setTitle(resourceBundle.getString("open_file_dialog_title"));

			File file = fileChooser.showOpenDialog(getStage());
			if (file != null) {
				fileChooser.setInitialDirectory(file.getParentFile());
			}

			createNewEditorTab(DocumentTypes.getDocumentModel(file.toPath(), getResources()));

			event.consume();
		});

		closeAppButton.setOnAction(event -> {
			onStageClose();
			event.consume();
		});

		mItemAbout.setOnAction(event -> {
			showAboutDialog();
			event.consume();
		});

		fileChooser = new FileChooser();
		fileChooser.setInitialFileName("");
		fileChooser.getExtensionFilters().addAll(DocumentTypes.getExtensionFilters());

		Stage stage = getStage();
		stage.setTitle(getResources().getString("appname") + " v. " + getResources().getString("appversion"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.setResizable(true);
		stage.setMaximized(true);

		scene.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(final DragEvent event) {
				final Dragboard db = event.getDragboard();
				if (db.hasFiles()) {
					event.acceptTransferModes(TransferMode.COPY);
				} else {
					event.consume();
				}
			}
		});

		scene.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(final DragEvent event) {
				final Dragboard db = event.getDragboard();
				boolean success = false;
				if (db.hasFiles()) {
					success = true;
					for (File file : db.getFiles()) {
						createNewEditorTab(DocumentTypes.getDocumentModel(file.toPath(), getResources()));
					}
				}
				event.setDropCompleted(success);
				event.consume();
			}
		});

		stage.show();
	}

	private void onStageClose() {
		App.tabControllers.forEach(tabController -> {
			if (tabController.isEdited()) {
				// todo: show yes/no save dialog
				tabController.saveContent();
			}
			tabController.shutDown();
		});

		getStage().hide();
	}

	private void showAboutDialog() {
		try {
			AboutDialogController.create(getApplication(), getStage());
		} catch (Exception ex) {
			Utils.showAlert(AlertType.ERROR, ex);
		}
	}

	public void createNewEditorTab(DocumentModel documentModel) {
		try {
			FXMLLoader loader = new FXMLLoader(
					EditorTabController.class.getResource("/ro/kuberam/getos/modules/editorTab/EditorTab.fxml"),
					ResourceBundle.getBundle("ro.kuberam.getos.modules.main.ui"), null,
					new ControllerFactory(getApplication(), getStage(), documentModel));
			loader.load();

			EditorController newTabController = loader.getController();

			EditorTab newTab = new EditorTab(documentModel.path().toFile());
			newTab.setClosable(true);
			newTab.setContent(newTabController.getRoot());

			newTab.setOnCloseRequest(event -> {
				// todo: show yes/no save dialog
				if (newTabController.isEdited()) {
					newTabController.saveContent();
				}
				App.tabControllers.remove(newTabController);
				newTabController.shutDown();
				if (tabPane.getTabs().size() == 1) {
					statusLabel.setText("");
				}
			});
			newTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
				if (newValue) {
					EditorController tabController = App.tabControllers.get(tabPane.getTabs().indexOf(newTab));
					tabController.onEditorTabSelected();
				}
			});

			newTabController.setEditorTab(newTab);
			App.tabControllers.add(newTabController);
			tabPane.getTabs().add(newTab);
			tabPane.getSelectionModel().select(newTab);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
