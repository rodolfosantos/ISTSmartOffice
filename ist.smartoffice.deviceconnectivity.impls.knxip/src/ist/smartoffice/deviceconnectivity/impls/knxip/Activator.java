package ist.smartoffice.deviceconnectivity.impls.knxip;



import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import ist.smartoffice.deviceconnectivity.IDatapointConnectivityService;
import ist.smartoffice.deviceconnectivity.osgi.registries.DeviceConnectivityServiceRegistry;
import ist.smartoffice.logger.Logger;
import ist.smartoffice.logger.LoggerService;

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
