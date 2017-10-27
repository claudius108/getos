package ro.kuberam.getos;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import ro.kuberam.getos.controller.factory.EditorController;
import ro.kuberam.getos.eventBus.EventBus;
import ro.kuberam.getos.eventBus.FXEventBus;
import ro.kuberam.getos.events.GetosEvent;
import ro.kuberam.getos.modules.main.MainWindowController;
import ro.kuberam.getos.utils.Utils;

public class Getos extends Application {

	public static EventBus eventBus;
	public static HashMap<String, GetosEvent> eventsRegistry = new HashMap<String, GetosEvent>();

	public static ArrayList<EditorController> tabControllers;

	static {
		eventBus = new FXEventBus();
		tabControllers = new ArrayList<>();

		try {
			Class.forName("ro.kuberam.getos.modules.pdfEditor.Module").getClass();
		} catch (ClassNotFoundException ex) {
			Utils.showAlert(AlertType.ERROR, ex);
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
