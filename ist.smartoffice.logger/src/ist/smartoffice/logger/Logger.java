package ist.smartoffice.logger;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import org.osgi.service.log.LogService;

public class Logger {

	private String className;
	private LogService log;
	private Queue<String> temporaryInfoQueue;
	private Queue<String> temporaryDebugQueue;
	private Queue<String> temporaryErrorQueue;

	public Logger(String className) {
		this.temporaryInfoQueue = new LinkedList<String>();
		this.temporaryDebugQueue = new LinkedList<String>();
		this.temporaryErrorQueue = new LinkedList<String>();
		this.className = className;
	}

	public void i(String msg) {
		Date date = new Date();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateInfo = formatter.format(date);
		String message = "[INFO:" + dateInfo + " @ " + className + "]: " + msg;
		System.out.println(message);
		if (log == null)
			temporaryInfoQueue.add(message);
		else
			log.log(LogService.LOG_INFO, message);

	}

	public void e(String msg) {
		Date date = new Date();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateInfo = formatter.format(date);
		String message = "[ERROR:" + dateInfo + " @ " + className + "]: " + msg;
		System.err.println(message);
		if (log == null)
			temporaryErrorQueue.add(message);
		else
			log.log(LogService.LOG_ERROR, message);
	}

	public void d(String msg) {
		Date date = new Date();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateInfo = formatter.format(date);
		String message = "[DEBUG:" + dateInfo + " @ " + className + "]: " + msg;
		System.out.println(message);
		if (log == null)
			temporaryDebugQueue.add(message);
		else
			log.log(LogService.LOG_INFO, message);
	}

	public void addLogger(LogService logger) {
		this.log = logger;
		for (String s : temporaryInfoQueue)
			log.log(LogService.LOG_INFO, s);
		temporaryInfoQueue.clear();
		
		for (String s : temporaryDebugQueue)
			log.log(LogService.LOG_INFO, s);
		temporaryDebugQueue.clear();
		
		for (String s : temporaryErrorQueue)
			log.log(LogService.LOG_ERROR, s);
		temporaryErrorQueue.clear();
	}

}
