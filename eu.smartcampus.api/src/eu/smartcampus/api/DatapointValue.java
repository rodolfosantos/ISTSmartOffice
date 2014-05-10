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
     * The boolean value. Used to encode binary states.
     */
    private final boolean booleanValue;

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
        booleanValue = false;
    }

    /**
     * Instantiates a new datapoint value.
     * 
     * @param i the integer value, must be greater that {@value Integer#MIN_VALUE}
     */
    public DatapointValue(int i) {
        if (i == Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Value cannot be Integer.MIN_VALUE");
        }
        datatype = DatapointMetadata.Datatype.INTEGER;
        intValue = i;
        stringValue = null;
        booleanValue = false;
    }
    
    /**
     * Instantiates a new datapoint value.
     * 
     * @param i the integer value, must be greater that {@value Integer#MIN_VALUE}
     */
    public DatapointValue(boolean b) {
        datatype = DatapointMetadata.Datatype.INTEGER;
        intValue = Integer.MIN_VALUE;
        stringValue = null;
        booleanValue = false;
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
     * Gets the boolean value.
     * 
     * @return the boolean value
     */
    public boolean getBooleanValue() {
        if (datatype != DatapointMetadata.Datatype.BOOLEAN) {
            throw new UnsupportedOperationException();
        }
        return booleanValue;
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
    
    /**
     * Checks if the value carrier is an boolean.
     * 
     * @return <code>true</code>, if is boolean
     */
    public boolean isBoolean() {
        assert stringValue != null;
        assert intValue != Integer.MIN_VALUE;
        return true;
    }


    /**
     * Implements toString method in order to print concrete value
     * 
     * @return the value in form of string
     */
    public String toString() {
        switch (datatype) {
            case INTEGER:
                return intValue + "";
            case STRING:
                return stringValue;
            case BOOLEAN:
                return booleanValue + "";
        }
        return "";

    }
}
