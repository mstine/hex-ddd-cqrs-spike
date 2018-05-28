package io.pivotal.spike.escqrs.eventstore;

import io.pivotal.spike.escqrs.support.Identity;
import io.pivotal.spike.escqrs.support.IdentityGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Matt Stine
 */
public class EventStoreTest {

	private IdentityGenerator identityGenerator;
	private EventStore eventStore;

	@Before
	public void setUp() {
		identityGenerator = mock(IdentityGenerator.class);
		eventStore = new InProcessEventStore(identityGenerator);
	}

	@Test
	public void createsNewTopic() {
		Topic topic = eventStore.topicOf("foo");
		assertThat(topic.getName()).isEqualTo("foo");
	}

	@Test
	public void storesEventInTopic() {
		Event event = new Event(new EventId(new Identity("12345")));
		Topic topic = eventStore.topicOf("foo");

		eventStore.publish(topic, event);

		assertThat(eventStore.contains(topic, event)).isTrue();
	}

	@Test
	public void unknownTopicThrowsException() {
		Event event = new Event(new EventId(new Identity("12345")));
		Topic topic = new Topic("bar");

		assertThatThrownBy(() -> eventStore.publish(topic, event))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void returnsNextEventIdentity() {
		when(identityGenerator.nextIdentity()).thenReturn(new Identity("12345"));

		EventId eventId = eventStore.nextIdentity();
		assertThat(eventId).isNotNull();
		assertThat(eventId.getId()).isNotNull();
	}

	@Test
	public void returnsEventLogForTopic() {
		Topic topic = eventStore.topicOf("manyEvents");

		for (int i = 0; i < 5; i++) {
			eventStore.publish(topic, new Event(eventStore.nextIdentity()));
		}

		List<Event> events = eventStore.eventsIn(topic);

		assertThat(events.size()).isEqualTo(5);
	}
}
