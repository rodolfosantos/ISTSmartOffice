package eu.smartcampus.util;

import java.security.Timestamp;

/**
 * Metadata information about a datapoint.
 */
/**
 * @author Administrator
 *
 */
public class Metadata {

	/**
	 * Indicates the type of representation of the data of the datapoint.
	 * <p>
	 * NOTE:We currently only support two representations: Integers for numeric
	 * types and String for everything else.
	 */
	public static enum Datatype {
		/**
		 * An integer data type. Integers can represent also decimal formats.
		 * Datapoints that have decimal values are encoded by scale.
		 */
		INTEGER,

		/**
		 * String datatypes are proxies to represent any other information.
		 */
		STRING
	};

	/**
	 * Distinguishes the type of the operations that can be performed on this
	 * datapoint.
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
	 * The datatype of the datapoint. Indicates what is the carrier of the
	 * information.
	 */
	private Datatype datatype;

	/**
	 * The access type.
	 * */
	private AccessType accessType;

	/**
	 * The maximum number of digits on a datapoint reading.
	 */
	private static final int MAX_DIGITS = 9;

	/**
	 * The precision (i.e. the number of digits) of the datapoint values. Should
	 * be positive and not greater than {@value #MAX_DIGITS}.
	 */
	private int precision;

	/**
	 * The number of decimal digits of the datapoint values. The scale can be
	 * negative, positive or zero and indicates that the value <i>v</i> of a
	 * datapoint must be interpreted as <i>v*10^-scale</i>
	 */
	private int scale;

	/**
	 * The smallest sampling rate interval in milliseconds that can be requested
	 * to the datapoint.
	 */
	private long smallestSamplingInterval;

	/**
	 * Current sampling rate interval in milliseconds that is being requested to
	 * the datapoint.
	 */
	private long currentSamplingInterval;

	/**
	 * The minimum increment that is detected by the sensor and reported in the
	 * datapoint. Should be zero if not applicable.
	 */
	private int changeOfValue;

	/**
	 * The minimum amount of time that the client must wait until another device
	 * is ready to receive another command though this datapoint.
	 */
	private int hysterisys;

	/**
	 * The maximum value that can be displayed to the user. This value will be
	 * interpreted visually as the top of a value scale. For example, a percent
	 * datapoint may have the value 100. In the case of a temperature, this will
	 * be the maximum temperature that the sensor is expected to reported.
	 */
	private int displayMax;

	/**
	 * The minimum value that can be displayed to the user. This value will be
	 * interpreted visually as the bottom of a value scale. For example, a
	 * percent datapoint may have the value 0. In the case of a temperature this
	 * is the minimum temperature that can be reported.
	 */
	private int displayMin;

	/**
	 * The maximum value that the hardware can read. Usually this value is
	 * greater that {@link #displayMax}, but not necessarily.
	 */
	private int readingMax;

	/**
	 * The minimum value that the hardware can read. Usually this value is
	 * smaller than {@link #displayMin}, but not necessarily. Values below this
	 * limit mean
	 */
	private int readingMin;

	/**
	 * A positive integer indicating the number of readings that can be cached
	 * (to be queried later) for this datapoint. If the device does not have
	 * memory this value can be zero. If the device is a virtual device backed
	 * by a database this value can be {@link Integer#MAX_VALUE}.
	 */
	private int readCacheSize;

	/**
	 * Builder for a {@link #Metadata()} object.
	 */
	public static class MetadataBuilder {
		/** @see Metadata#units */
		private String units;

		/** @see Metadata#datatype */
		private Datatype datatype;

		/** @see Metadata#accessType */
		private AccessType accessType;

		/** @see Metadata#precision */
		private int precision;

		/** @see Metadata#scale */
		private int scale;

		/** @see Metadata#smallestSamplingInterval */
		private long smallestSamplingInterval;

		/** @see Metadata#currentSamplingInterval */
		private long currentSamplingInterval;

		/** @see Metadata#changeOfValue */
		private int changeOfValue;

		/** @see Metadata#hysterisys */
		private int hysterisys;

		/** @see Metadata#displayMax */
		private int displayMax;

		/** @see Metadata#displayMin */
		private int displayMin;

		/** @see Metadata#readingMax */
		private int readingMax;

		/** @see Metadata#readingMin */
		private int readingMin;

		/** @see Metadata#readCacheSize */
		private int readCacheSize;

		public void setUnits(String units) {
			this.units = units;
		}

		public void setDatatype(Datatype datatype) {
			this.datatype = datatype;
		}

		public void setAccessType(AccessType accessType) {
			this.accessType = accessType;
		}

		public void setPrecision(int precision) {
			this.precision = precision;
		}

		public void setScale(int scale) {
			this.scale = scale;
		}

		public void setSmallestSamplingInterval(long smallestSamplingInterval) {
			this.smallestSamplingInterval = smallestSamplingInterval;
		}

		public void setCurrentSamplingInterval(long currentSamplingInterval) {
			this.currentSamplingInterval = currentSamplingInterval;
		}

		public void setChangeOfValue(int changeOfValue) {
			this.changeOfValue = changeOfValue;
		}

		public void setHysterisys(int hysterisys) {
			this.hysterisys = hysterisys;
		}

		public void setDisplayMax(int displayMax) {
			this.displayMax = displayMax;
		}

		public void setDisplayMin(int displayMin) {
			this.displayMin = displayMin;
		}

		public void setReadingMax(int readingMax) {
			this.readingMax = readingMax;
		}

		public void setReadingMin(int readingMin) {
			this.readingMin = readingMin;
		}

		public void setReadCacheSize(int readCacheSize) {
			this.readCacheSize = readCacheSize;
		}

		public Metadata build() {
			final Metadata result = new Metadata();
			result.accessType = accessType;
			result.units = units;
			result.datatype = datatype;
			result.accessType = accessType;
			result.precision = precision;
			result.scale = scale;
			result.smallestSamplingInterval = smallestSamplingInterval;
			result.currentSamplingInterval = currentSamplingInterval;
			result.changeOfValue = changeOfValue;
			result.hysterisys = hysterisys;
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
	private Metadata() {
	}

	/**
	 * Gets the maximum number of readings cached a given datapoint.
	 * <p>
	 * The value returned by this method can be used prioritize the readings to
	 * different datapoints. The devices that maintain have smaller storage
	 * capacities must be queried more often in order to maintain the required
	 * data freshness.
	 * <p>
	 * This method returns <tt>0</tt> to signify that the device (or the driver
	 * on its behalf) maintains no memory for the readings. As a consequence,
	 * every calling
	 * 
	 * @param address
	 *            the absolute address of the datapoint for which we want to
	 *            know the storage capacity
	 * @return an indicative value <i>n</i> of the maximum storage capacity for
	 *         the datapoint such that
	 *         <tt><i>n</i>&ge;count(readDatapointTimeWindow(address, <i>s</i>,
	 * <i>f</i>))</tt> for any <tt><i>s</i></tt> &le; <tt><i>f</i></tt>
	 *         {@link #readDatapointWindow(DatapointAddress, Timestamp, Timestamp)}
	 *         should return an empty array.z\
	 */
	int getCacheCapacity(DatapointAddress address) {
		return 0;
	}

	/**
	 * Get the corresponding sampling rates for the datapoints of a given
	 * sensor.
	 * 
	 * @param sensor
	 *            the sensor to which belong the datapoints
	 * @param address
	 *            the list of datapoints for which we want to know the sampling
	 *            rate
	 * @return the set of sampling rates for the corresponding datapoints
	 */
	int getDatapointSamplingRate(DatapointAddress address) {
		return 0;
	}

	public String getUnits() {
		return units;
	}

	public Datatype getDatatype() {
		return datatype;
	}

	public AccessType getAccessType() {
		return accessType;
	}

	public int getPrecision() {
		return precision;
	}

	public int getScale() {
		return scale;
	}

	public long getSmallestSamplingInterval() {
		return smallestSamplingInterval;
	}

	public long getCurrentSamplingInterval() {
		return currentSamplingInterval;
	}

	public int getChangeOfValue() {
		return changeOfValue;
	}

	public int getHysterisys() {
		return hysterisys;
	}

	public int getDisplayMax() {
		return displayMax;
	}

	public int getDisplayMin() {
		return displayMin;
	}

	public int getReadingMax() {
		return readingMax;
	}

	public int getReadingMin() {
		return readingMin;
	}

	public int getReadCacheSize() {
		return readCacheSize;
	}
}
