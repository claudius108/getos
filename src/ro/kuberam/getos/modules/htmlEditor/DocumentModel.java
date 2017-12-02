package ro.kuberam.getos.modules.htmlEditor;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.rendering.PDFRenderer;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import ro.kuberam.getos.utils.Utils;

public class DocumentModel implements ro.kuberam.getos.DocumentModel {

	private ThreadLocal<PDDocument> localPdDocument = new ThreadLocal<PDDocument>() {
		@Override
		protected PDDocument initialValue() {
			return new PDDocument();
		}
	};

	private PDDocument document = localPdDocument.get();
	private PDFRenderer renderer;

	public static LinkedHashMap<String, String> generalMetadata;
	public static LinkedHashMap<String, String> specificMetadata;

	private Path path;

	public DocumentModel(Path path) {
		try {
			document = PDDocument.load(path.toFile(), MemoryUsageSetting.setupTempFileOnly());
			renderer = new PDFRenderer(document);

			PDDocumentInformation documentInformation = document.getDocumentInformation();

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");

			// set the general metadata
			generalMetadata = new LinkedHashMap<String, String>();
			generalMetadata.put("dc:title", documentInformation.getTitle());
			generalMetadata.put("dc:creator", documentInformation.getAuthor());
			generalMetadata.put("dc:subject", documentInformation.getKeywords());
			generalMetadata.put("dc:description", documentInformation.getSubject());
			generalMetadata.put("dc:publisher", "");
			generalMetadata.put("dc:contributor", "");
			generalMetadata.put("dc:date", dateFormat.format(documentInformation.getCreationDate().getTime()));
			generalMetadata.put("dc:type", "");
			generalMetadata.put("dc:format", Float.toString(document.getVersion()));
			generalMetadata.put("dc:identifier", "");
			generalMetadata.put("dc:source", "");
			generalMetadata.put("dc:language", "");
			generalMetadata.put("dc:relation", "");
			generalMetadata.put("dc:coverage", "");
			generalMetadata.put("dc:rights", "");
			generalMetadata.put("dcterms:modified",
					dateFormat.format(documentInformation.getModificationDate().getTime()));
			generalMetadata.put("dcterms:extent", Integer.toString(document.getNumberOfPages()));

			// set the specific metadata
			specificMetadata.put("pdf:creator", documentInformation.getCreator());
			specificMetadata.put("pdf:encrypted", "");
			specificMetadata.put("pdf:producer", documentInformation.getProducer());
			specificMetadata.put("pdf:optimized", "");
			specificMetadata.put("pdf:paperSize", "");
			specificMetadata.put("pdf:fonts", "");

			this.path = path;
		} catch (IOException ex) {
			Utils.showAlert(AlertType.ERROR, ex);
		}
	}

	@Override
	public LinkedHashMap<String, String> generalMetadata() {
		return generalMetadata;
	}

	@Override
	public LinkedHashMap<String, String> specificMetadata() {
		return specificMetadata;
	}

	@Override
	public Path path() {
		return path;
	}

	@Override
	public String fxml() {
		return "/ro/kuberam/getos/modules/pdfEditor/PdfEditor.fxml";
	}

	@Override
	public String bundle() {
		return "ro.kuberam.getos.modules.pdfEditor.ui";
	}

	@Override
	public Image goToPage(int pageNumber) {
		BufferedImage pageImage = null;
		try {
			pageImage = renderer.renderImage(pageNumber);

		} catch (IOException ex) {
			Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
		}

		return SwingFXUtils.toFXImage(pageImage, null);
	}

	@Override
	public void shutdown() {
		localPdDocument.remove();
	}

	@Override
	public Object extractTablesFromPage(int pageNumber) {
		// TODO Auto-generated method stub
		return null;
	}
}

// https://github.com/apache/tika/blob/master/tika-parsers/src/main/java/org/apache/tika/parser/pdf/PDFParser.java
