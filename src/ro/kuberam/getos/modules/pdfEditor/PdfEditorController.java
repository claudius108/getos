package ro.kuberam.getos.modules.pdfEditor;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import ro.kuberam.getos.DocumentModel;
import ro.kuberam.getos.controller.factory.RendererController;
import ro.kuberam.getos.events.EventBus;

public final class PdfEditorController extends RendererController {

	private final static String TAG = PdfEditorController.class.getSimpleName();

	@FXML
	private ComboBox<String> selectEditorCombobox;

	@FXML
	private Button extractTablesButton;

	@FXML
	private ScrollPane centerSourcePane;

	@FXML
	private ImageView contentSourcePane;
	
	public PdfEditorController(Application application, Stage stage, DocumentModel documentModel, EventBus eventBus) {
		super(application, stage, documentModel, eventBus);
	}

	@FXML
	public void initialize() {

		eventBus.addEventHandler(PdfEvent.GO_TO_PAGE, event -> {
			contentSourcePane.setImage(getSourceDocumentModel().goToPage((int) event.getData()));

			event.consume();
		});

		extractTablesButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				eventBus.fireEvent("open-target-document");
			}
		});

		// detect PDF version and select the viewer accordingly

		selectEditorCombobox.setValue("jpedal");

		// initialize the PDF viewer
		// setDocumentRenderer(new JpedalRenderer(centerSourcePane, contentSourcePane,
		// getDocumentModel().file()));
	}
}