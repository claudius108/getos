package ro.kuberam.getos.modules.about;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.kuberam.getos.controller.factory.StageController;

public final class AboutDialogController extends StageController {

	@FXML
	private DialogPane dialog;

	@FXML
	private Hyperlink hyperlink;

	private static HostServices hostServices;
	private static Application application;
	private static Stage stage;

	public AboutDialogController(Application application, Stage stage) {
		super(application, stage);
	}

	public AboutDialogController() {
		this(application, stage);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		dialog.lookupButton(ButtonType.CLOSE).setOnMouseClicked((MouseEvent ev) -> stage.close());

		hyperlink.setOnAction(event -> {
			getApplication().getHostServices().showDocument(hyperlink.getText());
			event.consume();
		});
	}

	public static void create(Stage parent, HostServices hostServices) throws Exception {
		setHostServices(hostServices);

		Stage dialog = new Stage();
		setStage(dialog);
		dialog.initOwner(parent);

		FXMLLoader loader = new FXMLLoader(
				AboutDialogController.class.getResource("/ro/kuberam/getos/modules/about/about-dialog.fxml"));
		Parent dialogRoot = loader.load();

		dialog.setScene(new Scene(dialogRoot));
		dialog.initModality(Modality.APPLICATION_MODAL);

		dialog.show();
	}

	public static void setHostServices(HostServices hostServices) {
		AboutDialogController.hostServices = hostServices;
	}

	public static void setStage(Stage stage) {
		AboutDialogController.stage = stage;
	}

}
