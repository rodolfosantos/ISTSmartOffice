package ist.smartoffice.autonmactuation.alarmsevents;

import ist.smartoffice.datapointconnectivity.DatapointAddress;
import ist.smartoffice.datapointconnectivity.DatapointMetadata;
import ist.smartoffice.datapointconnectivity.DatapointValue;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService;

public class AlarmsEventsService implements IDatapointConnectivityService {

	@Override
	public String getImplementationName() {
		return AlarmsEventsService.class.getName();
	}

	@Override
	public void addDatapointListener(DatapointListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DatapointAddress[] getAllDatapoints() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DatapointMetadata getDatapointMetadata(DatapointAddress address)
			throws OperationFailedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeDatapointListener(DatapointListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int requestDatapointRead(DatapointAddress address,
			ReadCallback readCallback) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int requestDatapointWindowRead(DatapointAddress address,
			long startTimestamp, long finishTimestamp, ReadCallback readCallback) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int requestDatapointWrite(DatapointAddress address, DatapointValue[] values,
			WriteCallback writeCallback) {
		// TODO Auto-generated method stub
		return 0;
	}

}
