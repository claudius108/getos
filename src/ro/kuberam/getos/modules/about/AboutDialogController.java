package ro.kuberam.getos.modules.about;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.kuberam.getos.controller.factory.ControllerFactory;
import ro.kuberam.getos.controller.factory.StageController;

public final class AboutDialogController extends StageController {

	@FXML
	private DialogPane root;

	@FXML
	private Hyperlink hyperlink;

	public AboutDialogController(Application application, Stage stage) {
		super(application, stage);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		root.lookupButton(ButtonType.CLOSE).setOnMouseClicked((MouseEvent ev) -> getStage().close());

		hyperlink.setOnAction(event -> {
			getApplication().getHostServices().showDocument(resources.getString("homelink"));
			event.consume();
		});

		Stage stage = getStage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle(resources.getString("about_dialog_title"));
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.centerOnScreen();

		stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			getStage().hide();
			event.consume();
		});

		stage.show();
	}

	public static void create(Application application, Stage parent) throws Exception {
		Stage stage = new Stage();
		stage.initOwner(parent);
		
		FXMLLoader.load(AboutDialogController.class.getResource("/ro/kuberam/getos/modules/about/about-dialog.fxml"),
				ResourceBundle.getBundle("ro.kuberam.getos.ui"), null, new ControllerFactory(application, stage));
	}

}
