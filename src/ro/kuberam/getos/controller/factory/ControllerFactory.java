package ro.kuberam.getos.controller.factory;

import java.lang.reflect.InvocationTargetException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Callback;
import ro.kuberam.getos.DocumentModel;

public final class ControllerFactory implements Callback<Class<?>, Object> {

	private final Application application;
	private final Stage stage;
	private final DocumentModel documentModel;

	public ControllerFactory(Application pApplication) {
		application = pApplication;
		stage = null;
		documentModel = null;
	}

	public ControllerFactory(Application pApplication, Stage pStage) {
		application = pApplication;
		stage = pStage;
		documentModel = null;
	}

	public ControllerFactory(Application pApplication, Stage pStage, DocumentModel pDocumentModel) {
		application = pApplication;
		stage = pStage;
		documentModel = pDocumentModel;
	}

	@Override
	public Object call(Class<?> type) {
		try {
			if (EditorController.class.isAssignableFrom(type)) {
				return type.getConstructors()[0].newInstance(application, stage, documentModel);
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
