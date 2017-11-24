package ro.kuberam.getos.modules.tableEditor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.SaxonApiUncheckedException;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmValue;
import ro.kuberam.getos.utils.Utils;

public class DocumentModel implements ro.kuberam.getos.DocumentModel {

	public static LinkedHashMap<String, String> generalMetadata;
	public static LinkedHashMap<String, String> specificMetadata;

	private Path path;

	public DocumentModel(Path path) {
		try {
			// set the general metadata
			generalMetadata = new LinkedHashMap<String, String>();
			Set<Entry<XdmAtomicValue, XdmValue>> metadataSet = Utils
					.transform(path.toFile(), getClass().getResourceAsStream("get-metadata.xql"), true, null, null)
					.itemAt(0).asMap().entrySet();
			for (Entry<XdmAtomicValue, XdmValue> metadata : metadataSet) {
				generalMetadata.put(metadata.getKey().getStringValue(), metadata.getValue().toString());
			}

			System.out.println("generalMetadata" + generalMetadata);

			this.path = path;
		} catch (IOException | IndexOutOfBoundsException | SaxonApiUncheckedException | SaxonApiException ex) {
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
		return null;
	}

	@Override
	public void shutdown() {
	}
}

// https://github.com/apache/tika/blob/master/tika-parsers/src/main/java/org/apache/tika/parser/pdf/PDFParser.java
// http://www.hascode.com/2012/12/content-detection-metadata-and-content-extraction-with-apache-tika/#Extracting_Metadata_from_a_PDF_using_a_concrete_Parser
