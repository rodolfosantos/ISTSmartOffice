package ist.smartoffice.datapointconnectivity.impls.meterip;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService;
import ist.smartoffice.datapointconnectivity.osgi.registries.DatapointConnectivityServiceRegistry;
import ist.smartoffice.logger.Logger;
import ist.smartoffice.logger.LoggerService;

public final class Activator implements BundleActivator {
	static private Logger log = LoggerService.getInstance().getLogger(Activator.class.getName());  
	
	@Override
	public void start(BundleContext context) throws Exception {
		// Create service implementation
		IDatapointConnectivityService serviceImpl = new DatapointConnectivityServiceMeterIPDriver();
		// Publish Service
		DatapointConnectivityServiceRegistry.getInstance().addService(
				DatapointConnectivityServiceMeterIPDriver.class.getName(),
				serviceImpl);
		log.i("Meter Started!");
		
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		DatapointConnectivityServiceRegistry.getInstance().removeService(
				DatapointConnectivityServiceMeterIPDriver.class.getName());
		log.i("Meter Stopped!");
	}

}
