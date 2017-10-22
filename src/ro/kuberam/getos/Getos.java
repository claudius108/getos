package ro.kuberam.getos;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import ro.kuberam.getos.controller.factory.EditorController;
import ro.kuberam.getos.modules.eventBus.EventBus;
import ro.kuberam.getos.modules.eventBus.FXEventBus;
import ro.kuberam.getos.modules.eventBus.GetosEvent;
import ro.kuberam.getos.modules.main.MainWindowController;

public class Getos extends Application {

	public static EventBus mainEventBus;
	public static HashMap<String, GetosEvent> mainEvents = new  HashMap<String, GetosEvent>();
	
	public static ArrayList<EditorController> tabControllers;
	public static TabPane tabPane;
	public static Label statusLabel;

	static {
		mainEventBus = new FXEventBus();
		tabControllers = new ArrayList<>();
	}

	@Override
	public void start(Stage stage) throws Exception {
		MainWindowController.create(this, stage);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
