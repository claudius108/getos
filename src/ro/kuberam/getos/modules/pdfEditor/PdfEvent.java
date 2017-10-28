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
	
	public PdfEvent(@NamedArg("eventType") EventType<? extends Event> eventType) {
		super(eventType);
	}
}