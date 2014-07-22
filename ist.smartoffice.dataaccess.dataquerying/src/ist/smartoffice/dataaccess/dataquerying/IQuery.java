package ist.smartoffice.dataaccess.dataquerying;

public interface IQuery {

	String getDatapoint();
	String getValue();
	void pushValue(String value);
}
