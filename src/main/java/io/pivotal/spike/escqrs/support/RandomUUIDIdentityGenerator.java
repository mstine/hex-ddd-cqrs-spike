package io.pivotal.spike.escqrs.support;

import java.util.UUID;

/**
 * @author Matt Stine
 */
public class RandomUUIDIdentityGenerator implements IdentityGenerator {
	@Override
	public Identity nextIdentity() {
		return new Identity(UUID.randomUUID().toString().toUpperCase());
	}
}
