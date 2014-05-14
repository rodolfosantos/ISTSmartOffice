package eu.smartcampus.api.deviceconnectivity.impls.taguspark;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
		String metersFilename = "tagusparkMeters.json";
		String knxFilename = "tagusparkKNX.json";

		// // meters datapoints
		// MetadataBuilder meterMetadata = new
		// DatapointMetadata.MetadataBuilder();
		// meterMetadata.setAccessType(AccessType.READ_ONLY);
		// meterMetadata.setDatatype(Datatype.STRING);
		//
		// Map<DatapointAddress, DatapointMetadata> meterDatapoints = new
		// HashMap<DatapointAddress, DatapointMetadata>();
		// DatapointAddress a1 = new DatapointAddress("172.20.70.229");
		// DatapointAddress a2 = new DatapointAddress("172.20.70.231");
		// DatapointAddress a3 = new DatapointAddress("172.20.70.232");
		// DatapointAddress a4 = new DatapointAddress("172.20.70.238");
		// DatapointAddress a5 = new DatapointAddress("172.20.70.234");
		// DatapointAddress a6 = new DatapointAddress("172.20.70.235");
		// DatapointAddress a7 = new DatapointAddress("172.20.70.236");
		// DatapointAddress a8 = new DatapointAddress("172.20.70.237");
		//
		// meterMetadata.setAlias("D14");
		// meterDatapoints.put(a1, meterMetadata.build());
		// meterMetadata.setAlias("A4");
		// meterDatapoints.put(a2, meterMetadata.build());
		// meterMetadata.setAlias("Library");
		// meterDatapoints.put(a3, meterMetadata.build());
		// meterMetadata.setAlias("D16");
		// meterDatapoints.put(a4, meterMetadata.build());
		// meterMetadata.setAlias("R117");
		// meterDatapoints.put(a5, meterMetadata.build());
		// meterMetadata.setAlias("R119");
		// meterDatapoints.put(a6, meterMetadata.build());
		// meterMetadata.setAlias("Lab1-58");
		// meterDatapoints.put(a7, meterMetadata.build());
		// meterMetadata.setAlias("UTA_A4");
		// meterDatapoints.put(a8, meterMetadata.build());
		//
		// IDatapointConnectivityService meterDriver = new
		// DatapointConnectivityServiceMeterIPDriver(
		// "root", "root", meterDatapoints);
		//
		// // knx datapoints
		// Map<DatapointAddress, DatapointMetadata> knxDatapoints = new
		// HashMap<DatapointAddress, DatapointMetadata>();
		//
		// MetadataBuilder knxMetadata1 = new
		// DatapointMetadata.MetadataBuilder();
		// knxMetadata1.setAlias("EnergyLab Temperature");
		// knxMetadata1.setAccessType(AccessType.READ_ONLY);
		// knxMetadata1.setDatatype(Datatype.INTEGER);
		// knxMetadata1.setScale(2);
		// DatapointAddress d1 = new DatapointAddress("0-4-5");// energy lab
		// // temperature
		//
		// MetadataBuilder knxMetadata2 = new
		// DatapointMetadata.MetadataBuilder();
		// knxMetadata2.setAlias("EnergyLab Door");
		// knxMetadata2.setAccessType(AccessType.WRITE_ONLY);
		// knxMetadata2.setDatatype(Datatype.BOOLEAN);
		// DatapointAddress d2 = new DatapointAddress("0-3-0");// energy lab
		// door
		// // (write true to
		// // open)
		//
		// MetadataBuilder knxMetadata3 = new
		// DatapointMetadata.MetadataBuilder();
		// knxMetadata3.setAlias("EnergyLab BlackboardLamps (write)");
		// knxMetadata3.setAccessType(AccessType.WRITE_ONLY);
		// knxMetadata3.setDatatype(Datatype.INTEGER);
		// knxMetadata3.setScale(2);
		// knxMetadata3.setDisplayMax(100);
		// knxMetadata3.setDisplayMin(1);
		// DatapointAddress d3 = new DatapointAddress("0-1-0");// blackboard
		// lamps
		// // write (0-100)
		//
		// MetadataBuilder knxMetadata4 = new
		// DatapointMetadata.MetadataBuilder();
		// knxMetadata4.setAlias("EnergyLab BlackboardLamps (read)");
		// knxMetadata4.setAccessType(AccessType.READ_ONLY);
		// knxMetadata4.setDatatype(Datatype.INTEGER);
		// knxMetadata4.setDisplayMax(100);
		// knxMetadata4.setDisplayMin(1);
		// knxMetadata4.setScale(2);
		// DatapointAddress d4 = new DatapointAddress("0-7-1");// blackboard
		// lamps
		// // read (0-100)
		//
		// MetadataBuilder knxMetadata5 = new
		// DatapointMetadata.MetadataBuilder();
		// knxMetadata5.setAlias("EnergyLab Blinds (write)");
		// knxMetadata5.setAccessType(AccessType.WRITE_ONLY);
		// knxMetadata5.setDatatype(Datatype.INTEGER);
		// knxMetadata5.setScale(2);
		// knxMetadata5.setDisplayMax(100);
		// knxMetadata5.setDisplayMin(1);
		// DatapointAddress d5 = new DatapointAddress("0-2-12");// energylab
		// blinds
		// // write (0-100)
		//
		// MetadataBuilder knxMetadata6 = new
		// DatapointMetadata.MetadataBuilder();
		// knxMetadata6.setAlias("EnergyLab Blinds (read)");
		// knxMetadata6.setAccessType(AccessType.READ_ONLY);
		// knxMetadata6.setDatatype(Datatype.INTEGER);
		// knxMetadata6.setDisplayMax(100);
		// knxMetadata6.setDisplayMin(1);
		// knxMetadata6.setScale(2);
		// DatapointAddress d6 = new DatapointAddress("0-2-0");// energylab
		// blinds
		// // read (0-100)
		//
		// knxDatapoints.put(d1, knxMetadata1.build());
		// knxDatapoints.put(d2, knxMetadata2.build());
		// knxDatapoints.put(d3, knxMetadata3.build());
		// knxDatapoints.put(d4, knxMetadata4.build());
		// knxDatapoints.put(d5, knxMetadata5.build());
		// knxDatapoints.put(d6, knxMetadata6.build());
		//

		// load datapoins
		Map<String, DatapointMetadata> knxDatapointsSettings = (Map<String, DatapointMetadata>) fromJsonFile(
				knxFilename, new TypeToken<Map<String, DatapointMetadata>>() {
				}.getType());
		
		Map<String, DatapointMetadata> meterDatapointsSettings = (Map<String, DatapointMetadata>) fromJsonFile(
				metersFilename, new TypeToken<Map<String, DatapointMetadata>>() {
				}.getType());
		
		Map<DatapointAddress, DatapointMetadata> knxDatapoints = new HashMap<DatapointAddress, DatapointMetadata>();
		Iterator<Entry<String, DatapointMetadata>> knxIt = knxDatapointsSettings.entrySet().iterator();
		while (knxIt.hasNext()) {
			Map.Entry<java.lang.String, eu.smartcampus.api.deviceconnectivity.DatapointMetadata> entry = (Map.Entry<java.lang.String, eu.smartcampus.api.deviceconnectivity.DatapointMetadata>) knxIt
					.next();
			knxDatapoints.put(new DatapointAddress(entry.getKey()), entry.getValue());			
		}
		
		Map<DatapointAddress, DatapointMetadata> meterDatapoints = new HashMap<DatapointAddress, DatapointMetadata>();
		Iterator<Entry<String, DatapointMetadata>> meterIt = meterDatapointsSettings.entrySet().iterator();
		while (meterIt.hasNext()) {
			Map.Entry<java.lang.String, eu.smartcampus.api.deviceconnectivity.DatapointMetadata> entry = (Map.Entry<java.lang.String, eu.smartcampus.api.deviceconnectivity.DatapointMetadata>) meterIt
					.next();
			meterDatapoints.put(new DatapointAddress(entry.getKey()), entry.getValue());			
		}

		IDatapointConnectivityService knxDriver = new DatapointConnectivityServiceKNXIPDriver(
				knxDatapoints);
		IDatapointConnectivityService meterDriver = new DatapointConnectivityServiceMeterIPDriver(
				"root", "root", meterDatapoints);

		Set<IDatapointConnectivityService> datapointsDrivers = new HashSet<IDatapointConnectivityService>();
		datapointsDrivers.add(meterDriver);

		KNXGatewayIPDriver.getInstance().start();
		if (KNXGatewayIPDriver.getInstance().isConnected()) {
			datapointsDrivers.add(knxDriver);
		}

		IDatapointConnectivityService driverAdapter = new DatapointConnectivityServiceAdapter(
				datapointsDrivers);
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

	private void toJsonFile(String filename, Object object) {
		Gson gson = new Gson();
		String json = gson.toJson(object);

		try {
			FileWriter writer = new FileWriter(filename);
			writer.write(json);
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private Object fromJsonFile(String filename, Type objectType) {
		Gson gson = new Gson();

		try {

			BufferedReader br = new BufferedReader(new FileReader(filename));

			Object result = gson.fromJson(br, objectType);
			return result;

		} catch (IOException e) {
			return null;
		}
	}

	private Object fromJsonFile(String filename, Class classType) {
		Gson gson = new Gson();
		try {

			BufferedReader br = new BufferedReader(new FileReader(filename));
			Object result = gson.fromJson(br, classType);
			return result;

		} catch (IOException e) {
			return null;
		}
	}

}
