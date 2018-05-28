package io.pivotal.spike.escqrs.movie;

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
public class Movie {
	private final MovieId id;
	private final String title;
}
