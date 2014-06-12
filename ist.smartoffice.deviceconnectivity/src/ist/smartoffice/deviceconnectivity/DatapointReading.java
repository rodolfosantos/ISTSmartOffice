package ist.smartoffice.deviceconnectivity;

import java.util.Date;

/**
 * A datapoint value reading with a timestamp.
 */
public class DatapointReading implements Comparable<DatapointReading> {

    /** The value. */
    private final String value;

    /** The timestamp. */
    private final long timestamp;

    /**
     * Constructs a new datapoint reading.
     *
     * @param value the datapoint value
     * @param timestamp the timestamp when it was read
     */
    public DatapointReading(String value, long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }
    
    /**
     * Constructs a new datapoint reading.
     *
     * @param value the datapoint value
     */
    public DatapointReading(String value) {
        this(value, new Date().getTime());
    }

    /**
     * Gets the datapoint value.
     *
     * @return the value read
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets the timestamp of the datapoint reading
     *
     * @return the timestamp of the reading
     */
    public long getTimestamp() {
        return timestamp;
    }

	@Override
	public int compareTo(DatapointReading other) {
		Long thisLong = new Long(timestamp);
		Long otherLong =  new Long(other.getTimestamp());
		return thisLong.compareTo(otherLong);
	}
}
