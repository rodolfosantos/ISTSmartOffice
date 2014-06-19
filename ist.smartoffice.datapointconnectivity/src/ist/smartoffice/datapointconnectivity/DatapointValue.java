package ist.smartoffice.datapointconnectivity;

import java.util.Date;

/**
 * A datapoint value with a timestamp.
 */
public class DatapointValue implements Comparable<DatapointValue> {

    /** The value. */
    private final String value;

    /** The timestamp. */
    private final long timestamp;

    /**
     * Constructs a new datapoint value.
     *
     * @param value the datapoint value
     * @param timestamp the timestamp when it was read
     */
    public DatapointValue(String value, long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }
    
    /**
     * Constructs a new datapoint reading.
     *
     * @param value the datapoint value
     */
    public DatapointValue(String value) {
        this(value, new Date().getTime());
    }
    

    public DatapointValue() {
        this("", 0);
    }

    /**
     * Gets the datapoint value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets the timestamp of the datapoint value
     *
     * @return the timestamp 
     */
    public long getTimestamp() {
        return timestamp;
    }

	@Override
	public int compareTo(DatapointValue other) {
		Long thisLong = new Long(timestamp);
		Long otherLong =  new Long(other.getTimestamp());
		return thisLong.compareTo(otherLong);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DatapointValue [value=" + value + ", timestamp=" + timestamp
				+ "]";
	}
	
	
}
