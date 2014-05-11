package eu.smartcampus.api.deviceconnectivity.wrappers.rest;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public final class Activator implements BundleActivator {

	private ServiceRegistration registration;

	@Override
	public void start(BundleContext context) throws Exception {
		DatapointConnectivityServiceRESTWrapper restWrapper = DatapointConnectivityServiceRESTWrapper
				.getInstance();
		if (registration == null) {
			registration = context.registerService(
					"eu.smartcampus.api.wrappers.rest", restWrapper, null);
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		registration.unregister();
		registration = null;
	}

}
