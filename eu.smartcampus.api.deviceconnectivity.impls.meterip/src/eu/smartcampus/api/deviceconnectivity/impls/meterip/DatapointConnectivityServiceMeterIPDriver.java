package eu.smartcampus.api.deviceconnectivity.impls.meterip;

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
 * The Class DatapointConnectivityServiceMeterIPDriver
 */
public class DatapointConnectivityServiceMeterIPDriver implements
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
	public DatapointConnectivityServiceMeterIPDriver() {
		this.username = "root";
		this.password = "root";
		this.datapoints = MeterIPServiceConfig.loadDatapointsConfigs();
		this.listeners = new HashSet<DatapointListener>();
		this.storageService = HistoryDataStorageServiceRegistry.getInstance()
				.getService(HistoryDataStorageServiceImpl.class.getName());

		if (this.storageService != null)
			startPollingJob();
		else {
			HistoryDataStorageServiceRegistry.getInstance().addServiceListener(
					new ServiceRegistryListener() {
						@Override
						public void serviceRemoved(String serviceName) {
							// TODO Auto-generated method stub

						}

						@Override
						public void serviceModified(String serviceName) {
							// TODO Auto-generated method stub

						}

						@Override
						public void serviceAdded(String serviceName) {
							if (serviceName
									.equals(HistoryDataStorageServiceImpl.class
											.getName())) {
								storageService = HistoryDataStorageServiceRegistry
										.getInstance()
										.getService(
												HistoryDataStorageServiceImpl.class
														.getName());
								startPollingJob();
							}

						}
					});
		}

	}

	private void startPollingJob() {
		Timer timer = new Timer();
		System.out.println(datapoints.size());
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

						try {
							MeterMeasure value = getNewMeasure(addr
									.getAddress());
							DatapointReading reading = new DatapointReading(
									new DatapointValue(value.getTotalPower()
											+ ""));
							//notify update
							notifyDatapointUpdate(addr, new DatapointReading[]{reading});
							System.out.println("METER UPDATE "+ reading);
							
							// store reading
							storageService.addValue(addr.getAddress(), reading
									.getTimestamp(), reading.getValue()
									.toString());
						} catch (MalformedURLException e) {
						}

					}
				}, 1000, interval);
			}
		}

	}

	/**
	 * Gets the new measure.
	 * 
	 * @param address
	 *            the address
	 * @return the new measure
	 * @throws MalformedURLException
	 *             the malformed url exception
	 */
	private MeterMeasure getNewMeasure(String address)
			throws MalformedURLException {
		return new MeterMeasure(fetchSensorReading_HTTPS(new URL("https://"
				+ address + "/reading")));
	}

	/**
	 * Fetch sensor reading_ https.
	 * 
	 * @param address
	 *            the address
	 * @return the string
	 */
	private String fetchSensorReading_HTTPS(URL address) {

		String res = "";
		// Sensor server uses HTTPS and use a self-signed certificate, then we
		// must
		// create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs,
					String authType) {
				return;
			}

			public void checkServerTrusted(X509Certificate[] certs,
					String authType) {
				return;
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		// now we are prepared to make HTTPS requests to a server with s
		// self signed certificates
		try {
			URL siteURL = address;
			URLConnection connectionURL = siteURL.openConnection();

			// Setting the doInput flag to indicate that the application intends
			// to read data from the URL connection.
			connectionURL.setDoInput(true);
			String userNamePasswordBase64 = "Basic "
					+ base64Encode(this.username + ":" + this.password);
			connectionURL.setRequestProperty("Authorization",
					userNamePasswordBase64);
			// connectionURL.connect(); //connection is already open

			BufferedReader in = new BufferedReader(new InputStreamReader(
					connectionURL.getInputStream()));

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				// System.out.println(inputLine);
				res = res + inputLine + "\n";
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * Base64 encode.
	 * 
	 * @param string
	 *            the string
	 * @return the string
	 */
	private static String base64Encode(String string) {
		String encodedString = "";
		byte bytes[] = string.getBytes();
		int i = 0;
		int pad = 0;
		while (i < bytes.length) {
			byte b1 = bytes[i++];
			byte b2;
			byte b3;
			if (i >= bytes.length) {
				b2 = 0;
				b3 = 0;
				pad = 2;
			} else {
				b2 = bytes[i++];
				if (i >= bytes.length) {
					b3 = 0;
					pad = 1;
				} else
					b3 = bytes[i++];
			}
			byte c1 = (byte) (b1 >> 2);
			byte c2 = (byte) (((b1 & 0x3) << 4) | (b2 >> 4));
			byte c3 = (byte) (((b2 & 0xf) << 2) | (b3 >> 6));
			byte c4 = (byte) (b3 & 0x3f);
			encodedString += base64Array[c1];
			encodedString += base64Array[c2];
			switch (pad) {
			case 0:
				encodedString += base64Array[c3];
				encodedString += base64Array[c4];
				break;
			case 1:
				encodedString += base64Array[c3];
				encodedString += "=";
				break;
			case 2:
				encodedString += "==";
				break;
			}
		}
		return encodedString;
	}

	private final static char base64Array[] = { 'A', 'B', 'C', 'D', 'E', 'F',
			'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
			'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
			'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
			't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', '+', '/' };

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

	private void notifyDatapointError(DatapointAddress address, ErrorType error) {
		synchronized (listeners) {
			Iterator<DatapointListener> it = listeners.iterator();
			while (it.hasNext()) {
				DatapointListener listener = it.next();
				listener.onDatapointError(address, error);
			}
		}
	}

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

		try {
			MeterMeasure value = getNewMeasure(address.getAddress());
			DatapointReading reading = new DatapointReading(new DatapointValue(
					value.getTotalPower() + ""));
			// store reading
			storageService.addValue(address.getAddress(), value.ts,
					value.getTotalPower() + "");

			readCallback.onReadCompleted(address,
					new DatapointReading[] { reading }, 0);
			return 0;
		} catch (MalformedURLException e) {
			readCallback.onReadAborted(address, ErrorType.DATAPOINT_NOT_FOUND,
					0);
		}

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

	/**
	 * @author Diogo Anjos
	 */
	public class MeterMeasure {

		private String id;
		private String name;
		private long ts;
		private Phase phase1;
		private Phase phase2;
		private Phase phase3;

		public MeterMeasure(String measureJSON) {
			JSONParser parser = new JSONParser();
			try {
				JSONObject measureJSONobject = (JSONObject) parser
						.parse(measureJSON);
				id = (String) measureJSONobject.get("id");
				name = (String) measureJSONobject.get("name");
				ts = (Long) measureJSONobject.get("timestamp");
				phase1 = parsePhaseJSONobject(measureJSONobject, "1");
				phase2 = parsePhaseJSONobject(measureJSONobject, "2");
				phase3 = parsePhaseJSONobject(measureJSONobject, "3");
			} catch (ParseException e) {
				System.err
						.println("[Diogo] Constructor JavaMeasure.Java: probably malformed JSON file...");
				e.printStackTrace();
			}
		}

		public String getID() {
			return id;
		}

		public String getName() {
			return name;
		}

		public long geTimestamp() {
			return ts;
		}

		public long getVoltagePhase1() {
			return phase1.getVoltage();
		}

		public float getCurrentPhase1() {
			return phase1.getCurrent();
		}

		public float getPowerFactorPhase1() {
			return phase1.getPowerFactor();
		}

		public long getVoltagePhase2() {
			return phase2.getVoltage();
		}

		public float getCurrentPhase2() {
			return phase2.getCurrent();
		}

		public float getPowerFactorPhase2() {
			return phase2.getPowerFactor();
		}

		public long getVoltagePhase3() {
			return phase3.getVoltage();
		}

		public float getCurrentPhase3() {
			return phase3.getCurrent();
		}

		public float getPowerFactorPhase3() {
			return phase3.getPowerFactor();
		}

		public float getTotalPower() {
			float totalPower1 = getCurrentPhase1() * getVoltagePhase1()
					* getPowerFactorPhase1();
			float totalPower2 = getCurrentPhase2() * getVoltagePhase2()
					* getPowerFactorPhase2();
			float totalPower3 = getCurrentPhase3() * getVoltagePhase3()
					* getPowerFactorPhase3();
			float result = totalPower1 + totalPower2 + totalPower3;

			float val = result * 100;
			val = Math.round(val);
			val = val / 100;
			return val;
		}

		@Override
		public String toString() {
			return "[Measure Dump] \n| id: " + id + "\n| name: " + name
					+ "\n| ts: " + ts + "\n| phase_1:" + phase1.toString()
					+ "\n| phase_2:" + phase2.toString() + "\n| phase_3:"
					+ phase3.toString();
		}

		private Phase parsePhaseJSONobject(JSONObject obj, String phaseNumber) {
			JSONObject phases = (JSONObject) obj.get((String) "phases");
			JSONObject specificPhase = (JSONObject) phases.get(phaseNumber);

			Float current = Float.parseFloat(specificPhase.get("current")
					.toString());
			int voltage = Integer.parseInt(specificPhase.get("voltage")
					.toString());
			Float powerfactor = Float.parseFloat(specificPhase.get(
					"powerfactor").toString());

			Phase result = new Phase(voltage, current, powerfactor);
			return result;
		}

		private class Phase {
			private int voltage;
			private float current;
			private float powerFactor;

			public Phase(int v, float c, float pf) {
				voltage = v;
				current = c;
				powerFactor = pf;
			}

			public long getVoltage() {
				return voltage;
			}

			public float getCurrent() {
				return current;
			}

			public float getPowerFactor() {
				return powerFactor;
			}

			@Override
			public String toString() {
				return " voltage: " + voltage + " | current: " + current
						+ " | powerfactor: " + powerFactor;
			}

		}

	}

	@Override
	public String getImplementationName() {
		return this.getClass().getName();
	}


}
