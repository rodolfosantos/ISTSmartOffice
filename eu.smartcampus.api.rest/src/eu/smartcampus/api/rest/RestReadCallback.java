package eu.smartcampus.api.rest;

import eu.smartcampus.api.IDeviceConnectivity;
import eu.smartcampus.api.IDeviceConnectivity.ErrorType;
import eu.smartcampus.util.DatapointAddress;
import eu.smartcampus.util.Reading;
import eu.smartcampus.util.Value;

public class RestReadCallback
        implements IDeviceConnectivity.ReadCallback {

    private Reading reading = null;

    @Override
    public void readAborted(DatapointAddress address, Value value, ErrorType reason, int requestId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void readAcknowledge(DatapointAddress address, Reading readings, int requestId) {
        reading = readings;
    }

    @Override
    public void onReadCompleted(DatapointAddress address, Reading[] readings, int requestId) {
        // TODO Auto-generated method stub
        
    }

    public Reading getReading() {
        Reading r = reading;
        reading = null;
        return r;
    }
    
}