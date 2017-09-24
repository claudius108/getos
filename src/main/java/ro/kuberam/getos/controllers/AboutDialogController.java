package ro.kuberam.getos.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AboutDialogController implements Initializable {

	@FXML
	private DialogPane aboutDialog;

	@FXML
	private Hyperlink hyperlink;

	private Stage stage;
	private HostServices hostServices;

	public HostServices getHostServices() {
		return hostServices;
	}

	public void setHostServices(HostServices hostServices) {
		this.hostServices = hostServices;
	}

	@FXML
	private void openHomePageAction() {
		hostServices.showDocument(hyperlink.getText());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		aboutDialog.lookupButton(ButtonType.CLOSE).setOnMouseClicked((MouseEvent ev) -> stage.close());
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

}
