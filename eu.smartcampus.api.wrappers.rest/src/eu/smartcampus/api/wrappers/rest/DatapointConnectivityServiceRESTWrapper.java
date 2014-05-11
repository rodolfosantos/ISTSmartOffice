package eu.smartcampus.api.wrappers.rest;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import eu.smartcampus.api.IDatapointConnectivityService;
import eu.smartcampus.api.wrappers.rest.impl.DatapointConnectivityServiceResources;

/**
 * The Class DatapointConnectivityServiceRESTWrapper.
 */
public class DatapointConnectivityServiceRESTWrapper extends Application {

	/** The service implementation. */
	private static IDatapointConnectivityService serviceImplementation;

	/** The singleton instance. */
	private static DatapointConnectivityServiceRESTWrapper singletonInstance = new DatapointConnectivityServiceRESTWrapper();

	/**
	 * Instantiates a new datapoint connectivity service rest wrapper.
	 */
	private DatapointConnectivityServiceRESTWrapper() {
		super();
	}

	/**
	 * Sets the service implementation.
	 * 
	 * @param serviceImplementation
	 *            the new service implementation
	 */
	public void setServiceImplementation(
			IDatapointConnectivityService serviceImplementation) {
		DatapointConnectivityServiceRESTWrapper.serviceImplementation = serviceImplementation;
	}

	/**
	 * Gets the service implementation.
	 * 
	 * @return the service implementation
	 */
	public IDatapointConnectivityService getServiceImplementation() {
		return serviceImplementation;
	}

	/**
	 * Gets the single instance of DatapointConnectivityServiceRESTWrapper.
	 * 
	 * @return single instance of DatapointConnectivityServiceRESTWrapper
	 */
	public static DatapointConnectivityServiceRESTWrapper getInstance() {
		return singletonInstance;
	}

	/**
	 * Creates a root Restlet which will receive all incoming calls.
	 * 
	 * @return the routing schema
	 */
	@Override
	public synchronized Restlet createInboundRoot() {
		final Router router = new Router(getContext());
		// GET
		router.attach(
				"/datapoints",
				DatapointConnectivityServiceResources.DatapointListingResource.class);
		// GET -> read; PUT -> write
		router.attach(
				"/datapoints/{addr}",
				DatapointConnectivityServiceResources.ReadWriteDatapointResource.class);
		// GET
		router.attach(
				"/datapoints/{addr}/metadata",
				DatapointConnectivityServiceResources.GetDatapointMetadataResource.class);
		// GET
		router.attach("/datapoints/{addr}/{start}/{finish}",
				DatapointConnectivityServiceResources.ReadDatapointWindow.class);
		System.out.println("Started!!!!");
		return router;
	}

}
