package ro.kuberam.getos.modules.pdfEditor;

import java.io.File;

import com.google.auto.service.AutoService;

import javafx.scene.control.Alert.AlertType;
import ro.kuberam.getos.DocumentModule;
import ro.kuberam.getos.Getos;
import ro.kuberam.getos.utils.Utils;

@AutoService(DocumentModule.class)
public class Module implements DocumentModule {

	static {
		Getos.eventsRegistry.put("PDF", new PdfEvent(PdfEvent.OPEN_PDF_FILE));
		Getos.eventsRegistry.put("pdf.metadata", new PdfEvent(PdfEvent.PDF_METADATA));
		Getos.eventsRegistry.put("pdf.zoom-in", new PdfEvent(PdfEvent.PDF_ZOOM_IN));
		Getos.eventsRegistry.put("pdf.zoom-out", new PdfEvent(PdfEvent.PDF_ZOOM_OUT));
		Getos.eventsRegistry.put("pdf.fit-to-width", new PdfEvent(PdfEvent.PDF_FIT_TO_WIDTH));
		Getos.eventsRegistry.put("pdf.fit-to-height", new PdfEvent(PdfEvent.PDF_FIT_TO_HEIGHT));
		Getos.eventsRegistry.put("pdf.fit-to-page", new PdfEvent(PdfEvent.PDF_FIT_TO_PAGE));
		Getos.eventsRegistry.put("pdf.enable-button", new PdfEvent(PdfEvent.PDF_ENABLE_BUTTON));
		Getos.eventsRegistry.put("pdf.disable-button", new PdfEvent(PdfEvent.PDF_DISABLE_BUTTON));
		Getos.eventsRegistry.put("pdf.update-page-count", new PdfEvent(PdfEvent.PDF_UPDATE_PAGE_COUNT));

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
