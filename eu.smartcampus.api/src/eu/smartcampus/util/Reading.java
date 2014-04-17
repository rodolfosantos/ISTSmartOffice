package eu.smartcampus.util;


public class Reading {
    private final Value value;
    private final long timestamp;

    public Reading(Value value, long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }

    public Value getValue() {
        return value;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
