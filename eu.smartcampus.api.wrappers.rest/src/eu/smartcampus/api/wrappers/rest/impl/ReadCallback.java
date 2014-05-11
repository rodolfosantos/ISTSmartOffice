package eu.smartcampus.api.wrappers.rest.impl;

import java.util.concurrent.Semaphore;

import eu.smartcampus.api.DatapointAddress;
import eu.smartcampus.api.DatapointReading;
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
    private DatapointReading[] reading = null;

    /** The error reason. */
    private ErrorType errorReason = null;


    /**
     * Instantiates a new read callback.
     */
    public ReadCallback() {
        super();
        this.semaphore = new Semaphore(0);
    }


    public void onReadCompleted(DatapointAddress address, DatapointReading[] readings, int requestId) {
        semaphore.release();
        this.reading = readings;
    }

    /**
     * Gets the reading.
     * 
     * @return the reading
     */
    public DatapointReading getReading() {
        try {
            semaphore.acquire();

            return reading[0];
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the readings.
     * 
     * @return the readings
     */
    public DatapointReading[] getReadings() {
        try {
            semaphore.acquire();
            return reading;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
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


    public void onReadAborted(DatapointAddress address, ErrorType reason, int requestId) {
        this.errorReason = reason;
        semaphore.release();
    }
}
