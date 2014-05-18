package eu.smartcampus.api.historydatastorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HistoryDataStorageServiceImpl implements
		IHistoryDataStorageService {

	private Map<String, List<HistoryValue>> readingsHistory;

	public HistoryDataStorageServiceImpl() {
		readingsHistory = new HashMap<String, List<HistoryValue>>();
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
