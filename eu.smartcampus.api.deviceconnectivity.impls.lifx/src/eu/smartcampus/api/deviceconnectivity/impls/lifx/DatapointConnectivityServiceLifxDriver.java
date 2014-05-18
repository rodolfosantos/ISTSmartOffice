package eu.smartcampus.api.deviceconnectivity.impls.lifx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jlifx.bulb.IBulb;
import eu.smartcampus.api.deviceconnectivity.DatapointAddress;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata;
import eu.smartcampus.api.deviceconnectivity.DatapointValue;
import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;

public class DatapointConnectivityServiceLifxDriver implements IDatapointConnectivityService {

	private LifxGateway gateway;
	
	
	public DatapointConnectivityServiceLifxDriver() {
		super();
		this.gateway = new LifxGateway();
		this.gateway.start();
	}

	@Override
	public String getImplementationName() {
		return "LIFX";
	}

	@Override
	public void addDatapointListener(DatapointListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DatapointAddress[] getAllDatapoints() {
		Map<String, IBulb> allDevices = gateway.getAllDevices(false);
		Set<String> addresses = allDevices.keySet();
		List<DatapointAddress> result = new ArrayList<DatapointAddress>();
		for (String string : addresses) {
			result.add(new DatapointAddress(string));
		}
		
		return (DatapointAddress[]) result.toArray();		
	}

	@Override
	public DatapointMetadata getDatapointMetadata(DatapointAddress address)
			throws OperationFailedException {
		throw new OperationFailedException(ErrorType.UNSUPORTED_DATAPOINT_OPERATION);
	}

	@Override
	public void removeDatapointListener(DatapointListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int requestDatapointRead(DatapointAddress address,
			ReadCallback readCallback) {
		return 0;
	}

	@Override
	public int requestDatapointWindowRead(DatapointAddress address,
			long startTimestamp, long finishTimestamp, ReadCallback readCallback) {
		return 0;
	}

	@Override
	public int requestDatapointWrite(DatapointAddress address,
			DatapointValue[] values, WriteCallback writeCallback) {
		
		if(values.length == 1){
			DatapointValue value = values[0];
			if(value.getBooleanValue()){
				gateway.switchOn(address.getAddress());
				writeCallback.onWriteCompleted(address, WritingConfirmationLevel.DEVICE_ACTION_TAKEN, 0);
			}
			else
			{
				gateway.switchOff(address.getAddress());
				writeCallback.onWriteCompleted(address, WritingConfirmationLevel.DEVICE_ACTION_TAKEN, 0);
			}
		}
		else if(values.length == 3){
			gateway.setColor(address.getAddress(), values[0].getStringValue(), values[1].getIntValue(), values[2].getStringValue());
		}
		
		return 0;
	}

}
