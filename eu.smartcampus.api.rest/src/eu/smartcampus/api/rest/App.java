package eu.smartcampus.api.rest;



import java.util.HashMap;
import java.util.Map;

import org.restlet.Component;
import org.restlet.data.Protocol;

import eu.smartcampus.api.DatapointAddress;
import eu.smartcampus.api.DatapointMetadata;
import eu.smartcampus.api.DatapointMetadata.AccessType;
import eu.smartcampus.api.DatapointMetadata.Datatype;
import eu.smartcampus.api.DatapointMetadata.MetadataBuilder;
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

        
        //Initialize datapoints and driver
        
        MetadataBuilder metadata = new DatapointMetadata.MetadataBuilder();
        metadata.setAccessType(AccessType.READ_ONLY);
        metadata.setDatatype(Datatype.STRING);
        Map<DatapointAddress, DatapointMetadata> datapoints = new HashMap<DatapointAddress, DatapointMetadata>();
        datapoints.put(new DatapointAddress("172.20.70.229"), metadata.build());
        datapoints.put(new DatapointAddress("172.20.70.231"), metadata.build());
        datapoints.put(new DatapointAddress("172.20.70.232"), metadata.build());
        datapoints.put(new DatapointAddress("172.20.70.238"), metadata.build());
        datapoints.put(new DatapointAddress("172.20.70.234"), metadata.build());
        datapoints.put(new DatapointAddress("172.20.70.235"), metadata.build());
        datapoints.put(new DatapointAddress("172.20.70.237"), metadata.build());
        datapoints.put(new DatapointAddress("172.20.70.236"), metadata.build());
        
        MeterDriverIP driver = new MeterDriverIP("root", "root");
        IDatapointConnectivityService deviceapi = new DatapointConnectivityServiceImpl(driver, datapoints);


        // Attach device api application
        component.getDefaultHost().attach("/deviceapi",
                new DatapointConnectivityServiceREST(deviceapi));

        // Start the component.  
        component.start();
    }
}
