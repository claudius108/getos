package ro.kuberam.getos.events;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventType;

public class GetosEvent extends Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = 831113937747538096L;

	private Object fData;

	public GetosEvent(@NamedArg("eventType") EventType<? extends Event> eventType) {
		super(eventType);
	}

	public Object getData() {
		return fData;
	}

	public GetosEvent setData(Object data) {
		fData = data;
		
		return this;
	}
}