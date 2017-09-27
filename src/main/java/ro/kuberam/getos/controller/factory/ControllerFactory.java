package ro.kuberam.getos.controller.factory;

import java.lang.reflect.InvocationTargetException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Callback;

public final class ControllerFactory implements Callback<Class<?>, Object> {

	private final Application mApplication;
	private final Stage mStage;

	public ControllerFactory(Application application) {
		mApplication = application;
		mStage = null;
	}

	public ControllerFactory(Application application, Stage stage) {
		mApplication = application;
		mStage = stage;
	}

	@Override
	public Object call(Class<?> type) {
		try {
			if (StageController.class.isAssignableFrom(type)) {
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
