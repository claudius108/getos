package ro.kuberam.getos.modules.htmlEditor;

import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ro.kuberam.getos.App;
import ro.kuberam.getos.controller.factory.EditorController;
import ro.kuberam.getos.events.EventBus;

public final class HtmlEditorController extends EditorController {

	@FXML
	private BorderPane root;

	@FXML
	private ComboBox<String> selectEditorCombobox;

	@FXML
	private Button backButton;

	@FXML
	private Button forwardButton;

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
	private Label pgCountLabel;

	@FXML
	private Button extractTablesButton;

	@FXML
	private SplitPane contentPane;

	@FXML
	private BorderPane sourcePane;

	@FXML
	private ScrollPane centerSourcePane;

	@FXML
	private Group contentSourcePane;

	@FXML
	private BorderPane targetPane;

	private static File pFile;

	private EventBus eventBus;

	public HtmlEditorController(Application application, Stage stage, DocumentModel documentModel, EventBus eventBus) {
		super(application, stage, documentModel);
	}

	@FXML
	public void initialize() {

		selectEditorCombobox.setValue("jpedal");

		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
			}
		});

		forwardButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
			}
		});

		zoomInButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				App.eventBus.fireEvent("pdf.zoom-in");
			}
		});

		zoomOutButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				App.eventBus.fireEvent("pdf.zoom-out");
			}
		});

		fitToWidthButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				App.eventBus.fireEvent("pdf.fit-to-width");
			}
		});

		fitToHeightButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				App.eventBus.fireEvent("pdf.fit-to-height");
			}
		});

		fitToPageButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				App.eventBus.fireEvent("pdf.fit-to-page");
			}
		});

		extractTablesButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				contentPane.getItems().add(new BorderPane());
			}
		});
	}
}
