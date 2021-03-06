package ist.smartoffice.deviceconnectivity.knxip;

import ist.smartoffice.datapointconnectivity.DatapointAddress;
import ist.smartoffice.datapointconnectivity.DatapointMetadata;
import ist.smartoffice.datapointconnectivity.DatapointMetadata.AccessType;
import ist.smartoffice.datapointconnectivity.DatapointValue;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService;
import ist.smartoffice.logger.Logger;
import ist.smartoffice.logger.LoggerService;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import tuwien.auto.calimero.DetachEvent;
import tuwien.auto.calimero.process.ProcessEvent;
import tuwien.auto.calimero.process.ProcessListener;

/**
 * The Class DatapointConnectivityServiceKNXIPDriver.
 */
public class DatapointConnectivityServiceKNXIPDriver implements
		IDatapointConnectivityService {
	static private Logger log = LoggerService.getInstance().getLogger(
			DatapointConnectivityServiceKNXIPDriver.class.getName());

	/**
	 * The drivers.
	 */
	private Map<String, KNXGatewayIPDriver> gateways;

	/**
	 * The listeners set
	 */
	private Set<DatapointListener> listeners;

	/**
	 * The datapoints.
	 */
	private Map<DatapointAddress, DatapointMetadata> datapointsMetadata;
	private Map<DatapointAddress, DatapointValue> datapointsStatus;
	private List<DatapointAddress> uptadingDatapoint;

	/**
	 * Instantiates a new datapoint connectivity service knxip driver.
	 * 
	 * @param driver
	 *            the driver
	 * @param datapointsMetadata
	 *            the datapoints
	 */
	public DatapointConnectivityServiceKNXIPDriver() {
		super();
		this.datapointsMetadata = KNXServiceConfig.loadDatapointsConfigs();
		this.datapointsStatus = new HashMap<DatapointAddress, DatapointValue>();
		this.uptadingDatapoint = new ArrayList<DatapointAddress>();
		this.listeners = new HashSet<DatapointListener>();
		this.gateways = new HashMap<String, KNXGatewayIPDriver>();

		connectToKNXGateways(datapointsMetadata);
		updateDatapointStatus();

		for (final String gwAddr : this.gateways.keySet()) {
			KNXGatewayIPDriver gw = this.gateways.get(gwAddr);
			gw.addProcessEventListener(new ProcessListener() {

				@Override
				public void groupWrite(ProcessEvent arg0) {
					final String knxAddress = arg0.getDestination().toString();
					log.d("KNX  GW Event: " + gwAddr + ":" + knxAddress);

					final DatapointAddress datapointAddress = getMasterAddress(knxAddress);

					if (datapointAddress == null) {
						return;
					}

					log.d("KNX Event: " + gwAddr + ":" + datapointAddress);

					if (!datapointsMetadata.keySet().contains(datapointAddress))
						return;

					if ((datapointsMetadata.get(datapointAddress)
							.getAccessType() == AccessType.WRITE_ONLY))
						return;

					if (uptadingDatapoint.contains(datapointAddress)) {
						// uptadingDatapoint.remove(datapointAddress);
						return;
					}

					uptadingDatapoint.add(datapointAddress);
					// update value
					new Thread(new Runnable() {

						@Override
						public void run() {

							requestDatapointReadDevice(datapointAddress,
									new ReadCallback() {

										@Override
										public void onReadCompleted(
												DatapointAddress address,
												DatapointValue[] readings,
												int requestId) {
											datapointsStatus.put(
													datapointAddress,
													readings[0]);
											log.d("KNX Update: " + gwAddr + ":"
													+ address + "="
													+ readings[0].getValue());

											// notifyDatapointUpdate(datapointAddress,
											// readings);

											new Thread(new Runnable() {

												@Override
												public void run() {
													// TODO Auto-generated
													// method stub
													try {
														Thread.sleep(800);
													} catch (InterruptedException e) {
														// TODO Auto-generated
														// catch block
														e.printStackTrace();
													}
													uptadingDatapoint
															.remove(datapointAddress);
												}
											}).start();

										}

										@Override
										public void onReadAborted(
												DatapointAddress address,
												ErrorType reason, int requestId) {
										}
									});
						}
					}).start();
				}

				private DatapointAddress getMasterAddress(String knxAddress) {
					for (Entry<DatapointAddress, DatapointMetadata> e : datapointsMetadata
							.entrySet()) {
						DatapointMetadata m = e.getValue();
						String readAddr = m.getReadDatapointAddress();
						if (readAddr == null)
							continue;

						if (m.getReadDatapointAddress().equals(knxAddress))
							return e.getKey();
					}
					return null;
				}

				@Override
				public void detached(DetachEvent arg0) {
				}
			});
		}

	}

	private void connectToKNXGateways(
			Map<DatapointAddress, DatapointMetadata> datapointsMetadata) {

		Iterator<DatapointAddress> it = datapointsMetadata.keySet().iterator();
		while (it.hasNext()) {
			DatapointAddress datapointAddress = (DatapointAddress) it.next();
			DatapointMetadata m = datapointsMetadata.get(datapointAddress);
			String gAddr = m.getGatewayAddress();

			if (!gateways.containsKey(gAddr)) {
				KNXGatewayIPDriver gw = new KNXGatewayIPDriver(gAddr);
				try {
					gw.start();
				} catch (UnknownHostException e) {
					gw.reconnectGateway();
				}
				gateways.put(gAddr, gw);
			}

		}

	}

	private void updateDatapointStatus() {
		Iterator<DatapointAddress> it = datapointsMetadata.keySet().iterator();
		while (it.hasNext()) {
			final DatapointAddress datapointAddress = (DatapointAddress) it
					.next();
			DatapointMetadata m = datapointsMetadata.get(datapointAddress);
			if (DatapointMetadata.AccessType.WRITE_ONLY != m.getAccessType()) {
				requestDatapointReadDevice(datapointAddress,
						new ReadCallback() {

							@Override
							public void onReadCompleted(
									DatapointAddress address,
									DatapointValue[] readings, int requestId) {
								datapointsStatus.put(datapointAddress,
										readings[0]);
								log.d("KNX Update: " + address + "="
										+ readings[0].getValue());

							}

							@Override
							public void onReadAborted(DatapointAddress address,
									ErrorType reason, int requestId) {
								notifyDatapointError(datapointAddress,
										ErrorType.DEVICE_NOT_RESPONDING);
							}
						});
			}
		}
	}

	@Override
	public void addDatapointListener(DatapointListener listener) {
		if (listener == null)
			listeners.clear();
		listeners.add(listener);
	}

	@Override
	public DatapointAddress[] getAllDatapoints() {
		DatapointAddress[] result = new DatapointAddress[datapointsMetadata
				.size()];

		Iterator<DatapointAddress> it = datapointsMetadata.keySet().iterator();
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
			DatapointValue[] values) {
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
		return this.datapointsMetadata.get(address);
	}

	@Override
	public int requestDatapointRead(DatapointAddress address,
			ReadCallback readCallback) {
		return requestDatapointReadCache(address, readCallback);
	}

	private int requestDatapointReadCache(DatapointAddress address,
			ReadCallback readCallback) {

		DatapointMetadata m = getDatapointMetadata(address);
		String readAddr = m.getReadDatapointAddress();
		KNXGatewayIPDriver gw = gateways.get(m.getGatewayAddress());

		if (m.getAccessType() == AccessType.WRITE_ONLY) {
			readCallback.onReadAborted(address,
					ErrorType.UNSUPORTED_DATAPOINT_OPERATION, 0);
			return 0;
		}

		DatapointValue reading = datapointsStatus.get(address);
		if (reading != null)
			readCallback.onReadCompleted(address,
					new DatapointValue[] { reading }, 0);
		else
			readCallback.onReadAborted(address,
					ErrorType.DEVICE_NOT_RESPONDING, 0);

		return 0;
	}

	private int requestDatapointReadDevice(DatapointAddress address,
			ReadCallback readCallback) {
		DatapointMetadata m = getDatapointMetadata(address);
		String readAddr = m.getReadDatapointAddress();
		KNXGatewayIPDriver gw = gateways.get(m.getGatewayAddress());

		if (m.getAccessType() == AccessType.WRITE_ONLY) {
			readCallback.onReadAborted(address,
					ErrorType.UNSUPORTED_DATAPOINT_OPERATION, 0);
			return 0;
		}

		switch (m.getDatatype()) {

		case BYTE_1:

		case BYTE_2:
			try {
				float val = gw.read2Bytes(readAddr);
				String value = val + "";
				DatapointValue reading = new DatapointValue(value);
				readCallback.onReadCompleted(address,
						new DatapointValue[] { reading }, 0);
				notifyDatapointUpdate(address, new DatapointValue[] { reading });
			} catch (Exception e) {
				readCallback.onReadAborted(address,
						ErrorType.DEVICE_CONNECTION_ERROR, 0);
				return 0;
			}
			break;
		case PERCENTAGE:
			try {
				float val = gw.readPercentage(readAddr);
				String value = val + "";
				DatapointValue reading = new DatapointValue(value);
				readCallback.onReadCompleted(address,
						new DatapointValue[] { reading }, 0);
				notifyDatapointUpdate(address, new DatapointValue[] { reading });
			} catch (Exception e) {
				readCallback.onReadAborted(address,
						ErrorType.DEVICE_CONNECTION_ERROR, 0);
				return 0;
			}
			break;
		case SWITCH:
			try {
				boolean value = gw.readSwitch(readAddr);
				DatapointValue reading = new DatapointValue(value + "");
				readCallback.onReadCompleted(address,
						new DatapointValue[] { reading }, 0);
				notifyDatapointUpdate(address, new DatapointValue[] { reading });
			} catch (Exception e) {
				readCallback.onReadAborted(address,
						ErrorType.DEVICE_CONNECTION_ERROR, 0);
				return 0;
			}
			break;

		}

		readCallback.onReadAborted(address,
				ErrorType.UNSUPORTED_DATAPOINT_OPERATION, 0);
		return 0;
	}

	@Override
	public int requestDatapointWindowRead(DatapointAddress address,
			long startTimestamp, long finishTimestamp, ReadCallback readCallback) {
		readCallback.onReadAborted(address,
				ErrorType.UNSUPORTED_DATAPOINT_OPERATION, 0);
		return 0;
	}

	@Override
	public int requestDatapointWrite(DatapointAddress address,
			DatapointValue[] values, WriteCallback writeCallback) {
		DatapointMetadata m = getDatapointMetadata(address);
		String writeAddr = m.getWriteDatapointAddress();
		KNXGatewayIPDriver gw = gateways.get(m.getGatewayAddress());
		System.err.println("escrita! " + address + writeAddr + " "
				+ values[0].getValue());

		if (m.getAccessType() == AccessType.READ_ONLY) {
			writeCallback.onWriteAborted(address,
					ErrorType.UNSUPORTED_DATAPOINT_OPERATION, 0);
			return 0;
		}

		switch (m.getDatatype()) {

		case BYTE_1:// TODO
			break;
		case BYTE_2:// TODO
			break;

		case PERCENTAGE:
			try {
				float f = Float.parseFloat(values[0].getValue());
				gw.writePercentage(writeAddr, Math.round(f));
				datapointsStatus.put(address, values[0]);
				writeCallback.onWriteCompleted(address,
						WritingConfirmationLevel.DEVICE_ACTION_TAKEN, 0);
				notifyDatapointUpdate(address, values);
			} catch (Exception e) {
				e.printStackTrace();
				writeCallback.onWriteAborted(address,
						ErrorType.DEVICE_CONNECTION_ERROR, 0);
			}

			break;
		case SWITCH:
			try {
				System.err.println("DOOR" + values[0].getValue());
				gw.writeSwitch(writeAddr,
						Boolean.parseBoolean(values[0].getValue()));
				datapointsStatus.put(address, values[0]);
				writeCallback.onWriteCompleted(address,
						WritingConfirmationLevel.DEVICE_ACTION_TAKEN, 0);
				notifyDatapointUpdate(address, values);
			} catch (Exception e) {
				writeCallback.onWriteAborted(address,
						ErrorType.DEVICE_CONNECTION_ERROR, 0);
			}
			break;
		}

		return 0;
	}

	@Override
	public String getImplementationName() {
		return "knx";
	}

}
