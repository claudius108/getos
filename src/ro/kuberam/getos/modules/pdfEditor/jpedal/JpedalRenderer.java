package ro.kuberam.getos.modules.pdfEditor.jpedal;

import java.io.File;

import org.jpedal.examples.viewer.gui.javafx.dialog.FXInputDialog;
import org.jpedal.exception.PdfException;
import org.jpedal.external.PluginHandler;
import org.jpedal.objects.PdfPageData;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import ro.kuberam.getos.DocumentRenderer;
import ro.kuberam.getos.Getos;
import ro.kuberam.getos.modules.pdfEditor.PdfEvent;

public class JpedalRenderer extends BorderPane implements DocumentRenderer {

	private ScrollPane scrollPane;

	private Group group;

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

	private File file;

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
	 * increase image quality as there's more pixels due to higher image resolutions
	 */
	FitToPage zoomMode = FitToPage.AUTO;

	public JpedalRenderer(ScrollPane pScrollPane, Group pGroup, File pFile) {
		this.scrollPane = pScrollPane;
		this.group = pGroup;
		this.file = pFile;

		Getos.eventBus.addEventHandler(PdfEvent.PDF_ZOOM_IN, event -> {
			zoomMode = FitToPage.NONE;

			if (currentScaling < scalings.length - 1) {
				currentScaling = findClosestIndex(scale, scalings);

				if (scale >= scalings[findClosestIndex(scale, scalings)]) {
					currentScaling++;
				}

				scale = scalings[currentScaling];
			}

			pdf.setPageParameters(scale, currentPage);
			adjustPagePosition(scrollPane.getViewportBounds());
			event.consume();
		});

		Getos.eventBus.addEventHandler(PdfEvent.PDF_ZOOM_OUT, event -> {
			zoomMode = FitToPage.NONE;

			if (currentScaling > 0) {
				currentScaling = findClosestIndex(scale, scalings);
				if (scale <= scalings[findClosestIndex(scale, scalings)]) {
					currentScaling--;
				}

				scale = scalings[currentScaling];
			}

			pdf.setPageParameters(scale, currentPage);
			adjustPagePosition(scrollPane.getViewportBounds());

			event.consume();
		});

		Getos.eventBus.addEventHandler(PdfEvent.PDF_FIT_TO_WIDTH, event -> {
			zoomMode = FitToPage.WIDTH;
			fitToX(FitToPage.WIDTH);

			event.consume();
		});

		Getos.eventBus.addEventHandler(PdfEvent.PDF_FIT_TO_HEIGHT, event -> {
			zoomMode = FitToPage.HEIGHT;
			fitToX(FitToPage.HEIGHT);

			event.consume();
		});

		Getos.eventBus.addEventHandler(PdfEvent.PDF_FIT_TO_PAGE, event -> {
			zoomMode = FitToPage.AUTO;
			fitToX(FitToPage.AUTO);

			event.consume();
		});

		scrollPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
			@Override
			public void changed(final ObservableValue<? extends Bounds> ov, final Bounds ob, final Bounds nb) {
				adjustPagePosition(nb);
			}
		});

		group.getChildren().add(pdf);

		// Auto adjust so dynamically resized as viewer width alters
		// getStage().getScene().widthProperty().addListener(new
		// ChangeListener<Number>() {
		// @Override
		// public void changed(final ObservableValue<? extends Number> observableValue,
		// final Number oldSceneWidth,
		// final Number newSceneWidth) {
		// fitToX(zoomMode);
		// }
		// });
		//
		// getStage().getScene().heightProperty().addListener(new
		// ChangeListener<Number>() {
		// @Override
		// public void changed(final ObservableValue<? extends Number> observableValue,
		// final Number oldSceneHeight,
		// final Number newSceneHeight) {
		// fitToX(zoomMode);
		//
		// }
		// });

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				openFile(file);
			}
		});
	}

	@Override
	public ScrollPane root() {
		return scrollPane;
	}

	@Override
	public void pageBack() {
		if (currentPage > 1) {
			goToPage(currentPage - 1);
		}
	}

	@Override
	public void pageForward() {
		if (currentPage < pdf.getPageCount()) {
			goToPage(currentPage + 1);
		}
	}

	/**
	 * Sets up a MenuBar to be used at the top of the window. It contains one Menu -
	 * navMenu - which allows the user to open and navigate pdf files
	 *
	 * @return ToolBar object used at the top of the user interface
	 */
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
					 * See if there's a JVM flag for the password & Use it if there is Otherwise
					 * prompt the user to enter a password
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

			Getos.eventBus.fireEvent(Getos.eventsRegistry.get("pdf.update-page-count").setData(pdf.getPageCount()));

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

		final FXInputDialog passwordInput = new FXInputDialog(null, titleText.getText()) {
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
			final float width = (float) (getWidth());
			if (rotation == 90 || rotation == 270) {
				scale = (width - insetX - insetX) / pageH;
			} else {
				scale = (width - insetX - insetX) / pageW;
			}
		} else if (fitToPage == FitToPage.HEIGHT) {
			final float height = (float) getHeight();

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
	 * @return int value of the index from scalings closest to the value of scale
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
			 * Only call this when the page is displayed vertically, otherwise it will mess
			 * up the document cropping on side-ways documents.
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
		adjustPagePosition(scrollPane.getViewportBounds());
	}

	private void updateNavButtons() {
		if (currentPage > 1) {
			Getos.eventBus.fireEvent(Getos.eventsRegistry.get("pdf.enable-button").setData("backButton"));
		} else {
			Getos.eventBus.fireEvent(Getos.eventsRegistry.get("pdf.disable-button").setData("backButton"));
		}

		if (currentPage < pdf.getPageCount()) {
			Getos.eventBus.fireEvent(Getos.eventsRegistry.get("pdf.enable-button").setData("forwardButton"));
		} else {
			Getos.eventBus.fireEvent(Getos.eventsRegistry.get("pdf.disable-button").setData("forwardButton"));
		}

		// ((ComboBox)
		// top.lookup("#pages")).getSelectionModel().select(currentPage - 1);
	}

	private void goToPage(final int newPage) {
		currentPage = newPage;
		decodePage();
	}

	private void adjustPagePosition(final Bounds nb) {

		double adjustment = ((nb.getWidth() / 2) - (group.getBoundsInLocal().getWidth() / 2));

		// Keep the group within the viewport of the scrollpane
		if (adjustment < 0) {
			adjustment = 0;
		}
		group.setTranslateX(adjustment);
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
// https://files.idrsolutions.com/samplecode/org/jpedal/examples/baseviewer/BaseViewerFX.java.html