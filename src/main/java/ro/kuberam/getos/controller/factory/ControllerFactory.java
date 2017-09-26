package ro.kuberam.getos.controller.factory;

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
		return null;
	}

}
