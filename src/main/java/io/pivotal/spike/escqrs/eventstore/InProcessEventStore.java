package io.pivotal.spike.escqrs.eventstore;

import io.pivotal.spike.escqrs.support.IdentityGenerator;

import java.util.*;

/**
 * @author Matt Stine
 */
public class InProcessEventStore implements EventStore {
	private final IdentityGenerator identityGenerator;
	private HashMap<Topic, ArrayList<Event>> eventsByTopic = new HashMap<>();

	InProcessEventStore(IdentityGenerator identityGenerator) {
		this.identityGenerator = identityGenerator;
	}

	@Override
	public Topic topicOf(String name) {
		Topic topic = new Topic(name);
		eventsByTopic.put(topic, new ArrayList<>());
		return topic;
	}

	@Override
	public void publish(Topic topic, Event event) {
		if (!eventsByTopic.containsKey(topic)) {
			throw new IllegalArgumentException("Topic does not exist in EventStore: " + topic);
		}

		eventsByTopic.get(topic).add(event);
	}

	@Override
	public boolean contains(Topic topic, Event event) {
		return eventsByTopic.get(topic).contains(event);
	}

	@Override
	public EventId nextIdentity() {
		return new EventId(identityGenerator.nextIdentity());
	}

	@Override
	public List<Event> eventsIn(Topic topic) {
		return Collections.unmodifiableList(eventsByTopic.get(topic));
	}
}
