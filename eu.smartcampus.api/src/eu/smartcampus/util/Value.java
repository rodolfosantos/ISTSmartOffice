package eu.smartcampus.util;

/**
 * Representation for datapoint values.
 */
public class Value {
    private final Metadata.Datatype datatype;
    private final int intValue;
    private final String stringValue;

    public Value(String s) {
        datatype = Metadata.Datatype.STRING;
        stringValue = s;
        intValue = 0;
    }

    public Value(int i) {
        datatype = Metadata.Datatype.INTEGER;
        intValue = i;
        stringValue = null;
    }

    public int getIntValue() {
        if (datatype != Metadata.Datatype.INTEGER) {
            throw new UnsupportedOperationException();
        }
        return intValue;
    }

    public String getStringValue() {
        if (datatype != Metadata.Datatype.STRING) {
            throw new UnsupportedOperationException();
        }
        return stringValue;
    }
}
