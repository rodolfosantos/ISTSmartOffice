package eu.smartcampus.api.deviceconnectivity;

// TODO: Auto-generated Javadoc
/**
 * Service definition that abstracts the connection to devices.
 * <p>
 */
/*
 * TODO: Doc updates - Timestamps are local to datapoints. - Read and write
 * operations are not instantaneous.
 */
public interface IDatapointConnectivityService {

	/**
	 * The unique name of this implementation used to identify it in a registry
	 * of existing implementations of this API.
	 * 
	 * @return a string containing the name of this API implementation
	 */
	String getImplementationName();

	/**
	 * Interface for listeners of datapoint events.
	 * 
	 * @see DatapointEvent
	 */
	interface DatapointListener {
		/**
		 * Invoked when a datapoint value update occurs.
		 * <p>
		 * This method can be used to track external changes made in datapoints,
		 * such as a sensor update.
		 * 
		 * @param address
		 *            the address of the datapoint
		 * @param values
		 *            the latest reading values
		 */
		void onDatapointUpdate(DatapointAddress address,
				DatapointReading[] values);

		/**
		 * Invoked when a datapoint error occurs.
		 * 
		 * @param address
		 *            the address of the datapoint
		 * @param error
		 *            the error code
		 */
		void onDatapointError(DatapointAddress address, ErrorType error);
	}

	/**
	 * The type of error found in read or write operations. Tthe detection of
	 * some of the error conditions is dependent on the hardware. Therefore some
	 * service implementations may not be able to report all error codes.
	 */
	enum ErrorType {
		/**
		 * The operation over the datapoint is not supported. This can happen if
		 * the datapoint is {@link Metadata.AccessType#READ_ONLY read-only} and
		 * the operation is a write request, or if the datapoint is
		 * {@link Metadata.AccessType#WRITE_ONLY write-only} and the operation
		 * is a read request.
		 */
		UNSUPORTED_DATAPOINT_OPERATION,
		/**
		 * The datapoint was not found at the given address.
		 */
		DATAPOINT_NOT_FOUND,
		/**
		 * An error occurred while contacting the device containing the
		 * datapoint.
		 */
		DEVICE_CONNECTION_ERROR,
		/**
		 * The device containing the specified datapoint is not responding. The
		 * connection to the gateway or device can be established and the
		 * datapoint address is valid but there is no response from the device
		 * regarding the datapoint. This can be due to a change in the
		 * configuration of the device or to a device malfunction.
		 */
		DEVICE_NOT_RESPONDING,
		/**
		 * The datapoint previously responding longer exists. This can happen if
		 * the configuration of the device changes or for example when a
		 * physical communication port to the gateway is removed.
		 */
		DEVICE_VANISHED,
		/**
		 * The gateway, the bus, or the device is busy and the read or write
		 * operation cannot be completed at the moment.
		 */
		DEVICE_BUSY,
		/**
		 * A communication error occurred while contacting the gateway.
		 */
		GATEWAY_CONNECTION_ERROR,
		/**
		 * The operation could not be completed because the gateway was not
		 * found.
		 */
		GATEWAY_NOT_FOUND,
		/**
		 * The gateway was found but is not responding to the datapoint
		 * operation request.
		 */
		GATEWAY_NOT_RESPONDING,
		/**
		 * The operation could no be completed because the gateway is busy at
		 * the moment, or the connection to the gateway is busy and cannot take
		 * more commands.
		 */
		GATEWAY_BUSY,
		/**
		 * The datapoint operation was aborted due to an internal error on the
		 * service implementation.
		 */
		SERVER_INTERNAL_ERROR
	}

	/**
	 * Interface of call-backs for datapoint read operations.
	 */
	interface ReadCallback {
		/**
		 * Notifies that a read operation was aborted.
		 * 
		 * @param address
		 *            the address of the datapoint
		 * @param reason
		 *            the reason that caused the write operation to be aborted
		 * @param requestId
		 *            the id of the request
		 */
		void onReadAborted(DatapointAddress address, ErrorType reason,
				int requestId);

		/**
		 * Notifies that a value read from datapoint has arrived.
		 * <p>
		 * 
		 * @param address
		 *            the datapoint address that was written.
		 * @param readings
		 *            an array of readings.
		 * @param requestId
		 *            the request id
		 */
		void onReadCompleted(DatapointAddress address,
				DatapointReading[] readings, int requestId);
	}

	/**
	 * Interface of call-backs for datapoint write operations.
	 */
	interface WriteCallback {
		/**
		 * Notifies that a write operation was aborted.
		 * 
		 * @param address
		 *            the address of the datapoint
		 * @param reason
		 *            the reason that caused the write operation to be aborted
		 * @param requestId
		 *            the id of the request
		 */
		void onWriteAborted(DatapointAddress address, ErrorType reason,
				int requestId);

		/**
		 * Notifies for an update in the request status. This method should be
		 * called every time a new confirmation level is reached.
		 * <p>
		 * In some situations it may not possible to guarantee that the value
		 * was written. The device or protocol may not support acknowledging
		 * writing events. This is the case for example of X10 devices. If the
		 * datapoint can be read, implementers
		 * 
		 * @param address
		 *            the datapoint where the datapoint was written.
		 * @param confirmationLevel
		 *            the confirmation level
		 * @param requestId
		 *            the request id
		 */
		void onWriteCompleted(DatapointAddress address,
				WritingConfirmationLevel confirmationLevel, int requestId);
	}

	/**
	 * Level of confirmation of datapoint write operations. Distinct
	 * confirmation levels will be returned, depending on the bus technology,
	 * gateway, and device characteristics.
	 */
	enum WritingConfirmationLevel {
		/**
		 * The device has confirmed that take action upon the write request.
		 */
		DEVICE_ACTION_TAKEN,

		/**
		 * The device confirmed the write operation but there is no confirmation
		 * that the device has actually performed the action requested.
		 */
		DEVICE_CONFIRMED,

		/**
		 * The write operation was sent to the gateway and confirmed. An
		 * acknowledge at this level means that the message has arrived at the
		 * gateway. There is no confirmation that the message has arrived at the
		 * device.
		 */
		GATEWAY_CONFIRMED,

		/**
		 * Write operation sent to the gateway but confirmation so far. This is
		 * the level to be used when there no confirmation is possible.
		 */
		UNCONFIRMED
	}

	/**
	 * Adds a datapoint listener.
	 * 
	 * @param listener
	 *            the datapoint listener
	 */
	void addDatapointListener(DatapointListener listener);

	/**
	 * Gets the addresses of all datapoints of devices controlled by this
	 * service.
	 * 
	 * @return the list of all registered datapoints.
	 */
	DatapointAddress[] getAllDatapoints();

	/**
	 * Returns the metadata of a given datapoint.
	 * 
	 * @param address
	 *            the for which we want to know the metadata
	 * @return the metadata object associated to the datapoint
	 * @throws OperationFailedException
	 *             the operation failed exception containing the error type
	 */
	DatapointMetadata getDatapointMetadata(DatapointAddress address)
			throws OperationFailedException;

	/**
	 * Removes a datapoint listener.
	 * 
	 * @param listener
	 *            the datapoint listener
	 */
	void removeDatapointListener(DatapointListener listener);

	/**
	 * Gets the latest available reading of a datapoint.
	 * <p>
	 * It is expected that implementations of this method force a reading to the
	 * specified datapoint. However, when the reading cannot be forced on the
	 * device, it is expected that the last available reading from the device
	 * (that could have been cached) is returned.
	 * 
	 * @param address
	 *            the absolute address of the datapoint to be read
	 * @param readCallback
	 *            the callback to be notified when data is available or if an
	 *            error occurred
	 * @return the id of the write request used to by the client to identify the
	 *         acknowledge of this request
	 */
	int requestDatapointRead(DatapointAddress address, ReadCallback readCallback);

	/**
	 * Request the readings a datapoint within a given time window.
	 * <p>
	 * Implementations may query a sensor or return cached values.
	 * <p>
	 * NOTE: Since sensors are devices with limited resources, specifying
	 * <tt>start</tt> to far into the past may result in returning only a subset
	 * of the values.
	 * <p>
	 * Upon calling this method, the method {@link ReadCallback#onReadCompleted}
	 * specified of <tt>callback</tt> will:
	 * <ol>
	 * <li><b>not be called</b>, if <tt>start &gt; finish</tt></li>
	 * <li><b>be called once</b> reporting a reading <i>r</i>, if available,
	 * such that <tt><i>r.ts</i> == start == finish</tt>, if
	 * <tt>start == finish</tt></li>
	 * <li><b>be called multiple times</b> for all readings<i>r</i>, such that
	 * <tt>start &le;<i>r.ts</i> &le; finish</tt>.
	 * </ol>
	 * Therefore, when no information exists
	 * {@link ReadCallback#onReadCompleted onReadCompleted} will not be called.
	 * If an error occurs the {@link ReadCallback#onReadAborted onReadAborted}
	 * is called instead.
	 * 
	 * @param address
	 *            the absolute address of the datapoint to be read
	 * @param start
	 *            the timestamp that defines the initial window
	 * @param finish
	 *            timestamp that defines the final window. Should be greater or
	 *            equal to start.
	 * @param readCallback
	 *            the callback to the notified of the datapoint readings
	 * @return the id of the read request used later to identify the callback of
	 *         this request
	 */
	int requestDatapointWindowRead(DatapointAddress address,
			long startTimestamp, long finishTimestamp, ReadCallback readCallback);

	/**
	 * Request a datapoint write.
	 * 
	 * @param address
	 *            the address of the datapoint
	 * @param values
	 *            the values to be written to the datapoint
	 * @param writeCallback
	 *            the callback to be notified of datapoint write operations.
	 * @return the id of the read request used later to identify the callback of
	 *         this request
	 */
	int requestDatapointWrite(DatapointAddress address,
			String[] values, WriteCallback writeCallback);

	/**
	 * The Class OperationFailedException.
	 */
	static class OperationFailedException extends Exception {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 6714056390769181513L;

		/** The error type. */
		private final ErrorType errorType;

		/**
		 * Instantiates a new operation failed exception.
		 * 
		 * @param errorType
		 *            the error type
		 */
		public OperationFailedException(ErrorType errorType) {
			this.errorType = errorType;
		}

		public ErrorType getErrorType() {
			return this.errorType;
		}

	}
}
