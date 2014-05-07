package eu.smartcampus.api;

/**
 * A datapoint value reading with a timestamp.
 */
public class DatapointReading {

    /** The value. */
    private final DatapointValue value;

    /** The timestamp. */
    private final long timestamp;

    /**
     * Constructs a new datapoint reading.
     *
     * @param value the datapoint value
     * @param timestamp the timestamp when it was read
     */
    public DatapointReading(DatapointValue value, long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }

    /**
     * Gets the datapoint value.
     *
     * @return the value read
     */
    public DatapointValue getValue() {
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
}
