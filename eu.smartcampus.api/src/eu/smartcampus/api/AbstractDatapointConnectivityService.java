package eu.smartcampus.api;

import java.security.Timestamp;
import java.util.HashSet;
import java.util.Set;

import eu.smartcampus.util.DatapointAddress;
import eu.smartcampus.util.Metadata;
import eu.smartcampus.util.Reading;
import eu.smartcampus.util.Value;

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
			Reading readings, int requestId) {
		// final DatapointListener l = readListeners.get(Integer
		// .valueOf(clientKey));
		// l.readAcknowledge(address, readings, requestId);
	}

	@Override
	public abstract DatapointAddress[] getAllDatapoints();

	@Override
	public abstract Metadata getDatapointMetadata(DatapointAddress address);

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
			Timestamp start, Timestamp finish, ReadCallback readCallback);

	@Override
	public abstract int requestDatapointWrite(DatapointAddress address,
			Value[] values, WriteCallback writeCallback);

}
