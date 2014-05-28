package eu.smartcampus.api.deviceconnectivity.adapters.protocolintegration;

import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;
import eu.smartcampus.api.deviceconnectivity.osgi.registries.DeviceConnectivityServiceRegistry;
import eu.smartcampus.api.osgi.registries.IServiceRegistry.ServiceRegistryListener;

public final class Activator implements BundleActivator {

	IDatapointConnectivityService serviceAdapterImpl = null;
	Set<IDatapointConnectivityService> discoveredServices = new HashSet<IDatapointConnectivityService>();

	@Override
	public void start(BundleContext context) throws Exception {

		DeviceConnectivityServiceRegistry.getInstance().addServiceListener(
				new ServiceRegistryListener() {

					@Override
					public void serviceRemoved(String serviceName) {
						if(!serviceName.contains("eu.smartcampus.api.deviceconnectivity.impls."))
							return;
						
						System.out.println("Service removed: " + serviceName);
						removeServiceImplementation(serviceName);
					}

					@Override
					public void serviceModified(String serviceName) {
						System.out.println("Service modified: " + serviceName);
						//do notting
					}

					@Override
					public void serviceAdded(String serviceName) {
						if(!serviceName.contains("eu.smartcampus.api.deviceconnectivity.impls."))
							return;
						
						System.out.println("Service added: " + serviceName);
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
		discoveredServices.add(newServiceImpl);

		if (serviceAdapterImpl == null) {
			serviceAdapterImpl = new DatapointConnectivityServiceAdapter(
					discoveredServices);
			DeviceConnectivityServiceRegistry.getInstance().addService(
					IDatapointConnectivityService.class.getName(),
					serviceAdapterImpl);
		} else {
			serviceAdapterImpl = new DatapointConnectivityServiceAdapter(
					discoveredServices);
			DeviceConnectivityServiceRegistry.getInstance().modifyService(
					IDatapointConnectivityService.class.getName(),
					serviceAdapterImpl);
		}

	}

	private void removeServiceImplementation(String serviceName) {
		System.out.println("Removed!! ->>>> " + discoveredServices.remove(serviceName));
		serviceAdapterImpl = new DatapointConnectivityServiceAdapter(
				discoveredServices);
		DeviceConnectivityServiceRegistry.getInstance().modifyService(
				IDatapointConnectivityService.class.getName(),
				serviceAdapterImpl);
	}
}
