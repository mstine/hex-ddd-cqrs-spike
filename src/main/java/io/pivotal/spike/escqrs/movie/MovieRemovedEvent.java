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
class MovieRemovedEvent extends MovieEvent {

	MovieRemovedEvent(EventId id, MovieId movieId) {
		super(id, movieId);
	}
}
