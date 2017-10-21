package ro.kuberam.getos;

import javafx.application.Application;
import javafx.stage.Stage;
import ro.kuberam.getos.modules.eventBus.EventBus;
import ro.kuberam.getos.modules.eventBus.FXEventBus;
import ro.kuberam.getos.modules.main.MainWindowController;

public class Getos extends Application {
	
	public static EventBus mainEventBus;

	static {
		mainEventBus = new FXEventBus();
	}

	@Override
	public void start(Stage stage) throws Exception {
		MainWindowController.create(this, stage);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
