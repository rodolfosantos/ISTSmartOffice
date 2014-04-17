package eu.smartcampus.api;

import java.security.Timestamp;
import java.util.HashMap;
import java.util.Map;

import eu.smartcampus.util.DatapointAddress;
import eu.smartcampus.util.Metadata;
import eu.smartcampus.util.Value;

public abstract class AbstractDeviceConnectivityAPI
        implements DeviceConnectivityAPI {

    @Override
    public abstract DatapointAddress[] getAllDatapoints();

    @Override
    public abstract Metadata getDatapointMetadata(DatapointAddress address);

    @Override
    public abstract int requestDatapointRead(DatapointAddress address, int clientKey);

    @Override
    public abstract int requestDatapointWindowRead(DatapointAddress address,
                                                   Timestamp start,
                                                   Timestamp finish,
                                                   int clientKey);

    
    @Override
    public abstract int requestDatapointWrite(DatapointAddress address, Value value, int clientKey);

    /**
     * Maps that keeps the read listeners.
     */
    private final Map<Integer, ReadListener> readListeners = new HashMap<Integer, ReadListener>();

    /**
     * Map that keeps the write listeners.
     */
    private final Map<Integer, WriteListener> writeListeners = new HashMap<Integer, WriteListener>();

    @Override
    public final int addReadListener(ReadListener listener) {
        final int key = listener.hashCode();
        if (!readListeners.containsKey(key)) {
            readListeners.put(Integer.valueOf(key), listener);
        }
        return key;
    }

    @Override
    public final int addWriteListener(WriteListener listener) {
        final int key = listener.hashCode();
        if (!writeListeners.containsKey(key)) {
            writeListeners.put(Integer.valueOf(key), listener);
        }
        return key;
    }

    @Override
    public final int removeReadListener(ReadListener listener) {
        final int key = listener.hashCode();
        readListeners.remove(Integer.valueOf(key));
        return key;
    }

    @Override
    public final int removeWriteListener(WriteListener listener) {
        final int key = listener.hashCode();
        writeListeners.remove(Integer.valueOf(key));
        return key;
    }
}
