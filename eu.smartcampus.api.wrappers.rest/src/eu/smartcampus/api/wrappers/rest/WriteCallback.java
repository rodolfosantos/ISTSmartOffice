package eu.smartcampus.api.wrappers.rest;

import java.util.concurrent.Semaphore;

import eu.smartcampus.api.DatapointAddress;
import eu.smartcampus.api.IDatapointConnectivityService;
import eu.smartcampus.api.IDatapointConnectivityService.ErrorType;
import eu.smartcampus.api.IDatapointConnectivityService.WritingConfirmationLevel;

// TODO: Auto-generated Javadoc
/**
 * The callback used by the REST wrapper to receive write requests.
 */
public class WriteCallback
        implements IDatapointConnectivityService.WriteCallback {
    /**
     * Semaphore used to indicate when the write has arrived.
     */
    private final Semaphore semaphore = new Semaphore(0);

    /**
     * The write operation success.
     */
    private boolean isWritten = false;

    /** The error reason. */
    private ErrorType errorReason = null;

    /**
     * Checks if is written.
     * 
     * @return true, if is written
     */
    public boolean isWritten() {
        try {
            semaphore.acquire();
            return isWritten;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isWritten;
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
     * @see eu.smartcampus.api.IDatapointConnectivityService.WriteCallback#onWriteAborted(eu.smartcampus.api.DatapointAddress, eu.smartcampus.api.IDatapointConnectivityService.ErrorType, int)
     */
    public void onWriteAborted(DatapointAddress address,
                               ErrorType reason,
                               int requestId) {

        isWritten = false;
        errorReason = reason;
        semaphore.release();
    }

    /* (non-Javadoc)
     * @see eu.smartcampus.api.IDatapointConnectivityService.WriteCallback#onWriteCompleted(eu.smartcampus.api.DatapointAddress, eu.smartcampus.api.IDatapointConnectivityService.WritingConfirmationLevel, int)
     */
    public void onWriteCompleted(DatapointAddress address,
                                 WritingConfirmationLevel confirmationLevel,
                                 int requestId) {

        isWritten = true;
        semaphore.release();

    }
}
