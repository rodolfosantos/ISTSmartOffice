package ist.smartoffice.dataaccess.remotesensingactuation;

import ist.smartoffice.datapointconnectivity.DatapointAddress;
import ist.smartoffice.datapointconnectivity.DatapointMetadata;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService;

public class RemoteSensingActuationService implements
		IDatapointConnectivityService {

	IDatapointConnectivityService protocolIntegrationImpl;

	public RemoteSensingActuationService(
			IDatapointConnectivityService protocolIntegrationImpl) {
		this.protocolIntegrationImpl = protocolIntegrationImpl;
	}

	@Override
	public String getImplementationName() {
		return RemoteSensingActuationService.class.getName();
	}

	@Override
	public void addDatapointListener(DatapointListener listener) {
		protocolIntegrationImpl.addDatapointListener(listener);

	}

	@Override
	public DatapointAddress[] getAllDatapoints() {
		return protocolIntegrationImpl.getAllDatapoints();
	}

	@Override
	public DatapointMetadata getDatapointMetadata(DatapointAddress address)
			throws OperationFailedException {
		return protocolIntegrationImpl.getDatapointMetadata(address);
	}

	@Override
	public void removeDatapointListener(DatapointListener listener) {
		protocolIntegrationImpl.removeDatapointListener(listener);
	}

	@Override
	public int requestDatapointRead(DatapointAddress address,
			ReadCallback readCallback) {
		return protocolIntegrationImpl.requestDatapointRead(address,
				readCallback);
	}

	@Override
	public int requestDatapointWindowRead(DatapointAddress address,
			long startTimestamp, long finishTimestamp, ReadCallback readCallback) {
		return protocolIntegrationImpl.requestDatapointWindowRead(address,
				startTimestamp, finishTimestamp, readCallback);
	}

	@Override
	public int requestDatapointWrite(DatapointAddress address, String[] values,
			WriteCallback writeCallback) {
		protocolIntegrationImpl.requestDatapointWrite(address, values,
				writeCallback);
		return 0;
	}

}
