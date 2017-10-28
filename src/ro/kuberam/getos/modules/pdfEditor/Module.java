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

//Create PDF documents
//Assemble documents (split, merge, combine, remove pages)
//Encrypt documents using RC4 or AES encryption, set passwords and permissions
//Apply and verify digital signatures
//Import, export and fill interactive form data
//Convert documents to TIFF, JPEG, PNG images
//Extract text content
//Print PDF documents
//Convert PDFs to images
//Permanently Redact PDFs
//Optional OCR module
//Add file attachments, header & footers, watermarks, bookmarks
//Edit document properties such as title, keywords, subject
//Linearize PDF documents for fast web viewing
//Create PDF layers and draw onto them