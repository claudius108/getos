package ro.kuberam.getos.modules.pdfEditor;

import com.google.auto.service.AutoService;

import ro.kuberam.getos.DocumentModule;
import ro.kuberam.getos.Getos;

@AutoService(DocumentModule.class)
public class Module implements DocumentModule {
	
	private String documentType = "PDF";

	@Override
	public String getDocumentType() {
		return documentType;
	}

	static {
		Getos.eventBus.registerEvent("pdf.zoom-in", new PdfEvent(PdfEvent.PDF_ZOOM_IN));
		Getos.eventBus.registerEvent("pdf.zoom-out", new PdfEvent(PdfEvent.PDF_ZOOM_OUT));
		Getos.eventBus.registerEvent("pdf.fit-to-width", new PdfEvent(PdfEvent.PDF_FIT_TO_WIDTH));
		Getos.eventBus.registerEvent("pdf.fit-to-height", new PdfEvent(PdfEvent.PDF_FIT_TO_HEIGHT));
		Getos.eventBus.registerEvent("pdf.fit-to-page", new PdfEvent(PdfEvent.PDF_FIT_TO_PAGE));
	}
}
