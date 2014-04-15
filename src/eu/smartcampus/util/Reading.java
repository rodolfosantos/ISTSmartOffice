package eu.smartcampus.util;


public class Reading {

    private final int value;
    private final long ts;

    public Reading(int value, long ts) {
        this.value = value;
        this.ts = ts;
    }

    public int getValue() {
        return value;
    }

    public long getTs() {
        return ts;
    }

}
