package ro.kuberam.getos.modules.pdfEditor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
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

	private final static String TAG = DocumentModel.class.getSimpleName();

	private ThreadLocal<PDDocument> localPdDocument = new ThreadLocal<PDDocument>() {
	    @Override
	    protected PDDocument initialValue() {
	        return new PDDocument();
	    }
	};
	
	private PDDocument document = localPdDocument.get();
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
	
	public DocumentModel(File file) {
		try {
			document = PDDocument.load(file, MemoryUsageSetting.setupTempFileOnly());
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
			path = file.getAbsolutePath();
			this.file = file;
		} catch (IOException ex) {
			Utils.showAlert(AlertType.ERROR, ex);
		}
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
		System.gc();
		localPdDocument.remove();
	}
}
