package ro.kuberam.getos.modules.pdfEditor;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventType;
import ro.kuberam.getos.events.GetosEvent;

public class PdfEvent extends GetosEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 831113937747538096L;

	public static final EventType<PdfEvent> OPEN_PDF_FILE = new EventType<>(Event.ANY, "OPEN_PDF_FILE");
	public static final EventType<PdfEvent> PDF_BACK = new EventType<>(Event.ANY, "PDF_BACK");
	public static final EventType<PdfEvent> PDF_FORWARD = new EventType<>(Event.ANY, "PDF_FORWARD");
	public static final EventType<PdfEvent> PDF_ZOOM_IN = new EventType<>(Event.ANY, "PDF_ZOOM_IN");
	public static final EventType<PdfEvent> PDF_ZOOM_OUT = new EventType<>(Event.ANY, "PDF_ZOOM_OUT");
	public static final EventType<PdfEvent> PDF_FIT_TO_WIDTH = new EventType<>(Event.ANY, "PDF_FIT_TO_WIDTH");
	public static final EventType<PdfEvent> PDF_FIT_TO_HEIGHT = new EventType<>(Event.ANY, "PDF_FIT_TO_HEIGHT");
	public static final EventType<PdfEvent> PDF_FIT_TO_PAGE = new EventType<>(Event.ANY, "PDF_FIT_TO_PAGE");
	public static final EventType<PdfEvent> PDF_ENABLE_BUTTON = new EventType<>(Event.ANY, "PDF_ENABLE_BUTTON");
	public static final EventType<PdfEvent> PDF_DISABLE_BUTTON = new EventType<>(Event.ANY, "PDF_DISABLE_BUTTON");
	
	public PdfEvent(@NamedArg("eventType") EventType<? extends Event> eventType) {
		super(eventType);
	}
}