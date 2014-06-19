package ist.smartoffice.deviceconnectivity.meterip;

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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import ist.smartoffice.datapointconnectivity.DatapointAddress;
import ist.smartoffice.datapointconnectivity.DatapointMetadata;
import ist.smartoffice.datapointconnectivity.DatapointMetadata.AccessType;
import ist.smartoffice.datapointconnectivity.DatapointMetadata.Datatype;
import ist.smartoffice.datapointconnectivity.DatapointMetadata.MetadataBuilder;

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
				Map.Entry<java.lang.String, ist.smartoffice.datapointconnectivity.DatapointMetadata> entry = (Map.Entry<java.lang.String, ist.smartoffice.datapointconnectivity.DatapointMetadata>) it
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
		meterMetadata.setDatatype(Datatype.BYTE_2);
		meterMetadata.setCurrentSamplingInterval(60000);

		Map<DatapointAddress, DatapointMetadata> meterDatapoints = new HashMap<DatapointAddress, DatapointMetadata>();
		DatapointAddress a1 = new DatapointAddress("meterd14");
		DatapointAddress a2 = new DatapointAddress("metera4");
		DatapointAddress a3 = new DatapointAddress("meterlib");
		DatapointAddress a4 = new DatapointAddress("meterd16");
		DatapointAddress a5 = new DatapointAddress("meterr117");
		DatapointAddress a6 = new DatapointAddress("meterr119");
		DatapointAddress a7 = new DatapointAddress("meterlab158");
		DatapointAddress a8 = new DatapointAddress("meterutaa4");
		DatapointAddress a9 = new DatapointAddress("meternewa4");

		meterMetadata.setDescription("D14");
		meterMetadata.setReadDatapointAddress("172.20.70.229");
		meterDatapoints.put(a1, meterMetadata.build());
		meterMetadata.setDescription("A4");
		meterMetadata.setReadDatapointAddress("172.20.70.231");
		meterDatapoints.put(a2, meterMetadata.build());
		meterMetadata.setDescription("Library");
		meterMetadata.setReadDatapointAddress("172.20.70.232");
		meterDatapoints.put(a3, meterMetadata.build());
		meterMetadata.setDescription("D16");
		meterMetadata.setReadDatapointAddress("172.20.70.238");
		meterDatapoints.put(a4, meterMetadata.build());
		meterMetadata.setDescription("R117");
		meterMetadata.setReadDatapointAddress("172.20.70.234");
		meterDatapoints.put(a5, meterMetadata.build());
		meterMetadata.setDescription("R119");
		meterMetadata.setReadDatapointAddress("172.20.70.235");
		meterDatapoints.put(a6, meterMetadata.build());
		meterMetadata.setDescription("Lab1-58");
		meterMetadata.setReadDatapointAddress("172.20.70.236");
		//meterDatapoints.put(a7, meterMetadata.build());
		meterMetadata.setDescription("UTA_A4");
		meterMetadata.setReadDatapointAddress("172.20.70.237");
		meterDatapoints.put(a8, meterMetadata.build());
		meterMetadata.setDescription("new A4");
		meterMetadata.setReadDatapointAddress("172.20.70.233");
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
