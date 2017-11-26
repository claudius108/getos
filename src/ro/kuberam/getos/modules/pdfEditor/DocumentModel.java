package ro.kuberam.getos.modules.pdfEditor;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Optional;
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

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

			// set the general metadata
			generalMetadata = new LinkedHashMap<String, String>();
			generalMetadata.put("dc:title",
					Optional.ofNullable(documentInformation.getTitle()).filter(str -> !str.isEmpty()).orElse(""));
			generalMetadata.put("dc:creator",
					Optional.ofNullable(documentInformation.getAuthor()).filter(str -> !str.isEmpty()).orElse(""));
			generalMetadata.put("dc:subject",
					Optional.ofNullable(documentInformation.getKeywords()).filter(str -> !str.isEmpty()).orElse(""));
			generalMetadata.put("dc:description",
					Optional.ofNullable(documentInformation.getSubject()).filter(str -> !str.isEmpty()).orElse(""));
			generalMetadata.put("dc:publisher", "");
			generalMetadata.put("dc:contributor", "");
			generalMetadata.put("dc:date", Optional.ofNullable(documentInformation.getCreationDate())
					.map(date -> date.getTime()).map(dateTime -> dateFormat.format(dateTime)).orElse(""));
			generalMetadata.put("dc:type", "");
			generalMetadata.put("dc:format", Float.toString(document.getVersion()));
			generalMetadata.put("dc:identifier", "");
			generalMetadata.put("dc:source", "");
			generalMetadata.put("dc:language", "");
			generalMetadata.put("dc:relation", "");
			generalMetadata.put("dc:coverage", "");
			generalMetadata.put("dc:rights", "");
			generalMetadata.put("dcterms:modified", Optional.ofNullable(documentInformation.getModificationDate())
					.map(date -> date.getTime()).map(dateTime -> dateFormat.format(dateTime)).orElse(""));
			generalMetadata.put("dcterms:extent", Integer.toString(document.getNumberOfPages()));

			// set the specific metadata
			specificMetadata = new LinkedHashMap<String, String>();
			specificMetadata.put("pdf:creator",
					Optional.ofNullable(documentInformation.getCreator()).filter(str -> !str.isEmpty()).orElse(""));
			specificMetadata.put("pdf:encrypted", "");
			specificMetadata.put("pdf:producer",
					Optional.ofNullable(documentInformation.getProducer()).filter(str -> !str.isEmpty()).orElse(""));
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
}

// https://github.com/apache/tika/blob/master/tika-parsers/src/main/java/org/apache/tika/parser/pdf/PDFParser.java
// http://www.hascode.com/2012/12/content-detection-metadata-and-content-extraction-with-apache-tika/#Extracting_Metadata_from_a_PDF_using_a_concrete_Parser
