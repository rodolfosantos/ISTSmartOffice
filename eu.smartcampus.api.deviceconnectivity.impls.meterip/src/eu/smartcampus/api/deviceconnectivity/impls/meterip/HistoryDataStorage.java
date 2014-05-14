package eu.smartcampus.api.deviceconnectivity.impls.meterip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import eu.smartcampus.api.deviceconnectivity.DatapointAddress;
import eu.smartcampus.api.deviceconnectivity.DatapointReading;

public class HistoryDataStorage {

	public static HistoryDataStorage instance = null;

	private Map<DatapointAddress, List<DatapointReading>> readingsHistory;

	private HistoryDataStorage() {
		readingsHistory = new HashMap<DatapointAddress, List<DatapointReading>>();
	}

	public static HistoryDataStorage getInstance() {
		if (instance == null)
			instance = new HistoryDataStorage();
		return instance;
	}

	public void addDatapointReading(DatapointAddress address,
			DatapointReading reading) {
		if (!readingsHistory.containsKey(address))
			readingsHistory.put(address, new LinkedList<DatapointReading>());

		List<DatapointReading> datapointReadings = readingsHistory.get(address);
		datapointReadings.add(reading);
	}

	public DatapointReading getLastReading(DatapointAddress address) {
		if (!readingsHistory.containsKey(address))
			return null;

		List<DatapointReading> datapointReadings = readingsHistory.get(address);

		Collections.sort(datapointReadings);
		return datapointReadings.get(datapointReadings.size() - 1);
	}

	public DatapointReading[] getTimeWindowReading(DatapointAddress address,
			long startTS, long endTS) {
		if (!readingsHistory.containsKey(address))
			return null;

		List<DatapointReading> result = new ArrayList<DatapointReading>();
		List<DatapointReading> datapointReadings = readingsHistory.get(address);
		Collections.sort(datapointReadings);

		Iterator<DatapointReading> it = datapointReadings.iterator();
		while (it.hasNext()) {
			DatapointReading datapointReading = it.next();
			if (datapointReading.getTimestamp() < startTS) {
				continue;
			} else if (datapointReading.getTimestamp() >= startTS
					&& datapointReading.getTimestamp() <= endTS) {
				result.add(datapointReading);
			} else {
				break;
			}

		}
		DatapointReading[] res = new DatapointReading[result.size()];
		for (int i = 0; i < res.length; i++) {
			res[i] = result.get(i);
		}
		return res;
	}
}
