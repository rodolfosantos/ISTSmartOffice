package eu.smartcampus.api.rest.impl;

import java.util.concurrent.Semaphore;

import eu.smartcampus.api.DatapointAddress;
import eu.smartcampus.api.DatapointReading;
import eu.smartcampus.api.DatapointValue;
import eu.smartcampus.api.IDatapointConnectivityService;
import eu.smartcampus.api.IDatapointConnectivityService.ErrorType;

/**
 * The callback used by the REST wrapper to receive read requests.
 */
public class ReadCallBack
        implements IDatapointConnectivityService.ReadCallback {
    /**
     * Semaphore used to indicate when the reading has arrived.
     */
    private Semaphore semaphore;
    /**
     * The resulting reading.
     */
    private DatapointReading reading;


    public ReadCallBack() {
        super();
        this.semaphore = new Semaphore(0);
        this.reading = null;
    }

    public void onReadCompleted(DatapointAddress address, DatapointReading[] readings, int requestId) {
        semaphore.release();
        this.reading = readings[0];
    }

    public DatapointReading getReading() {
        try {
            semaphore.acquire();
            return reading;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new DatapointReading(new DatapointValue(-1), 0);
        }
        
    }


    public void onReadAborted(DatapointAddress address, ErrorType reason, int requestId) {
        semaphore.release();
        // TODO: Return the appropriate error code
    }
}
