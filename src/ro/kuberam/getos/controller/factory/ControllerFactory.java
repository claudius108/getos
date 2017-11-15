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

	public ControllerFactory(Application application) {
		this.application = application;
		this.stage = null;
		this.documentModel = null;
	}

	public ControllerFactory(Application pApplication, Stage stage) {
		this.application = pApplication;
		this.stage = stage;
		this.documentModel = null;
	}

	public ControllerFactory(Application pApplication, Stage stage, DocumentModel documentModel) {
		this.application = pApplication;
		this.stage = stage;
		this.documentModel = documentModel;
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
