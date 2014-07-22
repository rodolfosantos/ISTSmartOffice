package ist.smartoffice.dataaccess.dataquerying;

import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService;
import ist.smartoffice.datapointconnectivity.osgi.registries.DatapointConnectivityServiceRegistry;
import ist.smartoffice.logger.Logger;
import ist.smartoffice.logger.LoggerService;
import ist.smartoffice.osgi.registries.IServiceRegistry.ServiceRegistryListener;
import net.sf.cglib.reflect.FastClass;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

public final class Activator implements BundleActivator {


	static private Logger log = LoggerService.getInstance().getLogger(
			Activator.class.getName());

	private static final String USE_SERVICE_NAME = "ist.smartoffice.dataaccess.remotesensingactuation.RemoteSensingActuationService";
	private static final String PROVIDE_SERVICE_NAME = "ist.smartoffice.dataaccess.dataquerying.DataQueryingService";

	IDatapointConnectivityService serviceImpl = null;

	@Override
	public void start(BundleContext context) throws Exception {
		
		
		serviceImpl = DatapointConnectivityServiceRegistry.getInstance()
				.getService(USE_SERVICE_NAME);

		if (serviceImpl != null) {
			DatapointConnectivityServiceRegistry.getInstance().addService(
					PROVIDE_SERVICE_NAME,
					new DataQueryingService(serviceImpl));
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

						}
					}

					@Override
					public void serviceAdded(String serviceName) {
						if (serviceName.equals(USE_SERVICE_NAME)) {
							serviceImpl = DatapointConnectivityServiceRegistry.getInstance()
									.getService(USE_SERVICE_NAME);
							
							DatapointConnectivityServiceRegistry.getInstance().addService(
									PROVIDE_SERVICE_NAME,
									new DataQueryingService(serviceImpl));
						}
					}
				});
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		serviceImpl = null;
	}

}
