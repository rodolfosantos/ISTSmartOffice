package ist.smartoffice.datapointconnectivity.wrappers.rest.impl;

import java.util.concurrent.Semaphore;

import ist.smartoffice.datapointconnectivity.DatapointAddress;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService.ErrorType;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService.WritingConfirmationLevel;

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
        	System.err.println(e.getMessage());
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
     * @see ist.smartoffice.IDatapointConnectivityService.WriteCallback#onWriteAborted(ist.smartoffice.DatapointAddress, ist.smartoffice.IDatapointConnectivityService.ErrorType, int)
     */
    public void onWriteAborted(DatapointAddress address,
                               ErrorType reason,
                               int requestId) {

        isWritten = false;
        errorReason = reason;
        semaphore.release();
    }

    /* (non-Javadoc)
     * @see ist.smartoffice.IDatapointConnectivityService.WriteCallback#onWriteCompleted(ist.smartoffice.DatapointAddress, ist.smartoffice.IDatapointConnectivityService.WritingConfirmationLevel, int)
     */
    public void onWriteCompleted(DatapointAddress address,
                                 WritingConfirmationLevel confirmationLevel,
                                 int requestId) {

        isWritten = true;
        semaphore.release();

    }
}
