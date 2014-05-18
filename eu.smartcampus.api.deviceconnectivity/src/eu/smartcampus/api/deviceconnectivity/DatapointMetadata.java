package eu.smartcampus.api.deviceconnectivity;

/**
 * Metadata information about a datapoint.
 */
public class DatapointMetadata {

    /**
     * Indicates the type of representation of the data of the datapoint.
     * <p>
     * NOTE:We currently only support two representations: Integers for numeric types and
     * String for everything else.
     */
    public static enum Datatype {
        /**
         * An integer data type. Integers can represent also decimal formats. Datapoints
         * that have decimal values are encoded by scale.
         */
        INTEGER,
        
        /**
         * An boolean data type. Booleans can represent only two states, true or
         * false
         * 
         */
        BOOLEAN,

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
     * The alias of this data point.
     */
    private String description;

    /**
     * The units of this data point.
     */
    private String units;

    /**
     * The datatype of the datapoint. Indicates what is the carrier of the information.
     */
    private Datatype datatype;

    /**
     * The access type.
     */
    private AccessType accessType;

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
     * The smallest sampling rate interval in milliseconds that can be requested to the
     * datapoint.
     */
    private long smallestReadInterval;

    /**
     * Current sampling rate interval in milliseconds that is being requested to the
     * datapoint.
     */
    private long currentSamplingInterval;

    /**
     * The minimum increment that is detected by the sensor and reported in the datapoint.
     * Should be zero if not applicable.
     */
    private int changeOfValue;

    /**
     * The minimum amount of time that the client must wait until another device is ready
     * to receive another command though this datapoint in milliseconds.
     */
    private int hysteresis;

    /**
     * The maximum value that can be displayed to the user.
     */
    private int displayMax;

    /**
     * The minimum value that can be displayed to the user.
     */
    private int displayMin;

    /**
     * The maximum value that the hardware can read. Usually this value is greater that
     * {@link #displayMax}, but not necessarily.
     */
    private int readingMax;

    /**
     * The minimum value that the hardware can read. Usually this value is smaller than
     * {@link #displayMin}, but not necessarily.
     */
    private int readingMin;

    /**
     * A positive integer indicating the number of readings that can be cached (to be
     * queried later) for this datapoint. If the device does not have memory this value
     * can be zero. If the device is a virtual device backed by a database this value can
     * be {@link Integer#MAX_VALUE}.
     */
    private int readCacheSize;

    /**
     * The maximum valid value for a scale.
     */
    private static final int SCALE_MAX = 32;

    /**
     * The minimum valid value for a scale.
     */
    private static final int SCALE_MIN = -32;

    /**
     * Builder for a {@link DatapointMetadata} object.
     */
    public static class MetadataBuilder {
    	private String alias;
        private String units;
        private Datatype datatype;
        private AccessType accessType;
        private int precision;
        private int scale;
        private long smallestSamplingInterval;
        private long currentSamplingInterval;
        private int changeOfValue;
        private int hysteresis;
        private int displayMax;
        private int displayMin;
        private int readingMax;
        private int readingMin;
        private int readCacheSize;

        /**
         * Sets the alias of the datapoint.
         *
         * @param alias the new alias
         */
        public void setAlias(String alias) {
            this.alias = alias;
        }
        
        /**
         * Sets the units of the datapoint.
         *
         * @param units the new units
         */
        public void setUnits(String units) {
            this.units = units;
        }

        /**
         * Sets the datatype of the datapoint.
         *
         * @param datatype the new datatype
         */
        public void setDatatype(Datatype datatype) {
            this.datatype = datatype;
        }

        /**
         * Sets the access type of the datapoint.
         *
         * @param accessType the new access type
         */
        public void setAccessType(AccessType accessType) {
            this.accessType = accessType;
        }

        /**
         * Sets the precision of the datapoint.
         *
         * @param precision the new precision, must be positive and not greater than
         *            {@value DatapointMetadata#MAX_DIGITS}
         */
        public void setPrecision(int precision) {
            if (precision < 0 || precision > DatapointMetadata.MAX_DIGITS) {
                throw new IllegalArgumentException("Invalid precision:"
                                                   + precision);
            }
            this.precision = precision;
        }

        /**
         * Sets the scale of the datapoint.
         *
         * @param s the new scale such that {@value DatapointMetadata#SCALE_MIN} &le; s
         *            &le; {@value DatapointMetadata#SCALE_MAX}
         */
        public void setScale(int s) {
            if (s < SCALE_MIN || s > SCALE_MAX) {
                throw new IllegalArgumentException("Invalid scale:" + s);
            }
            this.scale = s;
        }

        /**
         * Sets the smallest sampling interval of the datapoint.
         *
         * @param interval the new smallest sampling interval, must be positive
         */
        public void setSmallestSamplingInterval(long interval) {
            if (interval <= 0) {
                throw new IllegalArgumentException(
                        "Sampling interval must be positive");
            }
            this.smallestSamplingInterval = interval;
        }

        /**
         * Sets the current sampling interval of the datapoint.
         *
         * @param interval the new current sampling interval, must be posiitive
         */
        public void setCurrentSamplingInterval(long interval) {
            if (interval <= 0) {
                throw new IllegalArgumentException(
                        "Sampling interval must be positive");
            }
            this.currentSamplingInterval = interval;
        }

        /**
         * Sets the change of value of the datapoint.
         *
         * @param delta the new change of value, must be positive without scale correction
         */
        public void setChangeOfValue(int delta) {
            if (delta <= 0) {
                throw new IllegalArgumentException(
                        "Change of value must be positive");
            }
            this.changeOfValue = delta;
        }

        /**
         * Sets the hysteresis of the datapoint.
         *
         * @param h the new hysteresis, must be positive
         */
        public void sethysteresis(int h) {
            if (h <= 0) {
                throw new IllegalArgumentException("Hyterisis must be positive");
            }
            this.hysteresis = h;
        }

        /**
         * Sets the display max of the datapoint.
         *
         * @param max the new display max value, without scale correction
         */
        public void setDisplayMax(int max) {
            this.displayMax = max;
        }

        /**
         * Sets the display min of the datapoint.
         *
         * @param min the new display minimum, without scale correction
         */
        public void setDisplayMin(int min) {
            this.displayMin = min;
        }

        /**
         * Sets the reading max of the datapoint.
         *
         * @param max the new reading maximum, without scale correction
         */
        public void setReadingMax(int max) {
            this.readingMax = max;
        }

        /**
         * Sets the reading minimum of the datapoint.
         *
         * @param min the new reading minimum
         */
        public void setReadingMin(int min) {
            this.readingMin = min;
        }

        /**
         * Sets the read cache size of the datapoint.
         *
         * @param readCacheSize the new read cache size, without scale correction
         */
        public void setReadCacheSize(int readCacheSize) {
            this.readCacheSize = readCacheSize;
        }

        /**
         * Builds the of the datapoint.
         *
         * @return the datapoint metadata
         */
        public DatapointMetadata build() {
            final DatapointMetadata result = new DatapointMetadata();
            result.accessType = accessType;
            result.description = alias;
            result.units = units;
            result.datatype = datatype;
            result.accessType = accessType;
            result.precision = precision;
            result.scale = scale;
            result.smallestReadInterval = smallestSamplingInterval;
            result.currentSamplingInterval = currentSamplingInterval;
            result.changeOfValue = changeOfValue;
            result.hysteresis = hysteresis;
            result.displayMax = displayMax;
            result.displayMin = displayMin;
            result.readingMax = readingMax;
            result.readingMin = readingMin;
            result.readCacheSize = readCacheSize;
            return result;
        }
    }

    /**
     * Metadata cannot be instantiated from outside.
     */
    private DatapointMetadata() {
    }
    
    /**
     * Gets the alias.
     *
     * @return the alias
     */
    public String getAlias() {
        return description;
    }


    /**
     * Gets the units.
     *
     * @return the units
     */
    public String getUnits() {
        return units;
    }

    /**
     * Gets the datatype of the datapoint
     *
     * @return the datatype of the datapoint
     */
    public Datatype getDatatype() {
        return datatype;
    }

    /**
     * Gets the access type.
     *
     * @return the access type
     */
    public AccessType getAccessType() {
        return accessType;
    }

    /**
     * Gets the precision of the datapoint. The precision is the number of digits of the
     * datapoint values. Should be positive and not greater than
     * {@value DatapointMetadata#MAX_DIGITS}.
     *
     * @return a positive number not greater than {@value DatapointMetadata#MAX_DIGITS}.
     */
    public int getPrecision() {
        return precision;
    }

    /**
     * The number of decimal digits of the datapoint values.
     * <p>
     * The scale can be negative, positive or zero and indicates that the value <i>v</i>
     * of a datapoint must be interpreted as <i>v*10^-scale</i>
     * 
     * @return the number of decimal digits of the datapoint values.
     */
    public int getScale() {
        return scale;
    }

    /**
     * Gets the smallest interval between consecutive reads supported by the hardware.
     *
     * @return a read interval in milliseconds
     */
    public long getSmallestReadInterval() {
        return smallestReadInterval;
    }

    /**
     * Gets the current interval used to sample the datapoint values.
     *
     * @return the current sampling interval in milliseconds.
     */
    public long getCurrentSamplingInterval() {
        return currentSamplingInterval;
    }

    /**
     * Gets the minimum increment of value is detected by the sensor and reported in the
     * datapoint. Should be zero if not applicable.
     *
     * @return the change of value in the without scale correction
     */
    public int getChangeOfValue() {
        return changeOfValue;
    }

    /**
     * Gets the amount of time that clients must wait until the device is ready to receive
     * another command.
     *
     * @return the hysteresis value in milliseconds
     */
    public int getHysteresis() {
        return hysteresis;
    }

    /**
     * Gets the maximum value that can be displayed to the user. This value will be
     * interpreted visually as the top of a value scale. For example, a percent datapoint
     * may have the value 100. In the case of a temperature, this will be the maximum
     * temperature that the sensor is expected to reported.
     * 
     * @return an integer value without the scale correction
     */
    public int getDisplayMax() {
        return displayMax;
    }

    /**
     * Gets the maximum value that can be displayed to the user. This value will be
     * interpreted visually as the bottom of a value scale. For example, a percent
     * datapoint may have the value 0. In the case of a temperature this is the minimum
     * temperature that can be reported.
     *
     * @return an integer value without the scale correction.
     */
    public int getDisplayMin() {
        return displayMin;
    }

    /**
     * Gets maximum value that the hardware can read. Usually this value is greater than
     * {@link #displayMax}, but not necessarily.
     *
     * @return the maximum reading value without scale correction
     */
    public int getReadingMax() {
        return readingMax;
    }

    /**
     * GGets minimum value that the hardware can read. Usually this value is smaller than
     * {@link #displayMin}, but not necessarily.
     * 
     * @return the maximum reading value without scale correction
     */
    public int getReadingMin() {
        return readingMin;
    }

    /**
     * Gets the maximum number of readings that can be cached for this datapoint.
     * <p>
     * If the device is a virtual device backed by a database this value can be
     * {@link Integer#MAX_VALUE}.
     * <p>
     * The devices that have smaller storage capacities must be queried more often in
     * order to maintain the required data freshness. If the device does not have memory
     * this value can be zero. The value returned by this method can be used prioritize
     * the readings to different datapoints.
     * 
     * @return a positive integer <i>n</i> indicating the number of readings that can be
     *         cached (to be queried later) for this datapoint, where:
     *         <ul>
     *         <li><i>n == 0</i> means that the datapoint cannot report any readings</li>
     *         <li><i>n == 1</i> means that the datapoint can report only one (the last)
     *         reading</li> <li><i>n > 1</i> means that the datapoint can report multiple
     *         readings into the past</li>
     *         </ul>
     */
    public int getReadCacheSize() {
        return readCacheSize;
    }
}
