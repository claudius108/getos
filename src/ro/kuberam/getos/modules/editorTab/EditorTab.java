package ro.kuberam.getos.modules.editorTab;

import javafx.scene.control.Tab;

import java.io.File;

public class EditorTab extends Tab {

	private File file;

	public EditorTab() {
	}

	public EditorTab(File pFile) {
		super(pFile.getName());
		file = pFile;
		setText(file.getName());
	}

	public File getFile() {
		return file;
	}

	public void setFile(File pFile) {
		file = pFile;
	}

}
