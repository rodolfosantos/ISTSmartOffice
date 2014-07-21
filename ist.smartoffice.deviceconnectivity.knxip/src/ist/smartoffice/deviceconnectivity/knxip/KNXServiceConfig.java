package ist.smartoffice.deviceconnectivity.knxip;

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

public class KNXServiceConfig {

	static final String CONFIG_FILENAME = "Config_KNX.json";

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
		Map<String, DatapointMetadata> datapointSettings = (Map<String, DatapointMetadata>) KNXServiceConfig
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
			return defaultKNXDatapointsConfig();
		}

	}

	/**
	 * Sets the default knx datapoints.
	 * 
	 * @param filename
	 *            the filename
	 * @return the map
	 */
	private static Map<DatapointAddress, DatapointMetadata> defaultKNXDatapointsConfig() {

		// knx datapoints
		Map<DatapointAddress, DatapointMetadata> knxDatapoints = new LinkedHashMap<DatapointAddress, DatapointMetadata>();

		MetadataBuilder m = new DatapointMetadata.MetadataBuilder();
		
		m.setDescription("EnergyLab Light Blackboard");
		m.setAccessType(AccessType.WRITE_ONLY);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/0/1");
		m.setWriteDatapointAddress("0/0/1");
		knxDatapoints.put(new DatapointAddress("n14lightall"), m.build());
		
		m.setDescription("EnergyLab Light Blackboard");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/0/2");
		m.setWriteDatapointAddress("0/0/2");
		knxDatapoints.put(new DatapointAddress("n14light1"), m.build());
		
		
//		// ================================================================
//		m.setDescription("EnergyLab Light Blackboard");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.PERCENTAGE);
//		m.setReadDatapointAddress("0/7/1");
//		m.setWriteDatapointAddress("0/1/0");
//		knxDatapoints.put(new DatapointAddress("knxlight1"), m.build());
//		
//		m.setDescription("EnergyLab Light Middle1");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.PERCENTAGE);
//		m.setReadDatapointAddress("0/7/21");
//		m.setWriteDatapointAddress("0/1/2");
//		knxDatapoints.put(new DatapointAddress("knxlight2"), m.build());
//		
//		m.setDescription("EnergyLab Light Middle2");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.PERCENTAGE);
//		m.setReadDatapointAddress("0/7/41");
//		m.setWriteDatapointAddress("0/1/4");
//		knxDatapoints.put(new DatapointAddress("knxlight3"), m.build());
//
//		m.setDescription("EnergyLab Light TV");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.PERCENTAGE);
//		m.setReadDatapointAddress("0/7/61");
//		m.setWriteDatapointAddress("0/1/6");
//		knxDatapoints.put(new DatapointAddress("knxlight4"), m.build());
//		
//		m.setDescription("EnergyLab All Lights");
//		m.setAccessType(AccessType.WRITE_ONLY);
//		m.setDatatype(Datatype.PERCENTAGE);
//		m.setReadDatapointAddress(null);
//		m.setWriteDatapointAddress("0/1/8");
//		knxDatapoints.put(new DatapointAddress("knxlightall"), m.build());
//		
//		
//		// ================================================================
//		m.setDescription("EnergyLab Blind1");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.PERCENTAGE);
//		m.setReadDatapointAddress("0/2/0");
//		m.setWriteDatapointAddress("0/2/3");
//		knxDatapoints.put(new DatapointAddress("knxblind1"), m.build());
//		
//		m.setDescription("EnergyLab Blind2");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.PERCENTAGE);
//		m.setReadDatapointAddress("0/2/13");
//		m.setWriteDatapointAddress("0/2/6");
//		knxDatapoints.put(new DatapointAddress("knxblind2"), m.build());
//		
//		m.setDescription("EnergyLab Blind3");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.PERCENTAGE);
//		m.setReadDatapointAddress("0/2/14");
//		m.setWriteDatapointAddress("0/2/9");
//		knxDatapoints.put(new DatapointAddress("knxblind3"), m.build());
//		
//		m.setDescription("EnergyLab All Blinds");
//		m.setAccessType(AccessType.WRITE_ONLY);
//		m.setDatatype(Datatype.PERCENTAGE);
//		m.setReadDatapointAddress(null);
//		m.setWriteDatapointAddress("0/2/12");
//		knxDatapoints.put(new DatapointAddress("knxblindall"), m.build());
//		
//		// ================================================================
//		m.setDescription("EnergyLab Door");
//		m.setAccessType(AccessType.WRITE_ONLY);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress(null);
//		m.setWriteDatapointAddress("0/3/0");
//		knxDatapoints.put(new DatapointAddress("knxdoor"), m.build());
//		
//		// ================================================================
//		
//		m.setDescription("EnergyLab CO2");
//		m.setAccessType(AccessType.READ_ONLY);
//		m.setDatatype(Datatype.BYTE_2);
//		m.setReadDatapointAddress("0/4/0");
//		m.setWriteDatapointAddress(null);
//		knxDatapoints.put(new DatapointAddress("knxsensorco"), m.build());
//		
//		m.setDescription("EnergyLab Humidity");
//		m.setAccessType(AccessType.READ_ONLY);
//		m.setDatatype(Datatype.BYTE_2);
//		m.setReadDatapointAddress(null);
//		m.setWriteDatapointAddress("0/4/1");
//		knxDatapoints.put(new DatapointAddress("knxsensorhum"), m.build());
//		
//		m.setDescription("EnergyLab Temperature");
//		m.setAccessType(AccessType.READ_ONLY);
//		m.setDatatype(Datatype.BYTE_2);
//		m.setReadDatapointAddress("0/4/3");
//		m.setWriteDatapointAddress(null);
//		knxDatapoints.put(new DatapointAddress("knxsensortemp"), m.build());
//		
//		m.setDescription("EnergyLab Temperature Door");
//		m.setAccessType(AccessType.READ_ONLY);
//		m.setDatatype(Datatype.BYTE_2);
//		m.setReadDatapointAddress("0/4/5");
//		m.setWriteDatapointAddress(null);
//		knxDatapoints.put(new DatapointAddress("knxsensortempd"), m.build());
//		
//		
//		m.setDescription("EnergyLab Lux");
//		m.setAccessType(AccessType.READ_ONLY);
//		m.setDatatype(Datatype.BYTE_2);
//		m.setReadDatapointAddress("0/4/4");
//		m.setWriteDatapointAddress(null);
//		knxDatapoints.put(new DatapointAddress("knxsensorlux"), m.build());
//		
//		// ================================================================
//		m.setDescription("EnergyLab HVAC ONOFF");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("1/0/8");
//		m.setWriteDatapointAddress("1/0/0");
//		knxDatapoints.put(new DatapointAddress("knxhvac"), m.build());
//		
//		m.setDescription("EnergyLab HVAC Mode");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("1/0/9");
//		m.setWriteDatapointAddress("1/0/1");
//		knxDatapoints.put(new DatapointAddress("knxhvacmode"), m.build());
//		
//		
//		// ================================================================
//		
//		m.setDescription("Weather Station - Lux East");
//		m.setAccessType(AccessType.READ_ONLY);
//		m.setDatatype(Datatype.BYTE_2);
//		m.setReadDatapointAddress("0/6/5");
//		m.setWriteDatapointAddress(null);
//		knxDatapoints.put(new DatapointAddress("knxweatherluxe"), m.build());
//		
//		m.setDescription("Weather Station - Lux South");
//		m.setAccessType(AccessType.READ_ONLY);
//		m.setDatatype(Datatype.BYTE_2);
//		m.setReadDatapointAddress("0/6/6");
//		m.setWriteDatapointAddress(null);
//		knxDatapoints.put(new DatapointAddress("knxweatherluxs"), m.build());
//		
//		m.setDescription("Weather Station - Lux West");
//		m.setAccessType(AccessType.READ_ONLY);
//		m.setDatatype(Datatype.BYTE_2);
//		m.setReadDatapointAddress("0/6/7");
//		m.setWriteDatapointAddress(null);
//		knxDatapoints.put(new DatapointAddress("knxweatherluxw"), m.build());
//		
//		m.setDescription("Weather Station - Wind Speed");
//		m.setAccessType(AccessType.READ_ONLY);
//		m.setDatatype(Datatype.BYTE_2);
//		m.setReadDatapointAddress("0/6/10");
//		m.setWriteDatapointAddress(null);
//		knxDatapoints.put(new DatapointAddress("knxweatherwind"), m.build());
//		
//		m.setDescription("Weather Station - Rain Sensor");
//		m.setAccessType(AccessType.READ_ONLY);
//		m.setDatatype(Datatype.BYTE_2);
//		m.setReadDatapointAddress("0/6/13");
//		m.setWriteDatapointAddress(null);
//		knxDatapoints.put(new DatapointAddress("knxweatherrain"), m.build());
//		
//		m.setDescription("Weather Station - Temperature");
//		m.setAccessType(AccessType.READ_ONLY);
//		m.setDatatype(Datatype.BYTE_2);
//		m.setReadDatapointAddress("0/6/16");
//		m.setWriteDatapointAddress(null);
//		knxDatapoints.put(new DatapointAddress("knxweathertemp"), m.build());
//		
//		m.setDescription("Weather Station - Relative Humidity");
//		m.setAccessType(AccessType.READ_ONLY);
//		m.setDatatype(Datatype.BYTE_2);
//		m.setReadDatapointAddress("0/6/22");
//		m.setWriteDatapointAddress(null);
//		knxDatapoints.put(new DatapointAddress("knxweatherrhum"), m.build());
//		
//		m.setDescription("Weather Station - Abolute Humidity");
//		m.setAccessType(AccessType.READ_ONLY);
//		m.setDatatype(Datatype.BYTE_2);
//		m.setReadDatapointAddress("0/6/27");
//		m.setWriteDatapointAddress(null);
//		knxDatapoints.put(new DatapointAddress("knxweatherahum"), m.build());
//		
//		m.setDescription("Weather Station - Dew Point");
//		m.setAccessType(AccessType.READ_ONLY);
//		m.setDatatype(Datatype.BYTE_2);
//		m.setReadDatapointAddress("0/6/25");
//		m.setWriteDatapointAddress(null);
//		knxDatapoints.put(new DatapointAddress("knxweatherdew"), m.build());
//		
//		m.setDescription("Weather Station - Solar Radiation");
//		m.setAccessType(AccessType.READ_ONLY);
//		m.setDatatype(Datatype.BYTE_2);
//		m.setReadDatapointAddress("0/6/29");
//		m.setWriteDatapointAddress(null);
//		knxDatapoints.put(new DatapointAddress("knxweathersolarrad"), m.build());
//		
//		
//		
		
		
		

		// store
		toJsonFile(CONFIG_FILENAME, knxDatapoints);

		return knxDatapoints;

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
