package ro.kuberam.getos.modules.htmlEditor;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ro.kuberam.getos.Getos;
import ro.kuberam.getos.controller.factory.ControllerFactory;
import ro.kuberam.getos.controller.factory.EditorController;
import ro.kuberam.getos.modules.pdfEditor.jpedal.JpedalRenderer;

public final class HtmlEditorController extends EditorController {

	private final static String TAG = HtmlEditorController.class.getSimpleName();

	@FXML
	private BorderPane root;

	@FXML
	private ComboBox<String> selectEditorCombobox;

	@FXML
	private Button backButton;

	@FXML
	private Button forwardButton;

	@FXML
	private Button zoomInButton;

	@FXML
	private Button zoomOutButton;

	@FXML
	private Button fitToWidthButton;

	@FXML
	private Button fitToHeightButton;

	@FXML
	private Button fitToPageButton;

	@FXML
	private Label pgCountLabel;

	@FXML
	private Button extractTablesButton;

	@FXML
	private SplitPane contentPane;

	@FXML
	private BorderPane sourcePane;

	@FXML
	private ScrollPane centerSourcePane;

	@FXML
	private Group contentSourcePane;

	@FXML
	private BorderPane targetPane;

	private static File pFile;

	public HtmlEditorController(Application application, Stage stage, File file) {
		super(application, stage, file);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		Getos.eventBus.fireEvent(Getos.eventsRegistry.get("update-status-label").setData(pFile.getAbsolutePath()));

		selectEditorCombobox.setValue("jpedal");

		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
			}
		});

		forwardButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
			}
		});

		zoomInButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				Getos.eventBus.fireEvent(Getos.eventsRegistry.get("pdf.zoom-in"));
			}
		});

		zoomOutButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				Getos.eventBus.fireEvent(Getos.eventsRegistry.get("pdf.zoom-out"));
			}
		});

		fitToWidthButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				Getos.eventBus.fireEvent(Getos.eventsRegistry.get("pdf.fit-to-width"));
			}
		});

		fitToHeightButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				Getos.eventBus.fireEvent(Getos.eventsRegistry.get("pdf.fit-to-height"));
			}
		});

		fitToPageButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				Getos.eventBus.fireEvent(Getos.eventsRegistry.get("pdf.fit-to-page"));
			}
		});
		
		extractTablesButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				contentPane.getItems().add(new BorderPane());
			}
		});

		// detect PDF version and select the viewer accordingly

		// initialize the PDF viewer
		new JpedalRenderer(centerSourcePane, contentSourcePane, pFile);
	}

	public static HtmlEditorController create(File file) throws Exception {
		setFile(file);

		FXMLLoader loader = new FXMLLoader(
				HtmlEditorController.class.getResource("/ro/kuberam/getos/modules/pdfEditor/PdfEditor.fxml"),
				ResourceBundle.getBundle("ro.kuberam.getos.modules.pdfEditor.ui"), null,
				new ControllerFactory(getApplication(), getStage()));

		loader.load();

		return loader.getController();
	}

	public File getFile() {
		return pFile;
	}

	public static void setFile(File pFile) {
		HtmlEditorController.pFile = pFile;
	}

}
