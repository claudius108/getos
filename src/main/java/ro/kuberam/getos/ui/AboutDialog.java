package ro.kuberam.getos.ui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import ro.kuberam.getos.utils.PropertiesUtils;

public class AboutDialog {

	private final Alert alert = new Alert(Alert.AlertType.INFORMATION);

	public AboutDialog() {
		alert.setHeaderText(PropertiesUtils.getAppDetails().get("version"));
		alert.setTitle("About " + PropertiesUtils.getAppDetails().get("name"));
		alert.getDialogPane().setContent(createContent());
		alert.getDialogPane().setPrefSize(400, 100);
	}

	public void showAbout() {
		alert.show();
	}

	private Node createContent() {
		Hyperlink link = new Hyperlink();
		link.setText("Click here to visit us");
		link.setOnAction(this::openHomepage);
		return link;
	}

	private void openHomepage(ActionEvent event) {
		try {
			Desktop.getDesktop().browse(new URI(PropertiesUtils.getAppDetails().get("link")));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
}