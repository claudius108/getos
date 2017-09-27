package ro.kuberam.getos.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ro.kuberam.getos.controller.factory.ControllerFactory;
import ro.kuberam.getos.controller.factory.StageController;
import ro.kuberam.getos.modules.about.AboutDialogController;

public final class MainWindowController extends StageController {

	@FXML private BorderPane mRoot;
	
	@FXML
	void quitAction(ActionEvent event) {
		getStage().close();
	}

	@FXML
	void menuHelpAbout(ActionEvent event) throws Exception {
		AboutDialogController.create(getStage());
	}

	public MainWindowController(Application application, Stage stage) {
		super(application, stage);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		Stage stage = getStage();
		stage.setTitle(resources.getString("appname"));
		stage.setScene(new Scene(mRoot));
		stage.centerOnScreen();
		stage.setResizable(true);
//		stage.setOnCloseRequest(event -> {
//			onStageClose();
//			event.consume();
//		});
		stage.show();
	}

	public static void create(Application application, Stage stage) throws Exception {
		FXMLLoader.load(MainWindowController.class.getResource("/ro/kuberam/getos/app.fxml"), ResourceBundle.getBundle("ro.kuberam.getos.ui"), null,
				new ControllerFactory(application, stage));
	}

}
