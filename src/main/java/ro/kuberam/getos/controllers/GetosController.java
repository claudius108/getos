package ro.kuberam.getos.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

public class GetosController implements Initializable {

	private Stage stage;

	@FXML
	void quitAction(ActionEvent event) {
		stage.close();
	}

	@FXML
	void aboutAction(ActionEvent event) {
		AboutDialog aboutDialog = new AboutDialog(stage);
		aboutDialog.showAbout();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
