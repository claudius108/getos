package ro.kuberam.getos.modules.pdfEditor;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;
import ro.kuberam.getos.Getos;
import ro.kuberam.getos.controller.factory.ControllerFactory;
import ro.kuberam.getos.controller.factory.EditorController;

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

	private static File pFile;

	public PdfEditorController(Application application, Stage stage, File file) {
		super(application, stage, file);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		Getos.eventBus.fireEvent(Getos.eventsRegistry.get("update-status-label").setData(pFile.getAbsolutePath()));

		selectEditorCombobox.setValue("jpedal");

		// initialize the PDF viewer

		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				Getos.eventBus.fireEvent(Getos.eventsRegistry.get("pdf.back"));
			}
		});

		forwardButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				Getos.eventBus.fireEvent(Getos.eventsRegistry.get("pdf.forward"));
			}
		});

		// contentPane.getChildren().add(new
		// Label(selectEditorCombobox.getValue()));

		// testLabel.textProperty().bind(selectEditorCombobox.valueProperty().asString());

	}

	public static PdfEditorController create(File file) throws Exception {
		setFile(file);

		FXMLLoader loader = new FXMLLoader(
				PdfEditorController.class.getResource("/ro/kuberam/getos/modules/pdfEditor/PdfEditor.fxml"),
				ResourceBundle.getBundle("ro.kuberam.getos.modules.pdfEditor.ui"), null,
				new ControllerFactory(getApplication(), getStage()));

		loader.load();

		return loader.getController();
	}

	public File getFile() {
		return pFile;
	}

	public static void setFile(File pFile) {
		PdfEditorController.pFile = pFile;
	}

}
