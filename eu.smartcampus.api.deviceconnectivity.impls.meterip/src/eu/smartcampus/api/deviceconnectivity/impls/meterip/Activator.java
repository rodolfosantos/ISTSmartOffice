package eu.smartcampus.api.deviceconnectivity.impls.meterip;

import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;
import eu.smartcampus.api.deviceconnectivity.osgi.registries.DeviceConnectivityServiceRegistry;

public final class Activator implements BundleActivator {
	static private Logger log = Logger.getLogger(Activator.class.getName());  
	
	@Override
	public void start(BundleContext context) throws Exception {
		// Create service implementation
		IDatapointConnectivityService serviceImpl = new DatapointConnectivityServiceMeterIPDriver();
		// Publish Service
		DeviceConnectivityServiceRegistry.getInstance().addService(
				DatapointConnectivityServiceMeterIPDriver.class.getName(),
				serviceImpl);
		log.info("Meter Started!");
		
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		DeviceConnectivityServiceRegistry.getInstance().removeService(
				DatapointConnectivityServiceMeterIPDriver.class.getName());
		log.info("Meter Stopped!");
	}

}
