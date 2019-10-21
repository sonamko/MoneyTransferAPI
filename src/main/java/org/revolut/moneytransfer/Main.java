package org.revolut.moneytransfer;

import org.revolut.moneytransfer.exception.CustomExceptionMapper;
import org.revolut.moneytransfer.resource.TransactionResource;
import org.revolut.moneytransfer.resource.UserAccountResource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main {

	public static void main(String[] args) throws Exception {
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");

		Server jettyServer = new Server(8080);
		jettyServer.setHandler(context);

		ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
		jerseyServlet.setInitOrder(0);

		jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",
				UserAccountResource.class.getCanonicalName() + "," + CustomExceptionMapper.class.getCanonicalName()
						+ "," + TransactionResource.class.getCanonicalName());

		try {
			jettyServer.start();
			jettyServer.join();
		} finally {
			jettyServer.destroy();
		}
	}

}
