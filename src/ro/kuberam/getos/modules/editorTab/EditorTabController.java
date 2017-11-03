package ro.kuberam.getos.modules.editorTab;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ro.kuberam.getos.controller.factory.ControllerFactory;
import ro.kuberam.getos.controller.factory.EditorController;

public final class EditorTabController extends EditorController {

	private final static String TAG = EditorTabController.class.getSimpleName();

	@FXML
	private BorderPane root;

	@FXML
	private SplitPane contentPane;

	private final ExecutorService mExecutorService;

	public static EditorTabController create(Application application, Stage stage, File pFile) throws Exception {
		FXMLLoader loader = new FXMLLoader(
				EditorTabController.class.getResource("/ro/kuberam/getos/modules/editorTab/EditorTab.fxml"),
				ResourceBundle.getBundle("ro.kuberam.getos.modules.main.ui"), null,
				new ControllerFactory(application, stage));

		loader.load();
		return loader.getController();
	}

	public EditorTabController(Application application, Stage stage, File file) {
		super(application, stage, file);
		mExecutorService = Executors.newFixedThreadPool(2);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		ResourceBundle resourceBundle = getResources();
	}

	public void onEditorTabSelected() {
	}

	public void loadContent() {
	}

	public void saveContent() {
		Logger.getLogger(TAG).log(Level.INFO, "Not implemented: {0}", getEditorTab().getFile());
	}

	public void shutDown() {
		try {
			mExecutorService.shutdown();
			mExecutorService.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException ex) {
			Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
		}
	}

	public boolean isEdited() {
		Logger.getLogger(TAG).log(Level.INFO, "Not implemented");
		return false;
	}

	public BorderPane getRoot() {
		return root;
	}

	public SplitPane getSplitPane() {
		return contentPane;
	}
}
