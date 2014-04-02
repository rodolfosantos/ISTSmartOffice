/**
 * 
 */
package eu.smartcampus.api;

import java.security.Timestamp;
import java.util.List;
import java.util.Map;

import sun.management.Sensor;
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
 */
public interface DeviceConnectivityAPI {

    /**
     * Get the corresponding sampling rates for the datapoints of a given sensor.
     *
     * @param  sensor the sensor to which belong the datapoints
     * @param  datapoints the list of datapoints for which we want to know the sampling rate
     * @return the set of sampling rates for the corresponding datapoints
     */
    public Map<DatapointAddress, SamplingRate> getCurrentSamplingRate(Sensor sensor,
                                                                      List<DatapointAddress> datapoints);
      
    /**
     * Gets the datapoints of a given sensor
     *
     * @param  sensor the sensor for which we want to know the sampling rate
     * @return the list of datapoints for the corresponding sensor
     */
    public List<DatapointAddress> getDataPointAddresses(SensorId sensor);
    
    
    /**
     * Gets the last datapoint's reading of a given sensor
     *
     * @param  sensor the sensor for which we want to know the last reading
     * @param  datapoints the set of datapoints, belonging to the sensor, for which we want to know the last reading
     * @return the set of last readings for the corresponding datapoints
     */
    public Map<DatapointAddress, Reading> getLastReading(SensorId sensor,
                                                         List<DatapointAddress> datapoints);
    
    
    /**
     * Gets the sensor datapoint's reading of a given time window
     *
     * @param  sensor the sensor for which we want to know the readings
     * @param  datapoints the set of datapoints, belonging to the sensor, for which we want to know the readings
     * @param  window the interval of time for which we want to receive the serie of datapoint's readings  
     * @return the set of last readings for the corresponding datapoints
     */
    public Map<Timestamp, List<Reading>> readTimeWindow(SensorId sensor,
                                                        List<DatapointAddress> datapoints,
                                                        Timewindow window);
    
    
    /**
     * Gets the last N sensor datapoint's readings
     *
     * @param  sensor the sensor for which we want to know the readings
     * @param  datapoints the set of datapoints, belonging to the sensor, for which we want to know the readings
     * @param  nReadings the number of readings that should compose the set of datapoint's readings  
     * @return the set of N last readings for the corresponding datapoints
     */
    public Map<Timestamp, List<Reading>> readSizeWindow(SensorId sensor,
                                                        List<DatapointAddress> datapoints,
                                                        Integer nReadings);
    
    
    /**
     * Force the sensor to measure and return all datapoint's values
     *
     * @param  sensor the sensor for which we want to know the readings
     * @param  datapoints the set of datapoints, belonging to the sensor, for which we want to know the readings
     * @param  nReadings the number of readings that should compose the set of datapoint's readings  
     * @return the set of readings for the corresponding datapoints
     */
    public Map<DatapointAddress, Reading> forceDatapointReading(SensorId sensor,
                                                                List<DatapointAddress> datapoints);
    
    
    /**
     * For each datapoint of a given sensor gets the corresponding maximum storage capacity 
     *
     * @param  sensor the sensor for which we want to know the storage capacity
     * @param  datapoints the set of datapoints, belonging to the sensor, for which we want to know the storage capacity
     * @return the set of maximum capacity for each datapoint
     */
    public Map<DatapointAddress, Integer> getReadStorageCapacity(SensorId sensor,
                                                                 List<DatapointAddress> datapoints);
    
    
    /**
     * For each datapoint of a given sensor gets their metadata 
     *
     * @param  sensor the sensor for which we want to know the metadata
     * @param  datapoints the set of datapoints, belonging to the sensor, for which we want to know the metadata
     * @return the metadata for each datapoint
     */
    public Map<DatapointAddress, Metadata> getSensorMetadata(SensorId sensor,
                                                             List<DatapointAddress> datapoints);

    
    /**
     * Gets all sensors belonging to a given type
     *
     * @param  type the type of sensor for which we are looking for
     * @return the list of sensors belonging to a certain type
     */
    public List<SensorId> getSensorByType(SensorType type);

    
    /**
     * Gets all sensors register in the system
     *
     * @return the list of all registered sensors
     */
    public List<SensorId> getAllSensors();

    
    /**
     * Register a client that should be notified for each new datapoint's sensor reading
     *
     * @param  client the client that should be notified
     * @param  sensor the sensor in which the client are interested
     * @return datapoints the sensro's datapoints in which the client is interested
     */
    public void registerListner(Client client, int idSensor, List<DatapointAddress> datapoints);

    
    /**
     * Unregister a client to stop being notified for each new datapoint's sensor reading
     *
     * @param  client the client that no longer wants to be notified notified
     * @param  sensor the sensor for which the client want to stop to be notified
     * @return datapoints the sensro's datapoints in which the client is not interested anymore 
     */
    public void removeListner(Client client, int idSensor, List<DatapointAddress> datapoints);
}
