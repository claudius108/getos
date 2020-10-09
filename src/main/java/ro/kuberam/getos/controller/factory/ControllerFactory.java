package ro.kuberam.getos.controller.factory;

import java.lang.reflect.InvocationTargetException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Callback;
import ro.kuberam.getos.DocumentModel;
import ro.kuberam.getos.modules.editorTab.EditorModel;

public final class ControllerFactory implements Callback<Class<?>, Object> {

	private final Application application;
	private final Stage stage;
	private final DocumentModel documentModel;
	private final EditorModel editorModel;

	public ControllerFactory(Application application) {
		this.application = application;
		this.stage = null;
		this.documentModel = null;
		this.editorModel = null;
	}

	public ControllerFactory(Application pApplication, Stage stage) {
		this.application = pApplication;
		this.stage = stage;
		this.documentModel = null;
		this.editorModel = null;
	}

	public ControllerFactory(Application pApplication, Stage stage, DocumentModel documentModel) {
		this.application = pApplication;
		this.stage = stage;
		this.documentModel = documentModel;
		this.editorModel = null;
	}
	
	public ControllerFactory(Application pApplication, Stage stage, DocumentModel documentModel, EditorModel editorModel) {
		this.application = pApplication;
		this.stage = stage;
		this.documentModel = documentModel;
		this.editorModel = editorModel;
	}

	@Override
	public Object call(Class<?> type) {
		try {
			if (RendererController.class.isAssignableFrom(type)) {
				return type.getConstructors()[0].newInstance(application, stage, documentModel, editorModel);
			} else if (EditorController.class.isAssignableFrom(type)) {
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
