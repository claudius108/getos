package ro.kuberam.getos.modules.editorTab;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import ro.kuberam.getos.DocumentModel;
import ro.kuberam.getos.controller.factory.ControllerFactory;
import ro.kuberam.getos.controller.factory.RendererController;
import ro.kuberam.getos.events.EventBus;
import ro.kuberam.getos.events.FXEventBus;
import ro.kuberam.getos.events.GetosEvent;
import ro.kuberam.getos.utils.Utils;

public class EditorModel {

	private Application application;
	private Stage stage;
	public EventBus eventBus;
	public HashMap<String, GetosEvent> eventsRegistry = new HashMap<String, GetosEvent>();
	public List<Path> openedDocuments = new ArrayList<Path>();
	private SplitPane contentPane;

	public EditorModel(Application application, Stage stage, SplitPane contentPane) {
		this.application = application;
		this.stage = stage;
		this.contentPane = contentPane;
		eventBus = new FXEventBus();

		eventBus.registerEvent("go-to-page", new EditorEvent(EditorEvent.GO_TO_PAGE));
		eventBus.registerEvent("open-target-document", new EditorEvent(EditorEvent.OPEN_TARGET_DOCUMENT));
		eventBus.registerEvent("document-opened", new EditorEvent(EditorEvent.DOCUMENT_OPENED));

		eventBus.addEventHandler(EditorEvent.OPEN_TARGET_DOCUMENT, event -> {
			Path targetDocumentPath = (Path) event.getData();

			loadRenderer(new ro.kuberam.getos.modules.tableEditor.DocumentModel(targetDocumentPath), this);

			event.consume();
		});

		eventBus.addEventHandler(EditorEvent.DOCUMENT_OPENED, event -> {
			openedDocuments.add((Path) event.getData());

			event.consume();
		});

	}

	public void loadRenderer(DocumentModel documentModel, EditorModel editorModel) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource(documentModel.fxml()),
							ResourceBundle.getBundle(documentModel.bundle()), null,
							new ControllerFactory(application, stage, documentModel, editorModel));

					loader.load();

					contentPane.getItems().add(((RendererController) loader.getController()).getRoot());
				} catch (Exception ex) {
					Utils.showAlert(AlertType.ERROR, ex);
				}
			}
		});
	}

}
