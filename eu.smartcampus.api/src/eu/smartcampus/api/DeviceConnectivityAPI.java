package eu.smartcampus.api;

import java.security.Timestamp;
import java.util.List;

import eu.smartcampus.util.Client;
import eu.smartcampus.util.DatapointAddress;
import eu.smartcampus.util.Metadata;
import eu.smartcampus.util.Reading;
import eu.smartcampus.util.SamplingRate;
import eu.smartcampus.util.SensorId;
import eu.smartcampus.util.SensorType;

/**
 * API that abstracts the connection details to devices.
 * <p>
 * Timestamps are local to datapoints.
 */
public interface DeviceConnectivityAPI {

    /**
     * Possible errors
     * - Gateway not found
     * - Gateway communication error
     * - Device communication error
     * - 
     * - Device not responding
     * - Device busy
     * - Device network busy
     * - Wrong device address
     * - 
     */
    
    /**
     * Metadata:
     * 
     * - Sampling rate min - 
     * - Sampling rate max - readings/sec
     * - Units - 
     * - Precision - 
     * - Datatype - 
     * - 
     */
    
    
    public class InexistentDatapointException extends
            Exception {


    }

    public class ConnectionErrorException extends
            Exception {

    }

    public class TimeoutException extends
            Exception {

    }

    /**
     * Gets all sensors belonging to a given type.
     * 
     * @param type the type of sensor for which we are looking for
     * @return the list of sensors belonging to a certain type
     */
    List<SensorId> getSensorByType(SensorType type);

    /**
     * Gets all sensors register in the system.
     * 
     * @return the list of all registered sensors
     */
    List<SensorId> getAllSensors();

    /**
     * Gets the datapoints of a given sensor.
     * 
     * @param sensor the sensor for which we want to know the sampling rate
     * @return the list of datapoints for the corresponding sensor
     */
    List<DatapointAddress> getDataPointAddresses(SensorId sensor);

    /**
     * Get the corresponding sampling rates for the datapoints of a given sensor.
     * 
     * @param sensor the sensor to which belong the datapoints
     * @param address the list of datapoints for which we want to know the sampling rate
     * @return the set of sampling rates for the corresponding datapoints
     */
    int getDatapointSamplingRate(DatapointAddress address);

    /**
     * Gets the last available reading of a datapoint.
     * <p>
     * It is expected that implementations of this method force a reading to the specified
     * datapoint. However, when the reading cannot be forced on the device, it is expected
     * that the last available reading from the device (that could have been cached) is
     * returned.
     * 
     * @param address the absolute address of the datapoint to be read
     * @return the last reading of a datapoint
     */
    Reading readDatapoint(DatapointAddress address) throws InexistentDatapointException,
            ConnectionErrorException;

    /**
     * Gets the readings a datapoint within a given time window.
     * <p>
     * Implementations of this method may query the meter or return cached values. Since
     * meters are devices with limited resources, specifying <tt>start</tt> to far into
     * the past may result in only a subset of the values.
     * 
     * @param address the absolute address of the datapoint to be read
     * @param start the timestamp that defines the initial window
     * @param finish timestamp that defines the final window. Should be greater or equal
     *            to start.
     * @return <ol>
     *         <li>an empty array, if <tt>start &gt; finish</tt></li><li>a single reading
     *         <i>r</i>, if available, such that <tt><i>r.ts</i> == start == finish</tt>,
     *         if <tt>start == finish</tt></li> <li>all readings <i>r</i>, if avaliable,
     *         such that <tt>start &le;<i>r.ts</i> &le; finish</tt>
     *         </ol>
     * @throws InexistentDatapointException if the datapoint with the given address does
     *             not exist
     * @throws ConnectionErrorException if the device cannot be contacted
     */
    Reading[] readDatapointWindow(DatapointAddress address, Timestamp start, Timestamp finish) throws InexistentDatapointException,
            ConnectionErrorException;

    /**
     * Gets the maximum number of readings cached a given datapoint.
     * <p>
     * The value returned by this method can be used prioritize the readings to different
     * datapoints. The devices that maintain have smaller storage capacities must be
     * queried more often in order to maintain the required data freshness.
     * <p>
     * This method returns <tt>0</tt> to signify that the device (or the driver on its
     * behalf) maintains no memory for the readings. As a consequence, every calling
     * {@link #readDatapointWindow(DatapointAddress, Timestamp, Timestamp)} should return
     * an empty array.z\
     * 
     * @param address the absolute address of the datapoint for which we want to know the
     *            storage capacity
     * @return an indicative value <i>n</i> of the maximum storage capacity for the
     *         datapoint such that
     *         <tt><i>n</i>&ge;count(readDatapointTimeWindow(address, <i>s</i>,
     *         <i>f</i>))</tt> for any <tt><i>s</i></tt> &le; <tt><i>f</i></tt>
     */
    int getCacheCapacity(DatapointAddress address);

    /**
     * For each datapoint of a given sensor gets their metadata.
     * 
     * @param address the set of datapoints, belonging to the sensor, for which we want to
     *            know the metadata
     * @return the metadata for each datapoint
     */
    Metadata getDatapointMetadata(DatapointAddress address);

    /**
     * Gets all sensors belonging to a given type.
     * 
     * @param type the type of sensor for which we are looking for
     * @return the list of sensors belonging to a certain type
     */
    DatapointAddress getDatapointByType(SensorType type);

    /**
     * Gets all sensors register in the system.
     * 
     * @return the list of all registered sensors
     */
    DatapointAddress[] getAllDatapoints();

    /**
     * Register a client that should be notified for each new datapoint's sensor reading.
     * 
     * @param client the client that should be notified
     * @param sensor the sensor in which the client are interested
     * @return datapoints the sensro's datapoints in which the client is interested
     */
    public void registerListner(Client client, int idSensor, List<DatapointAddress> datapoints);

    /**
     * Unregister a client to stop being notified for each new datapoint's sensor reading.
     * 
     * @param client the client that no longer wants to be notified notified
     * @param sensor the sensor for which the client want to stop to be notified
     * @return datapoints the sensro's datapoints in which the client is not interested
     *         anymore
     */
    public void removeListner(Client client, int idSensor, List<DatapointAddress> datapoints);
}
