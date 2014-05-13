package eu.smartcampus.api.deviceconnectivity.impls.knxip;

import java.util.Iterator;
import java.util.Map;

import tuwien.auto.calimero.exception.KNXException;
import eu.smartcampus.api.deviceconnectivity.DatapointAddress;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata;
import eu.smartcampus.api.deviceconnectivity.DatapointReading;
import eu.smartcampus.api.deviceconnectivity.DatapointValue;
import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;

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
	 * The datapoints.
	 */
	private Map<DatapointAddress, DatapointMetadata> datapoints;

	/**
	 * Instantiates a new datapoint connectivity service knxip driver.
	 * 
	 * @param driver
	 *            the driver
	 * @param datapoints
	 *            the datapoints
	 */
	public DatapointConnectivityServiceKNXIPDriver(
			Map<DatapointAddress, DatapointMetadata> datapoints) {
		super();
		this.driver = KNXGatewayIPDriver.getInstance();
		driver.start();
		this.datapoints = datapoints;
	}

	@Override
	public void addDatapointListener(DatapointListener listener) {
		return;// TODO never call this
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
		return;// TODO never call this
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

		case STRING:// TODO not used yet
			readCallback.onReadAborted(address,
					ErrorType.UNSUPORTED_DATAPOINT_OPERATION, 0);
		}

		readCallback.onReadAborted(address,
				ErrorType.UNSUPORTED_DATAPOINT_OPERATION, 0);
		return 0;
	}

	@Override
	public int requestDatapointWindowRead(DatapointAddress address,
			long startTimestamp, long finishTimestamp, ReadCallback readCallback) {
		readCallback.onReadAborted(address,
				ErrorType.UNSUPORTED_DATAPOINT_OPERATION, 0);// not yet (missing
																// history data
																// storage)
		return 0;
	}

	@Override
	public int requestDatapointWrite(DatapointAddress address,
			DatapointValue[] values, WriteCallback writeCallback) {
		DatapointMetadata m = getDatapointMetadata(address);
		String addr = address.getAddress();

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
