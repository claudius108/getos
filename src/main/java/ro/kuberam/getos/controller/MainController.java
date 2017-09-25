package ro.kuberam.getos.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

public class MainController implements Initializable {

	private Stage stage;
	private HostServices hostServices;

	@FXML
	void quitAction(ActionEvent event) {
		stage.close();
	}

	@FXML
	void menuHelpAbout(ActionEvent event) throws Exception {
		AboutDialogController.create(hostServices);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setHostServices(HostServices hostServices) {
		this.hostServices = hostServices;
	}
}
