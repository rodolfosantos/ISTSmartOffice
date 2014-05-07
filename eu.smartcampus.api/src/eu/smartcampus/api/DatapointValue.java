package eu.smartcampus.api;

/**
 * Representation for datapoint values.
 */
public class DatapointValue {
    /**
     * The datatype of the value.
     */
    private final DatapointMetadata.Datatype datatype;

    /**
     * The integer value. Depending on the datapoint metadata, this value can be used to
     * encode decimal values. If this field is assigned then {@link #stringValue} must be
     * <code>null</code>.
     */
    private final int intValue;

    /**
     * The string value. Used to encode any non-scalar data. If this value is assigned
     * then {@link #intValue} in {@link Integer#MIN_VALUE}.
     */
    private final String stringValue;

    /**
     * Instantiates a new datapoint value.
     *
     * @param s the string value, cannot be <code>null</code>
     */
    public DatapointValue(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        datatype = DatapointMetadata.Datatype.STRING;
        stringValue = s;
        intValue = Integer.MIN_VALUE;
    }

    /**
     * Instantiates a new datapoint value.
     *
     * @param i the integer value, must be greater that {@value Integer#MIN_VALUE}
     */
    public DatapointValue(int i) {
        if (i == Integer.MIN_VALUE) {
            throw new IllegalArgumentException(
                    "Value cannot be Integer.MIN_VALUE");
        }
        datatype = DatapointMetadata.Datatype.INTEGER;
        intValue = i;
        stringValue = null;
    }

    /**
     * Gets the int value.
     *
     * @return the int value
     */
    public int getIntValue() {
        if (datatype != DatapointMetadata.Datatype.INTEGER) {
            throw new UnsupportedOperationException();
        }
        return intValue;
    }

    /**
     * Gets the string value.
     *
     * @return the string value
     */
    public String getStringValue() {
        if (datatype != DatapointMetadata.Datatype.STRING) {
            throw new UnsupportedOperationException();
        }
        return stringValue;
    }

    /**
     * Checks if the value carrier is a string.
     *
     * @return <code>true</code>, if is string
     */
    public boolean isString() {
        assert intValue != Integer.MIN_VALUE;
        return stringValue != null;
    }

    /**
     * Checks if the value carrier is an integer.
     *
     * @return <code>true</code>, if is integer
     */
    public boolean isInteger() {
        assert stringValue != null;
        return intValue != Integer.MIN_VALUE;
    }
}
