package ro.kuberam.getos.documentTypeDetection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.stage.FileChooser;
import ro.kuberam.getos.utils.Utils;

public enum ViewerFileType {
	PDF("PDF", "PDF", "pdf"), CSV("Comma Separated Values", "csv", "xlsx");

	private final String mName;
	private final String[] mExtensions;

	ViewerFileType(String name, String... extensions) {
		mName = name;
		mExtensions = extensions;
	}

	public String getName() {
		return mName;
	}

	public String[] getExtensions() {
		return mExtensions;
	}

	public static ViewerFileType getTypeByExtension(File file) {
		String extension = Utils.getExtension(file);
		for (ViewerFileType type : values()) {
			for (String ext : type.mExtensions) {
				if (ext.equals(extension)) {
					return type;
				}
			}
		}
		return null;
	}

	public static ViewerFileType getTypeByName(String name) {
		for (ViewerFileType type : values()) {
			if (type.mName.equals(name)) {
				return type;
			}
		}
		return null;
	}

	public static List<FileChooser.ExtensionFilter> getExtensionFilters() {
		ArrayList<FileChooser.ExtensionFilter> filters = new ArrayList<>();
		ArrayList<String> extensionsList = new ArrayList<>();

		for (ViewerFileType type : values()) {
			for (String extension : type.mExtensions) {
				extensionsList.add("*." + extension);
			}

			filters.add(new FileChooser.ExtensionFilter(type.mName, extensionsList));
			extensionsList.clear();
		}

		return filters;
	}

}
