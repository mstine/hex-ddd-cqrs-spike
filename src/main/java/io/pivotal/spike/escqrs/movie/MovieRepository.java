package io.pivotal.spike.escqrs.movie;

import io.pivotal.spike.escqrs.eventstore.EventStore;
import io.pivotal.spike.escqrs.eventstore.Topic;
import io.pivotal.spike.escqrs.support.IdentityGenerator;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Matt Stine
 */
public class MovieRepository {
	private final IdentityGenerator identityGenerator;
	private final EventStore eventStore;
	private final Topic movieTopic;

	public MovieRepository(IdentityGenerator identityGenerator, EventStore eventStore) {
		this.identityGenerator = identityGenerator;
		this.eventStore = eventStore;
		this.movieTopic = eventStore.topicOf("movie");
	}

	public MovieId nextIdentity() {
		return new MovieId(identityGenerator.nextIdentity());
	}

	public Movie movieOf(MovieId movieId, String title) {
		return new Movie(movieId, title);
	}

	public void add(Movie movie) {
		eventStore.publish(movieTopic,
				new MovieAddedEvent(eventStore.nextIdentity(),
						movie.getId(),
						movie.getTitle()));
	}

	public Optional<Movie> findById(MovieId movieId) {
		return eventStore.eventsIn(movieTopic)
				.stream()
				.map (e -> (MovieEvent) e)
				.filter(e -> e.getMovieId().equals(movieId))
				.filter(e -> e instanceof MovieAddedEvent || e instanceof MovieRemovedEvent)
				.map(event -> {
					Optional<Movie> movie = Optional.empty();
					if (event instanceof MovieAddedEvent) {
						MovieAddedEvent mae = (MovieAddedEvent) event;
						movie = Optional.of(new Movie(mae.getMovieId(), mae.getTitle()));
					} else if (event instanceof MovieRemovedEvent) {
						movie = Optional.empty();
					}
					return movie;
				})
				.reduce(Optional.empty(), (acc, cur) -> cur);
	}

	public void remove(Movie movie) {
		eventStore.publish(movieTopic,
				new MovieRemovedEvent(eventStore.nextIdentity(),
						movie.getId()));
	}
}
