package ro.kuberam.getos.modules.pdfEditor;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

public class DocumentMetadata implements ro.kuberam.getos.DocumentMetadata {

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

	public DocumentMetadata(File file) {
		PDDocument pdfDocument = null;

		try {
			pdfDocument = PDDocument.load(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		PDDocumentInformation documentInformation = pdfDocument.getDocumentInformation();

		title = documentInformation.getTitle();
		subject = documentInformation.getSubject();
		author = documentInformation.getAuthor();
		keywords = documentInformation.getKeywords();
		producer = documentInformation.getProducer();
		creator = documentInformation.getCreator();
		created = documentInformation.getCreationDate();
		modified = documentInformation.getModificationDate();
		format = pdfDocument.getVersion();
		numberOfPages = pdfDocument.getNumberOfPages();
		optimized = "";
		security = "";
		paperSize = "";
		fonts = "";
		path = file.getAbsolutePath();
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

}
