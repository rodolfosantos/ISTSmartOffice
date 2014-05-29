package eu.smartcampus.api.deviceconnectivity.adapters.protocolintegration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import eu.smartcampus.api.deviceconnectivity.DatapointAddress;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata;
import eu.smartcampus.api.deviceconnectivity.DatapointValue;
import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;

/**
 * The Class DatapointConnectivityServiceAdapter.
 */
public class DatapointConnectivityServiceAdapter implements
		IDatapointConnectivityService {

	/**
	 * The datapoints drivers mapping.
	 */
	private Map<DatapointAddress, IDatapointConnectivityService> datapointsDriversMap;
	
	/**
	 * The datapoints drivers.
	 */
	private Collection<IDatapointConnectivityService> drivers;

	/**
	 * Instantiates a new datapoint connectivity service adapter.
	 * 
	 * @param datapointsDrivers
	 *            the datapoints drivers
	 */
	public DatapointConnectivityServiceAdapter(
			Collection<IDatapointConnectivityService> datapointsDrivers) {
		super();
		this.drivers = datapointsDrivers;
		this.datapointsDriversMap = initDatapointsDriversMap(datapointsDrivers);
	}

	/**
	 * Instantiates datapoints and correspondent driver implementation mapping
	 * 
	 * @param datapointsDrivers
	 *            the datapoints drivers
	 */
	private Map<DatapointAddress, IDatapointConnectivityService> initDatapointsDriversMap(
			Collection<IDatapointConnectivityService> datapointsDrivers) {
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
		for (IDatapointConnectivityService driver : drivers) {
			driver.addDatapointListener(listener);
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
		return datapointsDriversMap.get(address);
	}

	public DatapointAddress[] getAllDatapoints() {
		DatapointAddress[] result = new DatapointAddress[datapointsDriversMap
				.size()];

		Iterator<DatapointAddress> it = datapointsDriversMap.keySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			DatapointAddress datapointAddress = (DatapointAddress) it.next();
			result[i++] = datapointAddress;
		}
		return result;
	}

	public DatapointMetadata getDatapointMetadata(DatapointAddress address)
			throws OperationFailedException {
		IDatapointConnectivityService d = getDriver(address);
		if(d == null)
			throw new OperationFailedException(ErrorType.DATAPOINT_NOT_FOUND);
		return getDriver(address).getDatapointMetadata(address);

	}

	public void removeDatapointListener(DatapointListener listener) {
		for (IDatapointConnectivityService driver : drivers) {
			driver.removeDatapointListener(listener);
		}
	}

	public int requestDatapointRead(DatapointAddress address,
			ReadCallback readCallback) {
		IDatapointConnectivityService d = getDriver(address);
		if (d == null) {
			readCallback.onReadAborted(address, ErrorType.DATAPOINT_NOT_FOUND,
					0);
			return 0;
		}
		return d.requestDatapointRead(address, readCallback);
	}

	public int requestDatapointWrite(DatapointAddress address,
			DatapointValue[] values, WriteCallback writeCallback) {
		IDatapointConnectivityService d = getDriver(address);
		if (d == null) {
			writeCallback.onWriteAborted(address,
					ErrorType.DATAPOINT_NOT_FOUND, 0);
			return 0;
		}
		return d.requestDatapointWrite(address, values, writeCallback);
	}

	@Override
	public int requestDatapointWindowRead(DatapointAddress address,
			long startTimestamp, long finishTimestamp, ReadCallback readCallback) {
		IDatapointConnectivityService d = getDriver(address);
		if (d == null) {
			readCallback.onReadAborted(address, ErrorType.DATAPOINT_NOT_FOUND,
					0);
			return 0;
		}
		return d.requestDatapointWindowRead(address, startTimestamp,
				finishTimestamp, readCallback);
	}

	@Override
	public String getImplementationName() {
		return this.getClass().getName();
	}

}
