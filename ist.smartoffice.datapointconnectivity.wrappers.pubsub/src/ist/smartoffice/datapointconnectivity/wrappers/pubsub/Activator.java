package ist.smartoffice.datapointconnectivity.wrappers.pubsub;

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
	static private Logger log = LoggerService.getInstance().getLogger(
			Activator.class.getName());

	private static final Map<String, String> paths = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("ist.smartoffice.deviceconnectivity.protocolintegration.DatapointConnectivityServiceAdapter",
					"/deviceconnectivityapi");
			put("ist.smartoffice.dataaccess.remotesensingactuation.RemoteSensingActuationService",
					"/remoteactuation");
			put("ist.smartoffice.autonmactuation.scenarios.ScenariosService",
					"/scenarios");
			
			put("ist.smartoffice.dataaccess.dataquerying.DataQueryingService",
					"/dataquerying");
		}
	};

	@Override
	public void start(BundleContext context) throws Exception {
		String serverAddr = context
				.getProperty("ist.smartoffice.datapointconnectivity.wrappers.pubsub.server");
		if (serverAddr == null) {
			log.e("Missing \"ist.smartoffice.datapointconnectivity.wrappers.rest.port\" property.");
			context.getBundle().stop();
			return;
		}

		DatapointConnectivityServicePubSubWrapper.getInstance().connect(
				serverAddr);

		for (String serviceName : paths.keySet()) {
			// try discover services
			IDatapointConnectivityService serviceImplementation = DatapointConnectivityServiceRegistry
					.getInstance().getService(serviceName);

			// try attach implementation
			if (serviceImplementation != null) {
				DatapointConnectivityServicePubSubWrapper.getInstance()
						.addServiceImplementation(paths.get(serviceName),
								serviceImplementation);
				log.i("PubSub Wrapper - Service Added:" + serviceName);
			}
		}

		// add service listener
		DatapointConnectivityServiceRegistry.getInstance().addServiceListener(
				new ServiceRegistryListener() {

					@Override
					public void serviceRemoved(String serviceName) {
					}

					@Override
					public void serviceModified(String serviceName) {
						// try discover services
						if (paths.containsKey(serviceName)) {
							log.i("PubSub Wrapper - Service Modified:" + serviceName);
							IDatapointConnectivityService serviceImplementation = DatapointConnectivityServiceRegistry
									.getInstance().getService(serviceName);

							// try attach implementation
							if (serviceImplementation != null) {
								DatapointConnectivityServicePubSubWrapper
										.getInstance()
										.addServiceImplementation(
												paths.get(serviceName),
												serviceImplementation);
							}
						}
					}

					@Override
					public void serviceAdded(String serviceName) {
						if (paths.containsKey(serviceName)) {
							log.i("PubSub Wrapper - Service Added:" + serviceName);
							IDatapointConnectivityService serviceImplementation = DatapointConnectivityServiceRegistry
									.getInstance().getService(serviceName);

							if (serviceImplementation != null) {
								DatapointConnectivityServicePubSubWrapper
										.getInstance()
										.addServiceImplementation(
												paths.get(serviceName),
												serviceImplementation);
							}
						}
					}
				});

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		DatapointConnectivityServicePubSubWrapper.getInstance().disconnect();
	}

}
