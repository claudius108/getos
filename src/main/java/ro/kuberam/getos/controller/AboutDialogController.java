package ro.kuberam.getos.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class AboutDialogController implements Initializable {

	@FXML
	private DialogPane dialog;

	@FXML
	private Hyperlink hyperlink;

	private static HostServices hostServices;
	private static Stage stage;

	@FXML
	private void openHomePageAction() {
		getHostServices().showDocument(hyperlink.getText());
	}

	public static void create(Stage parent, HostServices hostServices) throws Exception {
		setHostServices(hostServices);

		Stage dialog = new Stage();
		setStage(dialog);
		dialog.initOwner(parent);

		FXMLLoader loader = new FXMLLoader(
				AboutDialogController.class.getResource("/ro/kuberam/getos/dialogs/about-dialog.fxml"));
		Parent dialogRoot = loader.load();

		dialog.setScene(new Scene(dialogRoot));
		dialog.initModality(Modality.APPLICATION_MODAL);

		dialog.show();
	}

	public static HostServices getHostServices() {
		return hostServices;
	}

	public static void setHostServices(HostServices hostServices) {
		AboutDialogController.hostServices = hostServices;
	}

	public static void setStage(Stage stage) {
		AboutDialogController.stage = stage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dialog.lookupButton(ButtonType.CLOSE).setOnMouseClicked((MouseEvent ev) -> stage.close());
	}

}
