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

	private Map<DatapointAddress, DatapointAddress> datapointsDriversAddressMap;
	private Map<DatapointAddress, DatapointAddress> datapointsDriversReverseAddressMap;

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
		initDatapointsDriversMap(datapointsDrivers);
	}

	/**
	 * Instantiates datapoints and correspondent driver implementation mapping
	 * 
	 * @param datapointsDrivers
	 *            the datapoints drivers
	 */
	private void initDatapointsDriversMap(
			Collection<IDatapointConnectivityService> datapointsDrivers) {
		this.datapointsDriversMap = new HashMap<DatapointAddress, IDatapointConnectivityService>();
		this.datapointsDriversAddressMap = new HashMap<DatapointAddress, DatapointAddress>();
		this.datapointsDriversReverseAddressMap = new HashMap<DatapointAddress, DatapointAddress>();

		Iterator<IDatapointConnectivityService> driversIterator = datapointsDrivers
				.iterator();
		while (driversIterator.hasNext()) {
			IDatapointConnectivityService driver = (IDatapointConnectivityService) driversIterator
					.next();

			DatapointAddress[] allDatapoints = driver.getAllDatapoints();
			int i = 0;
			for (DatapointAddress datapointAddress : allDatapoints) {
				// ================================================================
				// Address translation
				String newAddr = driver.getImplementationName() + i;
				datapointsDriversMap.put(datapointAddress, driver);
				datapointsDriversReverseAddressMap.put(datapointAddress,
						new DatapointAddress(newAddr));
				datapointsDriversAddressMap.put(new DatapointAddress(newAddr),
						datapointAddress);
				i++;
			}
		}
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
		return datapointsDriversMap.get(datapointsDriversAddressMap
				.get(address));
	}

	private DatapointAddress getRealDatapointAddress(DatapointAddress address) {
		return datapointsDriversAddressMap.get(address);
	}

	public DatapointAddress[] getAllDatapoints() {
		DatapointAddress[] result = new DatapointAddress[datapointsDriversAddressMap
				.size()];

		Iterator<DatapointAddress> it = datapointsDriversAddressMap.keySet()
				.iterator();
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
		if (d == null)
			throw new OperationFailedException(ErrorType.DATAPOINT_NOT_FOUND);
		return getDriver(address).getDatapointMetadata(
				getRealDatapointAddress(address));

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
		return d.requestDatapointRead(getRealDatapointAddress(address),
				readCallback);
	}

	public int requestDatapointWrite(DatapointAddress address,
			DatapointValue[] values, WriteCallback writeCallback) {
		IDatapointConnectivityService d = getDriver(address);
		if (d == null) {
			writeCallback.onWriteAborted(address,
					ErrorType.DATAPOINT_NOT_FOUND, 0);
			return 0;
		}
		return d.requestDatapointWrite(getRealDatapointAddress(address),
				values, writeCallback);
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
		return d.requestDatapointWindowRead(getRealDatapointAddress(address),
				startTimestamp, finishTimestamp, readCallback);
	}

	@Override
	public String getImplementationName() {
		return this.getClass().getName();
	}

}
