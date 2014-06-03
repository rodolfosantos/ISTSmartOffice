package eu.smartcampus.api.deviceconnectivity.impls.lifx;

import eu.smartcampus.api.deviceconnectivity.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;
import eu.smartcampus.api.deviceconnectivity.osgi.registries.DeviceConnectivityServiceRegistry;

public final class Activator implements BundleActivator {
	static private Logger log = Logger.getLogger(Activator.class.getName()); 

	@Override
	public void start(BundleContext context) throws Exception {

		// Create service implementation
		IDatapointConnectivityService serviceImpl = new DatapointConnectivityServiceLifxDriver();
		// Publish Service
		DeviceConnectivityServiceRegistry.getInstance().addService(
				DatapointConnectivityServiceLifxDriver.class.getName(),
				serviceImpl);
		log.i("Lifx Started!");

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		DeviceConnectivityServiceRegistry.getInstance().removeService(
				DatapointConnectivityServiceLifxDriver.class.getName());
		log.i("Lifx Stopped!");
	}

}
