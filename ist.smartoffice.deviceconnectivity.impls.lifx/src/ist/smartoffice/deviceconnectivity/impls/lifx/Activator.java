package ist.smartoffice.deviceconnectivity.impls.lifx;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import ist.smartoffice.deviceconnectivity.IDatapointConnectivityService;
import ist.smartoffice.deviceconnectivity.osgi.registries.DeviceConnectivityServiceRegistry;
import ist.smartoffice.logger.Logger;
import ist.smartoffice.logger.LoggerService;

public final class Activator implements BundleActivator {
	static private Logger log = LoggerService.getInstance().getLogger(Activator.class.getName()); 

	@Override
	public void start(BundleContext context) throws Exception {

		// Create service implementation
//		IDatapointConnectivityService serviceImpl = new DatapointConnectivityServiceLifxDriver();
//		// Publish Service
//		DeviceConnectivityServiceRegistry.getInstance().addService(
//				DatapointConnectivityServiceLifxDriver.class.getName(),
//				serviceImpl);
		log.i("Lifx Started!");

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		DeviceConnectivityServiceRegistry.getInstance().removeService(
				DatapointConnectivityServiceLifxDriver.class.getName());
		log.i("Lifx Stopped!");
	}

}
