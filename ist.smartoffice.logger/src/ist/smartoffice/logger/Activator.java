package ist.smartoffice.logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

/**
 * The Class Activator.
 */
public final class Activator implements BundleActivator {


	@Override
	public void start(final BundleContext context) throws Exception {

		// add log service
		ServiceReference ref = context.getServiceReference(LogService.class
				.getName());

		if (ref != null) {
			LogService log = (LogService) context.getService(ref);
			LoggerService instance = LoggerService.getInstance();
			instance.addLogger(log);
		}
		else{
			context.addServiceListener(new ServiceListener() {

				@Override
				public void serviceChanged(ServiceEvent event) {
					if (event.getType() == ServiceEvent.REGISTERED)
			        {
			            if(event.getServiceReference().toString().contains("org.osgi.service.log.LogService")){
			            	ServiceReference ref2 = context.getServiceReference(LogService.class
			        				.getName());
			            	LogService log = (LogService) context.getService(ref2);
			    			LoggerService.getInstance().addLogger(log);
			            }
			        }
				}
			});
		}

	}


	@Override
	public void stop(BundleContext arg0) throws Exception {
		
	}
}
