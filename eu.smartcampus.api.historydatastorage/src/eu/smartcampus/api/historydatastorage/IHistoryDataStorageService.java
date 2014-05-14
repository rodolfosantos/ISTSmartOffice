package eu.smartcampus.api.historydatastorage;

public interface IHistoryDataStorageService {

	String getImplementationName();

	// TODO improve

	void addValue(String address, long timestamp, String value);

	String getLastValue(String address);

	String[] getValuesTimeWindow(String address, long startTimestamp,
			long finishTimestamp);

}
