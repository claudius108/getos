package ro.kuberam.getos.modules.editorTab;

import javafx.scene.control.Tab;

import java.io.File;

public class EditorTab extends Tab {

	private File file;

	public EditorTab(File file) {
		super(file.getName());
		this.file = file;
		setText(file.getName());
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
