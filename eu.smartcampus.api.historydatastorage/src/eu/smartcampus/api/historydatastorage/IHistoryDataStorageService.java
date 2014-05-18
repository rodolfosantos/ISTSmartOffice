package eu.smartcampus.api.historydatastorage;

public interface IHistoryDataStorageService {

	String getImplementationName();

	// TODO improve

	void addValue(String address, long timestamp, String value);

	HistoryValue getLastValue(String address);

	HistoryValue[] getValuesTimeWindow(String address, long startTimestamp,
			long finishTimestamp);

}
