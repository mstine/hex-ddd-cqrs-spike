package io.pivotal.spike.escqrs.eventstore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @author Matt Stine
 */
@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class Event {
	private final EventId id;
}
