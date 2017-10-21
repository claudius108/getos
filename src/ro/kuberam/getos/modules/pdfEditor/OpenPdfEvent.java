package ro.kuberam.getos.modules.pdfEditor;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventType;
import ro.kuberam.getos.modules.eventBus.GetosEvent;

public class OpenPdfEvent extends GetosEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 831113937747538096L;

	public static final EventType<OpenPdfEvent> OPEN_FILE = new EventType<>(Event.ANY, "OPEN_FILE");
	
	public static final EventType<OpenPdfEvent> SAVE_FILE = new EventType<>(Event.ANY, "SAVE_FILE");

	public OpenPdfEvent(@NamedArg("eventType") EventType<? extends Event> eventType) {
		super(eventType);
	}
}