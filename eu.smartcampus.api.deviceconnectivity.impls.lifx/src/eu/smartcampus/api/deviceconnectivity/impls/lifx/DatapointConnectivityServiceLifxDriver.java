package eu.smartcampus.api.deviceconnectivity.impls.lifx;

import java.util.Iterator;
import java.util.Map;

import jlifx.bulb.IBulb;
import jlifx.commandline.Utils;
import eu.smartcampus.api.deviceconnectivity.DatapointAddress;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata.AccessType;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata.Datatype;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata.MetadataBuilder;
import eu.smartcampus.api.deviceconnectivity.DatapointReading;
import eu.smartcampus.api.deviceconnectivity.DatapointValue;
import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;

public class DatapointConnectivityServiceLifxDriver implements
		IDatapointConnectivityService {

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

		DatapointAddress[] result = new DatapointAddress[allDevices.size()];

		Iterator<String> it = allDevices.keySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			String datapointAddress = it.next();
			result[i++] = new DatapointAddress(datapointAddress);
		}
		return result;
	}

	@Override
	public DatapointMetadata getDatapointMetadata(DatapointAddress address)
			throws OperationFailedException {
		MetadataBuilder m = new DatapointMetadata.MetadataBuilder();
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.STRING);
		return m.build();
	}

	@Override
	public void removeDatapointListener(DatapointListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public int requestDatapointRead(DatapointAddress address,
			ReadCallback readCallback) {
		System.out.println(address);
		Map<String, IBulb> allDevices = gateway.getAllDevices(false);
		System.out.println(allDevices.entrySet().toString());
		if (allDevices.containsKey(address.getAddress())) {
			IBulb bulb = allDevices.get(address.getAddress());
			String res = "";
			res += "\nHue         : " + Utils.wordToHexString(bulb.getHue());
			res += "\nSaturation  : "
					+ Utils.wordToHexString(bulb.getSaturation());
			res += "\nBrightness  : "
					+ Utils.wordToHexString(bulb.getBrightness());
			res += "\nKelvin      : " + Utils.wordToHexString(bulb.getKelvin());
			res += "\nDim         : " + Utils.wordToHexString(bulb.getDim());
			res += "\nPower       : " + Utils.wordToHexString(bulb.getPower());

			DatapointReading r = new DatapointReading(new DatapointValue(res));
			readCallback.onReadCompleted(address, new DatapointReading[] { r },
					0);
		} else {
			readCallback.onReadAborted(address, ErrorType.DATAPOINT_NOT_FOUND,
					0);
		}

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

		if (values.length == 1) {
			DatapointValue value = values[0];
			if (value.getStringValue().equals("true")) {
				gateway.switchOn(address.getAddress());
				writeCallback.onWriteCompleted(address,
						WritingConfirmationLevel.DEVICE_ACTION_TAKEN, 0);
			} else {
				gateway.switchOff(address.getAddress());
				writeCallback.onWriteCompleted(address,
						WritingConfirmationLevel.DEVICE_ACTION_TAKEN, 0);
			}
		} else if (values.length == 3) {
			gateway.setColor(address.getAddress(), values[0].getStringValue(),
					Integer.parseInt(values[1].getStringValue()),
					values[2].getStringValue());
			writeCallback.onWriteCompleted(address,
					WritingConfirmationLevel.DEVICE_ACTION_TAKEN, 0);
		}

		return 0;
	}

}
