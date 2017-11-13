package ro.kuberam.getos.modules.pdfEditor;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ro.kuberam.getos.DocumentModel;
import ro.kuberam.getos.Getos;
import ro.kuberam.getos.controller.factory.ControllerFactory;
import ro.kuberam.getos.controller.factory.EditorController;

public final class PdfEditorController extends EditorController {

	private final static String TAG = PdfEditorController.class.getSimpleName();

	@FXML
	private BorderPane root;

	@FXML
	private ComboBox<String> selectEditorCombobox;

	@FXML
	private TextField currentPageTextfield;

	@FXML
	private Label pgCountLabel;

	@FXML
	private Button extractTablesButton;

	@FXML
	private ScrollPane centerSourcePane;

	@FXML
	private ImageView contentSourcePane;

	public PdfEditorController(Application application, Stage stage, DocumentModel documentModel) {
		super(application, stage, documentModel);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		BooleanBinding booleanBind = currentPageTextfield.textProperty().isEqualTo("1");

		Getos.eventBus.addEventHandler(PdfEvent.PDF_GO_TO_PAGE, event -> {
			contentSourcePane.setImage(getDocumentModel().goToPage((int) event.getData()));

			event.consume();
		});

		Getos.eventBus.addEventHandler(PdfEvent.PDF_UPDATE_PAGE_COUNT, event -> {
			pgCountLabel.setText(getResources().getString("pages_number_prefix") + " " + event.getData());

			event.consume();
		});

		extractTablesButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
//				contentPane.getItems().add(new BorderPane());
			}
		});

		// detect PDF version and select the viewer accordingly

		selectEditorCombobox.setValue("jpedal");

		// initialize the PDF viewer
		// setDocumentRenderer(new JpedalRenderer(centerSourcePane, contentSourcePane,
		// getDocumentModel().file()));
	}

	public static PdfEditorController create() throws Exception {

		FXMLLoader loader = new FXMLLoader(
				PdfEditorController.class.getResource("/ro/kuberam/getos/modules/pdfEditor/PdfEditor.fxml"),
				ResourceBundle.getBundle("ro.kuberam.getos.modules.pdfEditor.ui"), null,
				new ControllerFactory(getApplication(), getStage(), getDocumentModel()));

		loader.load();

		return loader.getController();
	}
}
