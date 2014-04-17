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
public interface DeviceConnectivityAPI {


    /**
     * Type of write confirmation.
     */
    enum ConfirmationLevel {
        /**
         * Write operation sent to the gateway but confirmation so far. This is the level
         * to be used when there no confirmation is possible.
         */
        UNCONFIRMED,

        /**
         * The write operation was sent to the gateway and confirmed. An acknowledge in
         * this level means that the message has arrived at the gateway.
         */
        GATEWAY_CONFIRMED,

        /**
         * The device confirmed the write operation.
         */
        DEVICE_CONFIRMED,

        /**
         * The device has confirmed to take action upon the write request.
         */
        DEVICE_ACTION_TAKEN
    }

    enum ErrorType {
        GATEWAY_NOT_FOUND, GATEWAY_CONNECTION_ERROR, GATEWAY_NOT_RESPONDING, GATEWAY_BUSY, DEVICE_NOT_FOUND, DEVICE_CONNECTION_ERROR, DEVICE_NOT_RESPONDING, DEVICE_BUSY
    }

    interface ReadListener {
        /**
         * Notifies that a write operation was aborted.
         * 
         * @param address the address of the datapoint
         * @param value the value being written
         * @param reason the reason that caused the write operation to be aborted
         * @param requestId the id of the request
         */
        void readAborted(DatapointAddress address, Value value, ErrorType reason, int requestId);


        /**
         * Notifies that a value read from datapoint has arrived.
         * <p>
         * 
         * @param address the datapoint where the datapoint was written.
         * @param readings an array of readings.
         */
        void readAcknowledge(DatapointAddress address, Reading[] readings, int requestId);
    };


    interface WriteListener {
        /**
         * Notifies that a write operation was aborted.
         * 
         * @param address the address of the datapoint
         * @param value the value being written
         * @param reason the reason that caused the write operation to be aborted
         * @param requestId the id of the request
         */
        void writeAborted(DatapointAddress address, Value value, ErrorType reason, int requestId);


        /**
         * Notifies that a value was written to the a datapoint.
         * <p>
         * In some situations it may not possible to guarantee that the value was written.
         * The device or protocol may not support acknowledging writing events. This is
         * the case for example of X10 devices. If the datapoint can be read, implementers
         * 
         * @param address the datapoint where the datapoint was written.
         * @param value the value that was written.
         */
        void writeAcknowledge(DatapointAddress address,
                              Value value,
                              ConfirmationLevel confirmationLevel,
                              int requestId);
    }



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
    int requestDatapointRead(DatapointAddress address, int clientKey);

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
     * @return <ol>
     *         <li>an empty array, if <tt>start &gt; finish</tt></li><li>a single reading
     *         <i>r</i>, if available, such that <tt><i>r.ts</i> == start == finish</tt>,
     *         if <tt>start == finish</tt></li> <li>all readings <i>r</i>, if avaliable,
     *         such that <tt>start &le;<i>r.ts</i> &le; finish</tt>
     *         </ol>
     */
    int requestDatapointWindowRead(DatapointAddress address,
                                   Timestamp start,
                                   Timestamp finish,
                                   int clientKey);

    /**
     * Request a datapoint write.
     * 
     * @param address the address of the datapoint
     * @param value the value to be written to the datapoint
     * @param clientKey the identifier of the client making the request
     * @return the id of the write request used to by the client to identify the
     *         acknowledge of this request
     */
    int requestDatapointWrite(DatapointAddress address, Value value, int clientKey);

    /**
     * Registers a datapoint read listener.
     * 
     * @param listener the read listener
     * @return a client key
     */
    int addReadListener(ReadListener listener);

    /**
     * Registers a datapoint write listener.
     * 
     * @param listener a write listener
     * @return a client key
     */
    int addWriteListener(WriteListener listener);

    /**
     * Removes a read listener.
     * 
     * @param listener the listener to be removed
     * @return the client key of the listener
     */
    int removeReadListener(ReadListener listener);

    /**
     * Removes a datapoint write listener.
     * 
     * @param listener a write listener
     * @return the client key of the listener
     */
    int removeWriteListener(WriteListener listener);
}
