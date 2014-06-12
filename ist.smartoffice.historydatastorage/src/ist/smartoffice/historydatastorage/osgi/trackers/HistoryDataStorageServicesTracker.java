package ist.smartoffice.historydatastorage.osgi.trackers;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import ist.smartoffice.historydatastorage.IHistoryDataStorageService;
import ist.smartoffice.historydatastorage.osgi.registries.HistoryDataStorageServiceRegistry;

@SuppressWarnings("rawtypes")
public class HistoryDataStorageServicesTracker extends ServiceTracker {

	/** The service registry. */
	private HistoryDataStorageServiceRegistry registry = HistoryDataStorageServiceRegistry.getInstance();

	/**
	 * Constructs a tracker that uses the specified bundle context to track
	 * services and notifies the specified application object about changes.
	 * 
	 * @param context
	 *            The bundle context to be used by the tracker.
	 * @param registry
	 *            The application object to notify about service changes.
	 **/
	@SuppressWarnings("unchecked")
	public HistoryDataStorageServicesTracker(BundleContext context) {
		super(context, IHistoryDataStorageService.class.getName(), null);
	}

	/**
	 * Overrides the <tt>ServiceTracker</tt> functionality to inform the
	 * application object about the added service.
	 * 
	 * @param ref
	 *            The service reference of the added service.
	 * @return The service object to be used by the tracker.
	 **/
	@Override
	public ServiceReference addingService(ServiceReference ref) {
		IHistoryDataStorageService service = (IHistoryDataStorageService) context
				.getService(ref);
		registry.addService(service.getImplementationName(), service);
		return ref;
	}

	/**
	 * Overrides the <tt>ServiceTracker</tt> functionality to inform the
	 * application object about the modified service.
	 * 
	 * @param ref
	 *            The service reference of the modified service.
	 * @param svc
	 *            The service object of the modified service.
	 **/
	@Override
	public void modifiedService(ServiceReference ref, Object svc) {
		IHistoryDataStorageService service = (IHistoryDataStorageService) context
				.getService(ref);
		registry.modifyService(service.getImplementationName(), service);
	}

	/**
	 * Overrides the <tt>ServiceTracker</tt> functionality to inform the
	 * application object about the removed service.
	 * 
	 * @param ref
	 *            The service reference of the removed service.
	 * @param svc
	 *            The service object of the removed service.
	 **/
	@Override
	public void removedService(ServiceReference ref, Object svc) {
		IHistoryDataStorageService service = (IHistoryDataStorageService) context
				.getService(ref);
		registry.removeService(service.getImplementationName());
	}

}
