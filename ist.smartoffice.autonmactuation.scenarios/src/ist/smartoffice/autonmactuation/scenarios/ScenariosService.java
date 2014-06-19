package ist.smartoffice.autonmactuation.scenarios;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ist.smartoffice.datapointconnectivity.DatapointAddress;
import ist.smartoffice.datapointconnectivity.DatapointMetadata;
import ist.smartoffice.datapointconnectivity.DatapointMetadata.AccessType;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService.ErrorType;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService.WritingConfirmationLevel;
import ist.smartoffice.datapointconnectivity.DatapointValue;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService;
import ist.smartoffice.datapointconnectivity.ReadCallbackImpl;
import ist.smartoffice.datapointconnectivity.WriteCallbackImpl;
import ist.smartoffice.datapointconnectivity.osgi.registries.DatapointConnectivityServiceRegistry;

public class ScenariosService implements IDatapointConnectivityService {

	private Map<DatapointAddress, Map<DatapointAddress, DatapointValue>> scenariosDatapoints;
	private Map<DatapointAddress, DatapointValue> scenariosStatus;
	
	public ScenariosService(){
		scenariosDatapoints = new HashMap<DatapointAddress, Map<DatapointAddress,DatapointValue>>();
		scenariosStatus = new HashMap<DatapointAddress, DatapointValue>();
	}
	
	@Override
	public String getImplementationName() {
		return ScenariosService.class.getName();
	}

	@Override
	public void addDatapointListener(DatapointListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DatapointAddress[] getAllDatapoints() {
		Set<DatapointAddress> addresses = scenariosDatapoints.keySet();
		DatapointAddress[] result = new DatapointAddress[addresses.size()];
		int i=0;
		for(DatapointAddress s : addresses)
			result[i++] = s;		
		
		return result;
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
		if(!scenariosStatus.containsKey(address)){
			readCallback.onReadAborted(address, ErrorType.DATAPOINT_NOT_FOUND, 0);
			return 0;
		}
		
		DatapointValue v = scenariosStatus.get(address);
		readCallback.onReadCompleted(address, new DatapointValue[]{v}, 0);		
		return 0;
	}

	@Override
	public int requestDatapointWindowRead(DatapointAddress address,
			long startTimestamp, long finishTimestamp, ReadCallback readCallback) {
		readCallback.onReadAborted(address, ErrorType.UNSUPORTED_DATAPOINT_OPERATION, 0);
		return 0;
	}

	@Override
	public int requestDatapointWrite(DatapointAddress address, DatapointValue[] values,
			WriteCallback writeCallback) {
		
		if(!scenariosDatapoints.containsKey(address)){
			System.err.println("novo:"+address.getAddress());
			Map<DatapointAddress, DatapointValue> datapointsStatus;
			datapointsStatus = captureDatapointStatus();
			
			scenariosDatapoints.put(address, datapointsStatus);
			scenariosStatus.put(address, new DatapointValue("false"));
			
			writeCallback.onWriteCompleted(address, WritingConfirmationLevel.DEVICE_ACTION_TAKEN, 0);
		}
		else{
			System.err.println("existente:"+address.getAddress());
			IDatapointConnectivityService remsenService = DatapointConnectivityServiceRegistry.getInstance().getService("ist.smartoffice.dataaccess.remotesensingactuation.RemoteSensingActuationService");
			Map<DatapointAddress, DatapointValue> deviceScenarioStatus = scenariosDatapoints.get(address);
			System.out.println(scenariosDatapoints.entrySet().toString());
			
			for(DatapointAddress addr : deviceScenarioStatus.keySet()){
				WriteCallbackImpl callback = new WriteCallbackImpl();
				remsenService.requestDatapointWrite(addr, new DatapointValue[]{deviceScenarioStatus.get(addr)},callback);
				callback.isWritten();
			}
			
			writeCallback.onWriteCompleted(address, WritingConfirmationLevel.DEVICE_ACTION_TAKEN, 0);
	
		}
		
		
		return 0;
	}


	private Map<DatapointAddress, DatapointValue> captureDatapointStatus() {
		Map<DatapointAddress, DatapointValue> result = new HashMap<DatapointAddress, DatapointValue>();
		
		
		IDatapointConnectivityService remsenService = DatapointConnectivityServiceRegistry.getInstance().getService("ist.smartoffice.dataaccess.remotesensingactuation.RemoteSensingActuationService");
		
		DatapointAddress[] dps = remsenService.getAllDatapoints();
		
		for(DatapointAddress addr : dps){
			try {
				DatapointMetadata m = remsenService.getDatapointMetadata(addr);
				ReadCallbackImpl callback = new ReadCallbackImpl();
				
				if(m.getAccessType() == AccessType.READ_WRITE){
					remsenService.requestDatapointRead(addr, callback);
					DatapointValue v = callback.getFirstReading();
					System.out.println(v);
					result.put(addr, v);
				}
			} catch (OperationFailedException e) {
				e.printStackTrace();
			}
		}
		
		
		return result;
	}

}
