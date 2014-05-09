package eu.smartcampus.api.rest.impl;

import java.util.concurrent.Semaphore;

import eu.smartcampus.api.DatapointAddress;
import eu.smartcampus.api.DatapointReading;
import eu.smartcampus.api.DatapointValue;
import eu.smartcampus.api.IDatapointConnectivityService;
import eu.smartcampus.api.IDatapointConnectivityService.ErrorType;

// TODO: Auto-generated Javadoc
/**
 * The callback used by the REST wrapper to receive read requests.
 */
public class ReadCallback
        implements IDatapointConnectivityService.ReadCallback {
    /**
     * Semaphore used to indicate when the reading has arrived.
     */
    private Semaphore semaphore;
    /**
     * The resulting reading.
     */
    private DatapointReading reading = null;

    /** The error reason. */
    private ErrorType errorReason = null;


    /**
     * Instantiates a new read callback.
     */
    public ReadCallback() {
        super();
        this.semaphore = new Semaphore(0);
    }

    /* (non-Javadoc)
     * @see eu.smartcampus.api.IDatapointConnectivityService.ReadCallback#onReadCompleted(eu.smartcampus.api.DatapointAddress, eu.smartcampus.api.DatapointReading[], int)
     */
    public void onReadCompleted(DatapointAddress address,
                                DatapointReading[] readings,
                                int requestId) {
        semaphore.release();
        this.reading = readings[0];
    }

    /**
     * Gets the reading.
     * 
     * @return the reading
     */
    public DatapointReading getReading() {
        try {
            semaphore.acquire();
            return reading;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new DatapointReading(new DatapointValue(-1), 0);
        }
    }

    /**
     * Gets the error reason that caused the operation to fail.
     * 
     * @return the error reason
     */
    public IDatapointConnectivityService.ErrorType getErrorReason() {
        return this.errorReason;
    }


    /* (non-Javadoc)
     * @see eu.smartcampus.api.IDatapointConnectivityService.ReadCallback#onReadAborted(eu.smartcampus.api.DatapointAddress, eu.smartcampus.api.IDatapointConnectivityService.ErrorType, int)
     */
    public void onReadAborted(DatapointAddress address,
                              ErrorType reason,
                              int requestId) {
        this.errorReason = reason;
        semaphore.release();
    }
}
