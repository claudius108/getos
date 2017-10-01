package ro.kuberam.getos.modules.editorTab;

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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import ro.kuberam.getos.controller.factory.ControllerFactory;
import ro.kuberam.getos.controller.factory.StageController;
import ro.kuberam.getos.modules.viewers.ViewerFileType;

public final class EditorTabController extends StageController {

	private final static String TAG = EditorTabController.class.getSimpleName();

	@FXML
	private BorderPane mRoot;

	@FXML
	private SplitPane mSplitPane;

	private ContextMenu mEditorContextMenu;

	private String mStatusMessage;
	private Paint mStatusColor;
	private Label mStatusLabel;

	private ViewerFileType fileType;
	private EditorTab editorTab;

	private final ExecutorService mExecutorService;

	public static EditorTabController create(Application application, Stage stage, ViewerFileType type)
			throws Exception {
		FXMLLoader loader = new FXMLLoader(
				EditorTabController.class.getResource("/ro/kuberam/getos/modules/editorTab/EditorTab.fxml"),
				ResourceBundle.getBundle("ro.kuberam.getos.ui"), null, new ControllerFactory(application, stage, type));

		loader.load();
		return loader.getController();
	}

	public EditorTabController(Application application, Stage stage, ViewerFileType type) {
		super(application, stage);
		fileType = type;
		mExecutorService = Executors.newFixedThreadPool(2);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		ResourceBundle resourceBundle = getResources();
	}

	public void onEditorTabSelected() {
		// todo: later we can use an other kind of control to show character
		// count
		// or caret position, number of words..
		setStatusMessage(mStatusMessage, mStatusColor);

		// Without this there is a bug when you open another tab then switch to
		// a previous one.
		// Without this trying to format text (ctrl+space) will always format
		// the last tab.
		// Platform.runLater(() -> mCodeArea.requestFocus());
	}

	public void loadContent() {
		String line;
		StringBuilder stringBuilder = new StringBuilder();

		// File file = getEditorTab().getFile();
		// String lineSeparator = String.format("%n");
		// try (BufferedReader reader = new BufferedReader(new
		// FileReader(file))) {
		// while ((line = reader.readLine()) != null) {
		// stringBuilder.append(line).append(lineSeparator);
		// }
		// } catch (IOException ex) {
		// Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
		// }
		//
		// mCodeArea.replaceText(stringBuilder.toString());
	}

	private void setStatusMessage(String message, Paint color) {
		mStatusColor = color;
		mStatusMessage = message;
		mStatusLabel.setText(mStatusMessage);
		mStatusLabel.setTextFill(mStatusColor);
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
		return mRoot;
	}

	public SplitPane getSplitPane() {
		return mSplitPane;
	}

	public void setStatusLabel(Label label) {
		mStatusLabel = label;
	}

	public Label getStatusLabel() {
		return mStatusLabel;
	}

	public EditorTab getEditorTab() {
		return editorTab;
	}

}
