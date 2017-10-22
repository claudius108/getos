package ro.kuberam.getos;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import ro.kuberam.getos.controller.factory.EditorController;
import ro.kuberam.getos.eventBus.EventBus;
import ro.kuberam.getos.eventBus.FXEventBus;
import ro.kuberam.getos.eventBus.GetosEvent;
import ro.kuberam.getos.modules.main.MainWindowController;
import ro.kuberam.getos.utils.Utils;

public class Getos extends Application {

	private final static String TAG = Getos.class.getSimpleName();

	public static EventBus mainEventBus;
	public static HashMap<String, GetosEvent> mainEvents = new HashMap<String, GetosEvent>();

	public static ArrayList<EditorController> tabControllers;
	public static TabPane tabPane;
	public static Label statusLabel;

	static {
		mainEventBus = new FXEventBus();
		tabControllers = new ArrayList<>();

		try {
			Package[] packages = Package.getPackages();
			Arrays.stream(packages).filter(p -> p.getName().startsWith("ro.kuberam.getos.modules"))
//					.map(c -> c.load().getPackage().getAnnotation(PackageOwner.class))
					.forEach(a -> System.out.println(a.getName()));
//			for (Package pack : packages) {
//
//				Annotation[] myPackageAnnotations = pack.getAnnotations();
//				System.out.println("Available annotations for package: " + pack.getName());
//
//				for (Annotation a : myPackageAnnotations) {
//					System.out.println("\t * " + a.annotationType());
//				}
//			}

			// ClassPath classPath = ClassPath.from(OwnerFinder.class.getClassLoader());
			// classPath.getTopLevelClassesRecursive("com.somepackage")
			// .stream()
			// .filter(c -> c.getSimpleName().equals("package-info"))
			// .map(c -> c.load().getPackage().getAnnotation(PackageOwner.class))
			// .forEach(a -> System.out.println(a.owner()));

			// Package[] packages = Package.getPackages();
			// for (Package p : packages) {
			// PackageLevelAnnotation annotation =
			// p.getAnnotation(PackageLevelAnnotation.class);
			// Logger.getLogger(TAG).log(Level.INFO, "implementation = " + annotation);
			// if (annotation != null) {
			// Class<?>[] implementations = annotation.implementations();
			// for (Class<?> impl : implementations) {
			// Logger.getLogger(TAG).log(Level.INFO, "implementation = " +
			// impl.getSimpleName());
			// }
			// }
			// }

			Class.forName("ro.kuberam.getos.modules.pdfEditor.Module").getClass();
		} catch (ClassNotFoundException ex) {
			Utils.showAlert(AlertType.ERROR, null, ex.getCause().getLocalizedMessage());
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		MainWindowController.create(this, stage);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
