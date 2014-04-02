/**
 * 
 */
package eu.smartcampus.api;

import java.util.List;
import java.util.Map;

import eu.smartcampus.util.*;


/**
 * @author Diogo
 *
 */
public interface RemoteAPI {

    public Map<DatapointAddress, SamplingRate> getCurrentSamplingRate(int idSensor, List<DatapointAddress> datapoints);
    
    public List<DatapointAddress> getDataPointAddresses(int idSensor);
    
    public Map<DatapointAddress, Reading> getLastReading(int idSensor, List<DatapointAddress> datapoints);
    
    public Map<Timestamp, List<Reading>> readTimeWindow(int idSensor, List<DatapointAddress> datapoints, Timewindow window);
    
    public Map<Integer, List<Reading>> readSizeWindow(int idSensor, List<DatapointAddress> datapoints, Timewindow nReadings);
            
    public Map<DatapointAddress, Reading> forceDatapointReading(int idSensor, List<DatapointAddress> datapoints);

    public Map<DatapointAddress, Integer> getReadStorageCapacity(int idSensor, List<DatapointAddress> datapoints);
    
    public Map<DatapointAddress, Metadata> getSensorMetadata(int idSensor, List<DatapointAddress> datapoints);
    
    public List<idSensor> getSensorByType(SensorType type);
    
    public List<idSensor> getAllSensors();
    
    public void registerListner(Client client, int idSensor, List<DatapointAddress> datapoints);
    
    public void removeListner(Client client, int idSensor, List<DatapointAddress> datapoints);
}


