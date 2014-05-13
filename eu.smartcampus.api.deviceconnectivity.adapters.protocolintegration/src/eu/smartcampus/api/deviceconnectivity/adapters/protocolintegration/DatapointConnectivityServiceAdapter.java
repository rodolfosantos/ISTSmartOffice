package eu.smartcampus.api.deviceconnectivity.adapters.protocolintegration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import eu.smartcampus.api.deviceconnectivity.DatapointAddress;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata;
import eu.smartcampus.api.deviceconnectivity.DatapointReading;
import eu.smartcampus.api.deviceconnectivity.DatapointValue;
import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;

/**
 * The Class DatapointConnectivityServiceAdapter.
 */
public class DatapointConnectivityServiceAdapter implements
		IDatapointConnectivityService {

	/**
	 * The datapoints drivers.
	 */
	private Map<DatapointAddress, IDatapointConnectivityService> datapointsDrivers;

	/**
	 * The listeners.
	 */
	private Set<DatapointListener> listeners;

	/**
	 * Instantiates a new datapoint connectivity service adapter.
	 * 
	 * @param datapointsDrivers
	 *            the datapoints drivers
	 */
	public DatapointConnectivityServiceAdapter(
			Set<IDatapointConnectivityService> datapointsDrivers) {
		super();
		this.listeners = new HashSet<DatapointListener>();
		this.datapointsDrivers = initDatapointsDrivers(datapointsDrivers);
	}

	/**
	 * Instantiates datapoints and correspondent driver implementation mapping
	 * 
	 * @param datapointsDrivers
	 *            the datapoints drivers
	 */
	private Map<DatapointAddress, IDatapointConnectivityService> initDatapointsDrivers(
			Set<IDatapointConnectivityService> datapointsDrivers) {
		Map<DatapointAddress, IDatapointConnectivityService> result = new HashMap<DatapointAddress, IDatapointConnectivityService>();

		Iterator<IDatapointConnectivityService> driversIterator = datapointsDrivers
				.iterator();
		while (driversIterator.hasNext()) {
			IDatapointConnectivityService driver = (IDatapointConnectivityService) driversIterator
					.next();

			DatapointAddress[] allDatapoints = driver.getAllDatapoints();
			for (DatapointAddress datapointAddress : allDatapoints) {
				result.put(datapointAddress, driver);
			}

		}

		return result;

	}

	public void addDatapointListener(DatapointListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}

	}

	/**
	 * Gets the correspondent driver for each Datapoint
	 * 
	 * @param address
	 *            the address
	 * @return the driver
	 */
	private IDatapointConnectivityService getDriver(DatapointAddress address) {
		return datapointsDrivers.get(address);
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

	public DatapointAddress[] getAllDatapoints() {
		DatapointAddress[] result = new DatapointAddress[datapointsDrivers
				.size()];

		Iterator<DatapointAddress> it = datapointsDrivers.keySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			DatapointAddress datapointAddress = (DatapointAddress) it.next();
			result[i++] = datapointAddress;
		}
		return result;
	}

	public DatapointMetadata getDatapointMetadata(DatapointAddress address) {
		try {
			return getDriver(address).getDatapointMetadata(address);
		} catch (OperationFailedException e) {
			return null;
		}
	}

	public void removeDatapointListener(DatapointListener listener) {
		this.listeners.remove(listener);
	}

	public int requestDatapointRead(DatapointAddress address,
			ReadCallback readCallback) {
		return getDriver(address).requestDatapointRead(address, readCallback);
	}

	public int requestDatapointWrite(DatapointAddress address,
			DatapointValue[] values, WriteCallback writeCallback) {
		return getDriver(address).requestDatapointWrite(address, values,
				writeCallback);
	}

	@Override
	public int requestDatapointWindowRead(DatapointAddress address,
			long startTimestamp, long finishTimestamp, ReadCallback readCallback) {
		return getDriver(address).requestDatapointWindowRead(address,
				startTimestamp, finishTimestamp, readCallback);
	}

	@Override
	public String getImplementationName() {
		return this.getClass().getName();
	}

}
