package ro.kuberam.getos.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController implements Initializable {

	private Stage stage;
	private HostServices hostServices;

	@FXML
	void quitAction(ActionEvent event) {
		stage.close();
	}

	@FXML
	void menuHelpAbout(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ro/kuberam/getos/dialogs/about-dialog.fxml"));
		Parent dialogRoot = loader.load();
		
		AboutDialogController dialogController = loader.getController();
		dialogController.setHostServices(hostServices);
		Stage dialog = new Stage();
		dialog.setScene(new Scene(dialogRoot));
		dialog.initModality(Modality.APPLICATION_MODAL);
		
		dialog.show();
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
