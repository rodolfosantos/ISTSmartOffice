package eu.smartcampus.api.rest.impl;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import eu.smartcampus.api.IDatapointConnectivityService;

public class DatapointConnectivityServiceREST extends Application {

	public static IDatapointConnectivityService service;

	public DatapointConnectivityServiceREST(
			IDatapointConnectivityService service) {
		super();
		DatapointConnectivityServiceREST.service = service;
	}

	/**
	 * Creates a root Restlet which will receive all incoming calls.
	 */
	@Override
	public synchronized Restlet createInboundRoot() {
		// Create a router Restlet that routes each call to a new instance of
		// HelloWorldResource.
		Router router = new Router(getContext());
		// GET
		router.attach("/datapoint/all", DatapointListingService.class);
		// GET -> read; PUT -> write
		router.attach(
				"/datapoint/{addr}",
				ReadWriteDatapointService.class);
		return router;
	}

}
