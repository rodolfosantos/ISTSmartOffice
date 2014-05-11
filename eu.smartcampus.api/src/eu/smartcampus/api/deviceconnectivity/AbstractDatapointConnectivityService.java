package eu.smartcampus.api.deviceconnectivity;

import java.util.HashSet;
import java.util.Set;

/**
 * Skeleton implementation for {@link IDatapointConnectivityService}.
 */
public abstract class AbstractDatapointConnectivityService implements
		IDatapointConnectivityService {

	/**
	 * Keeps the datapoint listeners.
	 */
	private final Set<DatapointListener> readListeners = new HashSet<DatapointListener>();

	protected void notifyReadAcknowledge(DatapointAddress address,
			DatapointReading readings, int requestId) {
		// final DatapointListener l = readListeners.get(Integer
		// .valueOf(clientKey));
		// l.readAcknowledge(address, readings, requestId);
	}

	@Override
	public abstract DatapointAddress[] getAllDatapoints();

	@Override
	public abstract DatapointMetadata getDatapointMetadata(DatapointAddress address);

	@Override
	public void addDatapointListener(DatapointListener listener) {
		if (!readListeners.contains(listener)) {
			readListeners.add(listener);
		}
	}

	@Override
	public void removeDatapointListener(DatapointListener listener) {
		readListeners.remove(listener);
	}

	@Override
	public abstract int requestDatapointRead(DatapointAddress address,
			ReadCallback readCallback);

	@Override
	public abstract int requestDatapointWindowRead(DatapointAddress address,
			long startTimestamp, long finishTimestamp, ReadCallback readCallback);

	@Override
	public abstract int requestDatapointWrite(DatapointAddress address,
			DatapointValue[] values, WriteCallback writeCallback);

}
