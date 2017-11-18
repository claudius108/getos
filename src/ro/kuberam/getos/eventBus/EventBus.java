package ro.kuberam.getos.eventBus;

import javafx.event.EventHandler;
import javafx.event.EventType;
import ro.kuberam.getos.events.GetosEvent;

/**
 * An event dispatcher that can be used for subscribing to events and posting the events.
 */
public interface EventBus {
    /**
     * Register event handler for event type.
     *
     * @param eventType type
     * @param eventHandler handler
     * @param <T> event
     */
    <T extends GetosEvent> Subscriber addEventHandler(EventType<T> eventType,
                                           EventHandler<? super T> eventHandler);

    /**
     * Remove event handler for event type.
     *
     * @param eventType type
     * @param eventHandler handler
     * @param <T> event
     */
    <T extends GetosEvent> void removeEventHandler(EventType<T> eventType,
                                              EventHandler<? super T> eventHandler);

    /**
     * Post (fire) given event.
     *
     * @param eventName the event name
     */
    void fireEvent(String eventName);
    
    /**
     * Post (fire) given event. The data for the event is passed, too.
     *
     * @param eventName the event name
     * @param data the event data
     */
    void fireEvent(String eventName, Object data);
    
    /**
     * Register an event.
     * 
     * @param eventName the event name
     * @param event the event
     */    
    void registerEvent(String eventName, GetosEvent event);
}
