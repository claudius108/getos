package ro.kuberam.getos.events;

import javafx.event.EventHandler;
import javafx.event.EventType;

public final class Subscriber {

    private EventBus bus;

    private EventType<? extends GetosEvent> eventType;
    private EventHandler<? super GetosEvent> eventHandler;

    Subscriber(EventBus bus, EventType<? extends GetosEvent> eventType, EventHandler<? super GetosEvent> eventHandler) {
        this.bus = bus;
        this.eventType = eventType;
        this.eventHandler = eventHandler;
    }

    /**
     * Stop listening for events.
     */
    public void unsubscribe() {
        bus.removeEventHandler(eventType, eventHandler);
    }
}
