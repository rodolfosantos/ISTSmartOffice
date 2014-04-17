/**
 * 
 */
package eu.smartcampus.api;

import java.security.Timestamp;
import java.util.List;
import java.util.Map;

import eu.smartcampus.util.Client;
import eu.smartcampus.util.DatapointAddress;
import eu.smartcampus.util.Metadata;
import eu.smartcampus.util.Reading;
import eu.smartcampus.util.SamplingRate;
import eu.smartcampus.util.SensorId;
import eu.smartcampus.util.SensorType;
import eu.smartcampus.util.Timewindow;

/**
 * @author Diogo
 *
 */
public interface DeviceConnectivityAPI {

    public Map<DatapointAddress, SamplingRate> getCurrentSamplingRate(SensorId sensor, List<DatapointAddress> datapoints);
    
    public List<DatapointAddress> getDataPointAddresses(SensorId sensor);
    
    public Map<DatapointAddress, Reading> getLastReading(SensorId sensor, List<DatapointAddress> datapoints);
    
    public Map<Timestamp, List<Reading>> readTimeWindow(SensorId sensor, List<DatapointAddress> datapoints, Timewindow window);
    
    public Map<Timestamp, List<Reading>> readSizeWindow(SensorId sensor, List<DatapointAddress> datapoints, Integer nReadings);
            
    public Map<DatapointAddress, Reading> forceDatapointReading(SensorId sensor, List<DatapointAddress> datapoints);

    public Map<DatapointAddress, Integer> getReadStorageCapacity(SensorId sensor, List<DatapointAddress> datapoints);
    
    public Map<DatapointAddress, Metadata> getSensorMetadata(SensorId sensor, List<DatapointAddress> datapoints);
    
    public List <SensorId> getSensorByType(SensorType type);
    
    public List<SensorId> getAllSensors();
    
    public void registerListner(Client client, int idSensor, List<DatapointAddress> datapoints);
    
    public void removeListner(Client client, int idSensor, List<DatapointAddress> datapoints);
}


