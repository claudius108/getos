package ro.kuberam.getos.controller;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;

public final class AboutDialogController {

	@FXML
	private DialogPane aboutDialog;

	@FXML
	private Hyperlink hyperlink;

	private HostServices hostServices;

	public static AboutDialogController create() {
		return new AboutDialogController();
	}

	public HostServices getHostServices() {
		return hostServices;
	}

	public void setHostServices(HostServices hostServices) {
		this.hostServices = hostServices;
	}

	@FXML
	private void openHomePageAction() {
		getHostServices().showDocument(hyperlink.getText());
	}

}
