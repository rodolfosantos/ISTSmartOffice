package eu.smartcampus.api.historydatastorage;

public class HistoryValue implements Comparable<HistoryValue>{

	private Long timestamp;
	private String value;
	

	public HistoryValue(Long timestamp, String value) {
		super();
		this.timestamp = timestamp;
		this.value = value;
	}


	public Long getTimestamp() {
		return timestamp;
	}


	public String getValue() {
		return value;
	}


	@Override
	public int compareTo(HistoryValue other) {
		Long thisLong = new Long(timestamp);
		Long otherLong =  new Long(other.getTimestamp());
		return thisLong.compareTo(otherLong);
	}

}
