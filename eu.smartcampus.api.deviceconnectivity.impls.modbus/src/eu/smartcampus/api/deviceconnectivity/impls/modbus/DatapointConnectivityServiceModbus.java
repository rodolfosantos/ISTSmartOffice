package eu.smartcampus.api.deviceconnectivity.impls.modbus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import eu.smartcampus.api.deviceconnectivity.DatapointAddress;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata;
import eu.smartcampus.api.deviceconnectivity.DatapointReading;
import eu.smartcampus.api.deviceconnectivity.DatapointValue;
import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;
import eu.smartcampus.api.historydatastorage.HistoryDataStorageServiceImpl;
import eu.smartcampus.api.historydatastorage.HistoryValue;
import eu.smartcampus.api.historydatastorage.IHistoryDataStorageService;
import eu.smartcampus.api.historydatastorage.osgi.registries.HistoryDataStorageServiceRegistry;
import eu.smartcampus.api.osgi.registries.IServiceRegistry.ServiceRegistryListener;

/**
 * The Class DatapointConnectivityServiceModbus
 */
public class DatapointConnectivityServiceModbus implements
		IDatapointConnectivityService {

	private String username;
	private String password;
	private Map<DatapointAddress, DatapointMetadata> datapoints;
	private IHistoryDataStorageService storageService;

	/**
	 * The listeners set
	 */
	private Set<DatapointListener> listeners;

	/**
	 * Instantiates a new datapoint connectivity service meter ip driver.
	 * 
	 * @param datapoints
	 *            the datapoints
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 */
	public DatapointConnectivityServiceModbus() {
		this.username = "root";
		this.password = "root";
		// this.datapoints = MeterIPServiceConfig.loadDatapointsConfigs();
		this.datapoints = new HashMap<DatapointAddress, DatapointMetadata>();
		this.listeners = new HashSet<DatapointListener>();
		this.storageService = HistoryDataStorageServiceRegistry.getInstance()
				.getService(HistoryDataStorageServiceImpl.class.getName());

		// if (this.storageService != null)
		// startPollingJob();
		// else {
		// HistoryDataStorageServiceRegistry.getInstance().addServiceListener(
		// new ServiceRegistryListener() {
		// @Override
		// public void serviceRemoved(String serviceName) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void serviceModified(String serviceName) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void serviceAdded(String serviceName) {
		// if (serviceName
		// .equals(HistoryDataStorageServiceImpl.class
		// .getName())) {
		// storageService = HistoryDataStorageServiceRegistry
		// .getInstance()
		// .getService(
		// HistoryDataStorageServiceImpl.class
		// .getName());
		// startPollingJob();
		// }
		//
		// }
		// });
		// }

	}

	private void startPollingJob() {
		Timer timer = new Timer();
		Iterator<Entry<DatapointAddress, DatapointMetadata>> elems = datapoints
				.entrySet().iterator();
		while (elems.hasNext()) {
			Map.Entry<DatapointAddress, DatapointMetadata> entry = (Map.Entry<DatapointAddress, DatapointMetadata>) elems
					.next();
			final DatapointAddress addr = entry.getKey();
			long interval = entry.getValue().getCurrentSamplingInterval();
			if (interval != 0) {

				timer.scheduleAtFixedRate(new TimerTask() {

					@Override
					public void run() {

						DatapointReading reading = new DatapointReading(
								new DatapointValue(0));
						// store reading
						storageService.addValue(addr.getAddress(), reading
								.getTimestamp(), reading.getValue().toString());

					}
				}, 1000, interval);
			}
		}

	}

	@Override
	public void addDatapointListener(DatapointListener listener) {
		listeners.add(listener);
	}

	@Override
	public DatapointAddress[] getAllDatapoints() {
		DatapointAddress[] result = new DatapointAddress[datapoints.size()];

		Iterator<DatapointAddress> it = datapoints.keySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			DatapointAddress datapointAddress = (DatapointAddress) it.next();
			result[i++] = datapointAddress;
		}
		return result;
	}

	@Override
	public DatapointMetadata getDatapointMetadata(DatapointAddress address) {
		return this.datapoints.get(address);
	}

	@Override
	public void removeDatapointListener(DatapointListener listener) {
		listeners.remove(listener);

	}

	@SuppressWarnings("unused")
	private void notifyDatapointError(DatapointAddress address, ErrorType error) {
		synchronized (listeners) {
			Iterator<DatapointListener> it = listeners.iterator();
			while (it.hasNext()) {
				DatapointListener listener = it.next();
				listener.onDatapointError(address, error);
			}
		}
	}

	@SuppressWarnings("unused")
	private void notifyDatapointUpdate(DatapointAddress address,
			DatapointReading[] values) {
		synchronized (listeners) {
			Iterator<DatapointListener> it = listeners.iterator();
			while (it.hasNext()) {
				DatapointListener listener = it.next();
				listener.onDatapointUpdate(address, values);
			}
		}
	}

	@Override
	public int requestDatapointRead(DatapointAddress address,
			ReadCallback readCallback) {

		HistoryValue lastReading = storageService.getLastValue(address
				.getAddress());

		DatapointMetadata m = datapoints.get(address);

		if (lastReading != null) {
			if (new Date().getTime() - lastReading.getTimestamp() < m
					.getCurrentSamplingInterval()) {
				readCallback.onReadCompleted(address,
						new DatapointReading[] { new DatapointReading(
								new DatapointValue(lastReading.getValue())) },
						0);
				return 0;
			}
		}

		DatapointReading reading = new DatapointReading(new DatapointValue(0));
		// store reading
		storageService.addValue(address.getAddress(), 0, 0 + "");

		readCallback.onReadCompleted(address,
				new DatapointReading[] { reading }, 0);

		return 0;
	}

	@Override
	public int requestDatapointWindowRead(DatapointAddress address,
			long startTimestamp, long finishTimestamp, ReadCallback readCallback) {

		HistoryValue[] readings = storageService.getValuesTimeWindow(
				address.getAddress(), startTimestamp, finishTimestamp);

		DatapointReading[] result = new DatapointReading[readings.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = new DatapointReading(new DatapointValue(
					readings[i].getValue()), readings[i].getTimestamp());
		}

		readCallback.onReadCompleted(address, result, 0);

		return 0;
	}

	@Override
	public int requestDatapointWrite(DatapointAddress address,
			DatapointValue[] values, WriteCallback writeCallback) {
		writeCallback.onWriteAborted(address,
				ErrorType.UNSUPORTED_DATAPOINT_OPERATION, 0);
		return 0;
	}

	@Override
	public String getImplementationName() {
		return "modbus";
	}

}
