package ist.smartoffice.dataaccess.historydata;

import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService;
import ist.smartoffice.datapointconnectivity.osgi.registries.DatapointConnectivityServiceRegistry;
import ist.smartoffice.logger.Logger;
import ist.smartoffice.logger.LoggerService;
import ist.smartoffice.osgi.registries.IServiceRegistry.ServiceRegistryListener;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public final class Activator implements BundleActivator {

	private static final String PROVIDED_SERVICE_NAME = "ist.smartoffice.historydatastorage.HistoryDataService";
	private static final String USED_SERVICE_NAME = "ist.smartoffice.historydatastorage.HistoryDataStorageService";

	IDatapointConnectivityService serviceImpl;
	
	static private Logger log = LoggerService.getInstance().getLogger(
			Activator.class.getName());


	@Override
	public void start(BundleContext context) throws Exception {
		IDatapointConnectivityService serviceDepencendy = DatapointConnectivityServiceRegistry.getInstance().getService(USED_SERVICE_NAME);
		
		if(serviceDepencendy != null){
			serviceImpl = new HistoryDataService(serviceDepencendy);
			DatapointConnectivityServiceRegistry.getInstance().addService(PROVIDED_SERVICE_NAME, serviceImpl);
		}
		
		DatapointConnectivityServiceRegistry.getInstance().addServiceListener(new ServiceRegistryListener() {
			
			@Override
			public void serviceRemoved(String serviceName) {
				if(serviceName.equals(USED_SERVICE_NAME)){
					serviceImpl = null;
					DatapointConnectivityServiceRegistry.getInstance().removeService(PROVIDED_SERVICE_NAME);
				}
				
			}
			
			@Override
			public void serviceModified(String serviceName) {
				if(serviceName.equals(USED_SERVICE_NAME)){
					serviceImpl = null;
					DatapointConnectivityServiceRegistry.getInstance().removeService(PROVIDED_SERVICE_NAME);
					IDatapointConnectivityService serviceDepencendy = DatapointConnectivityServiceRegistry.getInstance().getService(USED_SERVICE_NAME);
					serviceImpl = new HistoryDataService(serviceDepencendy);
					DatapointConnectivityServiceRegistry.getInstance().addService(PROVIDED_SERVICE_NAME, serviceImpl);
				}
			}
			
			@Override
			public void serviceAdded(String serviceName) {
				if(serviceName.equals(USED_SERVICE_NAME)){
					serviceImpl = null;
					DatapointConnectivityServiceRegistry.getInstance().removeService(PROVIDED_SERVICE_NAME);
				}
				
			}
		});
		
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		DatapointConnectivityServiceRegistry.getInstance().removeService(PROVIDED_SERVICE_NAME);
		serviceImpl = null;
	}

}
