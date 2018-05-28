package io.pivotal.spike.escqrs.movie;

import io.pivotal.spike.escqrs.support.Identity;
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
public class MovieId {
	private final Identity id;
}
