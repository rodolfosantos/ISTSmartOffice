package eu.smartcampus.api.impl;

import java.security.Timestamp;

import eu.smartcampus.api.AbstractDatapointConnectivityService;
import eu.smartcampus.util.DatapointAddress;
import eu.smartcampus.util.Metadata;
import eu.smartcampus.util.Reading;
import eu.smartcampus.util.Value;

public final class MeterDatapointServiceImpl extends
        AbstractDatapointConnectivityService {
    private int requestCount = 0;

    @Override
    public DatapointAddress[] getAllDatapoints() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Metadata getDatapointMetadata(DatapointAddress address) {
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
                final Reading[] readings = new Reading[] { new Reading(
                        new Value(value), ts) };
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
                                     Value[] values,
                                     WriteCallback writeCallback) {
        // TODO Auto-generated method stub
        return 0;
    }

}
