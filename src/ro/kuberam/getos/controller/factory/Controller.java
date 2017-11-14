package ro.kuberam.getos.controller.factory;

import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXML;

public class Controller {

	@FXML
	private ResourceBundle resources;

	private static Application application;

	public Controller(Application pApplication) {
		application = pApplication;
	}

	public static Application getApplication() {
		return application;
	}

	public ResourceBundle getResources() {
		return resources;
	}

}
