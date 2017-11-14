package ro.kuberam.getos.modules.pdfEditor;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ro.kuberam.getos.DocumentModel;
import ro.kuberam.getos.Getos;
import ro.kuberam.getos.controller.factory.EditorController;

public final class PdfEditorController extends EditorController {

	private final static String TAG = PdfEditorController.class.getSimpleName();

	@FXML
	private BorderPane root;

	@FXML
	private ComboBox<String> selectEditorCombobox;

	@FXML
	private Button extractTablesButton;

	@FXML
	private ScrollPane centerSourcePane;

	@FXML
	private ImageView contentSourcePane;

	public PdfEditorController(Application application, Stage stage, DocumentModel documentModel) {
		super(application, stage, documentModel);
	}

	@FXML
	public void initialize() {

		Getos.eventBus.addEventHandler(PdfEvent.PDF_GO_TO_PAGE, event -> {
			contentSourcePane.setImage(getDocumentModel().goToPage((int) event.getData()));

			event.consume();
		});

		extractTablesButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				// contentPane.getItems().add(new BorderPane());
			}
		});

		// detect PDF version and select the viewer accordingly

		selectEditorCombobox.setValue("jpedal");

		// initialize the PDF viewer
		// setDocumentRenderer(new JpedalRenderer(centerSourcePane, contentSourcePane,
		// getDocumentModel().file()));
	}
}