package ist.smartoffice.datapointconnectivity.wrappers.rest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService;
import ist.smartoffice.datapointconnectivity.wrappers.rest.impl.DatapointConnectivityServiceResources;
import ist.smartoffice.logger.Logger;
import ist.smartoffice.logger.LoggerService;

/**
 * The Class DatapointConnectivityServiceRESTWrapper.
 */
public class DatapointConnectivityServiceRESTWrapper extends Application {

	static private Logger log = LoggerService.getInstance().getLogger(
			DatapointConnectivityServiceRESTWrapper.class.getName());

	/** The singleton instance. */
	private static DatapointConnectivityServiceRESTWrapper singletonInstance = new DatapointConnectivityServiceRESTWrapper();

	private static Map<String, IDatapointConnectivityService> serviceImplementations = new HashMap<String, IDatapointConnectivityService>();

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
	public void setServiceImplementation(String path,
			IDatapointConnectivityService serviceImplementation) {
		serviceImplementations.put(path, serviceImplementation);
	}
	
	/**
	 * Remove the service implementation.
	 * 
	 * @param serviceImplementation
	 *            the  service implementation
	 */
	public void removeServiceImplementation(String path) {
		serviceImplementations.remove(path);
	}

	/**
	 * Gets the service implementation.
	 * 
	 * @return the service implementation
	 */
	public IDatapointConnectivityService getServiceImplementation(String path) {
		Iterator<Entry<String, IDatapointConnectivityService>> it = serviceImplementations
				.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<String, IDatapointConnectivityService> entry = (Map.Entry<String, IDatapointConnectivityService>) it
					.next();

			if (path.contains(entry.getKey())) {
				return entry.getValue();
			}

		}
		return null;

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
		log.i("REST Wrapper Started!");
		return router;
	}

}
