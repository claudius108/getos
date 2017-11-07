package ro.kuberam.getos;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ServiceLoader;

import javafx.application.Application;
import javafx.stage.Stage;
import ro.kuberam.getos.controller.factory.EditorController;
import ro.kuberam.getos.eventBus.EventBus;
import ro.kuberam.getos.eventBus.FXEventBus;
import ro.kuberam.getos.events.GetosEvent;
import ro.kuberam.getos.modules.main.MainWindowController;

public class Getos extends Application {

	public static EventBus eventBus;
	public static HashMap<String, GetosEvent> eventsRegistry = new HashMap<String, GetosEvent>();
	public static ArrayList<EditorController> tabControllers;
	public static HashMap<String, Constructor<?>> documentMetadataGeneratorsRegistry = new HashMap<String, Constructor<?>>();

	static {
		eventBus = new FXEventBus();
		tabControllers = new ArrayList<>();

		ServiceLoader<DocumentModule> moduleDescriptionServices = java.util.ServiceLoader.load(DocumentModule.class);
		
		moduleDescriptionServices.forEach(service -> {
			try {
				DocumentModule moduleDescription = service.getClass().newInstance();
				String documentType = moduleDescription.getDocumentType();
				String modulePackageName = service.getClass().getPackage().getName();
				
				documentMetadataGeneratorsRegistry.put(documentType, Class.forName(modulePackageName + ".DocumentMetadata").getConstructors()[0]);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		});
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

// Create PDF documents
// Assemble documents (split, merge, combine, remove pages)
// Encrypt documents using RC4 or AES encryption, set passwords and permissions
// Apply and verify digital signatures
// Import, export and fill interactive form data
// Convert documents to TIFF, JPEG, PNG images
// Extract text content
// Print PDF documents
// Convert PDFs to images
// Permanently Redact PDFs
// Optional OCR module
// Add file attachments, header & footers, watermarks, bookmarks
// Edit document properties such as title, keywords, subject
// Linearize PDF documents for fast web viewing
// Create PDF layers and draw onto them

// I have to process PDF files, that (supposedly) contain one big image
// per page, which is a result from a Document-Scanner. I'd like to avoid
// performing PDF-To-Image in these cases, and use the underlying image
// instead.
// I am not well-versed in all things PDF and have no idea how to
// detect if a page has content other than a single image.
// Please advise.
//
//
// Please have a look at the ExtractImages.java source code. You can change that
// one to your needs.