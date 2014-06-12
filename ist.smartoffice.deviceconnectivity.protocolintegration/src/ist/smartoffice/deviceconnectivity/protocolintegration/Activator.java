package ist.smartoffice.deviceconnectivity.protocolintegration;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService;
import ist.smartoffice.datapointconnectivity.osgi.registries.DatapointConnectivityServiceRegistry;
import ist.smartoffice.logger.Logger;
import ist.smartoffice.logger.LoggerService;
import ist.smartoffice.osgi.registries.IServiceRegistry.ServiceRegistryListener;

public final class Activator implements BundleActivator {

	private static final String SERVICE_NAME = IDatapointConnectivityService.class
			.getName();

	static private Logger log = LoggerService.getInstance().getLogger(
			Activator.class.getName());

	IDatapointConnectivityService serviceAdapterImpl = null;
	Map<String, IDatapointConnectivityService> discoveredServices = new HashMap<String, IDatapointConnectivityService>();

	@Override
	public void start(BundleContext context) throws Exception {

		String[] currServices = DatapointConnectivityServiceRegistry
				.getInstance().getRegisteredServicesNames();

		for (String s : currServices) {
			if (!s.contains("ist.smartoffice.deviceconnectivity.")
					|| s.equals(SERVICE_NAME))
				break;
			addServiceImplementation(s);
		}

		DatapointConnectivityServiceRegistry.getInstance().addServiceListener(
				new ServiceRegistryListener() {

					@Override
					public void serviceRemoved(String serviceName) {
						if (!serviceName
								.contains("ist.smartoffice.deviceconnectivity.")
								|| serviceName.equals(SERVICE_NAME))
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
								.contains("ist.smartoffice.deviceconnectivity")
								|| serviceName.equals(SERVICE_NAME))
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
		IDatapointConnectivityService newServiceImpl = DatapointConnectivityServiceRegistry
				.getInstance().getService(serviceName);
		discoveredServices.put(serviceName, newServiceImpl);

		if (serviceAdapterImpl == null) {
			serviceAdapterImpl = new DatapointConnectivityServiceAdapter(
					discoveredServices.values());
			DatapointConnectivityServiceRegistry.getInstance().addService(
					SERVICE_NAME, serviceAdapterImpl);
		} else {
			serviceAdapterImpl = new DatapointConnectivityServiceAdapter(
					discoveredServices.values());
			DatapointConnectivityServiceRegistry.getInstance().modifyService(
					SERVICE_NAME, serviceAdapterImpl);
		}

	}

	private void removeServiceImplementation(String serviceName) {
		discoveredServices.remove(serviceName);

		serviceAdapterImpl = new DatapointConnectivityServiceAdapter(
				discoveredServices.values());
		DatapointConnectivityServiceRegistry.getInstance().modifyService(
				SERVICE_NAME, serviceAdapterImpl);
	}
}
