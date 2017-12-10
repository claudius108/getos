package ro.kuberam.getos;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.ServiceLoader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ro.kuberam.getos.controller.factory.ControllerFactory;
import ro.kuberam.getos.controller.factory.EditorController;
import ro.kuberam.getos.events.EventBus;
import ro.kuberam.getos.events.FXEventBus;
import ro.kuberam.getos.modules.main.MainWindowController;
import ro.kuberam.getos.utils.Utils;

public class Getos extends Application {

	public static EventBus eventBus;
	public static ArrayList<EditorController> tabControllers;
	public static HashMap<String, Constructor<?>> documentModelsRegistry = new HashMap<String, Constructor<?>>();
	private static final String FONT_AWESOME = "/ro/kuberam/getos/fonts/fontawesome.ttf";

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
//		Font.loadFont(getClass().getResource(FONT_AWESOME).toExternalForm(), 10);
//        InputStream fontStream = Getos.class.getResourceAsStream("/ro/kuberam/getos/fonts/fontawesome-webfont.ttf");
//        Font.loadFont(fontStream, 10);
//        fontStream.close();
//        
//        System.out.println("fonts url: " + getClass().getResource(FONT_AWESOME).toExternalForm());
//        System.out.println("fonts: " + Font.getFontNames("FontAwesome"));
//        System.out.println("fontstream: " + (fontStream != null));
		
		Font font = null;
      try {
        InputStream fontStream = Getos.class.getResourceAsStream("fontawesome.ttf");
//          InputStream fontStream = new FileInputStream(new File("/home/claudius/fontawesome.ttf"));
          font = Font.loadFont(fontStream, 22);
          
			fontStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
      
      System.out.println("font: " + font);
      System.out.println("fonts: " + Font.getFontNames("FontAwesome Regular"));
		
		try {
			FXMLLoader.load(MainWindowController.class.getResource("/ro/kuberam/getos/modules/main/MainWindow.fxml"),
					ResourceBundle.getBundle("ro.kuberam.getos.modules.main.ui"), null,
					new ControllerFactory(this, stage));
		} catch (Exception ex) {
			Utils.showAlert(AlertType.ERROR, ex);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}

// IMAGE document type
// Java library for reading, writing, converting and manipulating images and
// metadata https://github.com/dragon66/icafe
// Highlight differences between images,
// https://stackoverflow.com/questions/25022578/highlight-differences-between-images

// PDF document type
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

// Document type conversions
// HTML to XHTML
// HTML to DITA
// Markdown to XHTML
// Markdown to DITA
// XML to JSON
// JSON to XML
