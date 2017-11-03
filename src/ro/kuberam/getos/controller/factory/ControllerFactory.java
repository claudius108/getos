package ro.kuberam.getos.controller.factory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Callback;
import ro.kuberam.getos.modules.pdfEditor.PdfEditorController;
import ro.kuberam.getos.modules.pdfViewer.PdfViewerController;

public final class ControllerFactory implements Callback<Class<?>, Object> {

	private final Application pApplication;
	private final Stage pStage;
	private final File pFile;

	public ControllerFactory(Application application) {
		pApplication = application;
		pStage = null;
		pFile = null;
	}

	public ControllerFactory(Application application, Stage stage) {
		pApplication = application;
		pStage = stage;
		pFile = null;
	}

	public ControllerFactory(Application application, Stage stage, File file) {
		pApplication = application;
		pStage = stage;
		pFile = file;
	}

	@Override
	public Object call(Class<?> type) {
		try {
			if (PdfEditorController.class.isAssignableFrom(type)) {
				return new PdfEditorController(pApplication, pStage, pFile);
			} else if (PdfViewerController.class.isAssignableFrom(type)) {
				return new PdfViewerController(pApplication, pStage, pFile);
			} else if (StageController.class.isAssignableFrom(type)) {
				return type.getConstructors()[0].newInstance(pApplication, pStage);
			} else if (Controller.class.isAssignableFrom(type)) {
				return type.getConstructors()[0].newInstance(pApplication);
			}
		} catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new RuntimeException(e);
		}

		throw new RuntimeException("No constructor found");
	}

}
