package ist.smartoffice.dataaccess.remotesensingactuation;

import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService;
import ist.smartoffice.datapointconnectivity.osgi.registries.DatapointConnectivityServiceRegistry;
import ist.smartoffice.logger.Logger;
import ist.smartoffice.logger.LoggerService;
import ist.smartoffice.osgi.registries.IServiceRegistry.ServiceRegistryListener;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public final class Activator implements BundleActivator {

	private static final String PROVIDE_SERVICE_NAME = "ist.smartoffice.dataaccess.remotesensingactuation.RemoteSensingActuationService";
	private static final String USE_SERVICE_NAME = "ist.smartoffice.deviceconnectivity.protocolintegration.DatapointConnectivityServiceAdapter";

	static private Logger log = LoggerService.getInstance().getLogger(
			Activator.class.getName());

	IDatapointConnectivityService serviceImpl = null;

	@Override
	public void start(BundleContext context) throws Exception {
		serviceImpl = DatapointConnectivityServiceRegistry.getInstance()
				.getService(USE_SERVICE_NAME);

		if (serviceImpl != null) {
			DatapointConnectivityServiceRegistry.getInstance().addService(
					PROVIDE_SERVICE_NAME,
					new RemoteSensingActuationService(serviceImpl));
		}

		DatapointConnectivityServiceRegistry.getInstance().addServiceListener(
				new ServiceRegistryListener() {

					@Override
					public void serviceRemoved(String serviceName) {
						if (serviceName.equals(USE_SERVICE_NAME)) {
							serviceImpl = null;
						}
					}

					@Override
					public void serviceModified(String serviceName) {
						if (serviceName.equals(USE_SERVICE_NAME)) {

							serviceImpl = new RemoteSensingActuationService(
									DatapointConnectivityServiceRegistry
											.getInstance().getService(
													USE_SERVICE_NAME));
							
							if (serviceImpl != null) {
								DatapointConnectivityServiceRegistry.getInstance().removeService(PROVIDE_SERVICE_NAME);
								DatapointConnectivityServiceRegistry.getInstance().addService(
										PROVIDE_SERVICE_NAME,
										new RemoteSensingActuationService(serviceImpl));
							}
						}
					}

					@Override
					public void serviceAdded(String serviceName) {
						if (serviceName.equals(USE_SERVICE_NAME)) {
							serviceImpl = new RemoteSensingActuationService(
									DatapointConnectivityServiceRegistry
											.getInstance().getService(
													USE_SERVICE_NAME));

							if (serviceImpl != null) {
								DatapointConnectivityServiceRegistry
										.getInstance()
										.addService(
												PROVIDE_SERVICE_NAME,
												new RemoteSensingActuationService(
														serviceImpl));
							}
						}
					}
				});
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		serviceImpl = null;
	}

}
