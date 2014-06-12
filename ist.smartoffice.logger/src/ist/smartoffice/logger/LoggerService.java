package ist.smartoffice.logger;

import java.util.HashSet;
import java.util.Set;

import org.osgi.service.log.LogService;

public class LoggerService {

	private Set<Logger> logInstances;
	private static LoggerService instance;

	private LoggerService() {
		this.logInstances = new HashSet<Logger>();
	}

	public static LoggerService getInstance() {
		if (instance == null)
			instance = new LoggerService();
		return instance;
	}

	public void addLogger(LogService logger) {
		for (Logger l : logInstances) {
			l.addLogger(logger);
		}
	}

	public Logger getLogger(String className) {
		Logger l = new Logger(className);
		logInstances.add(l);
		return l;
	}

}
