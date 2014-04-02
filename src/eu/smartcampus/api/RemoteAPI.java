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

    public Map<DatapointAddress,SamplingRate> getCurrentSamplingRate(int idSensor, List<DatapointAddress> datapoints);
    
    public List<DatapointAddress> getDataPointAddresses(int idSensor);
    
    public Map<DatapointAddress,LastReading> getLastReading(int idSensor, List<DatapointAddress> datapoints);
    
    public Map<> readTimeWindow(int idSensor, List<DatapointAddress> datapoints, Timespan delta);
    
    
    
}
