package eu.smartcampus.api.wrappers.rest;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import eu.smartcampus.api.IDatapointConnectivityService;

public class DatapointConnectivityServiceREST extends
        Application {

    public static IDatapointConnectivityService serviceImplementation;

    public DatapointConnectivityServiceREST(IDatapointConnectivityService service) {
        super();
        DatapointConnectivityServiceREST.serviceImplementation = service;
    }

    /**
     * Creates a root Restlet which will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        // Create a router Restlet that routes each call to a new instance of DatapointConnectivityServiceREST
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
        return router;
    }

}
