package eu.smartcampus.api.rest;



import org.restlet.Component;
import org.restlet.data.Protocol;

import eu.smartcampus.api.IDatapointConnectivityService;
import eu.smartcampus.api.rest.deviceapi.impl.DatapointConnectivityServiceImpl;
import eu.smartcampus.api.rest.deviceapi.impl.MeterDriverIP;
import eu.smartcampus.api.rest.impl.DatapointConnectivityServiceREST;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {
        // Create a new Component.  
        Component component = new Component();

        // Add a new HTTP server listening on port 8182.  
        component.getServers().add(Protocol.HTTP, 8182);

        // Attach device api application
        MeterDriverIP driver = new MeterDriverIP("root", "root");
        IDatapointConnectivityService deviceapi = new DatapointConnectivityServiceImpl(driver);
        component.getDefaultHost().attach("/", new DatapointConnectivityServiceREST(deviceapi));

        // Start the component.  
        component.start();
    }
}
