package eu.smartcampus.api.deviceconnectivity.impls.meterip;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;

public final class Activator implements BundleActivator {

	private ServiceRegistration registration;

	@Override
	public void start(BundleContext context) throws Exception {
		DatapointConnectivityServiceMeterIPDriver driver = new DatapointConnectivityServiceMeterIPDriver(
				"root", "root");
		if (registration == null) {
			registration = context
					.registerService(
							IDatapointConnectivityService.class.getName(),
							driver, null);
		}

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		registration.unregister();
		registration = null;
	}

}
