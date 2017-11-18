package ro.kuberam.getos;

import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ServiceLoader;

import javafx.application.Application;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import ro.kuberam.getos.controller.factory.EditorController;
import ro.kuberam.getos.events.EventBus;
import ro.kuberam.getos.events.FXEventBus;
import ro.kuberam.getos.modules.main.MainWindowController;
import ro.kuberam.getos.utils.Utils;

public class Getos extends Application {

	public static EventBus eventBus;
	public static ArrayList<EditorController> tabControllers;
	public static HashMap<String, Constructor<?>> documentModelsRegistry = new HashMap<String, Constructor<?>>();

	static {
		eventBus = new FXEventBus();
		tabControllers = new ArrayList<>();

		ServiceLoader<DocumentModule> moduleDescriptionServices = java.util.ServiceLoader.load(DocumentModule.class);

		moduleDescriptionServices.forEach(service -> {
			try {
				DocumentModule moduleDescription = service.getClass().newInstance();
				String documentType = moduleDescription.getDocumentType();
				String modulePackageName = service.getClass().getPackage().getName();

				documentModelsRegistry.put(documentType,
						Class.forName(modulePackageName + ".DocumentModel").getConstructor(Path.class));
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException
					| SecurityException ex) {
				Utils.showAlert(AlertType.ERROR, ex);
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

// Images
// Java library for reading, writing, converting and manipulating images and
// metadata https://github.com/dragon66/icafe
// Highlight differences between images, https://stackoverflow.com/questions/25022578/highlight-differences-between-images

// PDF
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

// Zoomable and Pannable JavaFX ImageView Example
// https://gist.github.com/james-d/ce5ec1fd44ce6c64e81a