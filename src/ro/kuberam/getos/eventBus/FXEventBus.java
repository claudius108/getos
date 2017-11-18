package ro.kuberam.getos.eventBus;

import java.util.HashMap;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import ro.kuberam.getos.events.GetosEvent;

public final class FXEventBus implements EventBus {

	private Group eventHandlers = new Group();
	private HashMap<String, GetosEvent> eventsRegistry = new HashMap<String, GetosEvent>();

	@Override
	public <T extends GetosEvent> Subscriber addEventHandler(EventType<T> eventType,
			EventHandler<? super T> eventHandler) {
		eventHandlers.addEventHandler(eventType, eventHandler);
		
		return new Subscriber(this, eventType, (EventHandler<? super GetosEvent>) eventHandler);
	}

	@Override
	public <T extends GetosEvent> void removeEventHandler(EventType<T> eventType,
			EventHandler<? super T> eventHandler) {
		eventHandlers.removeEventHandler(eventType, eventHandler);
	}

	@Override
	public void fireEvent(String eventName) {
		eventHandlers.fireEvent(eventsRegistry.get(eventName));
	}
	
	@Override
	public void fireEvent(String eventName, Object data) {
		eventHandlers.fireEvent(eventsRegistry.get(eventName).setData(data));
	}

	@Override
	public void registerEvent(String eventName, GetosEvent event) {
		eventsRegistry.put(eventName, event);		
	}
}
