package ro.kuberam.getos.controller.factory;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ro.kuberam.getos.modules.editorTab.EditorTab;
import ro.kuberam.getos.modules.pdfViewer.PdfViewerController;

public class EditorController extends Controller {

	private final static String TAG = PdfViewerController.class.getSimpleName();

	private final Stage pStage;
	private final File pFile;
	private BorderPane root;
	protected ExecutorService mExecutorService = null;
	private static EditorTab mEditorTab;

	public EditorController(Application application, Stage stage, File file) {
		super(application);

		pStage = stage;
		pFile = file;
		
		mExecutorService = Executors.newFixedThreadPool(2);
	}

	public Stage getStage() {
		return pStage;
	}

	public File getFile() {
		return pFile;
	}

	public Node getRoot() {
		return root;
	}

	public boolean isEdited() {
		Logger.getLogger(TAG).log(Level.INFO, "Not implemented");

		return false;
	}

	public void saveContent() {
		Logger.getLogger(TAG).log(Level.INFO, "Not implemented: {0}", pFile);
	}

	public void shutDown() {
		try {
			mExecutorService.shutdown();
			mExecutorService.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException ex) {
			Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
		}
	}

	public void setEditorPane(EditorTab editorTab) {
		mEditorTab = editorTab;
	}

	public static EditorTab getEditorTab() {
		return mEditorTab;
	}

	public static void setmEditorTab(EditorTab mEditorTab) {
		EditorController.mEditorTab = mEditorTab;
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
