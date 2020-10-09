package ro.kuberam.getos.controller.factory;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ro.kuberam.getos.DocumentModel;
import ro.kuberam.getos.modules.editorTab.EditorModel;

public class RendererController extends StageController {

	@FXML
	private BorderPane root;

	private DocumentModel sourceDocumentModel;
	
	public EditorModel editorModel;

	public RendererController(Application application, Stage stage, DocumentModel sourceDocumentModel, EditorModel editorModel) {
		super(application, stage);

		setSourceDocumentModel(sourceDocumentModel);
		this.editorModel = editorModel;
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
}
