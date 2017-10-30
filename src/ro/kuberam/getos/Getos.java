package ro.kuberam.getos;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import ro.kuberam.getos.controller.factory.EditorController;
import ro.kuberam.getos.eventBus.EventBus;
import ro.kuberam.getos.eventBus.FXEventBus;
import ro.kuberam.getos.events.GetosEvent;
import ro.kuberam.getos.modules.main.MainWindowController;
import ro.kuberam.getos.utils.Utils;

public class Getos extends Application {

	public static EventBus eventBus;
	public static HashMap<String, GetosEvent> eventsRegistry = new HashMap<String, GetosEvent>();

	public static ArrayList<EditorController> tabControllers;

	static {
		eventBus = new FXEventBus();
		tabControllers = new ArrayList<>();

		try {
			Class.forName("ro.kuberam.getos.modules.pdfEditor.Module").getClass();
		} catch (ClassNotFoundException ex) {
			Utils.showAlert(AlertType.ERROR, ex);
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		MainWindowController.create(this, stage);
	}

	public static void main(String[] args) {
		launch(args);
	}

}

// Java library for reading, writing, converting and manipulating images and
// metadata https://github.com/dragon66/icafe

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

//I have to process PDF files, that (supposedly) contain one big image
//per page, which is a result from a Document-Scanner. I'd like to avoid
//performing PDF-To-Image in these cases, and use the underlying image
//instead.
// I am not well-versed in all things PDF and have no idea how to
//detect if a page has content other than a single image.
// Please advise.
//
//
//Please have a look at the ExtractImages.java source code. You can change that one to your needs.