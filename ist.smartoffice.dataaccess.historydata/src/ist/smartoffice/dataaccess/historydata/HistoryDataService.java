package ist.smartoffice.dataaccess.historydata;

import ist.smartoffice.datapointconnectivity.DatapointAddress;
import ist.smartoffice.datapointconnectivity.DatapointMetadata;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService;

public class HistoryDataService implements IDatapointConnectivityService {

	IDatapointConnectivityService serviceImpl;
	

	public HistoryDataService(IDatapointConnectivityService serviceImpl) {
		super();
		this.serviceImpl = serviceImpl;
	}

	@Override
	public String getImplementationName() {
		return HistoryDataService.class.getName();
	}

	@Override
	public void addDatapointListener(DatapointListener listener) {
		serviceImpl.addDatapointListener(listener);
		
	}

	@Override
	public DatapointAddress[] getAllDatapoints() {
		return serviceImpl.getAllDatapoints();
	}

	@Override
	public DatapointMetadata getDatapointMetadata(DatapointAddress address)
			throws OperationFailedException {
		return serviceImpl.getDatapointMetadata(address);
	}

	@Override
	public void removeDatapointListener(DatapointListener listener) {
		serviceImpl.removeDatapointListener(listener);
		
	}

	@Override
	public int requestDatapointRead(DatapointAddress address,
			ReadCallback readCallback) {
		return serviceImpl.requestDatapointRead(address, readCallback);
	}

	@Override
	public int requestDatapointWindowRead(DatapointAddress address,
			long startTimestamp, long finishTimestamp, ReadCallback readCallback) {
		return serviceImpl.requestDatapointWindowRead(address, startTimestamp, finishTimestamp, readCallback);
	}

	@Override
	public int requestDatapointWrite(DatapointAddress address, String[] values,
			WriteCallback writeCallback) {
		return serviceImpl.requestDatapointWrite(address, values, writeCallback);
	}

}
