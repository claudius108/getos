package ro.kuberam.getos.modules.tableEditor;

import com.google.auto.service.AutoService;

import ro.kuberam.getos.DocumentModule;

@AutoService(DocumentModule.class)
public class Module implements DocumentModule {
	
	private String documentType = "table";

	@Override
	public String getDocumentType() {
		return documentType;
	}

	static {
	}
}
