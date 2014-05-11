package eu.smartcampus.api.deviceconnectivity.osgi.trackers;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * The Class Activator.
 */
public final class Activator implements BundleActivator {

	/** The service tracker. */
	private static DeviceConnectivityServicesTracker tracker;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		if (tracker == null)
			tracker = new DeviceConnectivityServicesTracker(context);
		tracker.open();
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
