package ist.smartoffice.datapointconnectivity.impls.knxip;



import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService;
import ist.smartoffice.datapointconnectivity.osgi.registries.DatapointConnectivityServiceRegistry;
import ist.smartoffice.logger.Logger;
import ist.smartoffice.logger.LoggerService;

public final class Activator implements BundleActivator {
	static private Logger log = LoggerService.getInstance().getLogger(Activator.class.getName());

	@Override
	public void start(final BundleContext context) throws Exception {

		IDatapointConnectivityService serviceImpl = new DatapointConnectivityServiceKNXIPDriver();
		// Publish Service
		DatapointConnectivityServiceRegistry.getInstance().addService(
				DatapointConnectivityServiceKNXIPDriver.class.getName(),
				serviceImpl);
		log.i("KNX Started!");

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		DatapointConnectivityServiceRegistry.getInstance().removeService(
				DatapointConnectivityServiceKNXIPDriver.class.getName());
		log.i("KNX Stopped!");
	}

}
