package ro.kuberam.getos.controller.factory;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ro.kuberam.getos.DocumentModel;
import ro.kuberam.getos.App;
import ro.kuberam.getos.modules.editorTab.EditorTab;

public class EditorController extends StageController {

	@FXML
	private BorderPane root;

	private DocumentModel sourceDocumentModel;
	private static EditorTab editorTab;

	public EditorController(Application application, Stage stage, DocumentModel sourceDocumentModel) {
		super(application, stage);

		setSourceDocumentModel(sourceDocumentModel);
	}

	public DocumentModel getSourceDocumentModel() {
		return sourceDocumentModel;
	}

	public void setSourceDocumentModel(DocumentModel sourceDocumentModel) {
		this.sourceDocumentModel = sourceDocumentModel;
	}

	public Node getRoot() {
		return root;
	}

	public boolean isEdited() {
		Logger.getLogger(TAG).log(Level.INFO, "Not implemented");

		return false;
	}

	public void saveContent() {
		Logger.getLogger(TAG).log(Level.INFO, "Not implemented: {0}");
	}

	public void shutDown() {
		sourceDocumentModel.shutdown();
	}

	public void setEditorTab(EditorTab pEditorTab) {
		editorTab = pEditorTab;
	}

	public EditorTab getEditorTab() {
		return editorTab;
	}

	public void onEditorTabSelected() {
		App.eventBus.fireEvent("update-status-label", getSourceDocumentModel().path().toString());

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
