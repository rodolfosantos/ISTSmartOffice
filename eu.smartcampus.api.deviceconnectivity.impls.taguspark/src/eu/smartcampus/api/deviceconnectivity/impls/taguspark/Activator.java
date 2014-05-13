package eu.smartcampus.api.deviceconnectivity.impls.taguspark;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import eu.smartcampus.api.deviceconnectivity.DatapointAddress;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata.AccessType;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata.Datatype;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata.MetadataBuilder;
import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;
import eu.smartcampus.api.deviceconnectivity.adapters.protocolintegration.DatapointConnectivityServiceAdapter;
import eu.smartcampus.api.deviceconnectivity.impls.knxip.DatapointConnectivityServiceKNXIPDriver;
import eu.smartcampus.api.deviceconnectivity.impls.knxip.KNXGatewayIPDriver;
import eu.smartcampus.api.deviceconnectivity.impls.meterip.DatapointConnectivityServiceMeterIPDriver;
import eu.smartcampus.api.deviceconnectivity.osgi.registries.DeviceConnectivityServiceRegistry;

public final class Activator implements BundleActivator {

	private ServiceRegistration registration;

	@Override
	public void start(BundleContext context) throws Exception {
		// create datapoints and metadata
		// Initialize datapoints and drivers

		// meters datapoints
		MetadataBuilder meterMetadata = new DatapointMetadata.MetadataBuilder();
		meterMetadata.setAccessType(AccessType.READ_ONLY);
		meterMetadata.setDatatype(Datatype.STRING);

		Map<DatapointAddress, DatapointMetadata> meterDatapoints = new HashMap<DatapointAddress, DatapointMetadata>();
		DatapointAddress a1 = new DatapointAddress("172.20.70.229");
		DatapointAddress a2 = new DatapointAddress("172.20.70.231");
		DatapointAddress a3 = new DatapointAddress("172.20.70.232");
		DatapointAddress a4 = new DatapointAddress("172.20.70.238");
		DatapointAddress a5 = new DatapointAddress("172.20.70.234");
		DatapointAddress a6 = new DatapointAddress("172.20.70.235");
		DatapointAddress a7 = new DatapointAddress("172.20.70.236");
		DatapointAddress a8 = new DatapointAddress("172.20.70.237");

		meterDatapoints.put(a1, meterMetadata.build());
		meterDatapoints.put(a2, meterMetadata.build());
		meterDatapoints.put(a3, meterMetadata.build());
		meterDatapoints.put(a4, meterMetadata.build());
		meterDatapoints.put(a5, meterMetadata.build());
		meterDatapoints.put(a6, meterMetadata.build());
		meterDatapoints.put(a7, meterMetadata.build());
		meterDatapoints.put(a8, meterMetadata.build());

		IDatapointConnectivityService meterDriver = new DatapointConnectivityServiceMeterIPDriver(
				"root", "root", meterDatapoints);

		// knx datapoints
		Map<DatapointAddress, DatapointMetadata> knxDatapoints = new HashMap<DatapointAddress, DatapointMetadata>();

		MetadataBuilder knxMetadata1 = new DatapointMetadata.MetadataBuilder();
		knxMetadata1.setAccessType(AccessType.READ_ONLY);
		knxMetadata1.setDatatype(Datatype.INTEGER);
		knxMetadata1.setScale(2);
		DatapointAddress d1 = new DatapointAddress("0-4-5");// energy lab
															// temperature

		MetadataBuilder knxMetadata2 = new DatapointMetadata.MetadataBuilder();
		knxMetadata2.setAccessType(AccessType.WRITE_ONLY);
		knxMetadata2.setDatatype(Datatype.BOOLEAN);
		DatapointAddress d2 = new DatapointAddress("0-3-0");// energy lab door
															// (write true to
															// open)

		MetadataBuilder knxMetadata3 = new DatapointMetadata.MetadataBuilder();
		knxMetadata3.setAccessType(AccessType.WRITE_ONLY);
		knxMetadata3.setDatatype(Datatype.INTEGER);
		knxMetadata3.setScale(2);
		knxMetadata3.setDisplayMax(100);
		knxMetadata3.setDisplayMin(1);
		DatapointAddress d3 = new DatapointAddress("0-1-0");// blackboard lamps
															// write (0-100)

		MetadataBuilder knxMetadata4 = new DatapointMetadata.MetadataBuilder();
		knxMetadata4.setAccessType(AccessType.READ_ONLY);
		knxMetadata4.setDatatype(Datatype.INTEGER);
		knxMetadata4.setDisplayMax(100);
		knxMetadata4.setDisplayMin(1);
		knxMetadata4.setScale(2);
		DatapointAddress d4 = new DatapointAddress("0-7-1");// blackboard lamps
															// read (0-100)

		MetadataBuilder knxMetadata5 = new DatapointMetadata.MetadataBuilder();
		knxMetadata5.setAccessType(AccessType.WRITE_ONLY);
		knxMetadata5.setDatatype(Datatype.INTEGER);
		knxMetadata5.setScale(2);
		knxMetadata5.setDisplayMax(100);
		knxMetadata5.setDisplayMin(1);
		DatapointAddress d5 = new DatapointAddress("0-2-12");// energylab blinds
																// write (0-100)

		MetadataBuilder knxMetadata6 = new DatapointMetadata.MetadataBuilder();
		knxMetadata6.setAccessType(AccessType.READ_ONLY);
		knxMetadata6.setDatatype(Datatype.INTEGER);
		knxMetadata6.setDisplayMax(100);
		knxMetadata6.setDisplayMin(1);
		knxMetadata6.setScale(2);
		DatapointAddress d6 = new DatapointAddress("0-2-0");// energylab blinds
															// read (0-100)

		knxDatapoints.put(d1, knxMetadata1.build());
		knxDatapoints.put(d2, knxMetadata2.build());
		knxDatapoints.put(d3, knxMetadata3.build());
		knxDatapoints.put(d4, knxMetadata4.build());
		knxDatapoints.put(d5, knxMetadata5.build());
		knxDatapoints.put(d6, knxMetadata6.build());

		
		
		IDatapointConnectivityService knxDriver = new DatapointConnectivityServiceKNXIPDriver(
				knxDatapoints);
		
		Set<IDatapointConnectivityService> datapointsDrivers = new HashSet<IDatapointConnectivityService>();
		datapointsDrivers.add(meterDriver);
		
		KNXGatewayIPDriver.getInstance().start();		
		if(KNXGatewayIPDriver.getInstance().isConnected()){
			datapointsDrivers.add(knxDriver);
		}

		IDatapointConnectivityService driverAdapter = new DatapointConnectivityServiceAdapter(datapointsDrivers);
		DeviceConnectivityServiceRegistry.getInstance().addService(
				DatapointConnectivityServiceAdapter.class.getName(),
				driverAdapter);

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		registration.unregister();
		registration = null;
		KNXGatewayIPDriver.getInstance().stop();
	}

}
