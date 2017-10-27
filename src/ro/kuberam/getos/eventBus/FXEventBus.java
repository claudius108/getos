package ro.kuberam.getos.eventBus;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import ro.kuberam.getos.events.GetosEvent;

public final class FXEventBus implements EventBus {

	private Group eventHandlers = new Group();

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
	public void fireEvent(GetosEvent event) {
		eventHandlers.fireEvent(event);
	}
}
