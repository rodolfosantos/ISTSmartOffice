package eu.smartcampus.api.deviceconnectivity.wrappers.rest;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.restlet.Component;
import org.restlet.data.Protocol;

import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;
import eu.smartcampus.api.deviceconnectivity.adapters.protocolintegration.DatapointConnectivityServiceAdapter;
import eu.smartcampus.api.deviceconnectivity.osgi.registries.DeviceConnectivityServiceRegistry;
import eu.smartcampus.api.osgi.registries.IServiceRegistry.ServiceRegistryListener;

public final class Activator implements BundleActivator {

	private static final int SERVER_PORT = 8182;
	private static final String PATH_TEMPLATE = "/deviceconnectivityapi";
	private Component component;

	@Override
	public void start(BundleContext context) throws Exception {

		/**
		 * TODO: Add a way to set the port and the implementation to use through
		 * some configuration file, GUI, or so.
		 */
		
		//try discover service
		IDatapointConnectivityService serviceImplementation = DeviceConnectivityServiceRegistry
				.getInstance().getService(
						IDatapointConnectivityService.class
								.getName());

		try {
			if (component == null)
				serverStart(SERVER_PORT, serviceImplementation);
			else
				serverAttach(PATH_TEMPLATE, serviceImplementation);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		
		ServiceRegistryListener serviceListener = new ServiceRegistryListener() {

			@Override
			public void serviceRemoved(String serviceName) {
				if (serviceName.equals(IDatapointConnectivityService.class
						.getName())) {
					serverDettach();
				}
			}

			@Override
			public void serviceModified(String serviceName) {
				if (serviceName.equals(IDatapointConnectivityService.class
						.getName())) {
					serverDettach();
					IDatapointConnectivityService serviceImplementation = DeviceConnectivityServiceRegistry
							.getInstance().getService(
									DatapointConnectivityServiceAdapter.class
											.getName());
					serverAttach(PATH_TEMPLATE, serviceImplementation);
				}
			}

			@Override
			public void serviceAdded(String serviceName) {
				// Bound an implementation to the REST adapter
				if (serviceName.equals(IDatapointConnectivityService.class
						.getName())) {
					IDatapointConnectivityService serviceImplementation = DeviceConnectivityServiceRegistry
							.getInstance().getService(
									IDatapointConnectivityService.class
											.getName());

					try {
						if (component == null)
							serverStart(SERVER_PORT, serviceImplementation);
						else
							serverAttach(PATH_TEMPLATE, serviceImplementation);
					} catch (Exception e) {
						System.err.println(e.getMessage());
					}
				}
			}
		};

		DeviceConnectivityServiceRegistry.getInstance().addServiceListener(
				serviceListener);
		System.out.println("inserted service listener");

	}

	private void serverStart(int serverPort,
			IDatapointConnectivityService serviceImplementation)
			throws Exception {
		// Create a new Component.
		this.component = new Component();

		// Add a new HTTP server listening on default port.
		component.getServers().add(Protocol.HTTP, serverPort);

		serverAttach(PATH_TEMPLATE, serviceImplementation);

		// Start the component.
		component.start();
	}

	private void serverAttach(String pathTemplate,
			IDatapointConnectivityService serviceImplementation) {
		DatapointConnectivityServiceRESTWrapper.getInstance()
				.setServiceImplementation(serviceImplementation);

		// Attach device api application
		component.getDefaultHost().attach(pathTemplate,
				DatapointConnectivityServiceRESTWrapper.getInstance());
	}

	private void serverDettach() {
		// Detach device api application
		component.getDefaultHost().detach(
				DatapointConnectivityServiceRESTWrapper.getInstance());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		component.stop();
	}

}
