package eu.smartcampus.api.deviceconnectivity.impls.taguspark;

import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import eu.smartcampus.api.deviceconnectivity.DatapointAddress;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata;
import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;
import eu.smartcampus.api.deviceconnectivity.adapters.protocolintegration.DatapointConnectivityServiceAdapter;
import eu.smartcampus.api.deviceconnectivity.impls.knxip.DatapointConnectivityServiceKNXIPDriver;
import eu.smartcampus.api.deviceconnectivity.impls.knxip.KNXGatewayIPDriver;
import eu.smartcampus.api.deviceconnectivity.impls.lifx.DatapointConnectivityServiceLifxDriver;
import eu.smartcampus.api.deviceconnectivity.impls.meterip.DatapointConnectivityServiceMeterIPDriver;
import eu.smartcampus.api.deviceconnectivity.osgi.registries.DeviceConnectivityServiceRegistry;

public final class Activator implements BundleActivator {

	//private ServiceRegistration registration;
	private Thread activatorThread;

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		
		final String metersFilename = "Settings_tagusparkMeters.json";
		final String knxFilename = "Settings_tagusparkKNX.json";

		Runnable activatorJob = new Runnable() {
			
			@Override
			public void run() {
				
				Map<DatapointAddress, DatapointMetadata> knxDatapoints = ServiceSettings
						.loadDatapointSettings(knxFilename);
				Map<DatapointAddress, DatapointMetadata> meterDatapoints = ServiceSettings
						.loadDatapointSettings(metersFilename);

				
				IDatapointConnectivityService knxDriver = new DatapointConnectivityServiceKNXIPDriver(
						knxDatapoints);
				IDatapointConnectivityService meterDriver = new DatapointConnectivityServiceMeterIPDriver(
						"root", "root", meterDatapoints);

				Set<IDatapointConnectivityService> datapointsDrivers = new HashSet<IDatapointConnectivityService>();
				datapointsDrivers.add(meterDriver);
				
				//TODO add lifx driver
				datapointsDrivers.add(new DatapointConnectivityServiceLifxDriver());

				try {
					KNXGatewayIPDriver.getInstance().start();
					if (KNXGatewayIPDriver.getInstance().isConnected()) {
						datapointsDrivers.add(knxDriver);
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}				

				IDatapointConnectivityService driverAdapter = new DatapointConnectivityServiceAdapter(
						datapointsDrivers);
				DeviceConnectivityServiceRegistry.getInstance().addService(
						DatapointConnectivityServiceAdapter.class.getName(),
						driverAdapter);
				
				
			}
		};
		
		activatorThread = new Thread(activatorJob);
		activatorThread.start();		

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		DeviceConnectivityServiceRegistry.getInstance().removeService(DatapointConnectivityServiceAdapter.class.getName());
		System.out.println("unregistedes tagus");
		
		if(KNXGatewayIPDriver.getInstance().isConnected())
			KNXGatewayIPDriver.getInstance().stop();
		
		activatorThread.join();
	}

}
