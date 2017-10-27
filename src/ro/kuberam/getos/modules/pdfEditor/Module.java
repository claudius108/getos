package ro.kuberam.getos.modules.pdfEditor;

import java.io.File;

import javafx.scene.control.Alert.AlertType;
import ro.kuberam.getos.Getos;
import ro.kuberam.getos.utils.Utils;

public class Module {

	static {
		Getos.eventsRegistry.put("PDF", new PdfEvent(PdfEvent.OPEN_PDF_FILE));

		Getos.eventBus.addEventHandler(PdfEvent.OPEN_PDF_FILE, event -> {
			try {
				Getos.eventBus.fireEvent(Getos.eventsRegistry.get("open-file")
						.setData(PdfEditorController.create((File) event.getData())));
			} catch (Exception ex) {
				Utils.showAlert(AlertType.ERROR, ex);
			}

			event.consume();
		});
	}

}
