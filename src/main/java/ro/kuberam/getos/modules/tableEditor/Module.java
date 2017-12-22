package ro.kuberam.getos.modules.tableEditor;

import ro.kuberam.getos.DocumentModule;

public class Module implements DocumentModule {
	
	private String documentType = "table";

	@Override
	public String getDocumentType() {
		return documentType;
	}

	static {
	}
}
