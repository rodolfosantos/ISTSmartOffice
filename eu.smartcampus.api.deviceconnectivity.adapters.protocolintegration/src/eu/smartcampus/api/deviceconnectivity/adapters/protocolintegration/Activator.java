package eu.smartcampus.api.deviceconnectivity.adapters.protocolintegration;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;
import eu.smartcampus.api.deviceconnectivity.osgi.registries.DeviceConnectivityServiceRegistry;
import eu.smartcampus.api.osgi.registries.IServiceRegistry.ServiceRegistryListener;

public final class Activator implements BundleActivator {
	
	static private Logger log = Logger.getLogger(Activator.class.getName());  

	IDatapointConnectivityService serviceAdapterImpl = null;
	Map<String, IDatapointConnectivityService> discoveredServices = new HashMap<String, IDatapointConnectivityService>();

	@Override
	public void start(BundleContext context) throws Exception {
		
		

		DeviceConnectivityServiceRegistry.getInstance().addServiceListener(
				new ServiceRegistryListener() {

					@Override
					public void serviceRemoved(String serviceName) {
						if (!serviceName
								.contains("eu.smartcampus.api.deviceconnectivity.impls."))
							return;

						log.info("Service removed: " + serviceName);
						removeServiceImplementation(serviceName);
					}

					@Override
					public void serviceModified(String serviceName) {
						log.info("Service modified: " + serviceName);
						// do notting
					}

					@Override
					public void serviceAdded(String serviceName) {
						if (!serviceName
								.contains("eu.smartcampus.api.deviceconnectivity.impls."))
							return;

						log.info("Service added: " + serviceName);
						addServiceImplementation(serviceName);
					}
				});
	}

	@Override
	public void stop(BundleContext context) throws Exception {

	}

	private void addServiceImplementation(String serviceName) {
		IDatapointConnectivityService newServiceImpl = DeviceConnectivityServiceRegistry
				.getInstance().getService(serviceName);
		discoveredServices.put(serviceName, newServiceImpl);

		if (serviceAdapterImpl == null) {
			serviceAdapterImpl = new DatapointConnectivityServiceAdapter(
					discoveredServices.values());
			DeviceConnectivityServiceRegistry.getInstance().addService(
					IDatapointConnectivityService.class.getName(),
					serviceAdapterImpl);
		} else {
			serviceAdapterImpl = new DatapointConnectivityServiceAdapter(
					discoveredServices.values());
			DeviceConnectivityServiceRegistry.getInstance().modifyService(
					IDatapointConnectivityService.class.getName(),
					serviceAdapterImpl);
		}

	}

	private void removeServiceImplementation(String serviceName) {
		discoveredServices.remove(serviceName);

		serviceAdapterImpl = new DatapointConnectivityServiceAdapter(
				discoveredServices.values());
		DeviceConnectivityServiceRegistry.getInstance().modifyService(
				IDatapointConnectivityService.class.getName(),
				serviceAdapterImpl);
	}
}
