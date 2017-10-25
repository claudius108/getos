package ro.kuberam.getos;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import ro.kuberam.getos.controller.factory.EditorController;
import ro.kuberam.getos.eventBus.EventBus;
import ro.kuberam.getos.eventBus.FXEventBus;
import ro.kuberam.getos.eventBus.GetosEvent;
import ro.kuberam.getos.modules.main.MainWindowController;
import ro.kuberam.getos.utils.Utils;

public class Getos extends Application {

	private final static String TAG = Getos.class.getSimpleName();

	public static EventBus mainEventBus;
	public static HashMap<String, GetosEvent> mainEvents = new HashMap<String, GetosEvent>();

	public static ArrayList<EditorController> tabControllers;
	public static TabPane tabPane;
	public static Label statusLabel;

	static {
		mainEventBus = new FXEventBus();
		tabControllers = new ArrayList<>();

		try {
			Class.forName("ro.kuberam.getos.modules.pdfEditor.Module").getClass();
		} catch (ClassNotFoundException ex) {
			Utils.showAlert(AlertType.ERROR, null, ex.getCause().getLocalizedMessage());
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		MainWindowController.create(this, stage);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
