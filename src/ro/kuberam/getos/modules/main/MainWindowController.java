package ro.kuberam.getos.modules.main;

import java.io.File;
import java.net.URL;
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
import ro.kuberam.getos.Getos;
import ro.kuberam.getos.controller.factory.ControllerFactory;
import ro.kuberam.getos.controller.factory.EditorController;
import ro.kuberam.getos.controller.factory.StageController;
import ro.kuberam.getos.documentTypeDetection.ViewerFileType;
import ro.kuberam.getos.events.FileEvent;
import ro.kuberam.getos.events.UserInterfaceEvent;
import ro.kuberam.getos.modules.about.AboutDialogController;
import ro.kuberam.getos.modules.editorTab.EditorTab;
import ro.kuberam.getos.modules.editorTab.EditorTabController;
import ro.kuberam.getos.utils.Utils;

public final class MainWindowController extends StageController {

	private final static String TAG = MainWindowController.class.getSimpleName();

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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		Getos.eventsRegistry.put("open-file", new FileEvent(FileEvent.OPEN_FILE));
		Getos.eventsRegistry.put("update-status-label", new UserInterfaceEvent(UserInterfaceEvent.UPDATE_STATUS_LABEL));

		Getos.eventBus.addEventHandler(FileEvent.OPEN_FILE, event -> {
			try {
				createNewEditorTab((EditorController) event.getData());
			} catch (Exception ex) {
				Utils.showAlert(AlertType.ERROR, ex);
			}

			event.consume();
		});
		Getos.eventBus.addEventHandler(UserInterfaceEvent.UPDATE_STATUS_LABEL, event -> {
			statusLabel.setText((String) event.getData());

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

			File file = new File("/home/claudius/Downloads/comune.pdf");

			openFile(file);

			event.consume();
		});

		pdfButton.setOnAction(event -> {
			File file = new File("/home/claudius/Downloads/comune.pdf");
			createNewEditorTab2(file);

			event.consume();
		});

		saveEditorContentButton.setOnAction(event -> {
			ResourceBundle resourceBundle = getResources();

			fileChooser.setTitle(resourceBundle.getString("open_file_dialog_title"));

			File file = fileChooser.showOpenDialog(getStage());
			if (file != null) {
				fileChooser.setInitialDirectory(file.getParentFile());
			}

			openFile(file);

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
		fileChooser.getExtensionFilters().addAll(ViewerFileType.getExtensionFilters());

		Stage stage = getStage();
		stage.setTitle(resources.getString("appname") + " v. " + resources.getString("appversion"));
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
						openFile(file);
					}
				}
				event.setDropCompleted(success);
				event.consume();
			}
		});

		stage.show();
	}

	public static void create(Application application, Stage stage) throws Exception {
		try {
			FXMLLoader.load(MainWindowController.class.getResource("/ro/kuberam/getos/modules/main/MainWindow.fxml"),
					ResourceBundle.getBundle("ro.kuberam.getos.modules.main.ui"), null,
					new ControllerFactory(application, stage));
		} catch (Exception ex) {
			Utils.showAlert(AlertType.ERROR, ex);
		}
	}

	private void onStageClose() {
		Getos.tabControllers.forEach(tabController -> {
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

	public void createNewEditorTab(EditorController newTabController) {
		File file = newTabController.getFile();

		EditorTab newTab = new EditorTab(file);
		newTab.setClosable(true);
		newTab.setContent(newTabController.getRoot());

		newTab.setOnCloseRequest(event -> {
			// todo: show yes/no save dialog
			if (newTabController.isEdited()) {
				newTabController.saveContent();
			}
			Getos.tabControllers.remove(newTabController);
			newTabController.shutDown();
			if (tabPane.getTabs().size() == 1) {
				statusLabel.setText("");
			}
		});
		newTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				EditorController tabController = Getos.tabControllers.get(tabPane.getTabs().indexOf(newTab));
				tabController.onEditorTabSelected();
			}
		});

		newTabController.setEditorTab(newTab);
		// newTabController.setStatusLabel(statusLabel);
		Getos.tabControllers.add(newTabController);
		tabPane.getTabs().add(newTab);
		tabPane.getSelectionModel().select(newTab);
	}

	public void createNewEditorTab2(File file) {
		try {
			EditorController newTabController = EditorTabController.create(getApplication(), getStage(), file);

			EditorTab newTab = new EditorTab(file);
			newTab.setClosable(true);
			newTab.setContent(newTabController.getRoot());

			newTab.setOnCloseRequest(event2 -> {
				// todo: show yes/no save dialog
				if (newTabController.isEdited()) {
					newTabController.saveContent();
				}
				Getos.tabControllers.remove(newTabController);
				newTabController.shutDown();
				if (tabPane.getTabs().size() == 1) {
					statusLabel.setText("");
				}
			});
			newTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
				if (newValue) {
					EditorController tabController = Getos.tabControllers.get(tabPane.getTabs().indexOf(newTab));
					tabController.onEditorTabSelected();
				}
			});

			newTabController.setEditorTab(newTab);
			// newTabController.setStatusLabel(statusLabel);
			Getos.tabControllers.add(newTabController);
			tabPane.getTabs().add(newTab);
			tabPane.getSelectionModel().select(newTab);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void openFile(File file) {
		String documentType = detectDocumentType(file);

		if (documentType == null) {
			Utils.showAlert(AlertType.ERROR, file.getName(), getResources().getString("cant_handle_filetype"));
			return;
		}

		createNewEditorTab2(file);
	}

	private String detectDocumentType(File file) {
		String documentType = null;

		ViewerFileType type = ViewerFileType.getTypeByExtension(file);

		if (type != null) {
			documentType = type.getName();
		}

		return documentType;
	}
}
