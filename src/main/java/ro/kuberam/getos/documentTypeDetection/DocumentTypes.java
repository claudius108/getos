package ro.kuberam.getos.documentTypeDetection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.stage.FileChooser;
import ro.kuberam.getos.utils.Utils;

public enum DocumentTypes {
	PDF("Adobe Portable Document Format (PDF)", "application/pdf", "pdf"), CSV("Comma-separated values (CSV)",
			"text/csv", "csv"), HTML("HyperText Markup Language (HTML)", "text/html", "html", "htm");

	private final String description;
	private final String mimeType;
	private final String[] extensions;

	DocumentTypes(String description, String mimeType, String... extensions) {
		this.description = description;
		this.mimeType = mimeType;
		this.extensions = extensions;
	}

	public String getDescription() {
		return this.description;
	}
	
	public String getMimeType() {
		return this.mimeType;
	}

	public String[] getExtensions() {
		return this.extensions;
	}

	public static String getTypeByExtension(File file) {
		String documentType = null;
		String extension = Utils.getExtension(file);

		for (DocumentTypes type : values()) {
			for (String registeredExtension : type.extensions) {
				if (registeredExtension.equals(extension)) {
					documentType = type.getMimeType();
				}
			}
		}

		return documentType;
	}

	public static DocumentTypes getTypeByMimeType(String mimeType) {
		for (DocumentTypes type : values()) {
			if (type.mimeType.equals(mimeType)) {
				return type;
			}
		}
		return null;
	}

	public static List<FileChooser.ExtensionFilter> getExtensionFilters() {
		ArrayList<FileChooser.ExtensionFilter> filters = new ArrayList<>();
		ArrayList<String> extensionsList = new ArrayList<>();

		for (DocumentTypes type : values()) {
			for (String extension : type.extensions) {
				extensionsList.add("*." + extension);
			}

			filters.add(new FileChooser.ExtensionFilter(type.description, extensionsList));
			extensionsList.clear();
		}

		return filters;
	}

}
