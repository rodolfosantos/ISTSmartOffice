package eu.smartcampus.api.rest.deviceapi.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import eu.smartcampus.api.DatapointAddress;
import eu.smartcampus.api.DatapointMetadata;
import eu.smartcampus.api.DatapointReading;
import eu.smartcampus.api.DatapointValue;
import eu.smartcampus.api.IDatapointConnectivityService;
import eu.smartcampus.api.IDatapointConnectivityService.ErrorType;

public class DatapointConnectivityServiceImpl
        implements IDatapointConnectivityService {


    private MeterDriverIP driver;
    private Map<DatapointAddress, DatapointMetadata> datapoints;
    private Set<DatapointListener> listeners;

    public DatapointConnectivityServiceImpl(MeterDriverIP driver,
                                            Map<DatapointAddress, DatapointMetadata> datapoints) {
        super();
        this.driver = driver;
        this.datapoints = new HashMap<DatapointAddress, DatapointMetadata>(datapoints);
        this.listeners = new HashSet<DatapointListener>();
    }


    public void addDatapointListener(DatapointListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }

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

    private void notifyDatapointUpdate(DatapointAddress address, DatapointReading[] values) {
        synchronized (listeners) {
            Iterator<DatapointListener> it = listeners.iterator();
            while (it.hasNext()) {
                DatapointListener listener = it.next();
                listener.onDatapointUpdate(address, values);
            }
        }
    }

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

    public DatapointMetadata getDatapointMetadata(DatapointAddress address) {
        return this.datapoints.get(address);
    }

    public void removeDatapointListener(DatapointListener listener) {
        this.listeners.remove(listener);
    }

    public int requestDatapointRead(DatapointAddress address, ReadCallback readCallback) {
        try {
            MeterMeasure value = driver.getNewMeasure(address.getAddress());
            DatapointReading reading = new DatapointReading(new DatapointValue(
                    value.getTotalPower() + ""));
            readCallback.onReadCompleted(address, new DatapointReading[] { reading }, 0);
            return 0;
        } catch (MalformedURLException e) {
            readCallback.onReadAborted(address, ErrorType.DATAPOINT_NOT_FOUND, 0);
        }

        return 0;
    }


    public int requestDatapointWrite(DatapointAddress address,
                                     DatapointValue[] values,
                                     WriteCallback writeCallback) {
        writeCallback.onWriteAborted(address, ErrorType.UNSUPORTED_DATAPOINT_OPERATION, 0);
        return 0;
    }

    @Override
    public int requestDatapointWindowRead(DatapointAddress address,
                                          long startTimestamp,
                                          long finishTimestamp,
                                          ReadCallback readCallback) {
        readCallback.onReadAborted(address, ErrorType.UNSUPORTED_DATAPOINT_OPERATION, 0);
        return 0;
    }

}
