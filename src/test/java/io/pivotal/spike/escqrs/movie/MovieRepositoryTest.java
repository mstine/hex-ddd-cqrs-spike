package io.pivotal.spike.escqrs.movie;

import io.pivotal.spike.escqrs.eventstore.EventId;
import io.pivotal.spike.escqrs.eventstore.EventStore;
import io.pivotal.spike.escqrs.eventstore.Topic;
import io.pivotal.spike.escqrs.support.Identity;
import io.pivotal.spike.escqrs.support.IdentityGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Matt Stine
 */
public class MovieRepositoryTest {
	private IdentityGenerator identityGenerator;
	private MovieRepository movieRepository;
	private MovieId movieId;
	private Movie movie;
	private EventStore eventStore;
	private Topic topic;

	@Before
	public void setUp() {
		eventStore = mock(EventStore.class);
		topic = new Topic("movie");
		when(eventStore.topicOf("movie")).thenReturn(topic);

		identityGenerator = mock(IdentityGenerator.class);
		when(identityGenerator.nextIdentity()).thenReturn(new Identity("12345"));

		movieRepository = new MovieRepository(identityGenerator, eventStore);
		movieId = movieRepository.nextIdentity();
		movie = movieRepository.movieOf(movieId, "Pulp Fiction");
	}

	@Test
	public void returnsNextMovieIdentity() {
		MovieId movieId = movieRepository.nextIdentity();
		assertThat(movieId).isNotNull();
		assertThat(movieId.getId()).isNotNull();
	}

	@Test
	public void createsMovie() {
		assertThat(movie.getId()).isEqualTo(movieId);
		assertThat(movie.getTitle()).isEqualTo("Pulp Fiction");
	}

	@Test
	public void addsMovie() {
		EventId eventId = new EventId(new Identity("foo"));
		when(eventStore.nextIdentity()).thenReturn(eventId);

		movieRepository.add(movie);

		MovieAddedEvent movieAddedEvent = new MovieAddedEvent(eventId, movie.getId(), movie.getTitle());
		verify(eventStore).publish(topic, movieAddedEvent);
	}

	@Test
	public void retrievesMovieById() {
		when(eventStore.eventsIn(topic)).thenReturn(Collections.singletonList(new MovieAddedEvent(new EventId(new Identity("foo")), movie.getId(), movie.getTitle())));

		movieRepository.add(movie);
		Optional<Movie> foundMovie = movieRepository.findById(movieId);

		assertThat(foundMovie.get()).isEqualTo(movie);
	}

	@Test
	public void removesMovie() {
		when(eventStore.nextIdentity()).thenReturn(new EventId(new Identity("bar")));

		movieRepository.remove(movie);

		MovieRemovedEvent movieRemovedEvent = new MovieRemovedEvent(new EventId(new Identity("bar")), movie.getId());
		verify(eventStore).publish(topic, movieRemovedEvent);
	}

	@Test
	public void cannotRetrieveDeletedMovie() {
		when(eventStore.eventsIn(topic)).thenReturn(Arrays.asList(
				new MovieAddedEvent(new EventId(new Identity("foo")), movieId, "Pulp Fiction"),
				new MovieRemovedEvent(new EventId(new Identity("bar")), movieId)
		));

		Optional<Movie> foundMovie = movieRepository.findById(movieId);

		assertThat(foundMovie.isPresent()).isFalse();
	}

	@Test
	public void canRetrieveMovieDeletedThenReadded() {
		when(eventStore.eventsIn(topic)).thenReturn(Arrays.asList(
				new MovieAddedEvent(new EventId(new Identity("foo")), movieId, "Pulp Fiction"),
				new MovieRemovedEvent(new EventId(new Identity("bar")), movieId),
				new MovieAddedEvent(new EventId(new Identity("baz")), movieId, "Pulp Fiction")
		));

		Optional<Movie> foundMovie = movieRepository.findById(movieId);

		assertThat(foundMovie.isPresent()).isTrue();
	}

}
