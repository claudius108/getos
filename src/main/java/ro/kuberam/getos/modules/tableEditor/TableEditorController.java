package ro.kuberam.getos.modules.tableEditor;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import ro.kuberam.getos.DocumentModel;
import ro.kuberam.getos.controller.factory.RendererController;
import ro.kuberam.getos.events.EventBus;
import ro.kuberam.getos.modules.editorTab.EditorEvent;

public final class TableEditorController extends RendererController {

	@FXML
	private ComboBox<String> selectEditorCombobox;

	@FXML
	private Button extractTablesButton;

	@FXML
	private ScrollPane centerSourcePane;

	@FXML
	private TableView contentSourcePane;

	public TableEditorController(Application application, Stage stage, DocumentModel documentModel, EventBus eventBus) {
		super(application, stage, documentModel, eventBus);
	}

	@FXML
	public void initialize() {

		eventBus.addEventHandler(EditorEvent.GO_TO_PAGE, event -> {
//			contentSourcePane.setImage(getSourceDocumentModel().goToPage((int) event.getData()));

			event.consume();
		});

	}
}
