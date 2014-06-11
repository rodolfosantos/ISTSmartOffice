package eu.smartcampus.api.logger.registries;

import java.util.HashSet;
import java.util.Set;

import org.osgi.service.log.LogService;

public class LoggerService {

	private static Set<Logger> logInstances;
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

	public static Logger getLogger(String className) {
		Logger l = new Logger(className);
		logInstances.add(l);
		return l;
	}

	public String getImplementationName() {
		return LoggerService.class.getName();
	}

}
