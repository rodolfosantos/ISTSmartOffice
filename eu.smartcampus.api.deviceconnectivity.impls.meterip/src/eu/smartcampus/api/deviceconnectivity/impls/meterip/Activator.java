package eu.smartcampus.api.deviceconnectivity.impls.meterip;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;
import eu.smartcampus.api.deviceconnectivity.osgi.registries.DeviceConnectivityServiceRegistry;
import eu.smartcampus.api.historydatastorage.HistoryDataStorageServiceImpl;
import eu.smartcampus.api.historydatastorage.osgi.registries.HistoryDataStorageServiceRegistry;
import eu.smartcampus.api.osgi.registries.IServiceRegistry.ServiceRegistryListener;

public final class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		// Create service implementation
		IDatapointConnectivityService serviceImpl = new DatapointConnectivityServiceMeterIPDriver();
		// Publish Service
		DeviceConnectivityServiceRegistry.getInstance().addService(
				DatapointConnectivityServiceMeterIPDriver.class.getName(),
				serviceImpl);
		System.out.println("Meter Started!");
		
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		DeviceConnectivityServiceRegistry.getInstance().removeService(
				DatapointConnectivityServiceMeterIPDriver.class.getName());
		System.out.println("Meter Stopped!");
	}

}
