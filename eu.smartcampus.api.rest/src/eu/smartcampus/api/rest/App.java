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
import eu.smartcampus.api.rest.deviceapi.impl.DatapointConnectivityServiceAdapter;
import eu.smartcampus.api.rest.deviceapi.impl.DatapointConnectivityServiceMeterDriverIP;
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


        //Initialize datapoints and drivers

        MetadataBuilder metadata = new DatapointMetadata.MetadataBuilder();
        metadata.setAccessType(AccessType.READ_ONLY);
        metadata.setDatatype(Datatype.STRING);
        
        Map<DatapointAddress, DatapointMetadata> datapoints = new HashMap<DatapointAddress, DatapointMetadata>();
        DatapointAddress a1 = new DatapointAddress("172.20.70.229");
        DatapointAddress a2 = new DatapointAddress("172.20.70.231");
        DatapointAddress a3 = new DatapointAddress("172.20.70.232");
        DatapointAddress a4 = new DatapointAddress("172.20.70.238");
        DatapointAddress a5 = new DatapointAddress("172.20.70.234");
        DatapointAddress a6 = new DatapointAddress("172.20.70.235");
        DatapointAddress a7 = new DatapointAddress("172.20.70.236");
        DatapointAddress a8 = new DatapointAddress("172.20.70.237");

        datapoints.put(a1, metadata.build());
        datapoints.put(a2, metadata.build());
        datapoints.put(a3, metadata.build());
        datapoints.put(a4, metadata.build());
        datapoints.put(a5, metadata.build());
        datapoints.put(a6, metadata.build());
        datapoints.put(a7, metadata.build());
        datapoints.put(a8, metadata.build());


        IDatapointConnectivityService meterDriver = new DatapointConnectivityServiceMeterDriverIP(
                datapoints, "root", "root");

        Map<DatapointAddress, IDatapointConnectivityService> datapointsDrivers = new HashMap<DatapointAddress, IDatapointConnectivityService>();
        datapointsDrivers.put(a1, meterDriver);
        datapointsDrivers.put(a2, meterDriver);
        datapointsDrivers.put(a3, meterDriver);
        datapointsDrivers.put(a4, meterDriver);
        datapointsDrivers.put(a5, meterDriver);
        datapointsDrivers.put(a6, meterDriver);
        datapointsDrivers.put(a7, meterDriver);


        IDatapointConnectivityService adapter = new DatapointConnectivityServiceAdapter(
                datapointsDrivers);


        // Attach device api application
        component.getDefaultHost().attach("/deviceapi",
                new DatapointConnectivityServiceREST(adapter));

        // Start the component.  
        component.start();
    }
}
