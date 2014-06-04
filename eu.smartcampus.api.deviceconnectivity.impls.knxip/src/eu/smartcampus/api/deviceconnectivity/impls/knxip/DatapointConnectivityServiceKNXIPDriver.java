package eu.smartcampus.api.deviceconnectivity.impls.knxip;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import eu.smartcampus.api.deviceconnectivity.DatapointAddress;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata.AccessType;
import eu.smartcampus.api.deviceconnectivity.DatapointReading;
import eu.smartcampus.api.deviceconnectivity.DatapointValue;
import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;
import eu.smartcampus.api.deviceconnectivity.Logger;
import eu.smartcampus.api.historydatastorage.HistoryDataStorageServiceImpl;
import eu.smartcampus.api.historydatastorage.HistoryValue;
import eu.smartcampus.api.historydatastorage.IHistoryDataStorageService;
import eu.smartcampus.api.historydatastorage.osgi.registries.HistoryDataStorageServiceRegistry;
import eu.smartcampus.api.osgi.registries.IServiceRegistry.ServiceRegistryListener;

/**
 * The Class DatapointConnectivityServiceKNXIPDriver.
 */
public class DatapointConnectivityServiceKNXIPDriver implements
		IDatapointConnectivityService {
	static private Logger log = Logger
			.getLogger(DatapointConnectivityServiceKNXIPDriver.class.getName());

	/**
	 * The driver.
	 */
	private KNXGatewayIPDriver driver;

	/**
	 * The listeners set
	 */
	private Set<DatapointListener> listeners;

	/**
	 * The datapoints.
	 */
	private Map<DatapointAddress, DatapointMetadata> datapoints;

	private IHistoryDataStorageService storageService;

	/**
	 * Instantiates a new datapoint connectivity service knxip driver.
	 * 
	 * @param driver
	 *            the driver
	 * @param datapoints
	 *            the datapoints
	 */
	public DatapointConnectivityServiceKNXIPDriver() {
		super();
		this.driver = KNXGatewayIPDriver.getInstance();
		this.datapoints = KNXServiceConfig.loadDatapointsConfigs();
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
		int delay = 1000;
		Timer timer = new Timer();
		Iterator<Entry<DatapointAddress, DatapointMetadata>> elems = datapoints
				.entrySet().iterator();
		while (elems.hasNext()) {
			delay = delay + 200;
			Map.Entry<DatapointAddress, DatapointMetadata> entry = (Map.Entry<DatapointAddress, DatapointMetadata>) elems
					.next();
			final DatapointAddress addr = entry.getKey();
			long interval = entry.getValue().getCurrentSamplingInterval();
			if (interval != 0) {

				timer.scheduleAtFixedRate(new TimerTask() {

					@Override
					public void run() {

						if (KNXGatewayIPDriver.getInstance().isConnected()) {
							requestDatapointRead(addr, new ReadCallback() {

								@Override
								public void onReadCompleted(
										DatapointAddress address,
										DatapointReading[] readings,
										int requestId) {

									
									// notify update
									notifyDatapointUpdate(
											addr,
											new DatapointReading[] { readings[0] });

									// store reading
									storageService.addValue(addr.getAddress(),
											readings[0].getTimestamp(),
											readings[0].getValue().toString());

								}

								@Override
								public void onReadAborted(
										DatapointAddress address,
										ErrorType reason, int requestId) {
								}
							});
						}

					}
				}, delay, interval);
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
	public DatapointMetadata getDatapointMetadata(DatapointAddress address) {
		return this.datapoints.get(address);
	}

	@Override
	public int requestDatapointRead(DatapointAddress address,
			ReadCallback readCallback) {
		DatapointMetadata m = getDatapointMetadata(address);
		String addr = address.getAddress();

		if (m.getAccessType() == AccessType.WRITE_ONLY) {
			readCallback.onReadAborted(address,
					ErrorType.UNSUPORTED_DATAPOINT_OPERATION, 0);
			return 0;
		}

		switch (m.getDatatype()) {
		case INTEGER:
			// Test scale type (0-100)
			if (m.getDisplayMax() <= 100 && m.getDisplayMin() > 0) {
				float val = driver.readScalar(addr);
				if (val == Float.MIN_VALUE) {
					readCallback.onReadAborted(address,
							ErrorType.DEVICE_CONNECTION_ERROR, 0);
					return 0;
				}
				String value = val + "";
				DatapointReading reading = new DatapointReading(
						new DatapointValue(value));
				readCallback.onReadCompleted(address,
						new DatapointReading[] { reading }, 0);
			} else {
				float val = driver.readFloat(addr);
				if (val == Float.MIN_VALUE) {
					readCallback.onReadAborted(address,
							ErrorType.DEVICE_CONNECTION_ERROR, 0);
					return 0;
				}
				String value = val + "";
				DatapointReading reading = new DatapointReading(
						new DatapointValue(value));
				readCallback.onReadCompleted(address,
						new DatapointReading[] { reading }, 0);
			}
			break;
		case BOOLEAN:
			boolean value = driver.readBoolean(addr);
			DatapointReading reading = new DatapointReading(new DatapointValue(
					value));
			readCallback.onReadCompleted(address,
					new DatapointReading[] { reading }, 0);
			break;
		case STRING:// TODO not used yet
			readCallback.onReadAborted(address,
					ErrorType.UNSUPORTED_DATAPOINT_OPERATION, 0);
			break;
		}

		readCallback.onReadAborted(address,
				ErrorType.UNSUPORTED_DATAPOINT_OPERATION, 0);
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
		DatapointMetadata m = getDatapointMetadata(address);
		String addr = address.getAddress();

		boolean ack = false;

		if (m.getAccessType() == AccessType.READ_ONLY) {
			writeCallback.onWriteAborted(address,
					ErrorType.UNSUPORTED_DATAPOINT_OPERATION, 0);
			return 0;
		}

		switch (m.getDatatype()) {
		case INTEGER:
			ack = driver.writeScalar(addr, values[0].getIntValue());

			break;
		case BOOLEAN:
			ack = driver.writeBool(addr, values[0].getBooleanValue());

			break;

		case STRING:// TODO not used yet

			break;
		}

		if (ack) {
			writeCallback.onWriteCompleted(address,
					WritingConfirmationLevel.DEVICE_ACTION_TAKEN, 0);
			notifyDatapointUpdate(address,
					new DatapointReading[] { new DatapointReading(values[0]) });
		} else {
			writeCallback.onWriteAborted(address,
					ErrorType.DEVICE_CONNECTION_ERROR, 0);
		}

		return 0;
	}

	@Override
	public String getImplementationName() {
		return "knx";
	}

}
