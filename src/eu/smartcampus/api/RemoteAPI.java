/**
 * 
 */
package eu.smartcampus.api;

import java.security.Timestamp;
import java.util.List;
import java.util.Map;

import eu.smartcampus.util.*;


/**
 * @author Diogo
 *
 */
public interface RemoteAPI {

    public Map<DatapointAddress, SamplingRate> getCurrentSamplingRate(Sensor sensor, List<DatapointAddress> datapoints);
    
    public List<DatapointAddress> getDataPointAddresses(Sensor sensor);
    
    public Map<DatapointAddress, Reading> getLastReading(Sensor sensor, List<DatapointAddress> datapoints);
    
    public Map<Timestamp, List<Reading>> readTimeWindow(Sensor sensor, List<DatapointAddress> datapoints, Timewindow window);
    
    public Map<Timestamp, List<Reading>> readSizeWindow(Sensor sensor, List<DatapointAddress> datapoints, Integer nReadings);
            
    public Map<DatapointAddress, Reading> forceDatapointReading(Sensor sensor, List<DatapointAddress> datapoints);

    public Map<DatapointAddress, Integer> getReadStorageCapacity(Sensor sensor, List<DatapointAddress> datapoints);
    
    public Map<DatapointAddress, Metadata> getSensorMetadata(Sensor sensor, List<DatapointAddress> datapoints);
    
    public List<Sensor> getSensorByType(SensorType type);
    
    public List<Sensor> getAllSensors();
    
    public void registerListner(Client client, int idSensor, List<DatapointAddress> datapoints);
    
    public void removeListner(Client client, int idSensor, List<DatapointAddress> datapoints);
}


