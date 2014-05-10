package eu.smartcampus.api.rest.deviceapi.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.Timestamp;

import eu.smartcampus.api.DatapointAddress;
import eu.smartcampus.api.DatapointMetadata;
import eu.smartcampus.api.DatapointReading;
import eu.smartcampus.api.DatapointValue;
import eu.smartcampus.api.IDatapointConnectivityService;

public class DatapointConnectivityServiceImpl
        implements IDatapointConnectivityService {


    private MeterDriverIP driver;

    public DatapointConnectivityServiceImpl(MeterDriverIP driver) {
        super();
        this.driver = driver;
    }

    public void addDatapointListener(DatapointListener listener) {
        // TODO Auto-generated method stub

    }

    public DatapointAddress[] getAllDatapoints() {
        // TODO Auto-generated method stub
        return new DatapointAddress[] { new DatapointAddress("addr1"),
                new DatapointAddress("addr2"), new DatapointAddress("addr3") };
    }

    public DatapointMetadata getDatapointMetadata(DatapointAddress address) {
        // TODO Auto-generated method stub
        return null;
    }

    public void removeDatapointListener(DatapointListener listener) {
        // TODO Auto-generated method stub

    }

    public int requestDatapointRead(DatapointAddress address, ReadCallback readCallback) {
        try {
            MeterMeasure value = driver.getNewMeasure(address.getAddress());
            DatapointReading reading = new DatapointReading(new DatapointValue(
                    value.getTotalPower() + ""));
            System.out.println("READING!!!" + " "+ reading.getValue());
            readCallback.onReadCompleted(address, new DatapointReading[] { reading }, 0);
            return 0;
        } catch (MalformedURLException e) {
            readCallback.onReadAborted(address, ErrorType.DATAPOINT_NOT_FOUND, 0);
        }

        return 0;
    }


    public int requestDatapointWrite(DatapointAddress address,
                                     DatapointValue[] values,
                                     WriteCallback writeCallback) {
        writeCallback.onWriteCompleted(address, WritingConfirmationLevel.DEVICE_ACTION_TAKEN, 0);
        return 0;
    }

    @Override
    public int requestDatapointWindowRead(DatapointAddress address,
                                          long startTimestamp,
                                          long finishTimestamp,
                                          ReadCallback readCallback) {
        // TODO Auto-generated method stub
        return 0;
    }

}
