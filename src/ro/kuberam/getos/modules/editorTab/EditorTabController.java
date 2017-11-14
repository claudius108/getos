package ro.kuberam.getos.modules.editorTab;

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
import ro.kuberam.getos.modules.pdfEditor.PdfEditorController;
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

	public EditorTabController(Application application, Stage stage, DocumentModel documentModel) {
		super(application, stage, documentModel);
	}

	@FXML
	public void initialize() {

		zoomInButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				Getos.eventBus.fireEvent(Getos.eventsRegistry.get("pdf.zoom-in"));
			}
		});

		zoomOutButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				Getos.eventBus.fireEvent(Getos.eventsRegistry.get("pdf.zoom-out"));
			}
		});

		fitToWidthButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				Getos.eventBus.fireEvent(Getos.eventsRegistry.get("pdf.fit-to-width"));
			}
		});

		fitToHeightButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				Getos.eventBus.fireEvent(Getos.eventsRegistry.get("pdf.fit-to-height"));
			}
		});

		fitToPageButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				Getos.eventBus.fireEvent(Getos.eventsRegistry.get("pdf.fit-to-page"));
			}
		});

		pagination.setCurrentPageIndex(0);
		pagination.pageCountProperty().bind(new SimpleIntegerProperty(getDocumentModel().numberOfPages()).asObject());
		pagination.setPageFactory(index -> {
			Getos.eventBus.fireEvent(Getos.eventsRegistry.get("pdf.go-to-page").setData(index));

			return paginationPane;
		});

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					FXMLLoader loader = new FXMLLoader(PdfEditorController.class.getResource(getDocumentModel().fxml()),
							ResourceBundle.getBundle(getDocumentModel().bundle()), null,
							new ControllerFactory(getApplication(), getStage(), getDocumentModel()));

					loader.load();

					contentPane.getItems().add(((EditorController) loader.getController()).getRoot());
				} catch (Exception ex) {
					Utils.showAlert(AlertType.ERROR, ex);
				}
			}
		});
	}
}
