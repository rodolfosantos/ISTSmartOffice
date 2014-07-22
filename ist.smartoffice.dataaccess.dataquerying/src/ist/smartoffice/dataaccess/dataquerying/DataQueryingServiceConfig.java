package ist.smartoffice.dataaccess.dataquerying;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import ist.smartoffice.datapointconnectivity.DatapointAddress;
import ist.smartoffice.datapointconnectivity.DatapointMetadata;
import ist.smartoffice.datapointconnectivity.DatapointMetadata.AccessType;
import ist.smartoffice.datapointconnectivity.DatapointMetadata.Datatype;
import ist.smartoffice.datapointconnectivity.DatapointMetadata.MetadataBuilder;

public class DataQueryingServiceConfig {

	static final String CONFIG_FILENAME = "Config_QueryingService.json";

	/**
	 * Load datapoint settings.
	 * 
	 * @param filename
	 *            the filename
	 * @return the map
	 */
	public static Map<DatapointAddress, DatapointMetadata> loadDatapointsConfigs() {
		int i = 0;
		if(i==0)
			return defaultDatapointsConfig();
		
		
		// load datapoins from json file
		@SuppressWarnings("unchecked")
		Map<String, DatapointMetadata> datapointSettings = (Map<String, DatapointMetadata>) DataQueryingServiceConfig
				.fromJsonFile(CONFIG_FILENAME,
						new TypeToken<Map<String, DatapointMetadata>>() {
						}.getType());

		Map<DatapointAddress, DatapointMetadata> datapoints = new LinkedHashMap<DatapointAddress, DatapointMetadata>();

		if (datapointSettings != null) {
			Iterator<Entry<String, DatapointMetadata>> it = datapointSettings
					.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<java.lang.String, ist.smartoffice.datapointconnectivity.DatapointMetadata> entry = (Map.Entry<java.lang.String, ist.smartoffice.datapointconnectivity.DatapointMetadata>) it
						.next();
				datapoints.put(new DatapointAddress(entry.getKey()),
						entry.getValue());
			}
			return datapoints;
		} else {
			// settings file missing
			return defaultDatapointsConfig();
		}

	}

	/**
	 * Sets the default datapoints.
	 * 
	 * @param filename
	 *            the filename
	 * @return the map
	 */
	private static Map<DatapointAddress, DatapointMetadata> defaultDatapointsConfig() {

		// knx datapoints
		Map<DatapointAddress, DatapointMetadata> datapoints = new LinkedHashMap<DatapointAddress, DatapointMetadata>();
		
		MetadataBuilder m = new DatapointMetadata.MetadataBuilder();

		m.setDescription("select max from meterlib");
		m.setAccessType(AccessType.READ_ONLY);
		datapoints.put(new DatapointAddress("querymeterlib_max"), m.build());

		m.setDescription("select avg from meterlib");
		m.setAccessType(AccessType.READ_ONLY);
		datapoints.put(new DatapointAddress("querymeterlib_avg"), m.build());
		
		// store
		toJsonFile(CONFIG_FILENAME, datapoints);

		return datapoints;

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

		Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(json);
		String prettyJsonString = gsonPretty.toJson(je);

		try {
			FileWriter writer = new FileWriter(filename);
			writer.write(prettyJsonString);
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
