package ist.smartoffice.datapointconnectivity;

public interface IDatapoint {

	DatapointAddress getDatapointReadAddress();

	DatapointAddress getDatapointWriteAddress();

	DatapointMetadata getDatapointMetadata();

	void setDatapointMetadata(DatapointMetadata metadata);
	
	DatapointAddress getDatapointMainAddress();
	
	DatapointValue getStatus();
	
	void setStatus(DatapointValue value);
	
	void setStatus(String value);
	
}
