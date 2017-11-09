package ro.kuberam.getos.modules.editorTab;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import ro.kuberam.getos.DocumentMetadata;
import ro.kuberam.getos.Getos;
import ro.kuberam.getos.controller.factory.EditorController;

public final class EditorTabController extends EditorController {

	private final static String TAG = EditorTabController.class.getSimpleName();

	@FXML
	private BorderPane root;

	@FXML
	private Pagination pagination;

	@FXML
	private SplitPane contentPane;

	public EditorTabController(Application application, Stage stage, DocumentMetadata documentMetadata) {
		super(application, stage, documentMetadata);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		pagination.setPageFactory(new Callback<Integer, Node>() {
			@Override
			public Node call(Integer pageNumber) {
				Getos.eventBus.fireEvent(Getos.eventsRegistry.get("pdf.go-to-page"));

				return contentPane.getItems().get(0).lookup("#root");
			}
		});

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				EditorController controller = null;
				try {
					controller = (EditorController) Class.forName(getDocumentMetadata().controller())
							.getDeclaredMethod("create").invoke(null);

					contentPane.getItems().add(controller.getRoot());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
