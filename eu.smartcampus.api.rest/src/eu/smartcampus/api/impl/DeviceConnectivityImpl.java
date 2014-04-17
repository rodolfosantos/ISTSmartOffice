package eu.smartcampus.api.impl;

import java.security.Timestamp;
import java.util.List;
import java.util.Map;

import eu.smartcampus.api.DeviceConnectivityAPI;
import eu.smartcampus.util.Client;
import eu.smartcampus.util.DatapointAddress;
import eu.smartcampus.util.Metadata;
import eu.smartcampus.util.Reading;
import eu.smartcampus.util.SamplingRate;
import eu.smartcampus.util.SensorId;
import eu.smartcampus.util.SensorType;
import eu.smartcampus.util.Timewindow;

public class DeviceConnectivityImpl implements DeviceConnectivityAPI {

    /* 
    private static DeviceConnectivityImpl instance = null;
    
    
    public DeviceConnectivityImpl() {
        instance = new DeviceConnectivityImpl();
    }*/
    
    
    @Override
    public Map<DatapointAddress, Reading> forceDatapointReading(SensorId arg0,
                                                                List<DatapointAddress> arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<SensorId> getAllSensors() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<DatapointAddress, SamplingRate> getCurrentSamplingRate(SensorId arg0,
                                                                      List<DatapointAddress> arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<DatapointAddress> getDataPointAddresses(SensorId arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<DatapointAddress, Integer> getReadStorageCapacity(SensorId arg0,
                                                                 List<DatapointAddress> arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<SensorId> getSensorByType(SensorType arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<DatapointAddress, Metadata> getSensorMetadata(SensorId arg0,
                                                             List<DatapointAddress> arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Reading readDatapoint(DatapointAddress address) {
        
        String sensorId = address.getAddress();   
        Reading response;
        SensorDriver sensor = null;
        switch(sensorId){
            case "library"          : sensor = new SensorDriver("https://172.20.70.232/reading", "root", "root"); break;
            case "kernel_14"        : sensor = new SensorDriver("https://172.20.70.229/reading", "root", "root"); break;
            case "kernel_16"        : sensor = new SensorDriver("https://172.20.70.238/reading", "root", "root"); break;
            case "room_1.17"        : sensor = new SensorDriver("https://172.20.70.234/reading", "root", "root"); break;
            case "room_1.19"        : sensor = new SensorDriver("https://172.20.70.235/reading", "root", "root"); break;                
            case "UTA_A4"           : sensor = new SensorDriver("https://172.20.70.237/reading", "root", "root"); break;                
            case "amphitheater_A4"  : sensor = new SensorDriver("https://172.20.70.231/reading", "root", "root"); break;
            case "amphitheater_A5"  : sensor = new SensorDriver("https://172.20.70.233/reading", "root", "root"); break;
            case "laboratory_1.58"  : sensor = new SensorDriver("https://172.20.70.236/reading", "root", "root"); break;
            default                 : System.out.println("*** Error ***: This sensor does NOT exist");
        }            
        double value = sensor.getNewMeasure().getTotalPower();                        
        long ts = sensor.getNewMeasure().geTimestamp()*1000;            
        response =  new Reading(value, ts);          
        return response;
    }

    @Override
    public Map<Timestamp, List<Reading>> readSizeWindow(SensorId arg0,
                                                        List<DatapointAddress> arg1,
                                                        Integer arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<Timestamp, List<Reading>> readTimeWindow(SensorId arg0,
                                                        List<DatapointAddress> arg1,
                                                        Timewindow arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void registerListner(Client arg0, int arg1, List<DatapointAddress> arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeListner(Client arg0, int arg1, List<DatapointAddress> arg2) {
        // TODO Auto-generated method stub

    }

}
