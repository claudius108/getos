package ro.kuberam.getos.controller.factory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Callback;
import ro.kuberam.getos.DocumentMetadata;

public final class ControllerFactory implements Callback<Class<?>, Object> {

	private final Application application;
	private final Stage stage;
	private final DocumentMetadata documentMetadata;
	private final File file;

	public ControllerFactory(Application pApplication) {
		application = pApplication;
		stage = null;
		documentMetadata = null;
		file = null;
	}

	public ControllerFactory(Application pApplication, Stage pStage) {
		application = pApplication;
		stage = pStage;
		documentMetadata = null;
		file = null;
	}

	public ControllerFactory(Application pApplication, Stage pStage, DocumentMetadata pDocumentMetadata, File pFile) {
		application = pApplication;
		stage = pStage;
		documentMetadata = pDocumentMetadata;
		file = pFile;
	}

	@Override
	public Object call(Class<?> type) {
		try {
			if (EditorController.class.isAssignableFrom(type)) {
				return type.getConstructors()[0].newInstance(application, stage, documentMetadata, file);
			} else if (StageController.class.isAssignableFrom(type)) {
				return type.getConstructors()[0].newInstance(application, stage);
			} else if (Controller.class.isAssignableFrom(type)) {
				return type.getConstructors()[0].newInstance(application);
			}
		} catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new RuntimeException(e);
		}

		throw new RuntimeException("No constructor found");
	}

}
