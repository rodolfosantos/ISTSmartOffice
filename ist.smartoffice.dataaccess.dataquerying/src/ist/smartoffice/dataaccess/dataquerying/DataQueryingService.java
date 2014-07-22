package ist.smartoffice.dataaccess.dataquerying;

import ist.smartoffice.datapointconnectivity.DatapointAddress;
import ist.smartoffice.datapointconnectivity.DatapointMetadata;
import ist.smartoffice.datapointconnectivity.DatapointValue;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DataQueryingService implements IDatapointConnectivityService {

	Map<DatapointAddress, DatapointMetadata> datapoints;
	Map<DatapointAddress, IQuery> queries;

	/**
	 * The listeners set
	 */
	private Set<DatapointListener> listeners;

	public DataQueryingService(
			IDatapointConnectivityService remoteActuationServiceImpl) {

		this.datapoints = DataQueryingServiceConfig.loadDatapointsConfigs();
		this.queries = new HashMap<DatapointAddress, IQuery>();
		this.listeners = new HashSet<DatapointListener>();

		for (DatapointAddress addr : datapoints.keySet()) {
			DatapointMetadata m = datapoints.get(addr);

			// select max from meterlib
			String query = m.getDescription();
			String dpAddr = query.split(" ")[3];
			String qType = query.split(" ")[1];

			switch (qType) {
			case "max":
				queries.put(addr, new QueryMaxValue(dpAddr));
				break;

			case "avg":
				queries.put(addr, new QueryAvgValue(dpAddr));
				break;

			default:
				break;
			}

		}

		remoteActuationServiceImpl
				.addDatapointListener(new DatapointListener() {

					@Override
					public void onDatapointUpdate(DatapointAddress address,
							DatapointValue[] values) {

						for (IQuery q : queries.values()) {
							if (q.getDatapoint().equals(address.getAddress())) {
								q.pushValue(values[0].getValue());
								String newVal = q.getValue();
								notifyDatapointUpdate(
										address,
										new DatapointValue[] { new DatapointValue(
												newVal) });
							}
						}
					}

					@Override
					public void onDatapointError(DatapointAddress address,
							ErrorType error) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onDatapointAddressListChanged(
							DatapointAddress[] address) {
						// TODO Auto-generated method stub

					}
				});

	}

	@Override
	public String getImplementationName() {
		return DataQueryingService.class.getName();
	}

	@Override
	public void addDatapointListener(DatapointListener listener) {
		if (listener == null)
			listeners.clear();
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
	public DatapointMetadata getDatapointMetadata(DatapointAddress address)
			throws OperationFailedException {
		datapoints.get(address);
		return null;
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
	public int requestDatapointRead(DatapointAddress address,
			ReadCallback readCallback) {
		IQuery q = queries.get(address);
		if (q != null)
			readCallback.onReadCompleted(address,
					new DatapointValue[] { new DatapointValue(q.getValue()) },
					0);
		else
			readCallback.onReadAborted(address, ErrorType.DATAPOINT_NOT_FOUND,
					0);

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
		writeCallback.onWriteAborted(address,
				ErrorType.UNSUPORTED_DATAPOINT_OPERATION, 0);
		return 0;
	}

}
