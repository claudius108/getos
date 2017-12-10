package ro.kuberam.getos.modules.htmlEditor;

import com.google.auto.service.AutoService;

import ro.kuberam.getos.DocumentModule;

@AutoService(DocumentModule.class)
public class Module implements DocumentModule {
	
	private String documentType = "text/html";

	@Override
	public String getDocumentType() {
		return documentType;
	}

	static {
	}
}
