package ro.kuberam.getos.modules.pdfEditor;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.Alert.AlertType;
import ro.kuberam.getos.Getos;
import ro.kuberam.getos.utils.Utils;

public class Module {
	
	private final static String TAG = Module.class.getSimpleName();
	
	static {
		Getos.mainEvents.put("PDF", new OpenPdfEvent(OpenPdfEvent.OPEN_FILE));
		
		Getos.mainEventBus.addEventHandler(OpenPdfEvent.OPEN_FILE, event -> {
			Logger.getLogger(TAG).log(Level.INFO, "target = " + event.getTarget());
			Logger.getLogger(TAG).log(Level.INFO, "getData = " + event.getData());
			
			try {
				PdfEditorController.create((File) event.getData());
			} catch (Exception ex) {
				if (ex.getCause() != null) {
					Utils.showAlert(AlertType.ERROR, null, ex.getCause().getLocalizedMessage());
				} else {
					Utils.showAlert(AlertType.ERROR, null, ex.getLocalizedMessage());
				}
			}
		});		
	}

}
