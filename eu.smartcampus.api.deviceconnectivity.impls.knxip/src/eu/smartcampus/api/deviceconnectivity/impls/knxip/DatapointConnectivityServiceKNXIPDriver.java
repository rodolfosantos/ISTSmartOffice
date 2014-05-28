package eu.smartcampus.api.deviceconnectivity.impls.knxip;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import tuwien.auto.calimero.exception.KNXException;
import eu.smartcampus.api.deviceconnectivity.DatapointAddress;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata.AccessType;
import eu.smartcampus.api.deviceconnectivity.DatapointReading;
import eu.smartcampus.api.deviceconnectivity.DatapointValue;
import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;
import eu.smartcampus.api.historydatastorage.HistoryDataStorageServiceImpl;
import eu.smartcampus.api.historydatastorage.HistoryValue;
import eu.smartcampus.api.historydatastorage.IHistoryDataStorageService;
import eu.smartcampus.api.historydatastorage.osgi.registries.HistoryDataStorageServiceRegistry;

/**
 * The Class DatapointConnectivityServiceKNXIPDriver.
 */
public class DatapointConnectivityServiceKNXIPDriver implements
		IDatapointConnectivityService {

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
		//startPollingJob();
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

						requestDatapointRead(addr, new ReadCallback() {

							@Override
							public void onReadCompleted(
									DatapointAddress address,
									DatapointReading[] readings, int requestId) {

								// store reading
								storageService.addValue(addr.getAddress(),
										readings[0].getTimestamp(), readings[0]
												.getValue().toString());
							}
							@Override
							public void onReadAborted(DatapointAddress address,
									ErrorType reason, int requestId) {
							}
						});

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
				try {
					String value = driver.readScalar(addr) + "";
					DatapointReading reading = new DatapointReading(
							new DatapointValue(value));
					readCallback.onReadCompleted(address,
							new DatapointReading[] { reading }, 0);

				} catch (KNXException e) {
					e.printStackTrace();
					readCallback.onReadAborted(address,
							ErrorType.DEVICE_CONNECTION_ERROR, 0);

				} catch (InterruptedException e) {
					e.printStackTrace();
					readCallback.onReadAborted(address,
							ErrorType.DEVICE_CONNECTION_ERROR, 0);
				}
			} else {
				try {
					String value = driver.readFloat(addr) + "";
					DatapointReading reading = new DatapointReading(
							new DatapointValue(value));
					readCallback.onReadCompleted(address,
							new DatapointReading[] { reading }, 0);
				} catch (KNXException e) {
					e.printStackTrace();
					readCallback.onReadAborted(address,
							ErrorType.DEVICE_CONNECTION_ERROR, 0);
				} catch (InterruptedException e) {
					e.printStackTrace();
					readCallback.onReadAborted(address,
							ErrorType.DEVICE_CONNECTION_ERROR, 0);
				}
			}
			break;
		case BOOLEAN:
			try {
				boolean value = driver.readBoolean(addr);
				DatapointReading reading = new DatapointReading(
						new DatapointValue(value));
				readCallback.onReadCompleted(address,
						new DatapointReading[] { reading }, 0);
			} catch (KNXException e) {
				e.printStackTrace();
				readCallback.onReadAborted(address,
						ErrorType.DEVICE_CONNECTION_ERROR, 0);
			} catch (InterruptedException e) {
				e.printStackTrace();
				readCallback.onReadAborted(address,
						ErrorType.DEVICE_CONNECTION_ERROR, 0);
			}
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

		if (m.getAccessType() == AccessType.READ_ONLY) {
			writeCallback.onWriteAborted(address,
					ErrorType.UNSUPORTED_DATAPOINT_OPERATION, 0);
			return 0;
		}

		switch (m.getDatatype()) {
		case INTEGER:
			try {
				driver.writeScalar(addr, values[0].getIntValue());
				writeCallback.onWriteCompleted(address,
						WritingConfirmationLevel.DEVICE_ACTION_TAKEN, 0);
			} catch (KNXException e) {
				e.printStackTrace();
				writeCallback.onWriteAborted(address,
						ErrorType.DEVICE_CONNECTION_ERROR, 0);
			}
			break;
		case BOOLEAN:
			try {
				driver.writeBool(addr, values[0].getBooleanValue());
				writeCallback.onWriteCompleted(address,
						WritingConfirmationLevel.DEVICE_ACTION_TAKEN, 0);
			} catch (KNXException e) {
				e.printStackTrace();
				writeCallback.onWriteAborted(address,
						ErrorType.DEVICE_CONNECTION_ERROR, 0);
			}
			break;

		case STRING:// TODO not used yet
			writeCallback.onWriteAborted(address,
					ErrorType.UNSUPORTED_DATAPOINT_OPERATION, 0);
			break;
		}

		// writeCallback.onWriteAborted(address,
		// ErrorType.UNSUPORTED_DATAPOINT_OPERATION, 0);
		return 0;
	}

	@Override
	public String getImplementationName() {
		return this.getClass().getName();
	}

}
