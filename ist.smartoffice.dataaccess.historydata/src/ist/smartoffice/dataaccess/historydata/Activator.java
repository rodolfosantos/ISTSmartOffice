package ist.smartoffice.dataaccess.historydata;

import ist.smartoffice.logger.Logger;
import ist.smartoffice.logger.LoggerService;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public final class Activator implements BundleActivator {

	private static final String SERVICE_NAME = HistoryDataService.class.getName();

	static private Logger log = LoggerService.getInstance().getLogger(
			Activator.class.getName());


	@Override
	public void start(BundleContext context) throws Exception {

	}

	@Override
	public void stop(BundleContext context) throws Exception {

	}

}
