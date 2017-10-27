package ro.kuberam.getos.events;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventType;

public class UserInterfaceEvent extends GetosEvent {
	
	private static final long serialVersionUID = -5276406197028817808L;
	
	public static final EventType<UserInterfaceEvent> UPDATE_STATUS_LABEL = new EventType<>(Event.ANY, "UPDATE_STATUS_LABEL");

	public UserInterfaceEvent(@NamedArg("eventType") EventType<? extends Event> eventType) {
		super(eventType);
	}
}