package ist.smartoffice.dataaccess.dataquerying;

public class QueryMaxValue implements IQuery {
	private String datapoint;
	private float maxValue;

	public QueryMaxValue(String datapoint) {
		this.datapoint = datapoint;
		this.maxValue = Float.MIN_VALUE;
	}

	public String getDatapoint() {
		return datapoint;
	}

	public String getValue() {
		return maxValue + "";
	}
	
	public void pushValue(String value){
		float currV = maxValue;
		float newV = Float.parseFloat(value);
		
		if(newV > currV)
			maxValue = newV;		
	}
}