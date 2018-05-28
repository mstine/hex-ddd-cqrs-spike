package io.pivotal.spike.escqrs.support;

import lombok.*;

/**
 * @author Matt Stine
 */
@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class Identity {
	private final String id;
}
