package eu.smartcampus.api.deviceconnectivity.impls.taguspark;

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
import eu.smartcampus.api.deviceconnectivity.impls.meterip.DatapointConnectivityServiceMeterIPDriver;
import eu.smartcampus.api.deviceconnectivity.osgi.registries.DeviceConnectivityServiceRegistry;

public final class Activator implements BundleActivator {

	private ServiceRegistration registration;

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		
		String metersFilename = "Settings_tagusparkMeters.json";
		String knxFilename = "Settings_tagusparkKNX.json";

		Map<DatapointAddress, DatapointMetadata> knxDatapoints = ServiceSettings
				.loadDatapointSettings(knxFilename);
		Map<DatapointAddress, DatapointMetadata> meterDatapoints = ServiceSettings
				.loadDatapointSettings(metersFilename);

		if (knxDatapoints == null) {
			System.err
					.println("KNX Datapoint settings file not exist. Generated "
							+ knxFilename + " with default settigns.");
			knxDatapoints = ServiceSettings
					.setDefaultKNXDatapoints(knxFilename);
		}
		if (meterDatapoints == null) {
			System.err
					.println("Meter Datapoint settings file not exist. Generated "
							+ metersFilename + " with default settigns.");
			meterDatapoints = ServiceSettings
					.setDefaultMetersDatapoints(metersFilename);
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

}
