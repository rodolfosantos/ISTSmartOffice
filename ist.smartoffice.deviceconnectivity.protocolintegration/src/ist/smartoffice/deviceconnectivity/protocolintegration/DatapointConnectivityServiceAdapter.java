package ist.smartoffice.deviceconnectivity.protocolintegration;

import ist.smartoffice.datapointconnectivity.DatapointAddress;
import ist.smartoffice.datapointconnectivity.DatapointMetadata;
import ist.smartoffice.datapointconnectivity.DatapointValue;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService;
import ist.smartoffice.datapointconnectivity.osgi.registries.DatapointConnectivityServiceRegistry;
import ist.smartoffice.logger.Logger;
import ist.smartoffice.logger.LoggerService;
import ist.smartoffice.osgi.registries.IServiceRegistry.ServiceRegistryListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * The Class DatapointConnectivityServiceAdapter.
 */
public class DatapointConnectivityServiceAdapter implements
		IDatapointConnectivityService {
	static private Logger log = LoggerService.getInstance().getLogger(
			DatapointConnectivityServiceAdapter.class.getName());

	/**
	 * The datapoints drivers mapping.
	 */
	private Map<DatapointAddress, IDatapointConnectivityService> datapointsDriversMap;


	/**
	 * The datapoints drivers.
	 */
	private Set<IDatapointConnectivityService> drivers;

	/**
	 * The listeners set
	 */
	private Set<DatapointListener> listeners;

	/**
	 * Instantiates a new datapoint connectivity service adapter.
	 * 
	 * @param datapointsDrivers
	 *            the datapoints drivers
	 */
	public DatapointConnectivityServiceAdapter() {
		super();
		this.listeners = new HashSet<IDatapointConnectivityService.DatapointListener>();

		this.datapointsDriversMap = new HashMap<DatapointAddress, IDatapointConnectivityService>();
		this.drivers = new HashSet<IDatapointConnectivityService>();

		String[] currServices = DatapointConnectivityServiceRegistry
				.getInstance().getRegisteredServicesNames();

		for (String serviceName : currServices) {
			System.out.println("currServices:" + serviceName);
			if (serviceName.contains("ist.smartoffice.deviceconnectivity.")
					&& !serviceName.equals(Activator.PROVIDED_SERVICE_NAME)) {
				addServiceImplementation(serviceName);
			}
		}

		DatapointConnectivityServiceRegistry.getInstance().addServiceListener(
				new ServiceRegistryListener() {

					@Override
					public void serviceRemoved(String serviceName) {
						// TODO Auto-generated method stub
						System.err.println("TODO");
					}

					@Override
					public void serviceModified(String serviceName) {
						// TODO Auto-generated method stub
						System.err.println("TODO");
					}

					@Override
					public void serviceAdded(String serviceName) {
						if (serviceName
								.contains("ist.smartoffice.deviceconnectivity.")
								&& !serviceName
										.equals(Activator.PROVIDED_SERVICE_NAME)) {
							addServiceImplementation(serviceName);
						}

					}
				});

	}

	private void addServiceImplementation(String s) {
		IDatapointConnectivityService driver = DatapointConnectivityServiceRegistry
				.getInstance().getService(s);
		drivers.add(driver);

		DatapointAddress[] allDatapoints = driver.getAllDatapoints();
		int i = 0;
		for (DatapointAddress datapointAddress : allDatapoints) {
			// ================================================================
			// Address translation
			String newAddr = driver.getImplementationName() + i;
			datapointsDriversMap.put(datapointAddress, driver);
//			datapointsDriversReverseAddressMap.put(datapointAddress,
//					new DatapointAddress(newAddr));
//			datapointsDriversAddressMap.put(new DatapointAddress(newAddr),
//					datapointAddress);

			i++;
		}

		// add event listener
		driver.addDatapointListener(new DatapointListener() {

			@Override
			public void onDatapointUpdate(DatapointAddress address,
					DatapointValue[] values) {
				notifyDatapointUpdate(
						address, values);

			}

			@Override
			public void onDatapointError(DatapointAddress address,
					ErrorType error) {
				notifyDatapointError(
						address, error);
			}

			@Override
			public void onDatapointAddressListChanged(DatapointAddress[] address) {
				// TODO Auto-generated method stub

			}
		});

		DatapointAddress[] addrList = new DatapointAddress[datapointsDriversMap.size()];

		int j = 0;
		for (DatapointAddress a : datapointsDriversMap.keySet()) {
			addrList[j++] = a;
		}

		notifyDatapointAddressListChanged(addrList);
		//debugAddressList2File();
	}

//	private void debugAddressList2File() {
//		ArrayList<String> resultList = new ArrayList<String>();
//
//		Iterator<DatapointAddress> it = datapointsDriversAddressMap.keySet()
//				.iterator();
//		while (it.hasNext()) {
//			DatapointAddress datapointAddress = (DatapointAddress) it.next();
//			String otherAddr = datapointsDriversAddressMap
//					.get(datapointAddress).getAddress();
//			try {
//				String description = this
//						.getDatapointMetadata(datapointAddress)
//						.getDescription();
//				String access = this.getDatapointMetadata(datapointAddress)
//						.getAccessType().name();
//				resultList.add(datapointAddress.getAddress() + ";" + otherAddr
//						+ ";" + description + ";" + access);
//			} catch (OperationFailedException e) {
//				resultList.add(datapointAddress.getAddress() + ";" + otherAddr
//						+ ";");
//			}
//		}
//
//		Collections.sort(resultList);
//		String result = "";
//		Iterator<String> it2 = resultList.iterator();
//		while (it2.hasNext()) {
//			String string = (String) it2.next();
//			result += string + "\n";
//		}
//
//		try {
//			FileWriter writer = new FileWriter("AddressMapping.txt");
//			writer.write(result);
//			writer.close();
//
//		} catch (IOException e) {
//			System.err.println(e.getMessage());
//		}
//
//	}

	public void addDatapointListener(DatapointListener listener) {
		listeners.add(listener);
	}

	/**
	 * Gets the correspondent driver for each Datapoint
	 * 
	 * @param address
	 *            the address
	 * @return the driver
	 */
	private IDatapointConnectivityService getDriverImplementation(DatapointAddress address) {
		return datapointsDriversMap.get(address);
	}


	public DatapointAddress[] getAllDatapoints() {
		DatapointAddress[] result = new DatapointAddress[datapointsDriversMap
				.size()];

		Iterator<DatapointAddress> it = datapointsDriversMap.keySet()
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
		IDatapointConnectivityService d = getDriverImplementation(address);
		if (d == null)
			throw new OperationFailedException(ErrorType.DATAPOINT_NOT_FOUND);
		return getDriverImplementation(address).getDatapointMetadata(address);

	}

	public void removeDatapointListener(DatapointListener listener) {
		listeners.remove(listener);
	}

	public int requestDatapointRead(DatapointAddress address,
			ReadCallback readCallback) {
		IDatapointConnectivityService d = getDriverImplementation(address);
		if (d == null) {
			readCallback.onReadAborted(address, ErrorType.DATAPOINT_NOT_FOUND,
					0);
			return 0;
		}
		return d.requestDatapointRead(address,
				readCallback);
	}

	public int requestDatapointWrite(DatapointAddress address, DatapointValue[] values,
			WriteCallback writeCallback) {
		IDatapointConnectivityService d = getDriverImplementation(address);
		if (d == null) {
			writeCallback.onWriteAborted(address,
					ErrorType.DATAPOINT_NOT_FOUND, 0);
			return 0;
		}
		return d.requestDatapointWrite(address,
				values, writeCallback);
	}

	@Override
	public int requestDatapointWindowRead(DatapointAddress address,
			long startTimestamp, long finishTimestamp, ReadCallback readCallback) {
		IDatapointConnectivityService d = getDriverImplementation(address);
		if (d == null) {
			readCallback.onReadAborted(address, ErrorType.DATAPOINT_NOT_FOUND,
					0);
			return 0;
		}
		return d.requestDatapointWindowRead(address,
				startTimestamp, finishTimestamp, readCallback);
	}

	@Override
	public String getImplementationName() {
		return this.getClass().getName();
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
			DatapointValue[] values) {
		synchronized (listeners) {
			Iterator<DatapointListener> it = listeners.iterator();
			while (it.hasNext()) {
				DatapointListener listener = it.next();
				listener.onDatapointUpdate(address, values);
			}
		}
	}

	private void notifyDatapointAddressListChanged(DatapointAddress[] addresses) {
		synchronized (listeners) {
			Iterator<DatapointListener> it = listeners.iterator();
			while (it.hasNext()) {
				DatapointListener listener = it.next();
				listener.onDatapointAddressListChanged(addresses);
			}
		}
	}

}
