package ro.kuberam.getos.modules.pdfEditor;

import java.io.File;

public class DocumentMetadata implements ro.kuberam.getos.DocumentMetadata {
	
	private String path;

	public DocumentMetadata(File file) {
		path = file.getAbsolutePath();
	}

	@Override
	public String path() {
		return path;
	}

	@Override
	public void pageForward() {
		// TODO Auto-generated method stub

	}

}
