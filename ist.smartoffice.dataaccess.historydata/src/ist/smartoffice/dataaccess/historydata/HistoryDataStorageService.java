package ist.smartoffice.dataaccess.historydata;

import ist.smartoffice.datapointconnectivity.DatapointAddress;
import ist.smartoffice.datapointconnectivity.DatapointMetadata;
import ist.smartoffice.datapointconnectivity.DatapointMetadata.AccessType;
import ist.smartoffice.datapointconnectivity.DatapointMetadata.MetadataBuilder;
import ist.smartoffice.datapointconnectivity.DatapointValue;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.reflect.TypeToken;

public class HistoryDataStorageService implements IDatapointConnectivityService {

	

	private Map<String, List<DatapointValue>> readingsHistory;
	private String db_filename;

	public HistoryDataStorageService(String db_filename) {
		this.db_filename = db_filename;
		readingsHistory = new HashMap<String, List<DatapointValue>>();
		loadFromDisk();
		if (readingsHistory == null)
			readingsHistory = new HashMap<String, List<DatapointValue>>();
	}

	private void saveOnDisk() {
		synchronized (readingsHistory) {
			DataFileStorage.toJsonFile(db_filename, readingsHistory);
		}
	}

	@SuppressWarnings("unchecked")
	private void loadFromDisk() {
		synchronized (readingsHistory) {
			readingsHistory = (Map<String, List<DatapointValue>>) DataFileStorage
					.fromJsonFile(db_filename,
							new TypeToken<Map<String, List<DatapointValue>>>() {
							}.getType());
		}

	}

	@Override
	public String getImplementationName() {
		return "HistoryDataStorageService";
	}

	@Override
	public DatapointAddress[] getAllDatapoints() {
		Set<String> addresses = readingsHistory.keySet();
		DatapointAddress[] result = new DatapointAddress[addresses.size()];
		int i=0;
		for(String s : addresses)
			result[i++] = new DatapointAddress(s);		
		
		return result;
	}

	@Override
	public DatapointMetadata getDatapointMetadata(DatapointAddress address)
			throws OperationFailedException {
		MetadataBuilder m = new DatapointMetadata.MetadataBuilder();
		m.setAccessType(AccessType.READ_WRITE);
		return m.build();
	}

	@Override
	public void removeDatapointListener(DatapointListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addDatapointListener(DatapointListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public int requestDatapointRead(DatapointAddress address,
			ReadCallback readCallback) {

		if (!readingsHistory.containsKey(address.getAddress())) {
			readCallback.onReadAborted(address, ErrorType.DATAPOINT_NOT_FOUND,
					0);
			return 0;
		}

		List<DatapointValue> datapointReadings = readingsHistory.get(address.getAddress());
		Collections.sort(datapointReadings);
		
		DatapointValue result = datapointReadings
				.get(datapointReadings.size() - 1);
		readCallback.onReadCompleted(address,
				new DatapointValue[] { new DatapointValue(
						result.getValue(), result.getTimestamp()) }, 0);

		return 0;
	}

	@Override
	public int requestDatapointWindowRead(DatapointAddress address,
			long startTimestamp, long finishTimestamp, ReadCallback readCallback) {

		if (!readingsHistory.containsKey(address.getAddress())) {
			readCallback.onReadAborted(address, ErrorType.DATAPOINT_NOT_FOUND,
					0);
			return 0;
		}

		List<DatapointValue> result = new ArrayList<DatapointValue>();
		synchronized (readingsHistory) {
			List<DatapointValue> datapointReadings = readingsHistory.get(address.getAddress());
			Collections.sort(datapointReadings);

			Iterator<DatapointValue> it = datapointReadings.iterator();
			while (it.hasNext()) {
				DatapointValue datapointReading = it.next();
				if (datapointReading.getTimestamp() < startTimestamp) {
					continue;
				} else if (datapointReading.getTimestamp() >= startTimestamp
						&& datapointReading.getTimestamp() <= finishTimestamp) {
					result.add(datapointReading);
				} else {
					break;
				}

			}

			DatapointValue[] res = new DatapointValue[result.size()];
			for (int i = 0; i < res.length; i++) {
				DatapointValue historyVal = result.get(i);
				res[i] = new DatapointValue(historyVal.getValue(),
						historyVal.getTimestamp());
			}
			readCallback.onReadCompleted(address, res, 0);
		}

		return 0;

	}

	@Override
	public int requestDatapointWrite(DatapointAddress address, DatapointValue[] values,
			WriteCallback writeCallback) {
		synchronized (readingsHistory) {
			if (!readingsHistory.containsKey(address.getAddress())) {
				readingsHistory.put(address.getAddress(),
						new LinkedList<DatapointValue>());
			}

			List<DatapointValue> devHistory = readingsHistory.get(address.getAddress());
			for (DatapointValue val : values) {
				devHistory.add(val);
			}
			saveOnDisk();
			writeCallback.onWriteCompleted(address,
					WritingConfirmationLevel.DEVICE_ACTION_TAKEN, 0);
		}
		return 0;
	}
}
