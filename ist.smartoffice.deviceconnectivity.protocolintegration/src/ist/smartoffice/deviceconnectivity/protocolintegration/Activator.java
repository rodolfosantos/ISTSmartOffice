package ist.smartoffice.deviceconnectivity.protocolintegration;

import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService;
import ist.smartoffice.datapointconnectivity.osgi.registries.DatapointConnectivityServiceRegistry;
import ist.smartoffice.logger.Logger;
import ist.smartoffice.logger.LoggerService;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public final class Activator implements BundleActivator {

	public static final String PROVIDED_SERVICE_NAME = "ist.smartoffice.deviceconnectivity.protocolintegration.DatapointConnectivityServiceAdapter";

	static private Logger log = LoggerService.getInstance().getLogger(
			Activator.class.getName());

	IDatapointConnectivityService serviceAdapterImpl = null;
	
	@Override
	public void start(BundleContext context) throws Exception {
		serviceAdapterImpl = new DatapointConnectivityServiceAdapter();
		DatapointConnectivityServiceRegistry.getInstance().addService(
				PROVIDED_SERVICE_NAME, serviceAdapterImpl);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		DatapointConnectivityServiceRegistry.getInstance().removeService(PROVIDED_SERVICE_NAME);
		serviceAdapterImpl = null;
	}


}
