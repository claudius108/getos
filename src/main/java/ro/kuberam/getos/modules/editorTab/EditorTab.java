package ro.kuberam.getos.modules.editorTab;

import javafx.scene.control.Tab;

import java.io.File;

public class EditorTab extends Tab {

	private File mFile;

	public EditorTab() {
	}

	public EditorTab(File file) {
		super(file.getName());
		mFile = file;
	}

	public File getFile() {
		return mFile;
	}

	public void setFile(File file) {
		mFile = file;
		setText(file.getName());
	}

}
