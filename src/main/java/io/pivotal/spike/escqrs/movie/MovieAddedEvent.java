package io.pivotal.spike.escqrs.movie;

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
class MovieAddedEvent extends MovieEvent {
	private final String title;

	MovieAddedEvent(EventId id, MovieId movieId, String title) {
		super(id, movieId);
		this.title = title;
	}
}
