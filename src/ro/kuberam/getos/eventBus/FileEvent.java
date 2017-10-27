package ro.kuberam.getos.eventBus;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventType;
import ro.kuberam.getos.eventBus.GetosEvent;

public class FileEvent extends GetosEvent {

	private static final long serialVersionUID = 2801302381856209226L;

	public static final EventType<FileEvent> OPEN_FILE = new EventType<>(Event.ANY, "OPEN_FILE");

	public FileEvent(@NamedArg("eventType") EventType<? extends Event> eventType) {
		super(eventType);
	}
}