package ist.smartoffice.dataaccess.dataaquisition;

import ist.smartoffice.logger.Logger;
import ist.smartoffice.logger.LoggerService;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public final class Activator implements BundleActivator {

	static private Logger log = LoggerService.getInstance().getLogger(
			Activator.class.getName());

	private Thread dataAquisitonThread;

	@Override
	public void start(BundleContext context) throws Exception {
		String remoteSensingServiceName = "ist.smartoffice.dataaccess.remotesensingactuation.RemoteSensingActuationService";
		String historyDataServiceName = "ist.smartoffice.dataaccess.historydata.HistoryDataStorageService";
		dataAquisitonThread = new Thread(new DataAquisitionWorker(
				remoteSensingServiceName, historyDataServiceName));
		dataAquisitonThread.start();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		dataAquisitonThread.join();
	}

}
