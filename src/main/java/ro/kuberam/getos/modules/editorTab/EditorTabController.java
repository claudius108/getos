package ro.kuberam.getos.modules.editorTab;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ro.kuberam.getos.DocumentModel;
import ro.kuberam.getos.Getos;
import ro.kuberam.getos.controller.factory.EditorController;

public final class EditorTabController extends EditorController {

	@FXML
	private BorderPane root;

	@FXML
	private Button zoomInButton;

	@FXML
	private Button zoomOutButton;

	@FXML
	private Button fitToWidthButton;

	@FXML
	private Button fitToHeightButton;

	@FXML
	private Button fitToPageButton;

	@FXML
	private Pagination pagination;

	@FXML
	private BorderPane paginationPane;

	@FXML
	private SplitPane contentPane;

	public EditorModel editorModel;

	public EditorTabController(Application application, Stage stage, DocumentModel documentModel) {
		super(application, stage, documentModel);
	}

	@FXML
	public void initialize() {

		editorModel = new EditorModel(getApplication(), getStage(), contentPane);

		zoomInButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				Getos.eventBus.fireEvent("pdf.zoom-in");
			}
		});

		zoomOutButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				Getos.eventBus.fireEvent("pdf.zoom-out");
			}
		});

		fitToWidthButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				Getos.eventBus.fireEvent("pdf.fit-to-width");
			}
		});

		fitToHeightButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				Getos.eventBus.fireEvent("pdf.fit-to-height");
			}
		});

		fitToPageButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				Getos.eventBus.fireEvent("pdf.fit-to-page");
			}
		});

		pagination.setCurrentPageIndex(0);
		pagination.pageCountProperty().bind(new SimpleIntegerProperty(
				Integer.parseInt(getSourceDocumentModel().generalMetadata().get("dcterms:extent"))).asObject());
		pagination.setPageFactory(index -> {
			editorModel.eventBus.fireEvent("go-to-page", index);

			return paginationPane;
		});

		editorModel.loadRenderer(getSourceDocumentModel(), editorModel);
	}
}
