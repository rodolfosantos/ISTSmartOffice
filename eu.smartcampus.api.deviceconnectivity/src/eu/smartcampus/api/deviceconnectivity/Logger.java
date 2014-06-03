package eu.smartcampus.api.deviceconnectivity;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

	private String className;

	private Logger(String className) {
		this.className = className;
	}

	public static Logger getLogger(String className) {
		return new Logger(className);
	}

	public void i(String msg) {
		Date date = new Date();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateInfo = formatter.format(date);
		System.out.println("[INFO:"+dateInfo+" @ "+className+"]: "+msg);
	}

	public void e(String msg) {
		Date date = new Date();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateInfo = formatter.format(date);
		System.err.println("[ERROR:"+dateInfo+" @ "+className+"]: "+msg);
	}

	public void d(String msg) {
		Date date = new Date();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateInfo = formatter.format(date);
		System.out.println("[DEBUG:"+dateInfo+" @ "+className+"]: "+msg);
	}

}
