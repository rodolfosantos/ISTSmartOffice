package ist.smartoffice.dataaccess.dataquerying;

public class QueryAvgValue implements IQuery{
	private String datapoint;
	private float sumValue;
	private int inc;

	public QueryAvgValue(String datapoint) {
		this.datapoint = datapoint;
		this.sumValue = 0.0f;
		this.inc = 0;
	}

	public String getDatapoint() {
		return datapoint;
	}

	public String getValue() {
		return (sumValue/inc) + "";
	}

	public void pushValue(String value) {
		float currV = Float.parseFloat(value);
		sumValue = sumValue + currV;
		inc++;
	}
}