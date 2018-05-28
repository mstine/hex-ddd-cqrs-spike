package io.pivotal.spike.escqrs.support;

import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * @author Matt Stine
 */
public class IdentityGeneratorTest {
	@Test
	public void returnsNextIdentity() {
		IdentityGenerator identityGenerator = new RandomUUIDIdentityGenerator();
		Identity eventId = identityGenerator.nextIdentity();
		assertThat(eventId).isNotNull();

		String uuidString = eventId.getId();
		assertThatCode(() -> UUID.fromString(uuidString)).doesNotThrowAnyException();
	}
}
