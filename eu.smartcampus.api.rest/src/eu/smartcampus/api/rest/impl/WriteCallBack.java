package eu.smartcampus.api.rest.impl;

import java.util.concurrent.Semaphore;

import eu.smartcampus.api.DatapointAddress;
import eu.smartcampus.api.IDatapointConnectivityService;
import eu.smartcampus.api.IDatapointConnectivityService.ErrorType;
import eu.smartcampus.api.IDatapointConnectivityService.WritingConfirmationLevel;

/**
 * The callback used by the REST wrapper to receive write requests.
 */
public class WriteCallBack
        implements IDatapointConnectivityService.WriteCallback {
    /**
     * Semaphore used to indicate when the write has arrived.
     */
    private final Semaphore semaphore = new Semaphore(0);

    /**
     * The write operation success.
     */
    private boolean isWritten = false;

    public boolean isWritten() {
        return true;
//        try {
//            semaphore.acquire();
//            return isWritten;
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return isWritten;
    }




    public void onWriteAborted(DatapointAddress address, ErrorType reason, int requestId) {

        isWritten = false;
        semaphore.release();
    }

    public void onWriteCompleted(DatapointAddress address,
                                 WritingConfirmationLevel confirmationLevel,
                                 int requestId) {
        
        isWritten = true;
        semaphore.release();

    }
}
