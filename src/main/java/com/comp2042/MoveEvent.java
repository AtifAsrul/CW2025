package com.comp2042;

/**
 * Event class representing a movement action in the game.
 * Encapsulate the type of event and its source.
 */
public final class MoveEvent {
    private final EventType eventType;
    private final EventSource eventSource;

    /**
     * Constructs a new MoveEvent.
     *
     * @param eventType   the type of movement.
     * @param eventSource the source of the event (User or Thread).
     */
    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    /**
     * Gets the type of event.
     *
     * @return the EventType.
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Gets the source of the event.
     *
     * @return the EventSource.
     */
    public EventSource getEventSource() {
        return eventSource;
    }
}
