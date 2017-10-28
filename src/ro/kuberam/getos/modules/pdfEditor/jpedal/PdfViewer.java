package ro.kuberam.getos.modules.pdfEditor.jpedal;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import org.jpedal.examples.viewer.gui.javafx.dialog.FXInputDialog;
import org.jpedal.exception.PdfException;
import org.jpedal.external.PluginHandler;
import org.jpedal.objects.PdfPageData;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ro.kuberam.getos.Getos;
import ro.kuberam.getos.controller.factory.EditorController;
import ro.kuberam.getos.modules.pdfEditor.PdfEvent;

public class PdfViewer extends EditorController {

	private final static String TAG = PdfViewer.class.getSimpleName();

	@FXML
	private BorderPane root;

	@FXML
	private VBox top;

	@FXML
	private Button backButton;

	@FXML
	private Button forwardButton;

	@FXML
	private Label pgCountLabel;

	@FXML
	private ScrollPane center;

	@FXML
	private Group contentPane;

	@FXML
	private HBox bottom;

	private final org.jpedal.PdfDecoderFX pdf = new org.jpedal.PdfDecoderFX();

	PluginHandler customPluginHandle;

	public enum FitToPage { // control how we fit the content to the page
		AUTO, // AUTO will automatically fit the content to the stage depending
				// on its orientation
		WIDTH, // WIDTH will fit the content to the stage width depending on its
				// orientation
		HEIGHT, // HEIGHT will fit the content to the stage height depending on
				// its orientation
		NONE
	}

	// Variable to hold the current file/directory
	static File pFile;

	// These two variables are to do with PDF encryption & passwords
	private String password; // Holds the password from the JVM or from User
								// input
	private boolean closePasswordPrompt; // Controls whether or not we should
											// close the prompt box

	private float scale = 1.0f;

	private final float[] scalings = { 0.01f, 0.1f, 0.25f, 0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f, 4.0f, 7.5f, 10.0f };

	private int currentScaling = 5;

	private static final float insetX = 25;

	private static final float insetY = 25;

	private int currentPage = 1;

	/*
	 * Controls size of the stage, in theory setting this to a higher value will
	 * increase image quality as there's more pixels due to higher image
	 * resolutions
	 */
	FitToPage zoomMode = FitToPage.AUTO;

	public PdfViewer(Application application, Stage stage, File file) {
		super(application, stage, file);
		
		Getos.eventBus.addEventHandler(PdfEvent.PDF_BACK, event -> {
			if (currentPage > 1) {
				goToPage(currentPage - 1);
			}
			event.consume();
		});
		
		Getos.eventBus.addEventHandler(PdfEvent.PDF_FORWARD, event -> {
			if (currentPage < pdf.getPageCount()) {
				goToPage(currentPage + 1);
			}
			event.consume();
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		center.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
			@Override
			public void changed(final ObservableValue<? extends Bounds> ov, final Bounds ob, final Bounds nb) {
				adjustPagePosition(nb);
			}
		});

		contentPane.getChildren().add(pdf);

		// Auto adjust so dynamically resized as viewer width alters
		getStage().getScene().widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(final ObservableValue<? extends Number> observableValue, final Number oldSceneWidth,
					final Number newSceneWidth) {
				fitToX(zoomMode);
			}
		});

		getStage().getScene().heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(final ObservableValue<? extends Number> observableValue, final Number oldSceneHeight,
					final Number newSceneHeight) {

				fitToX(zoomMode);

			}
		});

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				openFile(pFile);
			}
		});
	}

	/**
	 * Sets up a MenuBar to be used at the top of the window. It contains one
	 * Menu - navMenu - which allows the user to open and navigate pdf files
	 *
	 * @return ToolBar object used at the top of the user interface
	 */
	// final Button open = new Button("Open");
	// final Button back = new Button("▲");
	// final ComboBox<String> pages = new ComboBox<String>();
	// final Label pageCount = new Label();
	// final Button forward = new Button("▼");
	// final Button zoomIn = new Button("Zoom in");
	// final Button zoomOut = new Button("Zoom out");
	// final Button fitWidth = new Button("Fit to Width");
	// final Button fitHeight = new Button("Fit to Height");
	// final Button fitPage = new Button("Fit to Page");
	//
	// open.setId("open");
	// back.setId("back");
	// pageCount.setId("pgCount");
	// pages.setId("pages");
	// forward.setId("forward");
	// zoomIn.setId("zoomIn");
	// zoomOut.setId("zoomOut");
	// fitWidth.setId("fitWidth");
	// fitHeight.setId("fitHeight");
	// fitPage.setId("fitPage");
	//
	//
	// pages.getSelectionModel().selectedIndexProperty().addListener(new
	// ChangeListener<Number>() {
	// @Override
	// public void changed(final ObservableValue<? extends Number> ov, final
	// Number oldVal, final Number newVal) {
	// if (newVal.intValue() != -1 && newVal.intValue() + 1 != currentPage) {
	// final int newPage = newVal.intValue() + 1;
	// goToPage(newPage);
	// }
	// }
	// });
	//
	//
	// zoomIn.setOnAction(new EventHandler<ActionEvent>() {
	//
	// @Override
	// public void handle(final ActionEvent t) {
	// zoomMode = FitToPage.NONE;
	//
	// if (currentScaling < scalings.length - 1) {
	//
	// currentScaling = findClosestIndex(scale, scalings);
	//
	// if (scale >= scalings[findClosestIndex(scale, scalings)]) {
	//
	// currentScaling++;
	//
	// }
	//
	// scale = scalings[currentScaling];
	//
	// }
	//
	// pdf.setPageParameters(scale, currentPage);
	// adjustPagePosition(centerPane.getViewportBounds());
	// }
	// });
	//
	// zoomOut.setOnAction(new EventHandler<ActionEvent>() {
	//
	// @Override
	// public void handle(final ActionEvent t) {
	// zoomMode = FitToPage.NONE;
	//
	// if (currentScaling > 0) {
	//
	// currentScaling = findClosestIndex(scale, scalings);
	//
	// if (scale <= scalings[findClosestIndex(scale, scalings)]) {
	//
	// currentScaling--;
	//
	// }
	//
	// scale = scalings[currentScaling];
	//
	// }
	//
	// pdf.setPageParameters(scale, currentPage);
	// adjustPagePosition(centerPane.getViewportBounds());
	// }
	// });
	//
	// fitWidth.setOnAction(new EventHandler<ActionEvent>() {
	//
	// @Override
	// public void handle(final ActionEvent t) {
	// zoomMode = FitToPage.WIDTH;
	// fitToX(FitToPage.WIDTH);
	//
	// }
	// });
	//
	// fitHeight.setOnAction(new EventHandler<ActionEvent>() {
	//
	// @Override
	// public void handle(final ActionEvent t) {
	// zoomMode = FitToPage.HEIGHT;
	// fitToX(FitToPage.HEIGHT);
	//
	// }
	// });
	//
	// fitPage.setOnAction(new EventHandler<ActionEvent>() {
	//
	// @Override
	// public void handle(final ActionEvent t) {
	// zoomMode = FitToPage.AUTO;
	// fitToX(FitToPage.AUTO);
	//
	// }
	// });

	private void openFile(final File input) {
		try {
			// Open the pdf file so we can check for encryption
			pdf.openPdfFile(input.getAbsolutePath());

			if (customPluginHandle != null) {
				customPluginHandle.setFileName(input.getAbsolutePath());
			}

			// This code block deals with user input and JVM passwords in
			// Encrypted PDF documents.
			if (pdf.isEncrypted()) {

				int passwordCount = 0; // Monitors how many attempts there have
										// been to the password
				closePasswordPrompt = false; // Do not close the prompt box

				// While the PDF content is not viewable, repeat until the
				// correct password is found
				while (!pdf.isFileViewable() && !closePasswordPrompt) {

					/*
					 * See if there's a JVM flag for the password & Use it if
					 * there is Otherwise prompt the user to enter a password
					 */
					if (System.getProperty("org.jpedal.password") != null) {
						password = System.getProperty("org.jpedal.password");
					} else if (!closePasswordPrompt) {
						showPasswordPrompt(passwordCount);
					}

					// If we have a password, try and open the PdfFile again
					// with the password
					if (password != null) {
						pdf.openPdfFile(input.getAbsolutePath());
					}
					passwordCount += 1; // Increment the password attempt

				}

			}

			// Set up top bar values
			pgCountLabel.setText(getResources().getString("pages_number_prefix") + " " + pdf.getPageCount());

			// Goes to the first page and starts the decoding process
			goToPage(currentPage);

		} catch (final PdfException ex) {
			ex.printStackTrace();

		}

	}

	/**
	 * This method will show a popup box and request for a password. If the user
	 * does not enter the correct password it will ask them to try again. If the
	 * user presses the Cross button, the password prompt will close.
	 *
	 * @param passwordCount
	 *            is an int which represents the current input attempt
	 */
	private void showPasswordPrompt(final int passwordCount) {

		// Setup password prompt content
		final Text titleText = new Text("Password Request");
		final TextField inputPasswordField = new TextField("Please Enter Password");

		// If the user has attempted to enter the password more than once,
		// change the text
		if (passwordCount >= 1) {
			titleText.setText("Incorrect Password");
			inputPasswordField.setText("Please Try Again");
		}

		final FXInputDialog passwordInput = new FXInputDialog(getStage(), titleText.getText()) {
			@Override
			protected void positiveClose() {
				super.positiveClose();
				closePasswordPrompt = true;
			}
		};

		password = passwordInput.showInputDialog();

	}

	private void fitToX(final FitToPage fitToPage) {

		if (fitToPage == FitToPage.NONE) {
			return;
		}

		final float pageW = pdf.getPdfPageData().getCropBoxWidth(currentPage);
		final float pageH = pdf.getPdfPageData().getCropBoxHeight(currentPage);
		final int rotation = pdf.getPdfPageData().getRotation(currentPage);

		// Handle how we auto fit the content to the page
		if (fitToPage == FitToPage.AUTO && (pageW < pageH)) {
			if (pdf.getPDFWidth() < pdf.getPDFHeight()) {
				fitToX(FitToPage.HEIGHT);
			} else {
				fitToX(FitToPage.WIDTH);
			}
		}

		// Handle how we fit the content to the page width or height
		if (fitToPage == FitToPage.WIDTH) {
			final float width = (float) (getStage().getWidth());
			if (rotation == 90 || rotation == 270) {
				scale = (width - insetX - insetX) / pageH;
			} else {
				scale = (width - insetX - insetX) / pageW;
			}
		} else if (fitToPage == FitToPage.HEIGHT) {
			final float height = (float) (getStage().getScene().getHeight() - top.getBoundsInLocal().getHeight()
					- bottom.getHeight());

			if (rotation == 90 || rotation == 270) {
				scale = (height - insetY - insetY) / pageW;
			} else {
				scale = (height - insetY - insetY) / pageH;
			}
		}

		pdf.setPageParameters(scale, currentPage);
	}

	/**
	 * Locate scaling value closest to current scaling setting
	 *
	 * @param scale
	 *            float value of the scale value to check
	 * @param scalings
	 *            float array holding scaling values
	 * @return int value of the index from scalings closest to the value of
	 *         scale
	 */
	private static int findClosestIndex(final float scale, final float[] scalings) {
		float currentMinDiff = Float.MAX_VALUE;
		int closest = 0;

		for (int i = 0; i < scalings.length - 1; i++) {

			final float diff = Math.abs(scalings[i] - scale);

			if (diff < currentMinDiff) {
				currentMinDiff = diff;
				closest = i;
			}

		}
		return closest;
	}

	private void decodePage() {

		try {
			final PdfPageData pageData = pdf.getPdfPageData();
			final int rotation = pageData.getRotation(currentPage);

			/*
			 * Only call this when the page is displayed vertically, otherwise
			 * it will mess up the document cropping on side-ways documents.
			 */
			if (rotation == 0 || rotation == 180) {
				pdf.setPageParameters(scale, currentPage);
			}

			pdf.decodePage(currentPage);
			// wait to ensure decoded
			pdf.waitForDecodingToFinish();
		} catch (final Exception e) {
			e.printStackTrace();
		}

		fitToX(FitToPage.AUTO);
		updateNavButtons();
		setBorder();
		adjustPagePosition(center.getViewportBounds());
	}

	private void updateNavButtons() {
		if (currentPage > 1) {
			backButton.setDisable(false);
		} else {
			backButton.setDisable(true);
		}

		if (currentPage < pdf.getPageCount()) {
			forwardButton.setDisable(false);
		} else {
			forwardButton.setDisable(true);
		}

		// ((ComboBox)
		// top.lookup("#pages")).getSelectionModel().select(currentPage - 1);
	}

	private void goToPage(final int newPage) {
		currentPage = newPage;
		decodePage();
	}

	private void adjustPagePosition(final Bounds nb) {

		double adjustment = ((nb.getWidth() / 2) - (contentPane.getBoundsInLocal().getWidth() / 2));

		// Keep the group within the viewport of the scrollpane
		if (adjustment < 0) {
			adjustment = 0;
		}
		contentPane.setTranslateX(adjustment);
	}

	// Set a space between the top toolbar and the page
	private void setBorder() {

		// Why it's easier to use a dropshadow for this is beyond me, but here
		// it is...
		final int rotation = pdf.getPdfPageData().getRotation(currentPage);
		final double x = (rotation == 90 || rotation == 270) ? 40 : 0;
		final double y = (rotation == 90 || rotation == 270) ? 0 : 40;
		final DropShadow pdfBorder = new DropShadow(0, x, y, Color.TRANSPARENT);
		pdf.setEffect(pdfBorder);
	}

	public void addExternalHandler(final PluginHandler customPluginHandle) {
		this.customPluginHandle = customPluginHandle;
	}
}
