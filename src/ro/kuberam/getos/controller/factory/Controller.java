package ro.kuberam.getos.controller.factory;

import javafx.application.Application;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

	private static Application application;
	private ResourceBundle resources;

	public Controller(Application pApplication) {
		application = pApplication;
	}

	@Override
	public void initialize(URL location, ResourceBundle pResources) {
		resources = pResources;
	}

	public static Application getApplication() {
		return application;
	}

	public ResourceBundle getResources() {
		return resources;
	}

}
