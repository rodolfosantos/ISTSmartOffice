package eu.smartcampus.api.impl;

import java.security.Timestamp;

import eu.smartcampus.api.AbstractDatapointConnectivityService;
import eu.smartcampus.api.DatapointAddress;
import eu.smartcampus.api.DatapointMetadata;
import eu.smartcampus.api.DatapointReading;
import eu.smartcampus.api.DatapointValue;
import eu.smartcampus.api.IDatapointConnectivityService.WriteCallback;

public final class MeterDatapointServiceImpl extends
        AbstractDatapointConnectivityService {
    private int requestCount = 0;

    @Override
    public DatapointAddress[] getAllDatapoints() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DatapointMetadata getDatapointMetadata(DatapointAddress address) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int requestDatapointRead(final DatapointAddress address,
                                    final ReadCallback readCallback) {
        final int requestId = requestCount++;
        new Thread() {
            public void run() {
                /*
                 * Read meter.
                 */
                SensorDriver sensor = new SensorDriver("https://"
                                                       + address.getAddress()
                                                       + "/reading", "root",
                        "root");
                final String value = sensor.getNewMeasure().getTotalPower()
                                     + "";
                final long ts = sensor.getNewMeasure().geTimestamp() * 1000;
                final DatapointReading[] readings = new DatapointReading[] { new DatapointReading(
                        new DatapointValue(value), ts) };
                readCallback.onReadCompleted(address, readings, requestId);
                return;
            }
        }.start();
        return requestId;
    }

    @Override
    public int requestDatapointWindowRead(DatapointAddress address,
                                          Timestamp start,
                                          Timestamp finish,
                                          ReadCallback readCallback) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int requestDatapointWrite(DatapointAddress address,
                                     DatapointValue[] values,
                                     WriteCallback writeCallback) {
        // TODO Auto-generated method stub
        return 0;
    }
}
