package eu.smartcampus.api.rest;

import eu.smartcampus.api.DatapointAddress;
import eu.smartcampus.api.DatapointReading;
import eu.smartcampus.api.IDatapointConnectivityService;
import eu.smartcampus.api.IDatapointConnectivityService.ErrorType;

public class RestReadCallback
        implements IDatapointConnectivityService.ReadCallback {

    private DatapointReading reading = null;

    @Override
    public void onReadCompleted(DatapointAddress address,
                                DatapointReading[] readings,
                                int requestId) {
        reading = readings[0];

        // TODO: Hard coded... Sabemos que só foi feito
        // um reading, apenas por magia.

    }

    public DatapointReading getReading() {
        DatapointReading r = reading;
        reading = null;
        return r;
    }

    @Override
    public void onReadAborted(DatapointAddress address,
                              ErrorType reason,
                              int requestId) {
        // TODO Auto-generated method stub
    }

}
