package ro.kuberam.getos.controller.factory;

import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXML;

public class Controller {

	@FXML
	private ResourceBundle resources;

	private Application application;

	public Controller(Application application) {
		this.application = application;
	}

	public Application getApplication() {
		return application;
	}

	public ResourceBundle getResources() {
		return resources;
	}

}
