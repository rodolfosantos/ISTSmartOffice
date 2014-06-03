package eu.smartcampus.api.deviceconnectivity.impls.meterip;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import eu.smartcampus.api.deviceconnectivity.DatapointAddress;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata.AccessType;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata.Datatype;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata.MetadataBuilder;

public class MeterIPServiceConfig {

	static final String CONFIG_FILENAME = "Config_MeterIP.json";

	/**
	 * Load datapoint settings.
	 * 
	 * @param filename
	 *            the filename
	 * @return the map
	 */
	public static Map<DatapointAddress, DatapointMetadata> loadDatapointsConfigs() {
		// load datapoins from json file
		@SuppressWarnings("unchecked")
		Map<String, DatapointMetadata> datapointSettings = (Map<String, DatapointMetadata>) MeterIPServiceConfig
				.fromJsonFile(CONFIG_FILENAME,
						new TypeToken<Map<String, DatapointMetadata>>() {
						}.getType());

		Map<DatapointAddress, DatapointMetadata> datapoints = new HashMap<DatapointAddress, DatapointMetadata>();

		if (datapointSettings != null) {
			Iterator<Entry<String, DatapointMetadata>> it = datapointSettings
					.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<java.lang.String, eu.smartcampus.api.deviceconnectivity.DatapointMetadata> entry = (Map.Entry<java.lang.String, eu.smartcampus.api.deviceconnectivity.DatapointMetadata>) it
						.next();
				datapoints.put(new DatapointAddress(entry.getKey()),
						entry.getValue());
			}
			return datapoints;
		} else {
			// settings file missing
			return defaultMetersDatapointsConfig();
		}

	}

	/**
	 * Sets the default meters datapoints.
	 * 
	 * @param filename
	 *            the filename
	 * @return the map
	 */
	private static Map<DatapointAddress, DatapointMetadata> defaultMetersDatapointsConfig() {

		// meters datapoints
		MetadataBuilder meterMetadata = new DatapointMetadata.MetadataBuilder();
		meterMetadata.setAccessType(AccessType.READ_ONLY);
		meterMetadata.setDatatype(Datatype.STRING);
		meterMetadata.setCurrentSamplingInterval(7000);

		Map<DatapointAddress, DatapointMetadata> meterDatapoints = new HashMap<DatapointAddress, DatapointMetadata>();
		DatapointAddress a1 = new DatapointAddress("172.20.70.229");
		DatapointAddress a2 = new DatapointAddress("172.20.70.231");
		DatapointAddress a3 = new DatapointAddress("172.20.70.232");
		DatapointAddress a4 = new DatapointAddress("172.20.70.238");
		DatapointAddress a5 = new DatapointAddress("172.20.70.234");
		DatapointAddress a6 = new DatapointAddress("172.20.70.235");
		DatapointAddress a7 = new DatapointAddress("172.20.70.236");
		DatapointAddress a8 = new DatapointAddress("172.20.70.237");
		DatapointAddress a9 = new DatapointAddress("172.20.70.233");

		meterMetadata.setDescription("D14");
		meterDatapoints.put(a1, meterMetadata.build());
		meterMetadata.setDescription("A4");
		meterDatapoints.put(a2, meterMetadata.build());
		meterMetadata.setDescription("Library");
		meterDatapoints.put(a3, meterMetadata.build());
		meterMetadata.setDescription("D16");
		meterDatapoints.put(a4, meterMetadata.build());
		meterMetadata.setDescription("R117");
		meterDatapoints.put(a5, meterMetadata.build());
		meterMetadata.setDescription("R119");
		meterDatapoints.put(a6, meterMetadata.build());
		meterMetadata.setDescription("Lab1-58");
		meterDatapoints.put(a7, meterMetadata.build());
		meterMetadata.setDescription("UTA_A4");
		meterDatapoints.put(a8, meterMetadata.build());
		meterMetadata.setDescription("new A4");
		meterDatapoints.put(a9, meterMetadata.build());

		// store
		toJsonFile(CONFIG_FILENAME, meterDatapoints);

		return meterDatapoints;

	}

	/**
	 * Store a object to json file.
	 * 
	 * @param filename
	 *            the filename
	 * @param object
	 *            the object
	 */
	private static void toJsonFile(String filename, Object object) {
		Gson gson = new Gson();
		String json = gson.toJson(object);
		try {
			FileWriter writer = new FileWriter(filename);
			writer.write(json);
			writer.close();

		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

	}

	/**
	 * Get specific type object from json file.
	 * 
	 * @param filename
	 *            the filename
	 * @param objectType
	 *            the object type
	 * @return the object
	 */
	private static Object fromJsonFile(String filename, Type objectType) {
		Gson gson = new Gson();

		try {

			BufferedReader br = new BufferedReader(new FileReader(filename));

			Object result = gson.fromJson(br, objectType);
			return result;

		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Get object from json file.
	 * 
	 * @param filename
	 *            the filename
	 * @param classType
	 *            the class type
	 * @return the object
	 */
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	private static Object fromJsonFile(String filename, Class classType) {
		Gson gson = new Gson();
		try {

			BufferedReader br = new BufferedReader(new FileReader(filename));
			Object result = gson.fromJson(br, classType);
			return result;

		} catch (IOException e) {
			return null;
		}
	}

}
