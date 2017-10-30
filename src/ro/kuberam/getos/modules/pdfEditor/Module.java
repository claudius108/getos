package ro.kuberam.getos.modules.pdfEditor;

import java.io.File;

import javafx.scene.control.Alert.AlertType;
import ro.kuberam.getos.Getos;
import ro.kuberam.getos.utils.Utils;

public class Module {

	static {
		Getos.eventsRegistry.put("PDF", new PdfEvent(PdfEvent.OPEN_PDF_FILE));
		Getos.eventsRegistry.put("pdf.back", new PdfEvent(PdfEvent.PDF_BACK));
		Getos.eventsRegistry.put("pdf.forward", new PdfEvent(PdfEvent.PDF_FORWARD));
		Getos.eventsRegistry.put("pdf.zoom-in", new PdfEvent(PdfEvent.PDF_ZOOM_IN));
		Getos.eventsRegistry.put("pdf.zoom-out", new PdfEvent(PdfEvent.PDF_ZOOM_OUT));
		Getos.eventsRegistry.put("pdf.fit-to-width", new PdfEvent(PdfEvent.PDF_FIT_TO_WIDTH));
		Getos.eventsRegistry.put("pdf.fit-to-height", new PdfEvent(PdfEvent.PDF_FIT_TO_HEIGHT));
		Getos.eventsRegistry.put("pdf.fit-to-page", new PdfEvent(PdfEvent.PDF_FIT_TO_PAGE));
		Getos.eventsRegistry.put("pdf.enable-button", new PdfEvent(PdfEvent.PDF_ENABLE_BUTTON));
		Getos.eventsRegistry.put("pdf.disable-button", new PdfEvent(PdfEvent.PDF_DISABLE_BUTTON));

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
