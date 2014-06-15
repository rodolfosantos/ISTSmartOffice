package ist.smartoffice.dataaccess.historydata;

import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService;
import ist.smartoffice.datapointconnectivity.osgi.registries.DatapointConnectivityServiceRegistry;
import ist.smartoffice.logger.Logger;
import ist.smartoffice.logger.LoggerService;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public final class Activator implements BundleActivator {

	private static final String PROVIDED_SERVICE_NAME = "ist.smartoffice.dataaccess.historydata.HistoryDataStorageService";

	static private Logger log = LoggerService.getInstance().getLogger(
			Activator.class.getName());

	IDatapointConnectivityService serviceImpl = null;

	@Override
	public void start(BundleContext context) throws Exception {
		String db_filename = context
				.getProperty("ist.smartoffice.historydatastorage.db_filename");
		if (db_filename == null) {
			log.e("Using default DB Filename (HistoryDataDB.json) . Please set \"ist.smartoffice.historydatastorage.db_filename\" property.");
			db_filename = "HistoryDataDB.json";
		}

		serviceImpl = new HistoryDataStorageService(db_filename);
		DatapointConnectivityServiceRegistry.getInstance().addService(
				PROVIDED_SERVICE_NAME, serviceImpl);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		serviceImpl = null;
	}

}
