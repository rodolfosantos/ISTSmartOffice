package ist.smartoffice.dataaccess.remotesensingactuation;

import ist.smartoffice.datapointconnectivity.DatapointAddress;
import ist.smartoffice.datapointconnectivity.DatapointMetadata;
import ist.smartoffice.datapointconnectivity.DatapointReading;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService.ErrorType;
import ist.smartoffice.datapointconnectivity.osgi.registries.DatapointConnectivityServiceRegistry;

public class RemoteSensingActuationService implements
		IDatapointConnectivityService {

	IDatapointConnectivityService protocolIntegrationImpl;

	public RemoteSensingActuationService(
			IDatapointConnectivityService protocolIntegrationImpl) {
		this.protocolIntegrationImpl = protocolIntegrationImpl;
		
		if(protocolIntegrationImpl == null){
			System.err.println("e NULL");
		}
			
		else{
			
			System.err.println("nao é NULL");
		}
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
			final long startTimestamp, final long finishTimestamp,
			final ReadCallback readCallback) {
		
		final DatapointAddress origAddress = address;

		ReadCallback newCallback = new ReadCallback() {
			@Override
			public void onReadCompleted(DatapointAddress address,
					DatapointReading[] readings, int requestId) {
				readCallback.onReadCompleted(address, readings, requestId);
			}

			@Override
			public void onReadAborted(DatapointAddress address,
					ErrorType reason, int requestId) {
				IDatapointConnectivityService historyService = DatapointConnectivityServiceRegistry
						.getInstance()
						.getService(
								"ist.smartoffice.dataaccess.historydata.HistoryDataStorageService");
				if(historyService == null){
					readCallback.onReadAborted(origAddress, reason, requestId);
				}
				else{
					historyService.requestDatapointWindowRead(origAddress, startTimestamp, finishTimestamp, readCallback);
				}

			}
		};

		return protocolIntegrationImpl.requestDatapointWindowRead(address,
				startTimestamp, finishTimestamp, newCallback);
	}

	@Override
	public int requestDatapointWrite(DatapointAddress address, String[] values,
			WriteCallback writeCallback) {
		protocolIntegrationImpl.requestDatapointWrite(address, values,
				writeCallback);
		return 0;
	}

}
