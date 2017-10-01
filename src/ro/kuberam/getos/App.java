package ro.kuberam.getos;

import javafx.application.Application;
import javafx.stage.Stage;
import ro.kuberam.getos.modules.main.MainWindowController;

public class App extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		MainWindowController.create(this, stage);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
