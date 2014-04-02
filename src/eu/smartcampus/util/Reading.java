package eu.smartcampus.util;

import java.security.Timestamp;

public class Reading {

    private int value;
    private Timestamp ts;
    private DatapointAddress datapoint;
    
    
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
    public Timestamp getTs() {
        return ts;
    }
    public void setTs(Timestamp ts) {
        this.ts = ts;
    }
    public DatapointAddress getDatapoint() {
        return datapoint;
    }
    public void setDatapoint(DatapointAddress datapoint) {
        this.datapoint = datapoint;
    }
   
    
}
