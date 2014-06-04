package eu.smartcampus.api.deviceconnectivity.impls.knxip;

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

import eu.smartcampus.api.deviceconnectivity.DatapointAddress;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata.AccessType;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata.Datatype;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata.MetadataBuilder;

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
				Map.Entry<java.lang.String, eu.smartcampus.api.deviceconnectivity.DatapointMetadata> entry = (Map.Entry<java.lang.String, eu.smartcampus.api.deviceconnectivity.DatapointMetadata>) it
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

		MetadataBuilder knxMetadata1 = new DatapointMetadata.MetadataBuilder();
		knxMetadata1.setCurrentSamplingInterval(5000);
		knxMetadata1.setDescription("EnergyLab Temperature (door)");
		knxMetadata1.setAccessType(AccessType.READ_ONLY);
		knxMetadata1.setDatatype(Datatype.INTEGER);
		knxMetadata1.setScale(2);
		DatapointAddress d1 = new DatapointAddress("0/4/5");// energy lab
															// temperature door

		MetadataBuilder knxMetadata2 = new DatapointMetadata.MetadataBuilder();
		knxMetadata2.setDescription("EnergyLab Door");
		knxMetadata2.setAccessType(AccessType.WRITE_ONLY);
		knxMetadata2.setDatatype(Datatype.BOOLEAN);
		DatapointAddress d2 = new DatapointAddress("0/3/0");// energy lab door
															// (write true to
															// open)

		MetadataBuilder knxMetadata3 = new DatapointMetadata.MetadataBuilder();
		knxMetadata3.setDescription("EnergyLab BlackboardLamps (write)");
		knxMetadata3.setAccessType(AccessType.WRITE_ONLY);
		knxMetadata3.setDatatype(Datatype.INTEGER);
		knxMetadata3.setScale(2);
		knxMetadata3.setDisplayMax(100);
		knxMetadata3.setDisplayMin(1);
		DatapointAddress d3 = new DatapointAddress("0/1/0");// blackboard lamps
															// write (0/100)

		MetadataBuilder knxMetadata4 = new DatapointMetadata.MetadataBuilder();
		knxMetadata4.setCurrentSamplingInterval(10000);
		knxMetadata4.setDescription("EnergyLab BlackboardLamps (read)");
		knxMetadata4.setAccessType(AccessType.READ_ONLY);
		knxMetadata4.setDatatype(Datatype.INTEGER);
		knxMetadata4.setDisplayMax(100);
		knxMetadata4.setDisplayMin(1);
		knxMetadata4.setScale(2);
		DatapointAddress d4 = new DatapointAddress("0/7/1");// blackboard lamps
															// read (0/100)

		MetadataBuilder knxMetadata5 = new DatapointMetadata.MetadataBuilder();
		knxMetadata5.setDescription("EnergyLab All Blinds (write)");
		knxMetadata5.setAccessType(AccessType.WRITE_ONLY);
		knxMetadata5.setDatatype(Datatype.INTEGER);
		knxMetadata5.setScale(2);
		knxMetadata5.setDisplayMax(100);
		knxMetadata5.setDisplayMin(1);
		DatapointAddress d5 = new DatapointAddress("0/2/12");// energylab blinds
																// write (0/100)

		MetadataBuilder knxMetadata6 = new DatapointMetadata.MetadataBuilder();
		knxMetadata6.setCurrentSamplingInterval(10000);
		knxMetadata6.setDescription("EnergyLab Blinds 1 (read)");
		knxMetadata6.setAccessType(AccessType.READ_ONLY);
		knxMetadata6.setDatatype(Datatype.INTEGER);
		knxMetadata6.setDisplayMax(100);
		knxMetadata6.setDisplayMin(1);
		knxMetadata6.setScale(2);
		DatapointAddress d6 = new DatapointAddress("0/2/0");// energylab blinds
															// read (0/100)

		MetadataBuilder knxMetadata7 = new DatapointMetadata.MetadataBuilder();
		knxMetadata7.setDescription("EnergyLab All Lamps (write)");
		knxMetadata7.setAccessType(AccessType.WRITE_ONLY);
		knxMetadata7.setDatatype(Datatype.INTEGER);
		knxMetadata7.setDisplayMax(100);
		knxMetadata7.setDisplayMin(1);
		knxMetadata7.setScale(2);
		DatapointAddress d7 = new DatapointAddress("0/1/8");// energylab blinds
															// read (0/100)

		MetadataBuilder knxMetadata8 = new DatapointMetadata.MetadataBuilder();
		knxMetadata8.setDescription("EnergyLab Blinds 1 (write)");
		knxMetadata8.setAccessType(AccessType.WRITE_ONLY);
		knxMetadata8.setDatatype(Datatype.INTEGER);
		knxMetadata8.setScale(2);
		knxMetadata8.setDisplayMax(100);
		knxMetadata8.setDisplayMin(1);
		DatapointAddress d8 = new DatapointAddress("0/2/3");// energylab blinds
															// write (0/100)

		MetadataBuilder knxMetadata9 = new DatapointMetadata.MetadataBuilder();
		knxMetadata9.setDescription("EnergyLab Blinds 2 (write)");
		knxMetadata9.setAccessType(AccessType.WRITE_ONLY);
		knxMetadata9.setDatatype(Datatype.INTEGER);
		knxMetadata9.setScale(2);
		knxMetadata9.setDisplayMax(100);
		knxMetadata9.setDisplayMin(1);
		DatapointAddress d9 = new DatapointAddress("0/2/6");// energylab blinds
															// write (0/100)

		MetadataBuilder knxMetadata10 = new DatapointMetadata.MetadataBuilder();
		knxMetadata10.setDescription("EnergyLab Blinds 3 (write)");
		knxMetadata10.setAccessType(AccessType.WRITE_ONLY);
		knxMetadata10.setDatatype(Datatype.INTEGER);
		knxMetadata10.setScale(2);
		knxMetadata10.setDisplayMax(100);
		knxMetadata10.setDisplayMin(1);
		DatapointAddress d10 = new DatapointAddress("0/2/9");// energylab blinds
																// write (0/100)

		MetadataBuilder knxMetadata11 = new DatapointMetadata.MetadataBuilder();
		knxMetadata11.setCurrentSamplingInterval(10000);
		knxMetadata11.setDescription("EnergyLab Blinds 2 (read)");
		knxMetadata11.setAccessType(AccessType.READ_ONLY);
		knxMetadata11.setDatatype(Datatype.INTEGER);
		knxMetadata11.setDisplayMax(100);
		knxMetadata11.setDisplayMin(1);
		knxMetadata11.setScale(2);
		DatapointAddress d11 = new DatapointAddress("0/2/13");// energylab
																// blinds read
																// (0/100)

		MetadataBuilder knxMetadata12 = new DatapointMetadata.MetadataBuilder();
		knxMetadata12.setCurrentSamplingInterval(10000);
		knxMetadata12.setDescription("EnergyLab Blinds 3 (read)");
		knxMetadata12.setAccessType(AccessType.READ_ONLY);
		knxMetadata12.setDatatype(Datatype.INTEGER);
		knxMetadata12.setDisplayMax(100);
		knxMetadata12.setDisplayMin(1);
		knxMetadata12.setScale(2);
		DatapointAddress d12 = new DatapointAddress("0/2/14");// energylab
																// blinds read
																// (0/100)

		MetadataBuilder knxMetadata13 = new DatapointMetadata.MetadataBuilder();
		knxMetadata13.setCurrentSamplingInterval(10000);
		knxMetadata13.setDescription("EnergyLab Lux");
		knxMetadata13.setAccessType(AccessType.READ_ONLY);
		knxMetadata13.setDatatype(Datatype.INTEGER);
		knxMetadata13.setScale(2);
		DatapointAddress d13 = new DatapointAddress("0/4/4");// energy lab
																// temperature

		MetadataBuilder knxMetadata14 = new DatapointMetadata.MetadataBuilder();
		knxMetadata14.setCurrentSamplingInterval(10000);
		knxMetadata14.setDescription("EnergyLab ppm");
		knxMetadata14.setAccessType(AccessType.READ_ONLY);
		knxMetadata14.setDatatype(Datatype.INTEGER);
		knxMetadata14.setScale(2);
		DatapointAddress d14 = new DatapointAddress("0/4/0");// energy lab ppm

		MetadataBuilder knxMetadata15 = new DatapointMetadata.MetadataBuilder();
		knxMetadata15.setCurrentSamplingInterval(10000);
		knxMetadata15.setDescription("EnergyLab temperature");
		knxMetadata15.setAccessType(AccessType.READ_ONLY);
		knxMetadata15.setDatatype(Datatype.INTEGER);
		knxMetadata15.setScale(2);
		DatapointAddress d15 = new DatapointAddress("0/4/3");// energy lab temp

		knxDatapoints.put(d1, knxMetadata1.build());
		knxDatapoints.put(d2, knxMetadata2.build());
		knxDatapoints.put(d3, knxMetadata3.build());
		knxDatapoints.put(d4, knxMetadata4.build());
		knxDatapoints.put(d5, knxMetadata5.build());
		knxDatapoints.put(d6, knxMetadata6.build());
		knxDatapoints.put(d7, knxMetadata7.build());
		knxDatapoints.put(d8, knxMetadata8.build());
		knxDatapoints.put(d9, knxMetadata9.build());
		knxDatapoints.put(d10, knxMetadata10.build());
		knxDatapoints.put(d11, knxMetadata11.build());
		knxDatapoints.put(d12, knxMetadata12.build());
		knxDatapoints.put(d13, knxMetadata13.build());
		knxDatapoints.put(d14, knxMetadata14.build());
		knxDatapoints.put(d15, knxMetadata15.build());

//		// ================================================================
//		// EnergyLab Plugs
//		MetadataBuilder knxMetadataPlugs = new DatapointMetadata.MetadataBuilder();
//		knxMetadataPlugs.setAccessType(AccessType.WRITE_ONLY);
//		knxMetadataPlugs.setDatatype(Datatype.BOOLEAN);
//
//		for (int i = 1; i <= 12; i++) {
//			DatapointAddress add = new DatapointAddress("0/0/" + i);
//			knxMetadataPlugs.setDescription("Tomadas " + i);
//			knxDatapoints.put(add, knxMetadataPlugs.build());
//		}
//
//		// EnergyLab lights READ
//		MetadataBuilder knxMetadataLightR = new DatapointMetadata.MetadataBuilder();
//		knxMetadataLightR.setCurrentSamplingInterval(30000);
//		knxMetadataLightR.setAccessType(AccessType.READ_ONLY);
//		knxMetadataLightR.setDatatype(Datatype.INTEGER);
//		knxMetadataLightR.setDisplayMax(100);
//		knxMetadataLightR.setDisplayMin(1);
//		knxMetadataLightR.setScale(2);
//
//		DatapointAddress l = new DatapointAddress("0/1/3");
//		knxMetadataLightR.setDescription("Luz Quadro Status");
//		knxDatapoints.put(l, knxMetadataLightR.build());
//
//		l = new DatapointAddress("0/1/5");
//		knxMetadataLightR.setDescription("Luz Meio 1 Status");
//		knxDatapoints.put(l, knxMetadataLightR.build());
//
//		l = new DatapointAddress("0/1/7");
//		knxMetadataLightR.setDescription("Luz Meio 2 Status");
//		knxDatapoints.put(l, knxMetadataLightR.build());
//
//		l = new DatapointAddress("0/1/9");
//		knxMetadataLightR.setDescription("Luz TV Status");
//		knxDatapoints.put(l, knxMetadataLightR.build());
//
//		// EnergyLab lights WRITE
//		MetadataBuilder knxMetadataLightW = new DatapointMetadata.MetadataBuilder();
//		knxMetadataLightW.setAccessType(AccessType.WRITE_ONLY);
//		knxMetadataLightW.setDatatype(Datatype.INTEGER);
//		knxMetadataLightW.setDisplayMax(100);
//		knxMetadataLightW.setDisplayMin(1);
//
//		l = new DatapointAddress("0/1/1");
//		knxMetadataLightW.setDescription("￼ On/Off Luzes todas");
//		knxDatapoints.put(l, knxMetadataLightW.build());
//
//		l = new DatapointAddress("0/1/0");
//		knxMetadataLightW.setDescription("Luz Quadro");
//		knxDatapoints.put(l, knxMetadataLightW.build());
//
//		l = new DatapointAddress("0/1/2");
//		knxMetadataLightW.setDescription("Luz Meio 1");
//		knxDatapoints.put(l, knxMetadataLightW.build());
//
//		l = new DatapointAddress("0/1/4");
//		knxMetadataLightW.setDescription("Luz Meio 2");
//		knxDatapoints.put(l, knxMetadataLightW.build());
//
//		l = new DatapointAddress("0/1/6");
//		knxMetadataLightW.setDescription("Luz TV");
//		knxDatapoints.put(l, knxMetadataLightW.build());
//
//		// EnergyLab Lamelas READ
//		MetadataBuilder knxMetadataBlindsR = new DatapointMetadata.MetadataBuilder();
//		knxMetadataBlindsR.setCurrentSamplingInterval(30000);
//		knxMetadataBlindsR.setAccessType(AccessType.READ_ONLY);
//		knxMetadataBlindsR.setDatatype(Datatype.INTEGER);
//		knxMetadataBlindsR.setDisplayMax(100);
//		knxMetadataBlindsR.setDisplayMin(1);
//		knxMetadataBlindsR.setScale(2);
//		
//		l = new DatapointAddress("0/2/0");
//		knxMetadataBlindsR.setDescription("Posição Lamelas 1 Status");
//		knxDatapoints.put(l, knxMetadataBlindsR.build());
//		
//		l = new DatapointAddress("0/2/13");
//		knxMetadataBlindsR.setDescription("Posição Lamelas 2 Status");
//		knxDatapoints.put(l, knxMetadataBlindsR.build());
//		
//		l = new DatapointAddress("0/2/14");
//		knxMetadataBlindsR.setDescription("Posição Lamelas 3 Status");
//		knxDatapoints.put(l, knxMetadataBlindsR.build());
//
//		// EnergyLab Lamelas WRITE
//		MetadataBuilder knxMetadataBlindsW = new DatapointMetadata.MetadataBuilder();
//		knxMetadataBlindsW.setAccessType(AccessType.WRITE_ONLY);
//		knxMetadataBlindsW.setDatatype(Datatype.INTEGER);
//		knxMetadataBlindsW.setDisplayMax(100);
//		knxMetadataBlindsW.setDisplayMin(1);
//		
//		l = new DatapointAddress("0/2/3");
//		knxMetadataBlindsW.setDescription("Posição Lamelas 1");
//		knxDatapoints.put(l, knxMetadataBlindsW.build());
//		
//		l = new DatapointAddress("0/2/6");
//		knxMetadataBlindsW.setDescription("Posição Lamelas 2");
//		knxDatapoints.put(l, knxMetadataBlindsW.build());
//		
//		l = new DatapointAddress("0/2/9");
//		knxMetadataBlindsW.setDescription("Posição Lamelas 3");
//		knxDatapoints.put(l, knxMetadataBlindsW.build());
//		
//		l = new DatapointAddress("0/2/12");
//		knxMetadataBlindsW.setDescription("Posição Lamelas Todas");
//		knxDatapoints.put(l, knxMetadataBlindsW.build());
//		
//		//Energy Lab Door
//		MetadataBuilder knxMetadatDoor = new DatapointMetadata.MetadataBuilder();
//		knxMetadatDoor.setDescription("Controlo da Porta");
//		knxMetadatDoor.setAccessType(AccessType.WRITE_ONLY);
//		knxMetadatDoor.setDatatype(Datatype.BOOLEAN);
//		l = new DatapointAddress("0/3/0");
//		knxDatapoints.put(l, knxMetadatDoor.build());	
//		
//		//Energy Lab Sensing
//		MetadataBuilder knxMetadataSens = new DatapointMetadata.MetadataBuilder();
//		knxMetadataSens.setCurrentSamplingInterval(10000);
//		knxMetadataSens.setAccessType(AccessType.READ_ONLY);
//		knxMetadataSens.setDatatype(Datatype.INTEGER);
//		knxMetadataSens.setScale(2);
//		
//		l = new DatapointAddress("0/4/0");
//		knxMetadataSens.setDescription("CO2");
//		knxDatapoints.put(l, knxMetadataSens.build());	
//		
//		l = new DatapointAddress("0/4/1");
//		knxMetadataSens.setDescription("￼Humidade Relativa");
//		knxDatapoints.put(l, knxMetadataSens.build());
//		
//		l = new DatapointAddress("0/4/3");
//		knxMetadataSens.setDescription("Temperatura");
//		knxDatapoints.put(l, knxMetadataSens.build());
//		
//		l = new DatapointAddress("0/4/4");
//		knxMetadataSens.setDescription("Luminosidade");
//		knxDatapoints.put(l, knxMetadataSens.build());
//		
//		// ==
//		
//		l = new DatapointAddress("0/5/0");
//		knxMetadataSens.setDescription("Porta");
//		knxDatapoints.put(l, knxMetadataSens.build());
//		
//		l = new DatapointAddress("0/5/1");
//		knxMetadataSens.setDescription("Janelas");
//		knxDatapoints.put(l, knxMetadataSens.build());
//		
//		// ==
//		
//		l = new DatapointAddress("0/6/0");
//		knxMetadataSens.setDescription("Temperatura");
//		knxDatapoints.put(l, knxMetadataSens.build());
//		
//		l = new DatapointAddress("0/6/1");
//		knxMetadataSens.setDescription("Vento");
//		knxDatapoints.put(l, knxMetadataSens.build());
//		
//		l = new DatapointAddress("0/6/2");
//		knxMetadataSens.setDescription("Chuva");
//		knxDatapoints.put(l, knxMetadataSens.build());
//		
//		l = new DatapointAddress("0/6/3");
//		knxMetadataSens.setDescription("Luminosidade");
//		knxDatapoints.put(l, knxMetadataSens.build());
//		
//		l = new DatapointAddress("0/6/4");
//		knxMetadataSens.setDescription("Alimentacao");
//		knxDatapoints.put(l, knxMetadataSens.build());
//		
//		// AC TODO
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
