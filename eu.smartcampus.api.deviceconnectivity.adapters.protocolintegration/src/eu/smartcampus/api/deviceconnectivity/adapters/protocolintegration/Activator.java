package eu.smartcampus.api.deviceconnectivity.adapters.protocolintegration;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import eu.smartcampus.api.deviceconnectivity.DatapointAddress;
import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;
import eu.smartcampus.api.deviceconnectivity.impls.knxip.DatapointConnectivityServiceKNXIPDriver;
import eu.smartcampus.api.deviceconnectivity.impls.meterip.DatapointConnectivityServiceMeterIPDriver;
import eu.smartcampus.api.deviceconnectivity.osgi.registries.DeviceConnectivityServiceRegistry;

public final class Activator implements BundleActivator {
	DatapointConnectivityServiceAdapter service;

	@Override
	public void start(BundleContext context) throws Exception {
		// create datapoints and load service drivers
		DatapointAddress a1 = new DatapointAddress("172.20.70.229");
		DatapointAddress a2 = new DatapointAddress("172.20.70.231");
		DatapointAddress a3 = new DatapointAddress("172.20.70.232");
		DatapointAddress a4 = new DatapointAddress("172.20.70.238");
		DatapointAddress a5 = new DatapointAddress("172.20.70.234");
		DatapointAddress a6 = new DatapointAddress("172.20.70.235");
		DatapointAddress a7 = new DatapointAddress("172.20.70.236");
		DatapointAddress a8 = new DatapointAddress("172.20.70.237");

		DatapointAddress d1 = new DatapointAddress("0-4-5");// energy lab
															// temperature
		DatapointAddress d2 = new DatapointAddress("0-3-0");// energy lab door
															// (write true to
															// open)
		DatapointAddress d3 = new DatapointAddress("0-1-0");// blackboard lamps
															// write (0-100)
		DatapointAddress d4 = new DatapointAddress("0-4-5");// blackboard lamps
															// read (0-100)
		DatapointAddress d5 = new DatapointAddress("0-2-12");// energylab blinds
																// write (0-100)
		DatapointAddress d6 = new DatapointAddress("0-2-0");// energylab blinds
															// read (0-100)

		// load services
		IDatapointConnectivityService knxDriver = DeviceConnectivityServiceRegistry
				.getInstance().getService(DatapointConnectivityServiceKNXIPDriver.class.getName());
		IDatapointConnectivityService meterDriver = DeviceConnectivityServiceRegistry
				.getInstance().getService(DatapointConnectivityServiceMeterIPDriver.class.getName());

		Map<DatapointAddress, IDatapointConnectivityService> datapointsDrivers = new HashMap<DatapointAddress, IDatapointConnectivityService>();
		datapointsDrivers.put(a1, meterDriver);
		datapointsDrivers.put(a2, meterDriver);
		datapointsDrivers.put(a3, meterDriver);
		datapointsDrivers.put(a4, meterDriver);
		datapointsDrivers.put(a5, meterDriver);
		datapointsDrivers.put(a6, meterDriver);
		datapointsDrivers.put(a7, meterDriver);
		datapointsDrivers.put(a8, meterDriver);
		datapointsDrivers.put(d1, knxDriver);
		datapointsDrivers.put(d2, knxDriver);
		datapointsDrivers.put(d3, knxDriver);
		datapointsDrivers.put(d4, knxDriver);
		datapointsDrivers.put(d5, knxDriver);
		datapointsDrivers.put(d6, knxDriver);

		if (service == null) {
			service = new DatapointConnectivityServiceAdapter(datapointsDrivers);
			DeviceConnectivityServiceRegistry.getInstance().addService(
					"DatapointConnectivityServiceAdapter", service);
		}

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		DeviceConnectivityServiceRegistry.getInstance().removeService("DatapointConnectivityServiceAdapter");
		service = null;

	}

}
