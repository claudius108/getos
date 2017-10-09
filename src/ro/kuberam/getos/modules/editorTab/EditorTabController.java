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
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import ro.kuberam.getos.controller.factory.ControllerFactory;
import ro.kuberam.getos.controller.factory.StageController;

public class EditorTabController extends StageController {

	private final static String TAG = EditorTabController.class.getSimpleName();

	@FXML
	private BorderPane root;

	@FXML
	private SplitPane contentPane;

	@FXML
	private BorderPane sourcePane;

	@FXML
	private BorderPane targetPane;

	private String mStatusMessage;
	private Paint mStatusColor;
	private Label mStatusLabel;

	private static EditorTab mEditorTab;

	private final ExecutorService mExecutorService;

	public static EditorTabController create(Application application, Stage stage, File file)
			throws Exception {
		Logger.getLogger(TAG).log(Level.INFO, "file = " + file);
		
//		FXMLLoader loader = new FXMLLoader(
//				EditorTabController.class.getResource("/ro/kuberam/getos/modules/editorTab/EditorTab.fxml"),
//				ResourceBundle.getBundle("ro.kuberam.getos.ui"), null, new ControllerFactory(application, stage, type));
		
		FXMLLoader loader = new FXMLLoader(
				EditorTabController.class.getResource("/ro/kuberam/getos/modules/pdfViewer/PDF-viewer.fxml"),
				ResourceBundle.getBundle("ro.kuberam.getos.modules.pdfViewer.ui"), null, new ControllerFactory(application, stage, file));

		loader.load();
		return loader.getController();
	}

	public EditorTabController(Application application, Stage stage, File file) {
		super(application, stage);
		
		mExecutorService = Executors.newFixedThreadPool(2);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
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
//		String fileTypeName = fileType.getName();
//
//		switch (fileTypeName) {
//		case "PDF":
//			try {
////				PdfViewerController viewerController = PdfViewerController.create(getApplication(), getStage(),
////						getEditorTab().getFile());
//
//				//root.getChildren().add(viewerController.getRoot());
//			} catch (Exception ex) {
//				Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
//				if (ex.getCause() != null) {
//					Utils.showAlert(AlertType.ERROR, null, ex.getCause().getLocalizedMessage());
//				} else {
//					Utils.showAlert(AlertType.ERROR, null, ex.getLocalizedMessage());
//				}
//			}
//			break;
//		}
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

	public void setEditorPane(EditorTab editorTab) {
		mEditorTab = editorTab;
	}

	public static EditorTab getEditorTab() {
		return mEditorTab;
	}

	public BorderPane getRoot() {
		return root;
	}

	public void setStatusLabel(Label label) {
		mStatusLabel = label;
	}

	public Label getStatusLabel() {
		return mStatusLabel;
	}

}
