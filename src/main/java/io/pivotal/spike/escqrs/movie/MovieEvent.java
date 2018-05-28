package io.pivotal.spike.escqrs.movie;

import io.pivotal.spike.escqrs.eventstore.Event;
import io.pivotal.spike.escqrs.eventstore.EventId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Matt Stine
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
class MovieEvent extends Event {
	private final MovieId movieId;

	MovieEvent(EventId id, MovieId movieId) {
		super(id);
		this.movieId = movieId;
	}
}
