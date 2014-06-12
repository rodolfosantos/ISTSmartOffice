package ist.smartoffice.historydatastorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

public class HistoryDataStorageServiceImpl implements
		IHistoryDataStorageService {

	final String DB_FILE = "HistoryDataDB.json";

	private Map<String, List<HistoryValue>> readingsHistory;

	public HistoryDataStorageServiceImpl() {
		readingsHistory = new HashMap<String, List<HistoryValue>>();
		loadFromDisk();
		if (readingsHistory == null)
			readingsHistory = new HashMap<String, List<HistoryValue>>();
	}

	private void saveOnDisk() {
		synchronized (readingsHistory) {
			DataStorage.toJsonFile(DB_FILE, readingsHistory);
		}
	}

	@SuppressWarnings("unchecked")
	private void loadFromDisk() {
		synchronized (readingsHistory) {
			readingsHistory = (Map<String, List<HistoryValue>>) DataStorage
					.fromJsonFile(DB_FILE,
							new TypeToken<Map<String, List<HistoryValue>>>() {
							}.getType());
		}

	}

	@Override
	public String getImplementationName() {
		return "HistoryDataStorageServiceImpl";
	}

	@Override
	public void addValue(String address, long timestamp, String value) {
		synchronized (readingsHistory) {
			if (!readingsHistory.containsKey(address)) {
				readingsHistory.put(address, new LinkedList<HistoryValue>());
			}
			readingsHistory.get(address)
					.add(new HistoryValue(timestamp, value));

			saveOnDisk();
		}

	}

	@Override
	public HistoryValue getLastValue(String address) {
		if (!readingsHistory.containsKey(address))
			return null;

		List<HistoryValue> datapointReadings = readingsHistory.get(address);
		Collections.sort(datapointReadings);
		return datapointReadings.get(datapointReadings.size() - 1);
	}

	@Override
	public HistoryValue[] getValuesTimeWindow(String address,
			long startTimestamp, long finishTimestamp) {
		if (!readingsHistory.containsKey(address))
			return null;

		List<HistoryValue> result = new ArrayList<HistoryValue>();
		synchronized (readingsHistory) {
			List<HistoryValue> datapointReadings = readingsHistory.get(address);
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
			HistoryValue[] res = new HistoryValue[result.size()];
			for (int i = 0; i < res.length; i++) {
				res[i] = result.get(i);
			}
			return res;
		}

	}
}
