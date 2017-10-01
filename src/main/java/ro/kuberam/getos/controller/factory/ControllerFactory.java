package ro.kuberam.getos.controller.factory;

import java.lang.reflect.InvocationTargetException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Callback;
import ro.kuberam.getos.modules.editorTab.EditorTabController;
import ro.kuberam.getos.modules.viewers.ViewerFileType;

public final class ControllerFactory implements Callback<Class<?>, Object> {

	private final Application mApplication;
	private final Stage mStage;
	private final ViewerFileType viewerFileType;

	public ControllerFactory(Application application) {
		mApplication = application;
		mStage = null;
		viewerFileType = null;
	}

	public ControllerFactory(Application application, Stage stage) {
		mApplication = application;
		mStage = stage;
		viewerFileType = null;
	}

	public ControllerFactory(Application application, Stage stage, ViewerFileType type) {
		mApplication = application;
		mStage = stage;
		viewerFileType = type;
	}

	@Override
	public Object call(Class<?> type) {
		try {
			if (EditorTabController.class.isAssignableFrom(type)) {
				// EditorTabController is final so we don't need to guess the
				// ctor
				return new EditorTabController(mApplication, mStage, viewerFileType);
			} else if (StageController.class.isAssignableFrom(type)) {
				// Call the class first ctor
				return type.getConstructors()[0].newInstance(mApplication, mStage);
			} else if (Controller.class.isAssignableFrom(type)) {
				// Call the class first ctor
				return type.getConstructors()[0].newInstance(mApplication);
			}
		} catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new RuntimeException(e);
		}

		throw new RuntimeException("No constructor found");
	}

}
