package org.revolut.moneytransfer.resource;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.revolut.moneytransfer.exception.CustomExceptionMapper;

public class ApplicationConfig extends ResourceConfig {

	public ApplicationConfig() {

		register(CustomExceptionMapper.class);
		property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
		packages("org.revolut.moneytransfer");
	}
}