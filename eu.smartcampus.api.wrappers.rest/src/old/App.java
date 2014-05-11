package old;
//package eu.smartcampus.api.rest;
//
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.restlet.Component;
//import org.restlet.data.Protocol;
//
//import eu.smartcampus.api.DatapointAddress;
//import eu.smartcampus.api.DatapointMetadata;
//import eu.smartcampus.api.DatapointMetadata.AccessType;
//import eu.smartcampus.api.DatapointMetadata.Datatype;
//import eu.smartcampus.api.DatapointMetadata.MetadataBuilder;
//import eu.smartcampus.api.IDatapointConnectivityService;
//import eu.smartcampus.api.rest.impl.DatapointConnectivityServiceREST;
//
//
//public class App {
//    public static void main(String[] args) throws Exception {
//        // Create a new Component.  
//        Component component = new Component();
//
//        // Add a new HTTP server listening on port 8182.  
//        component.getServers().add(Protocol.HTTP, 8182);
//
//
//        //Initialize datapoints and drivers
//
//        
//        //meters datapoints
//        MetadataBuilder meterMetadata = new DatapointMetadata.MetadataBuilder();
//        meterMetadata.setAccessType(AccessType.READ_ONLY);
//        meterMetadata.setDatatype(Datatype.STRING);
//        
//        Map<DatapointAddress, DatapointMetadata> meterDatapoints = new HashMap<DatapointAddress, DatapointMetadata>();
//        DatapointAddress a1 = new DatapointAddress("172.20.70.229");
//        DatapointAddress a2 = new DatapointAddress("172.20.70.231");
//        DatapointAddress a3 = new DatapointAddress("172.20.70.232");
//        DatapointAddress a4 = new DatapointAddress("172.20.70.238");
//        DatapointAddress a5 = new DatapointAddress("172.20.70.234");
//        DatapointAddress a6 = new DatapointAddress("172.20.70.235");
//        DatapointAddress a7 = new DatapointAddress("172.20.70.236");
//        DatapointAddress a8 = new DatapointAddress("172.20.70.237");
//
//        meterDatapoints.put(a1, meterMetadata.build());
//        meterDatapoints.put(a2, meterMetadata.build());
//        meterDatapoints.put(a3, meterMetadata.build());
//        meterDatapoints.put(a4, meterMetadata.build());
//        meterDatapoints.put(a5, meterMetadata.build());
//        meterDatapoints.put(a6, meterMetadata.build());
//        meterDatapoints.put(a7, meterMetadata.build());
//        meterDatapoints.put(a8, meterMetadata.build());
//
//
//        IDatapointConnectivityService meterDriver = new DatapointConnectivityServiceMeterIPDriver(
//                meterDatapoints, "root", "root");
//        
//        //knx datapoints
//        Map<DatapointAddress, DatapointMetadata> knxDatapoints = new HashMap<DatapointAddress, DatapointMetadata>();
//        
//        MetadataBuilder knxMetadata1 = new DatapointMetadata.MetadataBuilder();
//        knxMetadata1.setAccessType(AccessType.READ_ONLY);
//        knxMetadata1.setDatatype(Datatype.INTEGER);
//        knxMetadata1.setUnits("ºC");
//        DatapointAddress d1 = new DatapointAddress("0-4-5");//energy lab temperature
//        
//        MetadataBuilder knxMetadata2 = new DatapointMetadata.MetadataBuilder();
//        knxMetadata2.setAccessType(AccessType.WRITE_ONLY);
//        knxMetadata2.setDatatype(Datatype.BOOLEAN);
//        DatapointAddress d2 = new DatapointAddress("0-3-0");//energy lab door (write true to open) 
//        
//        MetadataBuilder knxMetadata3 = new DatapointMetadata.MetadataBuilder();
//        knxMetadata3.setAccessType(AccessType.WRITE_ONLY);
//        knxMetadata3.setDatatype(Datatype.INTEGER);
//        knxMetadata3.setDisplayMax(100);
//        knxMetadata3.setDisplayMin(1);
//        DatapointAddress d3 = new DatapointAddress("0-1-0");//blackboard lamps write (0-100)
//        
//        MetadataBuilder knxMetadata4 = new DatapointMetadata.MetadataBuilder();
//        knxMetadata4.setAccessType(AccessType.READ_ONLY);
//        knxMetadata4.setDatatype(Datatype.INTEGER);
//        knxMetadata4.setDisplayMax(100);
//        knxMetadata4.setDisplayMin(1);//TODO fix default int value of metadata (0)
//        DatapointAddress d4 = new DatapointAddress("0-7-1");//blackboard lamps read (0-100)
//        
//        MetadataBuilder knxMetadata5 = new DatapointMetadata.MetadataBuilder();
//        knxMetadata5.setAccessType(AccessType.WRITE_ONLY);
//        knxMetadata5.setDatatype(Datatype.INTEGER);
//        knxMetadata5.setDisplayMax(100);
//        knxMetadata5.setDisplayMin(1);
//        DatapointAddress d5 = new DatapointAddress("0-2-12");//energylab blinds write (0-100)
//        
//        MetadataBuilder knxMetadata6 = new DatapointMetadata.MetadataBuilder();
//        knxMetadata6.setAccessType(AccessType.READ_ONLY);
//        knxMetadata6.setDatatype(Datatype.INTEGER);
//        knxMetadata6.setDisplayMax(100);
//        knxMetadata6.setDisplayMin(1);
//        DatapointAddress d6 = new DatapointAddress("0-2-0");//energylab blinds read (0-100)
//        
//        knxDatapoints.put(d1, knxMetadata1.build());
//        knxDatapoints.put(d2, knxMetadata2.build());
//        knxDatapoints.put(d3, knxMetadata3.build());
//        knxDatapoints.put(d4, knxMetadata4.build());
//        knxDatapoints.put(d5, knxMetadata5.build());
//        knxDatapoints.put(d6, knxMetadata6.build());
//        
//        KNXGatewayIPDriver knxGatewayDriver = new KNXGatewayIPDriver("172.20.70.147");
//        //knxGatewayDriver.start(); //TODO you only can start this in the same KNXGateway subnet
//        //knxGatewayDriver.writeBool("0-3-0", true);// be careful, this line opens de energy lab door
//        
//        IDatapointConnectivityService knxDriver = new DatapointConnectivityServiceKNXIPDriver(knxGatewayDriver, knxDatapoints);       
//
//        Map<DatapointAddress, IDatapointConnectivityService> datapointsDrivers = new HashMap<DatapointAddress, IDatapointConnectivityService>();
//        datapointsDrivers.put(a1, meterDriver);
//        datapointsDrivers.put(a2, meterDriver);
//        datapointsDrivers.put(a3, meterDriver);
//        datapointsDrivers.put(a4, meterDriver);
//        datapointsDrivers.put(a5, meterDriver);
//        datapointsDrivers.put(a6, meterDriver);
//        datapointsDrivers.put(a7, meterDriver);
//        
//        datapointsDrivers.put(d1, knxDriver);
//        datapointsDrivers.put(d2, knxDriver);
//        datapointsDrivers.put(d3, knxDriver);
//        datapointsDrivers.put(d4, knxDriver);
//        datapointsDrivers.put(d5, knxDriver);
//        datapointsDrivers.put(d6, knxDriver);
//
//
//        IDatapointConnectivityService adapter = new DatapointConnectivityServiceAdapter(
//                datapointsDrivers);
//
//
//        // Attach device api application
//        component.getDefaultHost().attach("/deviceapi",
//                new DatapointConnectivityServiceREST(adapter));
//
//        // Start the component.  
//        component.start();
//    }
//}
