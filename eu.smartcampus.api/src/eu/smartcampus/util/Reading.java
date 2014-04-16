package eu.smartcampus.util;


public class Reading {

    private final double value;
    private final long ts;

    public Reading(double value, long ts) {
        this.value = value;
        this.ts = ts;
    }

    public double getValue() {
        return value;
    }

    public long getTs() {
        return ts;
    }

}
