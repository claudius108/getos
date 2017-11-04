package ro.kuberam.getos.modules.editorTab;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ro.kuberam.getos.Getos;
import ro.kuberam.getos.controller.factory.ControllerFactory;
import ro.kuberam.getos.controller.factory.EditorController;
import ro.kuberam.getos.modules.pdfEditor.PdfEditorController;

public final class EditorTabController extends EditorController {

	private final static String TAG = EditorTabController.class.getSimpleName();

	@FXML
	private BorderPane root;

	@FXML
	private SplitPane contentPane;

	public EditorTabController(Application application, Stage stage, File file) {
		super(application, stage, file);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
//				Getos.eventBus.fireEvent(Getos.eventsRegistry.get(documentType).setData(file));
				EditorController controller = null;
				try {
					controller = PdfEditorController.create(getFile());
					
					contentPane.getItems().add(controller.getRoot());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static EditorTabController create(Application application, Stage stage, File pFile) throws Exception {
		FXMLLoader loader = new FXMLLoader(
				EditorTabController.class.getResource("/ro/kuberam/getos/modules/editorTab/EditorTab.fxml"),
				ResourceBundle.getBundle("ro.kuberam.getos.modules.main.ui"), null,
				new ControllerFactory(application, stage, pFile));

		loader.load();
		return loader.getController();
	}
}
