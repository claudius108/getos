package ro.kuberam.getos.controller.factory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Callback;
import ro.kuberam.getos.modules.pdfViewer.PdfViewerController;
import ro.kuberam.getos.modules.viewers.ViewerFileType;

public final class ControllerFactory implements Callback<Class<?>, Object> {

	private final Application mApplication;
	private final Stage mStage;
	private final File pFile;

	public ControllerFactory(Application application) {
		mApplication = application;
		mStage = null;
		pFile = null;
	}

	public ControllerFactory(Application application, Stage stage) {
		mApplication = application;
		mStage = stage;
		pFile = null;
	}

	public ControllerFactory(Application application, Stage stage, ViewerFileType type) {
		mApplication = application;
		mStage = stage;
		pFile = null;
	}

	public ControllerFactory(Application application, Stage stage, File file) {
		mApplication = application;
		mStage = stage;
		pFile = file;
	}

	@Override
	public Object call(Class<?> type) {
		try {
			if (PdfViewerController.class.isAssignableFrom(type)) {
				return new PdfViewerController(mApplication, mStage, pFile);
			} else if (StageController.class.isAssignableFrom(type)) {
				return type.getConstructors()[0].newInstance(mApplication, mStage);
			} else if (Controller.class.isAssignableFrom(type)) {
				return type.getConstructors()[0].newInstance(mApplication);
			}
		} catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new RuntimeException(e);
		}

		throw new RuntimeException("No constructor found");
	}

}
