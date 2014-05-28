package eu.smartcampus.api.historydatastorage.osgi.trackers;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import eu.smartcampus.api.historydatastorage.HistoryDataStorageServiceImpl;
import eu.smartcampus.api.historydatastorage.IHistoryDataStorageService;
import eu.smartcampus.api.historydatastorage.osgi.registries.HistoryDataStorageServiceRegistry;

/**
 * The Class Activator.
 */
public final class Activator implements BundleActivator {

	/** The service tracker. */
	private static HistoryDataStorageServicesTracker tracker;

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
			tracker = new HistoryDataStorageServicesTracker(context);
		tracker.open();
		
		IHistoryDataStorageService service = new HistoryDataStorageServiceImpl();
		HistoryDataStorageServiceRegistry.getInstance().addService(HistoryDataStorageServiceImpl.class.getName(), service);
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
