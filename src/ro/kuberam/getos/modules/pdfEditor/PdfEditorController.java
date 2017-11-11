package ro.kuberam.getos.modules.pdfEditor;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ro.kuberam.getos.DocumentModel;
import ro.kuberam.getos.Getos;
import ro.kuberam.getos.controller.factory.ControllerFactory;
import ro.kuberam.getos.controller.factory.EditorController;
import ro.kuberam.getos.modules.pdfEditor.jpedal.JpedalRenderer;

public final class PdfEditorController extends EditorController {

	private final static String TAG = PdfEditorController.class.getSimpleName();

	@FXML
	private BorderPane root;

	@FXML
	private ComboBox<String> selectEditorCombobox;

	@FXML
	private Button backButton;

	@FXML
	private Button forwardButton;

	@FXML
	private TextField currentPageTextfield;

	@FXML
	private Label pgCountLabel;

	@FXML
	private Button extractTablesButton;

	@FXML
	private ScrollPane centerSourcePane;

	@FXML
	private Group contentSourcePane;

	public PdfEditorController(Application application, Stage stage, DocumentModel documentModel) {
		super(application, stage, documentModel);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		Getos.eventBus.fireEvent(Getos.eventsRegistry.get("update-status-label").setData(getDocumentModel().path()));

		BooleanBinding booleanBind = currentPageTextfield.textProperty().isEqualTo("1");
		backButton.disableProperty().bind(booleanBind);

		// Getos.eventBus.addEventHandler(PdfEvent.PDF_ENABLE_BUTTON, event -> {
		// String buttonId = (String) event.getData();
		//
		// switch (buttonId) {
		// case "backButton":
		// backButton.setDisable(false);
		// case "forwardButton":
		// forwardButton.setDisable(false);
		// }
		//
		// event.consume();
		// });
		//
		// Getos.eventBus.addEventHandler(PdfEvent.PDF_DISABLE_BUTTON, event -> {
		// String buttonId = (String) event.getData();
		//
		// switch (buttonId) {
		// case "backButton":
		// backButton.setDisable(true);
		// case "forwardButton":
		// forwardButton.setDisable(true);
		// }
		//
		// event.consume();
		// });

		Getos.eventBus.addEventHandler(PdfEvent.PDF_GO_TO_PAGE, event -> {
			//getDocumentRenderer().pageForward();
			getDocumentModel().goToPage(5);

			event.consume();
		});

		Getos.eventBus.addEventHandler(PdfEvent.PDF_UPDATE_PAGE_COUNT, event -> {
			pgCountLabel.setText(getResources().getString("pages_number_prefix") + " " + event.getData());

			event.consume();
		});

		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				getDocumentRenderer().pageBack();
			}
		});

		forwardButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				getDocumentRenderer().pageForward();
			}
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
		//setDocumentRenderer(new JpedalRenderer(centerSourcePane, contentSourcePane, getDocumentModel().file()));
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
