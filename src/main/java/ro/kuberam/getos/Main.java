package ro.kuberam.getos;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import ro.kuberam.getos.controllers.GetosController;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ro/kuberam/getos/app.fxml"));
		BorderPane pane = loader.load();
		Scene scene = new Scene(pane, 650, 400);
		primaryStage.setScene(scene);
		GetosController controller = ((GetosController) loader.getController());

		controller.setStage(primaryStage);
		
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
