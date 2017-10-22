package ro.kuberam.getos.modules.pdfEditor;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import ro.kuberam.getos.Getos;
import ro.kuberam.getos.controller.factory.ControllerFactory;
import ro.kuberam.getos.controller.factory.EditorController;
import ro.kuberam.getos.modules.editorTab.EditorUtils;

public final class PdfEditorController extends EditorController {

	private final static String TAG = PdfEditorController.class.getSimpleName();

	@FXML
	private ComboBox<String> selectEditorCombobox;

	@FXML
	private Button backButton;

	@FXML
	private Button forwardButton;

	@FXML
	private Label pgCountLabel;

	@FXML
	private ScrollPane center;

	@FXML
	private Group contentPane;

	@FXML
	private Label testLabel;

	static File pFile;

	static {
		Getos.mainEvents.put("PDF", new OpenPdfEvent(OpenPdfEvent.OPEN_FILE));
	}

	public PdfEditorController(Application application, Stage stage, File file) {
		super(application, stage, file);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		selectEditorCombobox.setValue("jpedal");

		// contentPane.getChildren().add(new
		// Label(selectEditorCombobox.getValue()));

		testLabel.textProperty().bind(selectEditorCombobox.valueProperty().asString());

		Getos.mainEventBus.addEventHandler(OpenPdfEvent.OPEN_FILE, event -> {
//			Logger.getLogger(TAG).log(Level.INFO, "target = " + event.getTarget());
//			Logger.getLogger(TAG).log(Level.INFO, "getData = " + event.getData());
		});
	}

	public static void create(File file) throws Exception {
		pFile = file;
		
		FXMLLoader loader = new FXMLLoader(
				PdfEditorController.class.getResource("/ro/kuberam/getos/modules/pdfEditor/PdfEditor.fxml"),
				ResourceBundle.getBundle("ro.kuberam.getos.modules.pdfEditor.ui"), null,
				new ControllerFactory(getApplication(), getStage()));

		loader.load();
		
		EditorUtils.createNewEditorTab2(loader.getController(), file);
	}

}
