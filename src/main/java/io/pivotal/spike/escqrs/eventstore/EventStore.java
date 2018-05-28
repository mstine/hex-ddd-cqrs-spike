package io.pivotal.spike.escqrs.eventstore;

import java.util.List;

/**
 * @author Matt Stine
 */
public interface EventStore {
	Topic topicOf(String name);

	void publish(Topic topic, Event event);

	boolean contains(Topic topic, Event event);

	EventId nextIdentity();

	List<Event> eventsIn(Topic topic);
}
