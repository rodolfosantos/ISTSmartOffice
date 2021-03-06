package ist.smartoffice.datapointconnectivity;

import java.util.concurrent.Semaphore;

import ist.smartoffice.datapointconnectivity.DatapointAddress;
import ist.smartoffice.datapointconnectivity.DatapointValue;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService.ErrorType;

// TODO: Auto-generated Javadoc
/**
 * The callback used by the REST wrapper to receive read requests.
 */
public class ReadCallbackImpl implements IDatapointConnectivityService.ReadCallback {
	/**
	 * Semaphore used to indicate when the reading has arrived.
	 */
	private Semaphore semaphore;
	/**
	 * The resulting reading.
	 */
	private DatapointValue[] reading = null;

	/** The error reason. */
	private ErrorType errorReason = null;

	/**
	 * Instantiates a new read callback.
	 */
	public ReadCallbackImpl() {
		super();
		this.semaphore = new Semaphore(0);
	}

	public void onReadCompleted(DatapointAddress address,
			DatapointValue[] readings, int requestId) {
		semaphore.release();
		this.reading = readings;
	}

	/**
	 * Gets the reading.
	 * 
	 * @return the reading
	 */
	public DatapointValue getFirstReading() {
		try {
			semaphore.acquire();

			return reading[0];
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
			return null;
		} catch (NullPointerException e) {
			return null;
		}
	}

	/**
	 * Gets the readings.
	 * 
	 * @return the readings
	 */
	public DatapointValue[] getReadings() {
		try {
			semaphore.acquire();
			return reading;
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
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

	public void onReadAborted(DatapointAddress address, ErrorType reason,
			int requestId) {
		this.errorReason = reason;
		semaphore.release();
	}
}
