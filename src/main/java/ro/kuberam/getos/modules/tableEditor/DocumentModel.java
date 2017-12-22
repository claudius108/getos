package ro.kuberam.getos.modules.tableEditor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import ro.kuberam.getos.utils.Utils;

public class DocumentModel implements ro.kuberam.getos.DocumentModel {

	public static LinkedHashMap<String, String> generalMetadata;
	public static LinkedHashMap<String, String> specificMetadata;

	private Path path;
	private Document document;

	public DocumentModel(Path path) {
		try {
			// set the general metadata
			generalMetadata = new LinkedHashMap<String, String>();

			SAXBuilder documentBuilder = new SAXBuilder();
			document = documentBuilder.build(path.toFile());

			XPathExpression<Element> xpath = XPathFactory.instance().compile("/html/head/meta[@name]",
					Filters.element());
			List<Element> metaElements = xpath.evaluate(document);
			for (Element metaElement : metaElements) {
				generalMetadata.put(metaElement.getAttributeValue("name"), metaElement.getAttributeValue("content"));
			}

			// StringWriter writer = new StringWriter();
			// XMLOutputter outputter = new XMLOutputter();
			// outputter.setFormat(Format.getPrettyFormat());
			// outputter.output(document, writer);
			// outputter.output(document, System.out);
			// writer.close(); // close writer

			this.path = path;
		} catch (IOException | IndexOutOfBoundsException | JDOMException ex) {
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
		return "/ro/kuberam/getos/modules/tableEditor/TableEditor.fxml";
	}

	@Override
	public String bundle() {
		return "ro.kuberam.getos.modules.tableEditor.ui";
	}

	@Override
	public Image goToPage(int pageNumber) {
		return null;
	}

	@Override
	public void shutdown() {
	}

	@Override
	public Object extractTablesFromPage(int pageNumber) {
		// TODO Auto-generated method stub
		return null;
	}
}

// https://github.com/apache/tika/blob/master/tika-parsers/src/main/java/org/apache/tika/parser/pdf/PDFParser.java
// http://www.hascode.com/2012/12/content-detection-metadata-and-content-extraction-with-apache-tika/#Extracting_Metadata_from_a_PDF_using_a_concrete_Parser
