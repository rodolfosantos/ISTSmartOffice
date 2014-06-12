package ist.smartoffice.deviceconnectivity.adapters.protocolintegration;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import ist.smartoffice.deviceconnectivity.IDatapointConnectivityService;
import ist.smartoffice.deviceconnectivity.osgi.registries.DeviceConnectivityServiceRegistry;
import ist.smartoffice.logger.Logger;
import ist.smartoffice.logger.LoggerService;
import ist.smartoffice.osgi.registries.IServiceRegistry.ServiceRegistryListener;

public final class Activator implements BundleActivator {

	static private Logger log = LoggerService.getInstance().getLogger(Activator.class
			.getName());

	IDatapointConnectivityService serviceAdapterImpl = null;
	Map<String, IDatapointConnectivityService> discoveredServices = new HashMap<String, IDatapointConnectivityService>();

	@Override
	public void start(BundleContext context) throws Exception {

		DeviceConnectivityServiceRegistry.getInstance().addServiceListener(
				new ServiceRegistryListener() {

					@Override
					public void serviceRemoved(String serviceName) {
						if (!serviceName
								.contains("ist.smartoffice.deviceconnectivity.impls."))
							return;

						log.i("Service removed: " + serviceName);
						removeServiceImplementation(serviceName);
					}

					@Override
					public void serviceModified(String serviceName) {
						log.i("Service modified: " + serviceName);
						// do notting
					}

					@Override
					public void serviceAdded(String serviceName) {
						if (!serviceName
								.contains("ist.smartoffice.deviceconnectivity.impls."))
							return;

						log.i("Service added: " + serviceName);
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
