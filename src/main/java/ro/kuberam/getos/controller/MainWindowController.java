package ro.kuberam.getos.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import ro.kuberam.getos.controller.factory.ControllerFactory;
import ro.kuberam.getos.controller.factory.StageController;
import ro.kuberam.getos.modules.about.AboutDialogController;

public class MainWindowController extends StageController {

	private Stage stage;
	private HostServices hostServices;

	@FXML
	void quitAction(ActionEvent event) {
		stage.close();
	}

	@FXML
	void menuHelpAbout(ActionEvent event) throws Exception {
		AboutDialogController.create(stage, hostServices);
	}

	public MainWindowController(Application application, Stage stage) {
		super(application, stage);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
	}

	public static void create(Application application, Stage stage) throws Exception {
		FXMLLoader.load(MainWindowController.class.getResource("/ro/kuberam/getos/app.fxml"), null, null,
				new ControllerFactory(application, stage));
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setHostServices(HostServices hostServices) {
		this.hostServices = hostServices;
	}
}
