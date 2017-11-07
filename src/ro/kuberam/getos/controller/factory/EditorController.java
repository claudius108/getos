package ro.kuberam.getos.controller.factory;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ro.kuberam.getos.DocumentMetadata;
import ro.kuberam.getos.modules.editorTab.EditorTab;

public class EditorController extends Controller {

	private final static String TAG = EditorController.class.getSimpleName();

	@FXML
	private BorderPane root;

	private static Stage stage;
	private static DocumentMetadata documentMetadata;
	private static File file;
	protected ExecutorService executorService = null;
	private static EditorTab editorTab;

	public EditorController(Application application, Stage pStage, DocumentMetadata documentMetadata, File pFile) {
		super(application);

		stage = pStage;
		setDocumentMetadata(documentMetadata);
		file = pFile;
		executorService = Executors.newFixedThreadPool(2);
	}

	public static Stage getStage() {
		return stage;
	}
	
	public static Stage setStage(Stage stage) {
		return EditorController.stage = stage;
	}

	public static File getFile() {
		return file;
	}

	public static void setFile(File pFile) {
		file = pFile;
	}

	public static DocumentMetadata getDocumentMetadata() {
		return documentMetadata;
	}

	public void setDocumentMetadata(DocumentMetadata documentMetadata) {
		this.documentMetadata = documentMetadata;
	}

	public Node getRoot() {
		return root;
	}

	public boolean isEdited() {
		Logger.getLogger(TAG).log(Level.INFO, "Not implemented");

		return false;
	}

	public void saveContent() {
		Logger.getLogger(TAG).log(Level.INFO, "Not implemented: {0}", file);
	}

	public void shutDown() {
		try {
			executorService.shutdown();
			executorService.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException ex) {
			Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
		}
	}

	public void setEditorTab(EditorTab pEditorTab) {
		editorTab = pEditorTab;
	}

	public EditorTab getEditorTab() {
		return editorTab;
	}

	public void onEditorTabSelected() {
		// todo: later we can use an other kind of control to show character
		// count
		// or caret position, number of words..
		// setStatusMessage(mStatusMessage, mStatusColor);

		// Without this there is a bug when you open another tab then switch to
		// a previous one.
		// Without this trying to format text (ctrl+space) will always format
		// the last tab.
		// Platform.runLater(() -> mCodeArea.requestFocus());
	}
}
