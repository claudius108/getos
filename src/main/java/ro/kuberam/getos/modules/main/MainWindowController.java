package ro.kuberam.getos.modules.main;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ro.kuberam.getos.controller.factory.ControllerFactory;
import ro.kuberam.getos.controller.factory.StageController;
import ro.kuberam.getos.modules.about.AboutDialogController;
import ro.kuberam.getos.utils.Utils;

public final class MainWindowController extends StageController {

	private final static String TAG = MainWindowController.class.getSimpleName();

	@FXML
	private BorderPane root;

	@FXML
	private MenuItem openFileMenuItem;

	@FXML
	private MenuItem mItemAbout;

	@FXML
	private MenuItem mItemClose;

	@FXML
	private TabPane mTabPane;

	private FileChooser fileChooser;

	public MainWindowController(Application application, Stage stage) {
		super(application, stage);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		openFileMenuItem.setOnAction(event -> {
			loadFile();
			event.consume();
		});

		mItemClose.setOnAction(event -> {
			onStageClose();
			event.consume();
		});

		mItemAbout.setOnAction(event -> {
			showAboutDialog();
			event.consume();
		});

		fileChooser = new FileChooser();
		fileChooser.setInitialFileName("");
		// mFileChooser.getExtensionFilters().addAll(ParserFileType.getExtensionFilters());

		Stage stage = getStage();
		stage.setTitle(resources.getString("appname") + " v. " + resources.getString("appversion"));
		stage.setScene(new Scene(root));
		stage.centerOnScreen();
		stage.setResizable(true);

		stage.show();
	}

	public static void create(Application application, Stage stage) throws Exception {
		try {
			FXMLLoader.load(MainWindowController.class.getResource("/ro/kuberam/getos/app.fxml"),
					ResourceBundle.getBundle("ro.kuberam.getos.ui"), null, new ControllerFactory(application, stage));
		} catch (Exception ex) {
			Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
			if (ex.getCause() != null) {
				Utils.showAlert(AlertType.ERROR, null, ex.getCause().getLocalizedMessage());
			} else {
				Utils.showAlert(AlertType.ERROR, null, ex.getLocalizedMessage());
			}
		}
	}

	private void loadFile() {
		ResourceBundle resourceBundle = getResources();

		fileChooser.setTitle(resourceBundle.getString("open_file_dialog_title"));

		File file = fileChooser.showOpenDialog(getStage());
		if (file != null) {
			fileChooser.setInitialDirectory(file.getParentFile());

			Utils.showAlert(AlertType.INFORMATION, null, Utils.getExtension(file));

			// We load file according to their extensions, not the content
			// ParserFileType type = ParserFileType.getTypeByExtension(file);
			// createNewEditorTab(type, file, true);
		}
	}

	private void onStageClose() {
		getStage().hide();
	}

	private void showAboutDialog() {
		try {
			AboutDialogController.create(getApplication(), getStage());
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
