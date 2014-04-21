package eu.smartcampus.util;

import java.security.Timestamp;

/**
 * Metadata information about a datapoint.
 */
public class Metadata {

    /**
     * Indicates the type of representation of the data of the datapoint.
     * <p>
     * NOTE:We currently only support two representations: Integers for numeric types and
     * String for everything else.
     */
    public static enum Datatype {
        /**
         * An integer data type can most numeric formats. Datapoints that have decimal
         * values are encoded by the scale.
         */
        INTEGER,

        /**
         * String datatypes are proxies to represent any other information.
         */
        STRING
    };

    /**
     * Distinguishes the type of the operations that can be performed on this datapoint.
     */
    public static enum AccessType {

        /**
         * The datapoint only supports reading.
         */
        READ_ONLY,

        /**
         * The datapoint only supports writing.
         */
        WRITE_ONLY,

        /**
         * The datapoint supports reading and writting.
         */
        READ_WRITE
    };


    /**
     * The units of this data point.
     */
    private String units;

    /**
     * The datatype of the datapoint.
     */
    private Datatype datatype;

    /**
     * The maximum number of digits on a datapoint reading.
     */
    private static final int MAX_DIGITS = 9;

    /**
     * The precision (i.e. the number of digits) of the datapoint values. Should be
     * positive and not greater than {@value #MAX_DIGITS}.
     */
    private int precision;

    /**
     * The number of decimal digits of the datapoint values. The scale can be negative,
     * positive or zero and indicates that the value <i>v</i> of a datapoint must be
     * interpreted as <i>v*10^-scale</i>
     */
    private int scale;

    /**
     * The max sampling frequency.
     */
    private int maxSamplingFrequency;

    /**
     * The current sampling frequency.
     */
    private int currentSamplingFrequency;

    /**
     * The read/write access type.
     */
    private AccessType accessType;

    /**
     * The minimum value change that is detected by the sensor.
     */
    private int changeOfValue;

    /**
     * The time (in milliseconds) to wait until the completion of a command.
     */
    private int hysterisys;

    /**
     * The maximum value that should be displayed to the user.
     */
    private int displayMax;

    /**
     * The minimum value to be displayed to the user.
     */
    private int displayMin;

    /**
     * The upper acceptable limit. This is the maximum that a sensor can read or the
     * minimum that the actuator can see.
     */
    private int upperLimit;

    /**
     * The lower acceptable limit. This is the minimum that a sensor can read or the
     * minimum that the actuator can see.
     */
    private int lowerLimit;

    /**
     * The number or reads that can be cached.
     */
    private int readFifoCacheSize;

    /**
     * Gets the maximum number of readings cached a given datapoint.
     * <p>
     * The value returned by this method can be used prioritize the readings to different
     * datapoints. The devices that maintain have smaller storage capacities must be
     * queried more often in order to maintain the required data freshness.
     * <p>
     * This method returns <tt>0</tt> to signify that the device (or the driver on its
     * behalf) maintains no memory for the readings. As a consequence, every calling
     * 
     * @param address the absolute address of the datapoint for which we want to know the
     *            storage capacity
     * @return an indicative value <i>n</i> of the maximum storage capacity for the
     *         datapoint such that
     *         <tt><i>n</i>&ge;count(readDatapointTimeWindow(address, <i>s</i>,
     * <i>f</i>))</tt> for any <tt><i>s</i></tt> &le; <tt><i>f</i></tt>
     *         {@link #readDatapointWindow(DatapointAddress, Timestamp, Timestamp)} should
     *         return an empty array.z\
     */
    int getCacheCapacity(DatapointAddress address) {
        return 0;
    }


    /**
     * Get the corresponding sampling rates for the datapoints of a given sensor.
     * 
     * @param sensor the sensor to which belong the datapoints
     * @param address the list of datapoints for which we want to know the sampling rate
     * @return the set of sampling rates for the corresponding datapoints
     */
    int getDatapointSamplingRate(DatapointAddress address) {
        return 0;
    }

}
