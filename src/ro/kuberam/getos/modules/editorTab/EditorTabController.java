package ro.kuberam.getos.modules.editorTab;

import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ro.kuberam.getos.DocumentModel;
import ro.kuberam.getos.Getos;
import ro.kuberam.getos.controller.factory.ControllerFactory;
import ro.kuberam.getos.controller.factory.EditorController;
import ro.kuberam.getos.controller.factory.RendererController;
import ro.kuberam.getos.events.EventBus;
import ro.kuberam.getos.events.FXEventBus;
import ro.kuberam.getos.events.GetosEvent;
import ro.kuberam.getos.modules.pdfEditor.PdfEditorController;
import ro.kuberam.getos.modules.pdfEditor.PdfEvent;
import ro.kuberam.getos.utils.Utils;

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

	public EventBus eventBus;
	public HashMap<String, GetosEvent> eventsRegistry = new HashMap<String, GetosEvent>();

	public EditorTabController(Application application, Stage stage, DocumentModel documentModel) {
		super(application, stage, documentModel);
	}

	@FXML
	public void initialize() {

		// initialize the events
		eventBus = new FXEventBus();
		eventBus.registerEvent("go-to-page", new PdfEvent(PdfEvent.GO_TO_PAGE));
		eventBus.registerEvent("open-target-document", new PdfEvent(PdfEvent.OPEN_TARGET_DOCUMENT));

		// register the event listers
		eventBus.addEventHandler(PdfEvent.OPEN_TARGET_DOCUMENT, event -> {
			// contentSourcePane.setImage(getSourceDocumentModel().goToPage((int)
			// event.getData()));
			contentPane.getItems().add(new BorderPane());

			event.consume();
		});

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
		pagination.pageCountProperty()
				.bind(new SimpleIntegerProperty(getSourceDocumentModel().numberOfPages()).asObject());
		pagination.setPageFactory(index -> {
			//eventBus.fireEvent("go-to-page", index);

			return paginationPane;
		});

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					FXMLLoader loader = new FXMLLoader(
							PdfEditorController.class.getResource(getSourceDocumentModel().fxml()),
							ResourceBundle.getBundle(getSourceDocumentModel().bundle()), null,
							new ControllerFactory(getApplication(), getStage(), getSourceDocumentModel(), eventBus));

					loader.load();

					contentPane.getItems().add(((RendererController) loader.getController()).getRoot());
				} catch (Exception ex) {
					Utils.showAlert(AlertType.ERROR, ex);
				}
			}
		});
	}
}
