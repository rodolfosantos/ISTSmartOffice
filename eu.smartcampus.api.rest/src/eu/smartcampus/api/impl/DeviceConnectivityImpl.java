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
import eu.smartcampus.util.Value;

public class DeviceConnectivityImpl
        implements DeviceConnectivityAPI {
    
    @Override
    public DatapointAddress[] getAllDatapoints() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Metadata getDatapointMetadata(DatapointAddress address) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int requestDatapointRead(DatapointAddress address, int clientKey) {


        //          Reading response;
        //          SensorDriver sensor = new SensorDriver("https://"+address.getAddress()+"/reading", "root", "root");
        //          //String sensorId = address.getAddress();           
        //          /*SensorDriver sensor = null;
        //          switch(sensorId){
        //              case "library"          : sensor = new SensorDriver("https://172.20.70.232/reading", "root", "root"); break;
        //              case "kernel_14"        : sensor = new SensorDriver("https://172.20.70.229/reading", "root", "root"); break;
        //              case "kernel_16"        : sensor = new SensorDriver("https://172.20.70.238/reading", "root", "root"); break;
        //              case "room_1.17"        : sensor = new SensorDriver("https://172.20.70.234/reading", "root", "root"); break;
        //              case "room_1.19"        : sensor = new SensorDriver("https://172.20.70.235/reading", "root", "root"); break;                
        //              case "UTA_A4"           : sensor = new SensorDriver("https://172.20.70.237/reading", "root", "root"); break;                
        //              case "amphitheater_A4"  : sensor = new SensorDriver("https://172.20.70.231/reading", "root", "root"); break;
        //              case "amphitheater_A5"  : sensor = new SensorDriver("https://172.20.70.233/reading", "root", "root"); break;
        //              case "laboratory_1.58"  : sensor = new SensorDriver("https://172.20.70.236/reading", "root", "root"); break;
        //              default                 : System.out.println("*** Error ***: This sensor does NOT exist");
        //          }*/           
        //          double value = sensor.getNewMeasure().getTotalPower();                        
        //          long ts = sensor.getNewMeasure().geTimestamp()*1000;            
        //          response =  new Reading(value, ts);          
        //          return response;

        new Thread() {
            public void run() {
                //meter
                
            }
        }.start();


        return 0;
    }

    @Override
    public int requestDatapointWindowRead(DatapointAddress address,
                                          Timestamp start,
                                          Timestamp finish,
                                          int clientKey) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int requestDatapointWrite(DatapointAddress address, Value value, int clientKey) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int addReadListener(ReadListener listener) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int addWriteListener(WriteListener listener) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void removeReadListener(ReadListener listener) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeWriteListener(WriteListener listener) {
        // TODO Auto-generated method stub

    }








}
