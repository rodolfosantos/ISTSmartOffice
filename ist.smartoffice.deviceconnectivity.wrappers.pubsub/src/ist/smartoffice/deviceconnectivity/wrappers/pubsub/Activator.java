package ist.smartoffice.deviceconnectivity.wrappers.pubsub;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import ist.smartoffice.deviceconnectivity.IDatapointConnectivityService;
import ist.smartoffice.deviceconnectivity.osgi.registries.DeviceConnectivityServiceRegistry;
import ist.smartoffice.logger.Logger;
import ist.smartoffice.logger.LoggerService;
import ist.smartoffice.osgi.registries.IServiceRegistry.ServiceRegistryListener;

public final class Activator implements BundleActivator {
	static private Logger log = LoggerService.getInstance().getLogger(Activator.class.getName());  

	@Override
	public void start(BundleContext arg0) throws Exception {
		// TODO Auto-generated method stub
		log.i("PUBSUUUUB!!");
		
		final DatapointConnectivityServicePubSubWrapper pubsubwrapper = new DatapointConnectivityServicePubSubWrapper();
		
		
		IDatapointConnectivityService serviceImplementation = DeviceConnectivityServiceRegistry
				.getInstance().getService(
						IDatapointConnectivityService.class.getName());
		
		

		// try attach implementation
		if (serviceImplementation != null) {
			pubsubwrapper.setServiceImplementation(serviceImplementation);
		}

		// add service listener
		DeviceConnectivityServiceRegistry.getInstance().addServiceListener(
				new ServiceRegistryListener() {

					@Override
					public void serviceRemoved(String serviceName) {
						//Stop
					}

					@Override
					public void serviceModified(String serviceName) {
						log.i("Wrapper- Service Modif  "
								+ serviceName);
						if (serviceName
								.equals(IDatapointConnectivityService.class
										.getName())) {
							IDatapointConnectivityService serviceImplementation = DeviceConnectivityServiceRegistry
									.getInstance().getService(
											IDatapointConnectivityService.class
													.getName());
							//TODO
						}
					}

					@Override
					public void serviceAdded(String serviceName) {
						log.i("Wrapper- Service Added  "
								+ serviceName);
						// Bound an implementation to the REST adapter
						if (serviceName
								.equals(IDatapointConnectivityService.class
										.getName())) {
							IDatapointConnectivityService serviceImplementation = DeviceConnectivityServiceRegistry
									.getInstance().getService(
											IDatapointConnectivityService.class
													.getName());

							pubsubwrapper.setServiceImplementation(serviceImplementation);
						}
					}
				});
		
		
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}



}
