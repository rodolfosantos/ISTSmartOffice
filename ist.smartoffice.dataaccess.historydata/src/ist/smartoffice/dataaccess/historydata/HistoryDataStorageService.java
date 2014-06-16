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

	

	private Map<String, List<HistoryValue>> readingsHistory;
	private String db_filename;

	public HistoryDataStorageService(String db_filename) {
		this.db_filename = db_filename;
		readingsHistory = new HashMap<String, List<HistoryValue>>();
		loadFromDisk();
		if (readingsHistory == null)
			readingsHistory = new HashMap<String, List<HistoryValue>>();
	}

	private void saveOnDisk() {
		synchronized (readingsHistory) {
			DataFileStorage.toJsonFile(db_filename, readingsHistory);
		}
	}

	@SuppressWarnings("unchecked")
	private void loadFromDisk() {
		synchronized (readingsHistory) {
			readingsHistory = (Map<String, List<HistoryValue>>) DataFileStorage
					.fromJsonFile(db_filename,
							new TypeToken<Map<String, List<HistoryValue>>>() {
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
		return (DatapointAddress[]) addresses.toArray();
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

		if (!readingsHistory.containsKey(address)) {
			readCallback.onReadAborted(address, ErrorType.DATAPOINT_NOT_FOUND,
					0);
			return 0;
		}

		List<HistoryValue> datapointReadings = readingsHistory.get(address);
		Collections.sort(datapointReadings);
		HistoryValue result = datapointReadings
				.get(datapointReadings.size() - 1);
		readCallback.onReadCompleted(address,
				new DatapointValue[] { new DatapointValue(
						result.getValue(), result.getTimestamp()) }, 0);

		return 0;
	}

	@Override
	public int requestDatapointWindowRead(DatapointAddress address,
			long startTimestamp, long finishTimestamp, ReadCallback readCallback) {

		System.out.println(readingsHistory.keySet());
		System.out.println(address.getAddress());
		if (!readingsHistory.containsKey(address.getAddress())) {
			readCallback.onReadAborted(address, ErrorType.DATAPOINT_NOT_FOUND,
					0);
			return 0;
		}

		List<HistoryValue> result = new ArrayList<HistoryValue>();
		synchronized (readingsHistory) {
			List<HistoryValue> datapointReadings = readingsHistory.get(address.getAddress());
			Collections.sort(datapointReadings);

			Iterator<HistoryValue> it = datapointReadings.iterator();
			while (it.hasNext()) {
				HistoryValue datapointReading = it.next();
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
				HistoryValue historyVal = result.get(i);
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
						new LinkedList<HistoryValue>());
			}

			List<HistoryValue> devHistory = readingsHistory.get(address.getAddress());
			for (DatapointValue val : values) {
				devHistory.add(new HistoryValue(val.getTimestamp(), val.getValue()));
			}
			saveOnDisk();
			writeCallback.onWriteCompleted(address,
					WritingConfirmationLevel.DEVICE_ACTION_TAKEN, 0);
		}
		return 0;
	}
}
