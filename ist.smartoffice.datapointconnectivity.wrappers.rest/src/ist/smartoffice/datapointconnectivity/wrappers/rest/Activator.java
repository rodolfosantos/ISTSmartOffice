package ist.smartoffice.datapointconnectivity.wrappers.rest;

import java.util.HashMap;
import java.util.Map;

import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService;
import ist.smartoffice.datapointconnectivity.osgi.registries.DatapointConnectivityServiceRegistry;
import ist.smartoffice.logger.Logger;
import ist.smartoffice.logger.LoggerService;
import ist.smartoffice.osgi.registries.IServiceRegistry.ServiceRegistryListener;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.restlet.Component;
import org.restlet.data.Protocol;

public final class Activator implements BundleActivator {
	static private Logger log = LoggerService.getInstance().getLogger(
			Activator.class.getName());

	private static final Map<String, String> paths = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("ist.smartoffice.deviceconnectivity.protocolintegration.DatapointConnectivityServiceAdapter",
					"/deviceconnectivityapi");
			put("ist.smartoffice.dataaccess.remotesensingactuation.RemoteSensingActuationService",
					"/remoteactuation");
			
			//put("service1", "/deviceconnectivityapii");
		}
	};

	private Component component;
	
	private int serverPort;

	@Override
	public void start(BundleContext context) throws Exception {

		String serverPort = context
				.getProperty("ist.smartoffice.datapointconnectivity.wrappers.rest.port"); 
		if(serverPort == null){
			log.e("Using default port (8182). Set \"ist.smartoffice.datapointconnectivity.wrappers.rest.port\" property.");
			this.serverPort = 8182;
		}
		else{
			this.serverPort = Integer.parseInt(serverPort);
		}
		

		/**
		 * TODO: Add a way to set the port and the implementation to use through
		 * some configuration file, GUI, or so.
		 */
		// start Restlet component
		serverStart(this.serverPort);

		for (String serviceName : paths.keySet()) {
			// try discover services
			IDatapointConnectivityService serviceImplementation = DatapointConnectivityServiceRegistry
					.getInstance().getService(serviceName);

			// try attach implementation
			if (serviceImplementation != null) {
				serverAttach(paths.get(serviceName), serviceImplementation);
			}
		}

		// add service listener
		DatapointConnectivityServiceRegistry.getInstance().addServiceListener(
				new ServiceRegistryListener() {

					@Override
					public void serviceRemoved(String serviceName) {
						log.i("Wrapper - Service Rem  " + serviceName);
						if (paths.containsKey(serviceName))
							serverDettach(paths.get(serviceName));
					}

					@Override
					public void serviceModified(String serviceName) {
						log.i("Wrapper - Service Modif  " + serviceName);
						if (paths.containsKey(serviceName)) {
							IDatapointConnectivityService serviceImplementation = DatapointConnectivityServiceRegistry
									.getInstance().getService(serviceName);

							// attach implementation
							if (serviceImplementation != null) {
								serverAttach(paths.get(serviceName),
										serviceImplementation);
							}
						}
					}

					@Override
					public void serviceAdded(String serviceName) {
						log.i("Wrapper - Service Added  " + serviceName);
						// Bound an implementation to the REST adapter
						if (paths.containsKey(serviceName)) {
							IDatapointConnectivityService serviceImplementation = DatapointConnectivityServiceRegistry
									.getInstance().getService(serviceName);

							// attach implementation
							if (serviceImplementation != null) {
								serverAttach(paths.get(serviceName),
										serviceImplementation);
							}
						}
					}
				});

		log.i("inserted service listener!!!!!!!!!!!!!!!!!!!!!!!");
		System.err.println("STARTEEEEEEEEEEEEEEEEEEEED!!!!");

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		component.stop();
	}

	private void serverStart(int serverPort) throws Exception {
		// Create a new Component.
		this.component = new Component();

		// Add a new HTTP server listening on default port.
		component.getServers().add(Protocol.HTTP, serverPort);

		// Start the component.
		component.start();
	}

	private void serverAttach(String pathTemplate,
			IDatapointConnectivityService serviceImplementation) {
		DatapointConnectivityServiceRESTWrapper.getInstance()
				.setServiceImplementation(pathTemplate, serviceImplementation);

		// Attach device api application
		component.getDefaultHost().attach(pathTemplate,
				DatapointConnectivityServiceRESTWrapper.getInstance());
	}

	private void serverDettach(String path) {
		DatapointConnectivityServiceRESTWrapper.getInstance()
				.removeServiceImplementation(path);
	}

}
