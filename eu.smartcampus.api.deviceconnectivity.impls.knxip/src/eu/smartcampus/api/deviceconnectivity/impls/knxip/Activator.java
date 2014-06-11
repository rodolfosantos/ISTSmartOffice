package eu.smartcampus.api.deviceconnectivity.impls.knxip;



import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;
import eu.smartcampus.api.deviceconnectivity.osgi.registries.DeviceConnectivityServiceRegistry;
import eu.smartcampus.api.logger.Logger;
import eu.smartcampus.api.logger.LoggerService;

public final class Activator implements BundleActivator {
	static private Logger log = LoggerService.getInstance().getLogger(Activator.class.getName());

	@Override
	public void start(final BundleContext context) throws Exception {

		IDatapointConnectivityService serviceImpl = new DatapointConnectivityServiceKNXIPDriver();
		// Publish Service
		DeviceConnectivityServiceRegistry.getInstance().addService(
				DatapointConnectivityServiceKNXIPDriver.class.getName(),
				serviceImpl);
		log.i("KNX Started!");

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		DeviceConnectivityServiceRegistry.getInstance().removeService(
				DatapointConnectivityServiceKNXIPDriver.class.getName());
		log.i("KNX Stopped!");
	}

}
