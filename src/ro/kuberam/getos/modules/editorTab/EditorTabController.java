package ro.kuberam.getos.modules.editorTab;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ro.kuberam.getos.DocumentMetadata;
import ro.kuberam.getos.controller.factory.EditorController;
import ro.kuberam.getos.modules.pdfEditor.PdfEditorController;

public final class EditorTabController extends EditorController {

	private final static String TAG = EditorTabController.class.getSimpleName();

	@FXML
	private BorderPane root;

	@FXML
	private SplitPane contentPane;

	public EditorTabController(Application application, Stage stage, DocumentMetadata documentMetadata, File file) {
		super(application, stage, documentMetadata, file);
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
}
