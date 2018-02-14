package ro.kuberam.getos.modules.pdfEditor;

import ro.kuberam.getos.DocumentModule;
import ro.kuberam.getos.App;

public class Module implements DocumentModule {
	
	private String documentType = "application/pdf";

	@Override
	public String getDocumentType() {
		return documentType;
	}

	static {
		App.eventBus.registerEvent("pdf.zoom-in", new PdfEvent(PdfEvent.PDF_ZOOM_IN));
		App.eventBus.registerEvent("pdf.zoom-out", new PdfEvent(PdfEvent.PDF_ZOOM_OUT));
		App.eventBus.registerEvent("pdf.fit-to-width", new PdfEvent(PdfEvent.PDF_FIT_TO_WIDTH));
		App.eventBus.registerEvent("pdf.fit-to-height", new PdfEvent(PdfEvent.PDF_FIT_TO_HEIGHT));
		App.eventBus.registerEvent("pdf.fit-to-page", new PdfEvent(PdfEvent.PDF_FIT_TO_PAGE));
	}
}
