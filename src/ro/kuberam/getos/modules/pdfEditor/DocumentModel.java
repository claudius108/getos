package ro.kuberam.getos.modules.pdfEditor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.rendering.PDFRenderer;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import ro.kuberam.getos.utils.Utils;

public class DocumentModel implements ro.kuberam.getos.DocumentModel {

	private PDDocument document;
	private PDFRenderer renderer;
	private String title;
	private String subject;
	private String author;
	private String keywords;
	private String producer;
	private String creator;
	private Calendar created;
	private Calendar modified;
	private float format;
	private int numberOfPages;
	private String optimized;
	private String security;
	private String paperSize;
	private String fonts;
	private String path;
	private File file;

	public DocumentModel(File pFile) {
		PDDocument document = null;

		try {
			document = PDDocument.load(pFile);
			renderer = new PDFRenderer(document);

			PDDocumentInformation documentInformation = document.getDocumentInformation();

			title = documentInformation.getTitle();
			subject = documentInformation.getSubject();
			author = documentInformation.getAuthor();
			keywords = documentInformation.getKeywords();
			producer = documentInformation.getProducer();
			creator = documentInformation.getCreator();
			created = documentInformation.getCreationDate();
			modified = documentInformation.getModificationDate();
			format = document.getVersion();
			numberOfPages = document.getNumberOfPages();
			optimized = "";
			security = "";
			paperSize = "";
			fonts = "";
			path = pFile.getAbsolutePath();
			file = pFile;

		} catch (IOException ex) {
			Utils.showAlert(AlertType.ERROR, ex);
		}
	}

	@Override
	public PDDocument document() {
		return document;
	}

	@Override
	public String title() {
		return title;
	}

	@Override
	public String subject() {
		return subject;
	}

	@Override
	public String author() {
		return author;
	}

	@Override
	public String keywords() {
		return keywords;
	}

	@Override
	public String producer() {
		return producer;
	}

	@Override
	public String creator() {
		return creator;
	}

	@Override
	public Calendar created() {
		return created;
	}

	@Override
	public Calendar modified() {
		return modified;
	}

	@Override
	public float format() {
		return format;
	}

	@Override
	public int numberOfPages() {
		return numberOfPages;
	}

	@Override
	public String optimized() {
		return optimized;
	}

	@Override
	public String security() {
		return security;
	}

	@Override
	public String paperSize() {
		return paperSize;
	}

	@Override
	public String fonts() {
		return fonts;
	}

	@Override
	public String path() {
		return path;
	}

	@Override
	public File file() {
		return file;
	}

	@Override
	public String controller() {
		return "ro.kuberam.getos.modules.pdfEditor.PdfEditorController";
	}

	@Override
	public Image goToPage(int pageNumber) {
		BufferedImage pageImage = null;
		try {
			pageImage = renderer.renderImage(pageNumber);
		} catch (IOException ex) {
			Utils.showAlert(AlertType.ERROR, ex);
		}
		
		return SwingFXUtils.toFXImage(pageImage, null);
	}
}
