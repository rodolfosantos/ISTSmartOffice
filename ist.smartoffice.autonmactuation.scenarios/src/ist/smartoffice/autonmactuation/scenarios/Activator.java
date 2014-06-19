package ist.smartoffice.autonmactuation.scenarios;

import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService;
import ist.smartoffice.datapointconnectivity.osgi.registries.DatapointConnectivityServiceRegistry;
import ist.smartoffice.logger.Logger;
import ist.smartoffice.logger.LoggerService;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public final class Activator implements BundleActivator {

	private static final String PROVIDED_SERVICE_NAME = "ist.smartoffice.autonmactuation.scenarios.ScenariosService";

	static private Logger log = LoggerService.getInstance().getLogger(
			Activator.class.getName());

	IDatapointConnectivityService serviceImpl = null;

	@Override
	public void start(BundleContext context) throws Exception {
		
		serviceImpl = new ScenariosService();
		DatapointConnectivityServiceRegistry.getInstance().addService(PROVIDED_SERVICE_NAME, serviceImpl);

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		DatapointConnectivityServiceRegistry.getInstance().removeService(PROVIDED_SERVICE_NAME);
		serviceImpl = null;
	}

}
