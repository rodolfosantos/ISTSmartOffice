package eu.smartcampus.api.deviceconnectivity.impls.knxip;

import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import eu.smartcampus.api.deviceconnectivity.DatapointAddress;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata.AccessType;
import eu.smartcampus.api.deviceconnectivity.DatapointReading;
import eu.smartcampus.api.deviceconnectivity.DatapointValue;
import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;
import eu.smartcampus.api.deviceconnectivity.Logger;
import eu.smartcampus.api.historydatastorage.HistoryValue;
import eu.smartcampus.api.historydatastorage.IHistoryDataStorageService;

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
		this.driver = new KNXGatewayIPDriver("172.20.70.147");
		try {
			this.driver.start();
		} catch (UnknownHostException e) {
			driver.reconnectGateway();
		}
		this.datapoints = KNXServiceConfig.loadDatapointsConfigs();
		this.listeners = new HashSet<DatapointListener>();
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
				try {
					float val = driver.readPercentage(addr);
					String value = val + "";
					DatapointReading reading = new DatapointReading(
							new DatapointValue(value));
					readCallback.onReadCompleted(address,
							new DatapointReading[] { reading }, 0);
				} catch (Exception e) {
					readCallback.onReadAborted(address,
							ErrorType.DEVICE_CONNECTION_ERROR, 0);
					return 0;
				}
				
			} else {
				try {
					float val = driver.read2Bytes(addr);
					String value = val + "";
					DatapointReading reading = new DatapointReading(
							new DatapointValue(value));
					readCallback.onReadCompleted(address,
							new DatapointReading[] { reading }, 0);
				} catch (Exception e) {
					readCallback.onReadAborted(address,
							ErrorType.DEVICE_CONNECTION_ERROR, 0);
					return 0;
				}
			}
			break;
		case BOOLEAN:
			
			try {
				boolean value = driver.readSwitch(addr);
				DatapointReading reading = new DatapointReading(new DatapointValue(
						value));
				readCallback.onReadCompleted(address,
						new DatapointReading[] { reading }, 0);
			} catch (Exception e) {
				readCallback.onReadAborted(address,
						ErrorType.DEVICE_CONNECTION_ERROR, 0);
				return 0;				
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
				driver.writePercentage(addr, values[0].getIntValue());
				writeCallback.onWriteCompleted(address,
						WritingConfirmationLevel.DEVICE_ACTION_TAKEN, 0);
				notifyDatapointUpdate(address,
						new DatapointReading[] { new DatapointReading(values[0]) });
			} catch (Exception e) {
				writeCallback.onWriteAborted(address,
						ErrorType.DEVICE_CONNECTION_ERROR, 0);
			}
			

			break;
		case BOOLEAN:
			try {
				driver.writeSwitch(addr, values[0].getBooleanValue());
				writeCallback.onWriteCompleted(address,
						WritingConfirmationLevel.DEVICE_ACTION_TAKEN, 0);
				notifyDatapointUpdate(address,
						new DatapointReading[] { new DatapointReading(values[0]) });
			} catch (Exception e) {
				writeCallback.onWriteAborted(address,
						ErrorType.DEVICE_CONNECTION_ERROR, 0);
			}

			break;

		case STRING:// TODO not used yet

			break;
		}
		
		return 0;
	}

	@Override
	public String getImplementationName() {
		return "knx";
	}

}
