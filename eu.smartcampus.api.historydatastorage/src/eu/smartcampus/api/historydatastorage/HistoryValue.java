package eu.smartcampus.api.historydatastorage;

public class HistoryValue implements Comparable<HistoryValue>{

	private Long timestamp;
	private String value;
	

	public HistoryValue(Long timestamp, String value) {
		super();
		this.timestamp = timestamp;
		this.value = value;
	}
	
	//only for json
	@SuppressWarnings("unused")
	private HistoryValue(){}

	public Long getTimestamp() {
		return timestamp;
	}


	public String getValue() {
		return value;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((timestamp == null) ? 0 : timestamp.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HistoryValue other = (HistoryValue) obj;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public int compareTo(HistoryValue other) {
		Long thisLong = new Long(timestamp);
		Long otherLong =  new Long(other.getTimestamp());
		return thisLong.compareTo(otherLong);
	}

}
