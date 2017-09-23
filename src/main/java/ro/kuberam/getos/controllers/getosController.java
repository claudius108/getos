package ro.kuberam.getos.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class getosController implements Initializable {

	@FXML // fx:id="quitButton"
	private MenuItem quitButton; // Value injected by FXMLLoader

	private Stage stage;

	@FXML
	void quitAction(ActionEvent event) {
		stage.close();
	}

	@FXML // This method is called by the FXMLLoader when initialization is
			// complete
	void initialize() {
		assert quitButton != null : "fx:id=\"quitButton\" was not injected: check your FXML file 'app.fxml'.";

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
