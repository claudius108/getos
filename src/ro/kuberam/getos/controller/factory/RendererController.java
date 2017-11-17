package ro.kuberam.getos.controller.factory;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ro.kuberam.getos.DocumentModel;
import ro.kuberam.getos.eventBus.EventBus;

public class RendererController extends StageController {

	private final static String TAG = RendererController.class.getSimpleName();

	@FXML
	private BorderPane root;

	private DocumentModel sourceDocumentModel;
	
	public EventBus eventBus;

	public RendererController(Application application, Stage stage, DocumentModel sourceDocumentModel, EventBus eventBus) {
		super(application, stage);

		setSourceDocumentModel(sourceDocumentModel);
		this.eventBus = eventBus;
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
