package eu.smartcampus.api.deviceconnectivity.impls.knxip;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;

public final class Activator implements BundleActivator {

	private ServiceRegistration registration;
	private KNXGatewayIPDriver gw;

	@Override
	public void start(BundleContext context) throws Exception {

		gw = new KNXGatewayIPDriver("172.20.70.147"); // hard coded for testing
														// purpose
		gw.start();

		if (gw.isConnected()) {
			DatapointConnectivityServiceKNXIPDriver driver = new DatapointConnectivityServiceKNXIPDriver(
					gw);
			if (registration == null) {
				registration = context.registerService(
						IDatapointConnectivityService.class.getName(), driver,
						null);
			}
		} else {
			context.getBundle().stop();
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		registration.unregister();
		registration = null;
		gw.stop();
		gw = null;
	}

}
