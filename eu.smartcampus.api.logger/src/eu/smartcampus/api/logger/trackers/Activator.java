package eu.smartcampus.api.logger.trackers;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

import eu.smartcampus.api.logger.registries.LoggerService;
import eu.smartcampus.api.logger.registries.LoggerServiceRegistry;

/**
 * The Class Activator.
 */
public final class Activator implements BundleActivator {

	/** The service tracker. */
	private static LoggerServicesTracker tracker;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		if (tracker == null)
			tracker = new LoggerServicesTracker(context);
		tracker.open();

		// add log service
		ServiceReference ref = context.getServiceReference(LogService.class
				.getName());

		if (ref != null) {
			LogService log = (LogService) context.getService(ref);
			LoggerService instance = LoggerService.getInstance();
			instance.addLogger(log);
			log.log(1, "TOMAA! apanheite!");
			//LoggerServiceRegistry.getInstance().addService(serviceName, service);
			System.out.println("nao é null!!!!!!");
		}
		else{
			System.out.println("é null!!!!!!");
			context.addServiceListener(new ServiceListener() {

				@Override
				public void serviceChanged(ServiceEvent event) {
					if (event.getType() == ServiceEvent.REGISTERED)
			        {
			            System.out.println("REGISTERED!!" + event.getServiceReference().toString());
			            if(event.getServiceReference().toString().contains("org.osgi.service.log.LogService")){
			            	ServiceReference ref2 = context.getServiceReference(LogService.class
			        				.getName());
			            	LogService log = (LogService) context.getService(ref2);
			    			LoggerService instance = LoggerService.getInstance();
			    			instance.addLogger(log);
			    			log.log(1, "TOMAA! apanheite!");
			            }
			        }
				}
			});
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext arg0) throws Exception {
		tracker.close();
	}
}
