package ro.kuberam.getos.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import ro.kuberam.getos.utils.Utils;

public final class MainWindowController extends StageController {

	private final static String TAG = MainWindowController.class.getSimpleName();

	@FXML
	private BorderPane mRoot;

	@FXML
	void quitAction(ActionEvent event) {
		getStage().close();
	}

	@FXML
	void menuHelpAbout(ActionEvent event) throws Exception {
		AboutDialogController.create(getApplication(), getStage());
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

		stage.show();
	}

	public static void create(Application application, Stage stage) throws Exception {
		try {
			FXMLLoader.load(MainWindowController.class.getResource("/ro/kuberam/getos/app.fxml"),
					ResourceBundle.getBundle("ro.kuberam.getos.ui"), null, new ControllerFactory(application, stage));
		} catch (Exception ex) {
			Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
			if (ex.getCause() != null) {
				Utils.showErrorDialog(null, ex.getCause().getLocalizedMessage());
			} else {
				Utils.showErrorDialog(null, ex.getLocalizedMessage());
			}
		}
	}

}
