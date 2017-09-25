package ro.kuberam.getos;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import ro.kuberam.getos.controller.MainController;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class App extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ro/kuberam/getos/app.fxml"));
		BorderPane pane = loader.load();
		Scene scene = new Scene(pane, 650, 400);
		primaryStage.setScene(scene);
		MainController controller = (MainController) loader.getController();

		controller.setHostServices(getHostServices());
		controller.setStage(primaryStage);

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
