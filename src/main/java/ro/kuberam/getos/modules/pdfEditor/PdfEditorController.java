package ro.kuberam.getos.modules.pdfEditor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Optional;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import ro.kuberam.getos.DocumentModel;
import ro.kuberam.getos.controller.factory.RendererController;
import ro.kuberam.getos.modules.editorTab.EditorEvent;
import ro.kuberam.getos.modules.editorTab.EditorModel;
import ro.kuberam.getos.utils.Utils;

public final class PdfEditorController extends RendererController {

	@FXML
	private ComboBox<String> selectEditorCombobox;

	@FXML
	private Button extractMetadataButton;
	
	@FXML
	private Button extractTablesButton;

	@FXML
	private ScrollPane centerSourcePane;

	@FXML
	private ImageView contentSourcePane;

	private XMLStreamWriter xmlWriter;

	public PdfEditorController(Application application, Stage stage, DocumentModel documentModel,
			EditorModel editorModel) {
		super(application, stage, documentModel, editorModel);
	}

	@FXML
	public void initialize() {

		editorModel.eventBus.addEventHandler(EditorEvent.GO_TO_PAGE, event -> {
			contentSourcePane.setImage(getSourceDocumentModel().goToPage((int) ((Object[]) event.getData())[0]));

			event.consume();
		});

		extractTablesButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent t) {
				Path sourceDocumentPath = getSourceDocumentModel().path();
				Path targetDocumentPath = sourceDocumentPath.getParent()
						.resolve(sourceDocumentPath.getFileName().toString().replaceFirst(".pdf", ".html"));

				if (editorModel.openedDocuments.contains(targetDocumentPath)) {

				} else {
					if (!Files.exists(targetDocumentPath)) {
						generateHtmlFile(targetDocumentPath, getSourceDocumentModel());
					} else {
						getSourceDocumentModel().extractTablesFromPage(7);
					}

					editorModel.eventBus.fireEvent("open-target-document", targetDocumentPath);
				}
			}
		});

		// detect PDF version and select the viewer accordingly

		selectEditorCombobox.setValue("jpedal");

		// initialize the PDF viewer
		// setDocumentRenderer(new JpedalRenderer(centerSourcePane, contentSourcePane,
		// getDocumentModel().file()));

		editorModel.eventBus.fireEvent("document-opened", getSourceDocumentModel().path());
	}

	private void generateHtmlFile(Path targetDocumentPath, DocumentModel documentModel) {
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

		try {
			BufferedWriter writer = Files.newBufferedWriter(targetDocumentPath, Charset.forName("UTF-8"));

			xmlWriter = outputFactory.createXMLStreamWriter(writer);

			xmlWriter.writeDTD("<!DOCTYPE html>");
			xmlWriter.writeStartElement("html");
			xmlWriter.writeStartElement("head");
			xmlWriter.writeStartElement("title");
			xmlWriter.writeCharacters(documentModel.generalMetadata().get("dc:title"));
			xmlWriter.writeEndElement();
			xmlWriter.writeStartElement("meta");
			xmlWriter.writeAttribute("charset", "utf-8");
			xmlWriter.writeEndElement();
			writeGeneralMetadata(documentModel.generalMetadata());
			xmlWriter.writeEndElement();
			xmlWriter.writeStartElement("body");
			writePageSections(Integer.parseInt(documentModel.generalMetadata().get("dcterms:extent")));
			xmlWriter.writeEndElement();
			xmlWriter.writeEndElement();
			xmlWriter.flush();
			xmlWriter.close();
			writer.close();

		} catch (XMLStreamException | IOException ex) {
			Utils.showAlert(AlertType.ERROR, ex);
		}

	}

	private void writeGeneralMetadata(LinkedHashMap<String, String> generalMetadata) {
		try {
			for (Entry<String, String> metadata : generalMetadata.entrySet()) {
				xmlWriter.writeEmptyElement("meta");
				xmlWriter.writeAttribute("name", metadata.getKey());
				xmlWriter.writeAttribute("content",
						Optional.ofNullable(metadata.getValue()).filter(str -> !str.isEmpty()).orElse(""));
			}
		} catch (XMLStreamException ex) {
			Utils.showAlert(AlertType.ERROR, ex);
		}
	}

	private void writePageSections(int numberOfPages) {
		try {
			for (int i = 0; i < numberOfPages; i++) {
				xmlWriter.writeEmptyElement("div");
				xmlWriter.writeAttribute("data-page-number", Integer.toString(i + 1));
				xmlWriter.writeAttribute("style", "page-break-after: always;");
			}
		} catch (XMLStreamException ex) {
			Utils.showAlert(AlertType.ERROR, ex);
		}
	}
}