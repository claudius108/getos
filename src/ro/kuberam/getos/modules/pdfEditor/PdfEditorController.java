package ro.kuberam.getos.modules.pdfEditor;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ro.kuberam.getos.controller.factory.ControllerFactory;
import ro.kuberam.getos.controller.factory.EditorController;

public final class PdfEditorController extends EditorController {

	@FXML
	private BorderPane root;

	@FXML
	private ScrollPane center;

	@FXML
	private Group contentPane;

	@FXML
	private BorderPane sourcePane;

	@FXML
	private BorderPane targetPane;

	static File pFile;

	public PdfEditorController(Application application, Stage stage, File file) {
		super(application, stage, file);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
	}

	public static PdfEditorController create(Application application, Stage stage, File file) throws Exception {
		pFile = file;

		FXMLLoader loader = new FXMLLoader(
				PdfEditorController.class.getResource("/ro/kuberam/getos/modules/pdfEditor/PdfEditor.fxml"),
				ResourceBundle.getBundle("ro.kuberam.getos.modules.pdfEditor.ui"), null,
				new ControllerFactory(application, stage));

		loader.load();

		return loader.getController();
	}

}
