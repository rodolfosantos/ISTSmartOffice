package eu.smartcampus.api;

import java.security.Timestamp;

import eu.smartcampus.util.DatapointAddress;
import eu.smartcampus.util.Metadata;
import eu.smartcampus.util.Reading;
import eu.smartcampus.util.Value;

/**
 * API that abstracts the connection details to devices.
 * <p>
 * Timestamps are local to datapoints.
 */
public interface IDatapointConnectivityService {

    /**
     * Interface for listeners of datapoint events.
     */
    interface DatapointListener {
        /**
         * Invoked when a datapoint value update occurs.
         * <p>
         * This method can be used to track external changes made in datapoints, such as a
         * sensor update.
         * 
         * @param address the address of the datapoint
         * @param values the latest reading values
         */
        void onDatapointUpdate(DatapointAddress address, Reading[] values);

        /**
         * Invoked when a datapoint error occurs.
         * 
         * @param address the address of the datapoint
         * @param error the error code
         */
        void onDatapointError(DatapointAddress address, ErrorType error);
    }

    /**
     * 
     *
     */
    enum ErrorType {
        DATAPOINT_NOT_FOUND,
        DATAPOINT_CONNECTION_ERROR,
        DATAPOINT_NOT_RESPONDING,
        DATAPOINT_VANISHED,
        DEVICE_BUSY,
        GATEWAY_CONNECTION_ERROR,
        GATEWAY_NOT_FOUND,
        GATEWAY_NOT_RESPONDING,
        GATEWAY_BUSY
    }

    interface ReadCallback {
        /**
         * Notifies that a read operation was aborted.
         * 
         * @param address the address of the datapoint
         * @param reason the reason that caused the write operation to be aborted
         * @param requestId the id of the request
         */
        void onReadAborted(DatapointAddress address,
                           ErrorType reason,
                           int requestId);

        /**
         * Notifies that a value read from datapoint has arrived.
         * <p>
         * 
         * @param address the datapoint address that was written.
         * @param readings an array of readings.
         * @param requestId the request id
         */
        void onReadCompleted(DatapointAddress address,
                             Reading[] readings,
                             int requestId);
    }


    interface WriteCallback {
        /**
         * Notifies that a write operation was aborted.
         * 
         * @param address the address of the datapoint
         * @param reason the reason that caused the write operation to be aborted
         * @param requestId the id of the request
         */
        void onWriteAborted(DatapointAddress address,
                            ErrorType reason,
                            int requestId);

        /**
         * Notifies for an update in the request status. This method should be called
         * every time a new confirmation level is reached.
         * <p>
         * In some situations it may not possible to guarantee that the value was written.
         * The device or protocol may not support acknowledging writing events. This is
         * the case for example of X10 devices. If the datapoint can be read, implementers
         * 
         * @param address the datapoint where the datapoint was written.
         * @param confirmationLevel the confirmation level
         * @param requestId the request id
         */
        void onWriteCompleted(DatapointAddress address,
                              WritingConfirmationLevel confirmationLevel,
                              int requestId);
    }

    /**
     * Type of write confirmation.
     */
    enum WritingConfirmationLevel {
        /**
         * The device has confirmed to take action upon the write request.
         */
        DEVICE_ACTION_TAKEN,

        /**
         * The device confirmed the write operation.
         */
        DEVICE_CONFIRMED,

        /**
         * The write operation was sent to the gateway and confirmed. An acknowledge in
         * this level means that the message has arrived at the gateway.
         */
        GATEWAY_CONFIRMED,

        /**
         * Write operation sent to the gateway but confirmation so far. This is the level
         * to be used when there no confirmation is possible.
         */
        UNCONFIRMED
    }

    /**
     * Adds a datapoint listener.
     * 
     * @param listener the datapoint listener
     */
    void addDatapointListener(DatapointListener listener);

    /**
     * Gets all sensors register in the system.
     * 
     * @return the list of all registered sensors
     */
    DatapointAddress[] getAllDatapoints();

    /**
     * For each datapoint of a given sensor gets their metadata.
     * 
     * @param address the set of datapoints, belonging to the sensor, for which we want to
     *            know the metadata
     * @return the metadata for each datapoint
     */
    Metadata getDatapointMetadata(DatapointAddress address);

    /**
     * Removes a datapoint listener.
     * 
     * @param listener the datapoint listener
     */
    void removeDatapointListener(DatapointListener listener);

    /**
     * Gets the last available reading of a datapoint.
     * <p>
     * It is expected that implementations of this method force a reading to the specified
     * datapoint. However, when the reading cannot be forced on the device, it is expected
     * that the last available reading from the device (that could have been cached) is
     * returned.
     * 
     * @param address the absolute address of the datapoint to be read
     * @param clientKey the identifier of the client making the request * @return the id
     *            of the write request used to by the client to identify the acknowledge
     *            of this request
     * @return the id of the write request used to by the client to identify the
     *         acknowledge of this request
     */
    int requestDatapointRead(DatapointAddress address, ReadCallback readCallback);

    /**
     * Gets the readings a datapoint within a given time window.
     * <p>
     * Implementations of this method may query the meter or return cached values. Since
     * meters are devices with limited resources, specifying <tt>start</tt> to far into
     * the past may result in only a subset of the values.
     * 
     * @param address the absolute address of the datapoint to be read
     * @param start the timestamp that defines the initial window
     * @param finish timestamp that defines the final window. Should be greater or equal
     *            to start.
     * @param clientKey the identifier of the client making the request
     * @return the id of the read request used to by the client to identify the
     *         acknowledge of this request
     * 
     * @return <ol>
     *         <li>an empty array, if <tt>start &gt; finish</tt></li><li>a single reading
     *         <i>r</i>, if available, such that <tt><i>r.ts</i> == start == finish</tt>,
     *         if <tt>start == finish</tt></li> <li>all readings <i>r</i>, if available,
     *         such that <tt>start &le;<i>r.ts</i> &le; finish</tt>
     *         </ol>
     */
    int requestDatapointWindowRead(DatapointAddress address,
                                   Timestamp start,
                                   Timestamp finish,
                                   ReadCallback readCallback);

    /**
     * Request a datapoint write.
     * 
     * @param address the address of the datapoint
     * @param values the values to be written to the datapoint
     * @param clientKey the identifier of the client making the request
     * @return the id of the write request used to by the client to identify the
     *         acknowledge of this request
     */
    int requestDatapointWrite(DatapointAddress address,
                              Value[] values,
                              WriteCallback writeCallback);
}
