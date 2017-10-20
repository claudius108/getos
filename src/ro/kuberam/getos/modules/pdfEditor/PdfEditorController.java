package ro.kuberam.getos.modules.pdfEditor;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import ro.kuberam.getos.controller.factory.ControllerFactory;
import ro.kuberam.getos.controller.factory.EditorController;
import ro.kuberam.getos.modules.eventBus.Subscriber;
import ro.kuberam.getos.modules.main.MainWindowController;

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

		Logger.getLogger(TAG).log(Level.INFO, "mainEventBus = " + MainWindowController.mainEventBus);
		
		MainWindowController.mainEventBus.addEventHandler(ActionEvent.ACTION, event -> {
			Logger.getLogger(TAG).log(Level.INFO, "target = " + event.getTarget());
			Logger.getLogger(TAG).log(Level.INFO, "getEventType = " + event.getEventType());
		});
	}

	public static PdfEditorController create(File file) throws Exception {
		pFile = file;

		FXMLLoader loader = new FXMLLoader(
				PdfEditorController.class.getResource("/ro/kuberam/getos/modules/pdfEditor/PdfEditor.fxml"),
				ResourceBundle.getBundle("ro.kuberam.getos.modules.pdfEditor.ui"), null,
				new ControllerFactory(getApplication(), getStage()));

		loader.load();

		return loader.getController();
	}

}
