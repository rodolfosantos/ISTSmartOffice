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
		int i = 0;
		if(i==0)
			return defaultKNXDatapointsConfig();
		
		
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

		// LAB 1.58
		// ================================================================
		m.setGatewayAddress("172.20.70.209");
		
		m.setDescription("EnergyLab Light Blackboard");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/7/1");
		m.setWriteDatapointAddress("0/1/0");
		knxDatapoints.put(new DatapointAddress("knx158light1"), m.build());

		m.setDescription("EnergyLab Light Middle1");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/7/21");
		m.setWriteDatapointAddress("0/1/2");
		knxDatapoints.put(new DatapointAddress("knx158light2"), m.build());

		m.setDescription("EnergyLab Light Middle2");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/7/41");
		m.setWriteDatapointAddress("0/1/4");
		knxDatapoints.put(new DatapointAddress("knx158light3"), m.build());

		m.setDescription("EnergyLab Light TV");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/7/61");
		m.setWriteDatapointAddress("0/1/6");
		knxDatapoints.put(new DatapointAddress("knx158light4"), m.build());

		m.setDescription("EnergyLab All Lights");
		m.setAccessType(AccessType.WRITE_ONLY);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress(null);
		m.setWriteDatapointAddress("0/1/8");
		knxDatapoints.put(new DatapointAddress("knx158lightall"), m.build());

		// ================================================================
		m.setDescription("EnergyLab Blind1");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/2/0");
		m.setWriteDatapointAddress("0/2/3");
		knxDatapoints.put(new DatapointAddress("knx158blind1"), m.build());

		m.setDescription("EnergyLab Blind2");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/2/13");
		m.setWriteDatapointAddress("0/2/6");
		knxDatapoints.put(new DatapointAddress("knx158blind2"), m.build());

		m.setDescription("EnergyLab Blind3");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/2/14");
		m.setWriteDatapointAddress("0/2/9");
		knxDatapoints.put(new DatapointAddress("knx158blind3"), m.build());

		m.setDescription("EnergyLab All Blinds");
		m.setAccessType(AccessType.WRITE_ONLY);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress(null);
		m.setWriteDatapointAddress("0/2/12");
		knxDatapoints.put(new DatapointAddress("knx158blindall"), m.build());

		// ================================================================
		m.setDescription("EnergyLab Door");
		m.setAccessType(AccessType.WRITE_ONLY);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress(null);
		m.setWriteDatapointAddress("0/3/0");
		knxDatapoints.put(new DatapointAddress("knx158door"), m.build());

		// ================================================================

		m.setDescription("EnergyLab CO2");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/4/0");
		m.setWriteDatapointAddress(null);
		knxDatapoints.put(new DatapointAddress("knx158sensorco"), m.build());

		m.setDescription("EnergyLab Humidity");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress(null);
		m.setWriteDatapointAddress("0/4/1");
		knxDatapoints.put(new DatapointAddress("knx158sensorhum"), m.build());

		m.setDescription("EnergyLab Temperature");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/4/3");
		m.setWriteDatapointAddress(null);
		knxDatapoints.put(new DatapointAddress("knx158sensortemp"), m.build());

		m.setDescription("EnergyLab Temperature Door");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/4/5");
		m.setWriteDatapointAddress(null);
		knxDatapoints.put(new DatapointAddress("knx158sensortempd"), m.build());

		m.setDescription("EnergyLab Lux");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/4/4");
		m.setWriteDatapointAddress(null);
		knxDatapoints.put(new DatapointAddress("knx158sensorlux"), m.build());

		// ================================================================
		m.setDescription("EnergyLab HVAC ONOFF");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("1/0/8");
		m.setWriteDatapointAddress("1/0/0");
		knxDatapoints.put(new DatapointAddress("knx158hvac"), m.build());

		m.setDescription("EnergyLab HVAC Mode");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("1/0/9");
		m.setWriteDatapointAddress("1/0/1");
		knxDatapoints.put(new DatapointAddress("knx158hvacmode"), m.build());

		
		/* ================================================================ *
		 * Nucleus 14 - (Consultar: 'Endereços de Grupo - MIT - IST.pdf')   *
		 * ================================================================ */
		
		m.setGatewayAddress("172.20.70.241");

		// ILUMINACAO GABINETES - BUS NUCLEO 14 
		m.setDescription("2-N14 - All Lights");
		m.setAccessType(AccessType.WRITE_ONLY);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/0/1");
		m.setWriteDatapointAddress("0/0/1");
		knxDatapoints.put(new DatapointAddress("knx2n14alllights"), m.build());

		m.setDescription("2-N14.02 - Lights");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/0/2");
		m.setWriteDatapointAddress("0/0/2");
		knxDatapoints.put(new DatapointAddress("knx2n1402lights"), m.build());

		m.setDescription("2-N14.04 - Lights");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/0/3");
		m.setWriteDatapointAddress("0/0/3");
		knxDatapoints.put(new DatapointAddress("knx2n1404lights"), m.build());

		m.setDescription("2-N14.06 - Lights");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/0/4");
		m.setWriteDatapointAddress("0/0/4");
		knxDatapoints.put(new DatapointAddress("knx2n1406lights"), m.build());

		m.setDescription("2-N14.08 - Lights");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/0/5");
		m.setWriteDatapointAddress("0/0/5");
		knxDatapoints.put(new DatapointAddress("knx2n1408lights"), m.build());

		m.setDescription("2-N14.10 - Lights");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/0/6");
		m.setWriteDatapointAddress("0/0/6");
		knxDatapoints.put(new DatapointAddress("knx2n1410lights"), m.build());

		m.setDescription("2-N14.12 - Lights");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/0/7");
		m.setWriteDatapointAddress("0/0/7");
		knxDatapoints.put(new DatapointAddress("knx2n1412lights"), m.build());

		m.setDescription("2-N14.14 - Lights");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/0/8");
		m.setWriteDatapointAddress("0/0/8");
		knxDatapoints.put(new DatapointAddress("knx2n1414lights"), m.build());

		m.setDescription("2-N14.16 - Lights");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/0/9");
		m.setWriteDatapointAddress("0/0/9");
		knxDatapoints.put(new DatapointAddress("knx2n1416lights"), m.build());

		m.setDescription("2-N14.18 - Lights");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/0/10");
		m.setWriteDatapointAddress("0/0/10");
		knxDatapoints.put(new DatapointAddress("knx2n1418lights"), m.build());

		m.setDescription("2-N14.20 - Lights");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/0/11");
		m.setWriteDatapointAddress("0/0/11");
		knxDatapoints.put(new DatapointAddress("knx2n1420lights"), m.build());

		m.setDescription("2-N14.22 - Lights");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/0/12");
		m.setWriteDatapointAddress("0/0/12");
		knxDatapoints.put(new DatapointAddress("knx2n1422lights"), m.build());

		m.setDescription("2-N14.24 - Lights");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/0/13");
		m.setWriteDatapointAddress("0/0/13");
		knxDatapoints.put(new DatapointAddress("knx2n1424lights"), m.build());

		m.setDescription("2-N14.26 - Lights");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/0/14");
		m.setWriteDatapointAddress("0/0/14");
		knxDatapoints.put(new DatapointAddress("knx2n1426lights"), m.build());

		m.setDescription("2-N14.28 - Lights");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/0/15");
		m.setWriteDatapointAddress("0/0/15");
		knxDatapoints.put(new DatapointAddress("knx2n1428lights"), m.build());

		m.setDescription("2-N14.1.1E/2E - Lights Circuit");
		m.setAccessType(AccessType.WRITE_ONLY);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/0/16");
		m.setWriteDatapointAddress("0/0/16");
		knxDatapoints.put(new DatapointAddress("knx2n1411e2elights"), m.build());



		// HVAC - FUNCOES DE CONTROLO - BUS NUCLEO 14 
		m.setDescription("2-N14 - HVAC All Fan Step");
		m.setAccessType(AccessType.WRITE_ONLY);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/1/0");
		m.setWriteDatapointAddress("0/1/0");
		//knxDatapoints.put(new DatapointAddress("knx2n14hvacallfanstep"), m.build());

		m.setDescription("2-N14.02 - Fan Step");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/1");
		m.setWriteDatapointAddress("0/1/1");
		knxDatapoints.put(new DatapointAddress("knx2n1402fanstep"), m.build());

		m.setDescription("2-N14.04 - Fan Step");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/2");
		m.setWriteDatapointAddress("0/1/2");
		knxDatapoints.put(new DatapointAddress("knx2n1404fanstep"), m.build());

		m.setDescription("2-N14.06 - Fan Step");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/3");
		m.setWriteDatapointAddress("0/1/3");
		knxDatapoints.put(new DatapointAddress("knx2n1406fanstep"), m.build());

		m.setDescription("2-N14.08 - Fan Step");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/4");
		m.setWriteDatapointAddress("0/1/4");
		knxDatapoints.put(new DatapointAddress("knx2n1408fanstep"), m.build());

		m.setDescription("2-N14.10 - Fan Step");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/5");
		m.setWriteDatapointAddress("0/1/5");
		knxDatapoints.put(new DatapointAddress("knx2n1410fanstep"), m.build());

		m.setDescription("2-N14.12 - Fan Step");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/6");
		m.setWriteDatapointAddress("0/1/6");
		knxDatapoints.put(new DatapointAddress("knx2n1412fanstep"), m.build());

		m.setDescription("2-N14.14 - Fan Step");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/7");
		m.setWriteDatapointAddress("0/1/7");
		knxDatapoints.put(new DatapointAddress("knx2n1414fanstep"), m.build());

		m.setDescription("2-N14.16 - Fan Step");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/8");
		m.setWriteDatapointAddress("0/1/8");
		knxDatapoints.put(new DatapointAddress("knx2n1416fanstep"), m.build());

		m.setDescription("2-N14.18 - Fan Step");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/9");
		m.setWriteDatapointAddress("0/1/9");
		knxDatapoints.put(new DatapointAddress("knx2n1418fanstep"), m.build());

		m.setDescription("2-N14.20 - Fan Step");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/10");
		m.setWriteDatapointAddress("0/1/10");
		knxDatapoints.put(new DatapointAddress("knx2n1420fanstep"), m.build());

		m.setDescription("2-N14.24 - Fan Step");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/11");
		m.setWriteDatapointAddress("0/1/11");
		knxDatapoints.put(new DatapointAddress("knx2n1424fanstep"), m.build());

		m.setDescription("2-N14.26 - Fan Step");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/12");
		m.setWriteDatapointAddress("0/1/12");
		knxDatapoints.put(new DatapointAddress("knx2n1426fanstep"), m.build());

		m.setDescription("2-N14.28 - Fan Step");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/13");
		m.setWriteDatapointAddress("0/1/13");
		knxDatapoints.put(new DatapointAddress("knx2n1428fanstep"), m.build());

		m.setDescription("2-N14.02 - Hot H2O Valve");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/14");
		m.setWriteDatapointAddress("0/1/14");
		knxDatapoints.put(new DatapointAddress("knx2n1402hoth2ovalve"), m.build());

		m.setDescription("2-N14.04 - Hot H2O Valve");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/15");
		m.setWriteDatapointAddress("0/1/15");
		knxDatapoints.put(new DatapointAddress("knx2n1404hoth2ovalve"), m.build());

		m.setDescription("2-N14.06 - Hot H2O Valve");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/16");
		m.setWriteDatapointAddress("0/1/16");
		knxDatapoints.put(new DatapointAddress("knx2n1406hoth2ovalve"), m.build());

		m.setDescription("2-N14.08 - Hot H2O Valve");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/17");
		m.setWriteDatapointAddress("0/1/17");
		knxDatapoints.put(new DatapointAddress("knx2n1408hoth2ovalve"), m.build());

		m.setDescription("2-N14.10 - Hot H2O Valve");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/18");
		m.setWriteDatapointAddress("0/1/18");
		knxDatapoints.put(new DatapointAddress("knx2n1410hoth2ovalve"), m.build());

		m.setDescription("2-N14.12 - Hot H2O Valve");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/19");
		m.setWriteDatapointAddress("0/1/19");
		knxDatapoints.put(new DatapointAddress("knx2n1412hoth2ovalve"), m.build());

		m.setDescription("2-N14.14 - Hot H2O Valve");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/20");
		m.setWriteDatapointAddress("0/1/20");
		knxDatapoints.put(new DatapointAddress("knx2n1414hoth2ovalve"), m.build());

		m.setDescription("2-N14.16 - Hot H2O Valve");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/21");
		m.setWriteDatapointAddress("0/1/21");
		knxDatapoints.put(new DatapointAddress("knx2n1416hoth2ovalve"), m.build());

		m.setDescription("2-N14.18 - Hot H2O Valve");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/22");
		m.setWriteDatapointAddress("0/1/22");
		knxDatapoints.put(new DatapointAddress("knx2n1418hoth2ovalve"), m.build());



		// HVAC - FUNCOES DE CONTROLO - BUS NUCLEO 14 
		m.setDescription("2-N14.20 - Hot H2O Valve");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/23");
		m.setWriteDatapointAddress("0/1/23");
		knxDatapoints.put(new DatapointAddress("knx2n1420hoth2ovalve"), m.build());

		m.setDescription("2-N14.24 - Hot H2O Valve");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/24");
		m.setWriteDatapointAddress("0/1/24");
		knxDatapoints.put(new DatapointAddress("knx2n1424hoth2ovalve"), m.build());

		m.setDescription("2-N14.26 - Hot H2O Valve");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/25");
		m.setWriteDatapointAddress("0/1/25");
		knxDatapoints.put(new DatapointAddress("knx2n1426hoth2ovalve"), m.build());

		m.setDescription("2-N14.28 - Hot H2O Valve");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/26");
		m.setWriteDatapointAddress("0/1/26");
		knxDatapoints.put(new DatapointAddress("knx2n1428hoth2ovalve"), m.build());

		m.setDescription("2-N14.02 - Cold H2O Valve");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/27");
		m.setWriteDatapointAddress("0/1/27");
		knxDatapoints.put(new DatapointAddress("knx2n1402coldh2ovalve"), m.build());

		m.setDescription("2-N14.04 - Cold H2O Valve");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/28");
		m.setWriteDatapointAddress("0/1/28");
		knxDatapoints.put(new DatapointAddress("knx2n1404coldh2ovalve"), m.build());

		m.setDescription("2-N14.06 - Cold H2O Valve");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/29");
		m.setWriteDatapointAddress("0/1/29");
		knxDatapoints.put(new DatapointAddress("knx2n1406coldh2ovalve"), m.build());

		m.setDescription("2-N14.08 - Cold H2O Valve");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/30");
		m.setWriteDatapointAddress("0/1/30");
		knxDatapoints.put(new DatapointAddress("knx2n1408coldh2ovalve"), m.build());

		m.setDescription("2-N14.10 - Cold H2O Valve");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/31");
		m.setWriteDatapointAddress("0/1/31");
		knxDatapoints.put(new DatapointAddress("knx2n1410coldh2ovalve"), m.build());

		m.setDescription("2-N14.12 - Cold H2O Valve");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/32");
		m.setWriteDatapointAddress("0/1/32");
		knxDatapoints.put(new DatapointAddress("knx2n1412coldh2ovalve"), m.build());

		m.setDescription("2-N14.14 - Cold H2O Valve");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/33");
		m.setWriteDatapointAddress("0/1/33");
		knxDatapoints.put(new DatapointAddress("knx2n1414coldh2ovalve"), m.build());

		m.setDescription("2-N14.16 - Cold H2O Valve");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/34");
		m.setWriteDatapointAddress("0/1/34");
		knxDatapoints.put(new DatapointAddress("knx2n1416coldh2ovalve"), m.build());

		m.setDescription("2-N14.18 - Cold H2O Valve");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/35");
		m.setWriteDatapointAddress("0/1/35");
		knxDatapoints.put(new DatapointAddress("knx2n1418coldh2ovalve"), m.build());

		m.setDescription("2-N14.20 - Cold H2O Valve");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/36");
		m.setWriteDatapointAddress("0/1/36");
		knxDatapoints.put(new DatapointAddress("knx2n1420coldh2ovalve"), m.build());

		m.setDescription("2-N14.24 - Cold H2O Valve");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/37");
		m.setWriteDatapointAddress("0/1/37");
		knxDatapoints.put(new DatapointAddress("knx2n1424coldh2ovalve"), m.build());

		m.setDescription("2-N14.26 - Cold H2O Valve");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/38");
		m.setWriteDatapointAddress("0/1/38");
		knxDatapoints.put(new DatapointAddress("knx2n1426coldh2ovalve"), m.build());

		m.setDescription("2-N14.28 - Cold H2O Valve");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.PERCENTAGE);
		m.setReadDatapointAddress("0/1/39");
		m.setWriteDatapointAddress("0/1/39");
		knxDatapoints.put(new DatapointAddress("knx2n1428coldh2ovalve"), m.build());

		
		// HVAC - FUNCOES DE CONTROLO - BUS NUCLEO 14
		m.setDescription("2-N14.02 - HVAC Mode");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/1/40");
		m.setWriteDatapointAddress("0/1/40");
		knxDatapoints.put(new DatapointAddress("knx2n1402hvacmode"), m.build());


		m.setDescription("2-N14.04 - HVAC Mode");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/1/41");
		m.setWriteDatapointAddress("0/1/41");
		knxDatapoints.put(new DatapointAddress("knx2n1404hvacmode"), m.build());

		m.setDescription("2-N14.06 - HVAC Mode");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/1/42");
		m.setWriteDatapointAddress("0/1/42");
		knxDatapoints.put(new DatapointAddress("knx2n1406hvacmode"), m.build());

		m.setDescription("2-N14.08 - HVAC Mode");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/1/43");
		m.setWriteDatapointAddress("0/1/43");
		knxDatapoints.put(new DatapointAddress("knx2n1408hvacmode"), m.build());

		m.setDescription("2-N14.10 - HVAC Mode");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/1/44");
		m.setWriteDatapointAddress("0/1/44");
		knxDatapoints.put(new DatapointAddress("knx2n1410hvacmode"), m.build());

		m.setDescription("2-N14.12 - HVAC Mode");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/1/45");
		m.setWriteDatapointAddress("0/1/45");
		knxDatapoints.put(new DatapointAddress("knx2n1412hvacmode"), m.build());

		m.setDescription("2-N14.14 - HVAC Mode");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/1/46");
		m.setWriteDatapointAddress("0/1/46");
		knxDatapoints.put(new DatapointAddress("knx2n1414hvacmode"), m.build());

		m.setDescription("2-N14.16 - HVAC Mode");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/1/47");
		m.setWriteDatapointAddress("0/1/47");
		knxDatapoints.put(new DatapointAddress("knx2n1416hvacmode"), m.build());

		m.setDescription("2-N14.18 - HVAC Mode");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/1/48");
		m.setWriteDatapointAddress("0/1/48");
		knxDatapoints.put(new DatapointAddress("knx2n1418hvacmode"), m.build());

		m.setDescription("2-N14.20 - HVAC Mode");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/1/49");
		m.setWriteDatapointAddress("0/1/49");
		knxDatapoints.put(new DatapointAddress("knx2n1420hvacmode"), m.build());

		m.setDescription("2-N14.24 - HVAC Mode");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/1/50");
		m.setWriteDatapointAddress("0/1/50");
		knxDatapoints.put(new DatapointAddress("knx2n1424hvacmode"), m.build());

		m.setDescription("2-N14.26 - HVAC Mode");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/1/51");
		m.setWriteDatapointAddress("0/1/51");
		knxDatapoints.put(new DatapointAddress("knx2n1426hvacmode"), m.build());

		m.setDescription("2-N14.28 - HVAC Mode");
		m.setAccessType(AccessType.READ_WRITE);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/1/52");
		m.setWriteDatapointAddress("0/1/52");
		knxDatapoints.put(new DatapointAddress("knx2n1428hvacmode"), m.build());



		// ENERGIA e SENSORES GERAIS 
		m.setDescription("2-N14 - EnergyMeter - Circ. A - Hall Lights");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/2/0");
		m.setWriteDatapointAddress("0/2/0");
		knxDatapoints.put(new DatapointAddress("knx2n14energymetercircahalllights"), m.build());

		m.setDescription("2-N14 - EnergyMeter - Circ. B - Hall Lights");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/2/1");
		m.setWriteDatapointAddress("0/2/1");
		knxDatapoints.put(new DatapointAddress("knx2n14energymetercircbhalllights"), m.build());

		m.setDescription("2-N14 - EnergyMeter - Circ. C - HVAC Supply");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/2/2");
		m.setWriteDatapointAddress("0/2/2");
		knxDatapoints.put(new DatapointAddress("knx2n14energymetercircchvacsupply"), m.build());

		m.setDescription("2-N14 - EnergyMeter - Circ. D - HVAC Supply");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/2/3");
		m.setWriteDatapointAddress("0/2/3");
		knxDatapoints.put(new DatapointAddress("knx2n14energymetercircdhvacsupply"), m.build());

		m.setDescription("2-N14 - EnergyTimeCounter - Circ. A - Hall Lights");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/2/4");
		m.setWriteDatapointAddress("0/2/4");
		knxDatapoints.put(new DatapointAddress("knx2n14energytimecountercircahalllights"), m.build());

		m.setDescription("2-N14 - EnergyTimeCounter - Circ. B - Hall Lights");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/2/5");
		m.setWriteDatapointAddress("0/2/5");
		knxDatapoints.put(new DatapointAddress("knx2n14energytimecountercircbhalllights"), m.build());

		m.setDescription("2-N14 - EnergyTimeCounter - Circ. C - HVAC Supply");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/2/6");
		m.setWriteDatapointAddress("0/2/6");
		knxDatapoints.put(new DatapointAddress("knx2n14energytimecountercircchvacsupply"), m.build());

		m.setDescription("2-N14 - EnergyTimeCounter - Circ. D - HVAC Supply");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/2/7");
		m.setWriteDatapointAddress("0/2/7");
		knxDatapoints.put(new DatapointAddress("knx2n14energytimecountercircdhvacsupply"), m.build());

//		m.setDescription("2-N14 - EnergyTimeReset - Circ. A - Hall Lights");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/2/8");
//		m.setWriteDatapointAddress("0/2/8");
//		knxDatapoints.put(new DatapointAddress("knx2n14energytimeresetcircahalllights"), m.build());
//
//		m.setDescription("2-N14 - EnergyTimeReset - Circ. B - Hall Lights");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/2/9");
//		m.setWriteDatapointAddress("0/2/9");
//		knxDatapoints.put(new DatapointAddress("knx2n14energytimeresetcircbhalllights"), m.build());
//
//		m.setDescription("2-N14 - EnergyTimeReset - Circ. C - HVAC Supply");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/2/10");
//		m.setWriteDatapointAddress("0/2/10");
//		knxDatapoints.put(new DatapointAddress("knx2n14energytimeresetcircchvacsupply"), m.build());
//
//		m.setDescription("2-N14 - EnergyTimeReset - Circ. D - HVAC Supply");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/2/11");
//		m.setWriteDatapointAddress("0/2/11");
//		knxDatapoints.put(new DatapointAddress("knx2n14energytimeresetcircdhvacsupply"), m.build());
//
//		m.setDescription("2-N14 - Status - Circ. A - Hall Lights");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/2/12");
//		m.setWriteDatapointAddress("0/2/12");
//		knxDatapoints.put(new DatapointAddress("knx2n14statuscircahalllights"), m.build());
//
//		m.setDescription("2-N14 - Status - Circ. B - Hall Lights");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/2/13");
//		m.setWriteDatapointAddress("0/2/13");
//		knxDatapoints.put(new DatapointAddress("knx2n14statuscircbhalllights"), m.build());
//
//		m.setDescription("2-N14 - Status - Circ. C - HVAC Supply");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/2/14");
//		m.setWriteDatapointAddress("0/2/14");
//		knxDatapoints.put(new DatapointAddress("knx2n14statuscircchvacsupply"), m.build());



		// ENERGIA e SENSORES GERAIS 
//		m.setDescription("2-N14 - Status - Circ. D - HVAC Supply");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/2/15");
//		m.setWriteDatapointAddress("0/2/15");
//		knxDatapoints.put(new DatapointAddress("knx2n14statuscircdhvacsupply"), m.build());

		m.setDescription("2-N14 - Luminosity - Hall - North Sensor");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/2/16");
		m.setWriteDatapointAddress("0/2/16");
		knxDatapoints.put(new DatapointAddress("knx2n14luminosityhallnorthsensor"), m.build());

		m.setDescription("2-N14 - Luminosity - Hall - Middle Sensor");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/2/17");
		m.setWriteDatapointAddress("0/2/17");
		knxDatapoints.put(new DatapointAddress("knx2n14luminosityhallmiddlesensor"), m.build());

		m.setDescription("2-N14 - Luminosity - Hall - South Sensor");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/2/18");
		m.setWriteDatapointAddress("0/2/18");
		knxDatapoints.put(new DatapointAddress("knx2n14luminosityhallsouthsensor"), m.build());



		// ESTADOS e TEMPERATURAS HVAC 
//		m.setDescription("2-N14 - HVAC All States and Temp.");
//		m.setAccessType(AccessType.READ_ONLY);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/0");
//		m.setWriteDatapointAddress("0/3/0");
//		knxDatapoints.put(new DatapointAddress("knx2n14hvacallstatestemp"), m.build());

//		m.setDescription("2-N14.02 - HVAC Heating Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/1");
//		m.setWriteDatapointAddress("0/3/1");
//		knxDatapoints.put(new DatapointAddress("knx2n1402hvacheatstatus"), m.build());
//
//		m.setDescription("2-N14.04 - HVAC Heating Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/2");
//		m.setWriteDatapointAddress("0/3/2");
//		knxDatapoints.put(new DatapointAddress("knx2n1404hvacheatstatus"), m.build());
//
//		m.setDescription("2-N14.06 - HVAC Heating Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/3");
//		m.setWriteDatapointAddress("0/3/3");
//		knxDatapoints.put(new DatapointAddress("knx2n1406hvacheatstatus"), m.build());
//
//		m.setDescription("2-N14.08 - HVAC Heating Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/4");
//		m.setWriteDatapointAddress("0/3/4");
//		knxDatapoints.put(new DatapointAddress("knx2n1408hvacheatstatus"), m.build());
//
//		m.setDescription("2-N14.10 - HVAC Heating Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/5");
//		m.setWriteDatapointAddress("0/3/5");
//		knxDatapoints.put(new DatapointAddress("knx2n1410hvacheatstatus"), m.build());
//
//		m.setDescription("2-N14.12 - HVAC Heating Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/6");
//		m.setWriteDatapointAddress("0/3/6");
//		knxDatapoints.put(new DatapointAddress("knx2n1412hvacheatstatus"), m.build());
//
//		m.setDescription("2-N14.14 - HVAC Heating Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/7");
//		m.setWriteDatapointAddress("0/3/7");
//		knxDatapoints.put(new DatapointAddress("knx2n1414hvacheatstatus"), m.build());
//
//		m.setDescription("2-N14.16 - HVAC Heating Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/8");
//		m.setWriteDatapointAddress("0/3/8");
//		knxDatapoints.put(new DatapointAddress("knx2n1416hvacheatstatus"), m.build());
//
//		m.setDescription("2-N14.18 - HVAC Heating Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/9");
//		m.setWriteDatapointAddress("0/3/9");
//		knxDatapoints.put(new DatapointAddress("knx2n1418hvacheatstatus"), m.build());
//
//		m.setDescription("2-N14.20 - HVAC Heating Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/10");
//		m.setWriteDatapointAddress("0/3/10");
//		knxDatapoints.put(new DatapointAddress("knx2n1420hvacheatstatus"), m.build());
//
//		m.setDescription("2-N14.24 - HVAC Heating Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/11");
//		m.setWriteDatapointAddress("0/3/11");
//		knxDatapoints.put(new DatapointAddress("knx2n1424hvacheatstatus"), m.build());
//
//		m.setDescription("2-N14.26 - HVAC Heating Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/12");
//		m.setWriteDatapointAddress("0/3/12");
//		knxDatapoints.put(new DatapointAddress("knx2n1426hvacheatstatus"), m.build());
//
//		m.setDescription("2-N14.28 - HVAC Heating Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/13");
//		m.setWriteDatapointAddress("0/3/13");
//		knxDatapoints.put(new DatapointAddress("knx2n1428hvacheatstatus"), m.build());
//
//		m.setDescription("2-N14.02 - HVAC Heating Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/14");
//		m.setWriteDatapointAddress("0/3/14");
//		knxDatapoints.put(new DatapointAddress("knx2n1402hvacheatstatus"), m.build());
//
//		m.setDescription("2-N14.04 - HVAC Heating Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/15");
//		m.setWriteDatapointAddress("0/3/15");
//		knxDatapoints.put(new DatapointAddress("knx2n1404hvacheatstatus"), m.build());
//
//		m.setDescription("2-N14.06 - HVAC Heating Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/16");
//		m.setWriteDatapointAddress("0/3/16");
//		knxDatapoints.put(new DatapointAddress("knx2n1406hvacheatstatus"), m.build());
//
//		m.setDescription("2-N14.08 - HVAC Heating Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/17");
//		m.setWriteDatapointAddress("0/3/17");
//		knxDatapoints.put(new DatapointAddress("knx2n1408hvacheatstatus"), m.build());
//
//		m.setDescription("2-N14.10 - HVAC Heating Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/18");
//		m.setWriteDatapointAddress("0/3/18");
//		knxDatapoints.put(new DatapointAddress("knx2n1410hvacheatstatus"), m.build());
//
//
//
//		// ESTADOS e TEMPERATURAS HVAC 
//		m.setDescription("2-N14.12 - HVAC Cooling Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/19");
//		m.setWriteDatapointAddress("0/3/19");
//		knxDatapoints.put(new DatapointAddress("knx2n1412hvaccoolstatus"), m.build());
//
//		m.setDescription("2-N14.14 - HVAC Cooling Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/20");
//		m.setWriteDatapointAddress("0/3/20");
//		knxDatapoints.put(new DatapointAddress("knx2n1414hvaccoolstatus"), m.build());
//
//		m.setDescription("2-N14.16 - HVAC Cooling Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/21");
//		m.setWriteDatapointAddress("0/3/21");
//		knxDatapoints.put(new DatapointAddress("knx2n1416hvaccoolstatus"), m.build());
//
//		m.setDescription("2-N14.18 - HVAC Cooling Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/22");
//		m.setWriteDatapointAddress("0/3/22");
//		knxDatapoints.put(new DatapointAddress("knx2n1418hvaccoolstatus"), m.build());
//
//		m.setDescription("2-N14.20 - HVAC Cooling Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/23");
//		m.setWriteDatapointAddress("0/3/23");
//		knxDatapoints.put(new DatapointAddress("knx2n1420hvaccoolstatus"), m.build());
//
//		m.setDescription("2-N14.24 - HVAC Cooling Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/24");
//		m.setWriteDatapointAddress("0/3/24");
//		knxDatapoints.put(new DatapointAddress("knx2n1424hvaccoolstatus"), m.build());
//
//		m.setDescription("2-N14.26 - HVAC Cooling Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/25");
//		m.setWriteDatapointAddress("0/3/25");
//		knxDatapoints.put(new DatapointAddress("knx2n1426hvaccoolstatus"), m.build());
//
//		m.setDescription("2-N14.28 - HVAC Cooling Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/3/26");
//		m.setWriteDatapointAddress("0/3/26");
//		knxDatapoints.put(new DatapointAddress("knx2n1428hvaccoolstatus"), m.build());
//
//		m.setDescription("2-N14.02 - HVAC Cooling Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.BYTE_1);
//		m.setReadDatapointAddress("0/3/27");
//		m.setWriteDatapointAddress("0/3/27");
//		knxDatapoints.put(new DatapointAddress("knx2n1402hvaccoolstatus"), m.build());
//
//		m.setDescription("2-N14.04 - HVAC Cooling Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.BYTE_1);
//		m.setReadDatapointAddress("0/3/28");
//		m.setWriteDatapointAddress("0/3/28");
//		knxDatapoints.put(new DatapointAddress("knx2n1404hvaccoolstatus"), m.build());
//
//		m.setDescription("2-N14.06 - HVAC Cooling Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.BYTE_1);
//		m.setReadDatapointAddress("0/3/29");
//		m.setWriteDatapointAddress("0/3/29");
//		knxDatapoints.put(new DatapointAddress("knx2n1406hvaccoolstatus"), m.build());
//
//		m.setDescription("2-N14.08 - HVAC Cooling Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.BYTE_1);
//		m.setReadDatapointAddress("0/3/30");
//		m.setWriteDatapointAddress("0/3/30");
//		knxDatapoints.put(new DatapointAddress("knx2n1408hvaccoolstatus"), m.build());
//
//		m.setDescription("2-N14.10 - HVAC Cooling Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.BYTE_1);
//		m.setReadDatapointAddress("0/3/31");
//		m.setWriteDatapointAddress("0/3/31");
//		knxDatapoints.put(new DatapointAddress("knx2n1410hvaccoolstatus"), m.build());
//
//		m.setDescription("2-N14.12 - HVAC Cooling Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.BYTE_1);
//		m.setReadDatapointAddress("0/3/32");
//		m.setWriteDatapointAddress("0/3/32");
//		knxDatapoints.put(new DatapointAddress("knx2n1412hvaccoolstatus"), m.build());
//
//		m.setDescription("2-N14.14 - HVAC Cooling Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.BYTE_1);
//		m.setReadDatapointAddress("0/3/33");
//		m.setWriteDatapointAddress("0/3/33");
//		knxDatapoints.put(new DatapointAddress("knx2n1414hvaccoolstatus"), m.build());
//
//		m.setDescription("2-N14.16 - HVAC Cooling Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.BYTE_1);
//		m.setReadDatapointAddress("0/3/34");
//		m.setWriteDatapointAddress("0/3/34");
//		knxDatapoints.put(new DatapointAddress("knx2n1416hvaccoolstatus"), m.build());
//
//		m.setDescription("2-N14.18 - HVAC Cooling Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.BYTE_1);
//		m.setReadDatapointAddress("0/3/35");
//		m.setWriteDatapointAddress("0/3/35");
//		knxDatapoints.put(new DatapointAddress("knx2n1418hvaccoolstatus"), m.build());
//
//		m.setDescription("2-N14.20 - HVAC Cooling Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.BYTE_1);
//		m.setReadDatapointAddress("0/3/36");
//		m.setWriteDatapointAddress("0/3/36");
//		knxDatapoints.put(new DatapointAddress("knx2n1420hvaccoolstatus"), m.build());
//
//		m.setDescription("2-N14.24 - HVAC Cooling Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.BYTE_1);
//		m.setReadDatapointAddress("0/3/37");
//		m.setWriteDatapointAddress("0/3/37");
//		knxDatapoints.put(new DatapointAddress("knx2n1424hvaccoolstatus"), m.build());
//
//		m.setDescription("2-N14.26 - HVAC Cooling Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.BYTE_1);
//		m.setReadDatapointAddress("0/3/38");
//		m.setWriteDatapointAddress("0/3/38");
//		knxDatapoints.put(new DatapointAddress("knx2n1426hvaccoolstatus"), m.build());
//
//		m.setDescription("2-N14.28 - HVAC Cooling Status");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.BYTE_1);
//		m.setReadDatapointAddress("0/3/39");
//		m.setWriteDatapointAddress("0/3/39");
//		knxDatapoints.put(new DatapointAddress("knx2n1428hvaccoolstatus"), m.build());
//
		m.setDescription("2-N14.02 - HVAC Cooling Status");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/40");
		m.setWriteDatapointAddress("0/3/40");
		knxDatapoints.put(new DatapointAddress("knx2n1402hvaccoolstatus"), m.build());

		m.setDescription("2-N14.04 - HVAC Cooling Status");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/41");
		m.setWriteDatapointAddress("0/3/41");
		knxDatapoints.put(new DatapointAddress("knx2n1404hvaccoolstatus"), m.build());

		m.setDescription("2-N14.06 - HVAC Cooling Status");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/42");
		m.setWriteDatapointAddress("0/3/42");
		knxDatapoints.put(new DatapointAddress("knx2n1406hvaccoolstatus"), m.build());



		// ESTADOS e TEMPERATURAS HVAC 
		m.setDescription("2-N14.08 - HVAC Current Temp.");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/43");
		m.setWriteDatapointAddress("0/3/43");
		knxDatapoints.put(new DatapointAddress("knx2n1408hvaccurrenttemp"), m.build());

		m.setDescription("2-N14.10 - HVAC Current Temp.");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/44");
		m.setWriteDatapointAddress("0/3/44");
		knxDatapoints.put(new DatapointAddress("knx2n1410hvaccurrenttemp"), m.build());

		m.setDescription("2-N14.12 - HVAC Current Temp.");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/45");
		m.setWriteDatapointAddress("0/3/45");
		knxDatapoints.put(new DatapointAddress("knx2n1412hvaccurrenttemp"), m.build());

		m.setDescription("2-N14.14 - HVAC Current Temp.");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/46");
		m.setWriteDatapointAddress("0/3/46");
		knxDatapoints.put(new DatapointAddress("knx2n1414hvaccurrenttemp"), m.build());

		m.setDescription("2-N14.16 - HVAC Current Temp.");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/47");
		m.setWriteDatapointAddress("0/3/47");
		knxDatapoints.put(new DatapointAddress("knx2n1416hvaccurrenttemp"), m.build());

		m.setDescription("2-N14.18 - HVAC Current Temp.");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/48");
		m.setWriteDatapointAddress("0/3/48");
		knxDatapoints.put(new DatapointAddress("knx2n1418hvaccurrenttemp"), m.build());

		m.setDescription("2-N14.20 - HVAC Current Temp.");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/49");
		m.setWriteDatapointAddress("0/3/49");
		knxDatapoints.put(new DatapointAddress("knx2n1420hvaccurrenttemp"), m.build());

		m.setDescription("2-N14.24 - HVAC Current Temp.");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/50");
		m.setWriteDatapointAddress("0/3/50");
		knxDatapoints.put(new DatapointAddress("knx2n1424hvaccurrenttemp"), m.build());

		m.setDescription("2-N14.26 - HVAC Current Temp.");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/51");
		m.setWriteDatapointAddress("0/3/51");
		knxDatapoints.put(new DatapointAddress("knx2n1426hvaccurrenttemp"), m.build());

		m.setDescription("2-N14.28 - HVAC Current Temp.");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/52");
		m.setWriteDatapointAddress("0/3/52");
		knxDatapoints.put(new DatapointAddress("knx2n1428hvaccurrenttemp"), m.build());

		m.setDescription("2-N14.02 - HVAC Set Point Temp.");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/53");
		m.setWriteDatapointAddress("0/3/53");
		knxDatapoints.put(new DatapointAddress("knx2n1402hvacsetpointtemp"), m.build());

		m.setDescription("2-N14.04 - HVAC Set Point Temp.");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/54");
		m.setWriteDatapointAddress("0/3/54");
		knxDatapoints.put(new DatapointAddress("knx2n1404hvacsetpointtemp"), m.build());

		m.setDescription("2-N14.06 - HVAC Set Point Temp.");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/55");
		m.setWriteDatapointAddress("0/3/55");
		knxDatapoints.put(new DatapointAddress("knx2n1406hvacsetpointtemp"), m.build());

		m.setDescription("2-N14.08 - HVAC Set Point Temp.");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/56");
		m.setWriteDatapointAddress("0/3/56");
		knxDatapoints.put(new DatapointAddress("knx2n1408hvacsetpointtemp"), m.build());

		m.setDescription("2-N14.10 - HVAC Set Point Temp.");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/57");
		m.setWriteDatapointAddress("0/3/57");
		knxDatapoints.put(new DatapointAddress("knx2n1410hvacsetpointtemp"), m.build());

		m.setDescription("2-N14.12 - HVAC Set Point Temp.");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/58");
		m.setWriteDatapointAddress("0/3/58");
		knxDatapoints.put(new DatapointAddress("knx2n1412hvacsetpointtemp"), m.build());

		m.setDescription("2-N14.14 - HVAC Set Point Temp.");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/59");
		m.setWriteDatapointAddress("0/3/59");
		knxDatapoints.put(new DatapointAddress("knx2n1414hvacsetpointtemp"), m.build());

		m.setDescription("2-N14.16 - HVAC Set Point Temp.");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/60");
		m.setWriteDatapointAddress("0/3/60");
		knxDatapoints.put(new DatapointAddress("knx2n1416hvacsetpointtemp"), m.build());



		// ESTADOS e TEMPERATURAS HVAC 
		m.setDescription("2-N14.18 - HVAC Set Point Temp.");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/61");
		m.setWriteDatapointAddress("0/3/61");
		knxDatapoints.put(new DatapointAddress("knx2n1418hvacsetpointtemp"), m.build());

		m.setDescription("2-N14.20 - HVAC Set Point Temp.");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/62");
		m.setWriteDatapointAddress("0/3/62");
		knxDatapoints.put(new DatapointAddress("knx2n1420hvacsetpointtemp"), m.build());

		m.setDescription("2-N14.24 - HVAC Set Point Temp.");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/63");
		m.setWriteDatapointAddress("0/3/63");
		knxDatapoints.put(new DatapointAddress("knx2n1424hvacsetpointtemp"), m.build());

		m.setDescription("2-N14.26 - HVAC Set Point Temp.");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/64");
		m.setWriteDatapointAddress("0/3/64");
		knxDatapoints.put(new DatapointAddress("knx2n1426hvacsetpointtemp"), m.build());

		m.setDescription("2-N14.28 - HVAC Set Point Temp.");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/65");
		m.setWriteDatapointAddress("0/3/65");
		knxDatapoints.put(new DatapointAddress("knx2n1428hvacsetpointtemp"), m.build());

		m.setDescription("2-N14 - All HVAC Current Temp.");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/66");
		m.setWriteDatapointAddress("0/3/66");
		knxDatapoints.put(new DatapointAddress("knx2n14allhvaccurrenttemp"), m.build());

		m.setDescription("2-N14 - All HVAC Set Point Temp.");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/3/67");
		m.setWriteDatapointAddress("0/3/67");
		knxDatapoints.put(new DatapointAddress("knx2n14allhvacsetpointtemp"), m.build());



		// ESTACAO METEOROLOGICA - BUS Q.E. PISO 1 
		
		m.setGatewayAddress("172.20.70.209");
		
		m.setDescription("2-N14 - Meteo - Luminosity - East Sensor");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/6/5");
		m.setWriteDatapointAddress("0/6/5");
		knxDatapoints.put(new DatapointAddress("knx2n14meteoluminosityeastsensor"), m.build());

		m.setDescription("2-N14 - Meteo - Luminosity - South Sensor");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/6/6");
		m.setWriteDatapointAddress("0/6/6");
		knxDatapoints.put(new DatapointAddress("knx2n14meteoluminositysouthsensor"), m.build());

		m.setDescription("2-N14 - Meteo - Luminosity - West Sensor");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/6/7");
		m.setWriteDatapointAddress("0/6/7");
		knxDatapoints.put(new DatapointAddress("knx2n14meteoluminositywestsensor"), m.build());

		m.setDescription("2-N14 - Meteo - Crespuscular Sensor");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/6/8");
		m.setWriteDatapointAddress("0/6/8");
		knxDatapoints.put(new DatapointAddress("knx2n14meteocrespuscularsensor"), m.build());

		m.setDescription("2-N14 - Meteo -Warn Interval Wind Speed");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/6/9");
		m.setWriteDatapointAddress("0/6/9");
		knxDatapoints.put(new DatapointAddress("knx2n14meteowarnintervalwindspeed"), m.build());

		m.setDescription("2-N14 - Meteo - Wind Speed Sensor");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/6/10");
		m.setWriteDatapointAddress("0/6/10");
		knxDatapoints.put(new DatapointAddress("knx2n14meteowindspeedsensor"), m.build());

		m.setDescription("2-N14 - Meteo - Outside Temp. Sensor");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/6/11");
		m.setWriteDatapointAddress("0/6/11");
		knxDatapoints.put(new DatapointAddress("knx2n14meteooutsidetempsensor"), m.build());

//		m.setDescription("2-N14 - Meteo - Central Operational State");
//		m.setAccessType(AccessType.READ_ONLY);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/6/12");
//		m.setWriteDatapointAddress("0/6/12");
//		knxDatapoints.put(new DatapointAddress("knx2n14meteocentraloperationalstate"), m.build());

		m.setDescription("2-N14 - Meteo - Rain Sensor");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.SWITCH);
		m.setReadDatapointAddress("0/6/13");
		m.setWriteDatapointAddress("0/6/13");
		knxDatapoints.put(new DatapointAddress("knx2n14meteorainsensor"), m.build());

//		m.setDescription("2-N14 - Meteo - Rain Sensor Operational State");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/6/14");
//		m.setWriteDatapointAddress("0/6/14");
//		knxDatapoints.put(new DatapointAddress("knx2n14meteorainsensoroperationalstate"), m.build());

//		m.setDescription("2-N14 - Meteo - Rain Sensor Resistence Op. State");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/6/15");
//		m.setWriteDatapointAddress("0/6/15");
//		knxDatapoints.put(new DatapointAddress("knx2n14meteorainsensorresistenceopstate"), m.build());



		// ESTACAO METEOROLOGICA - BUS Q.E. PISO 1 
		m.setDescription("2-N14 - Meteo - Outside Temp. Sensor Precision");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/6/16");
		m.setWriteDatapointAddress("0/6/16");
		knxDatapoints.put(new DatapointAddress("knx2n14meteooutsidetempsensorprecision"), m.build());

//		m.setDescription("2-N14 - Meteo - Lim. Max. Temp Alarm Precision");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/6/17");
//		m.setWriteDatapointAddress("0/6/17");
//		knxDatapoints.put(new DatapointAddress("knx2n14meteolimmaxtempalarmprecision"), m.build());

//		m.setDescription("2-N14 - Meteo - Lim. Min. Temp Alarm Precision");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/6/18");
//		m.setWriteDatapointAddress("0/6/18");
//		knxDatapoints.put(new DatapointAddress("knx2n14meteolimmintempalarmprecision"), m.build());

		m.setDescription("2-N14 - Meteo - Max. Temp Reached Precision");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/6/19");
		m.setWriteDatapointAddress("0/6/19");
		knxDatapoints.put(new DatapointAddress("knx2n14meteomaxtempreachedprecision"), m.build());

		m.setDescription("2-N14 - Meteo - Min. Temp Reached Precision");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/6/20");
		m.setWriteDatapointAddress("0/6/20");
		knxDatapoints.put(new DatapointAddress("knx2n14meteomintempreachedprecision"), m.build());

//		m.setDescription("2-N14 - Meteo - Recorded Values Mem. Reset");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/6/21");
//		m.setWriteDatapointAddress("0/6/21");
//		knxDatapoints.put(new DatapointAddress("knx2n14meteorecordedvaluesmemreset"), m.build());

		m.setDescription("2-N14 - Meteo - Relative Hum. Sensor Precision");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/6/22");
		m.setWriteDatapointAddress("0/6/22");
		knxDatapoints.put(new DatapointAddress("knx2n14meteorelativehumsensorprecision"), m.build());

//		m.setDescription("2-N14 - Meteo - Lim. Max Relat. Hum. Alarm Precision");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/6/23");
//		m.setWriteDatapointAddress("0/6/23");
//		knxDatapoints.put(new DatapointAddress("knx2n14meteolimmaxrelathumalarmprecision"), m.build());
//
//		m.setDescription("2-N14 - Meteo - Lim. Min Relat. Hum. Alarm Precision");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/6/24");
//		m.setWriteDatapointAddress("0/6/24");
//		knxDatapoints.put(new DatapointAddress("knx2n14meteolimminrelathumalarmprecision"), m.build());

		m.setDescription("2-N14 - Meteo - Drew Point");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/6/25");
		m.setWriteDatapointAddress("0/6/25");
		knxDatapoints.put(new DatapointAddress("knx2n14meteodrewpoint"), m.build());

//		m.setDescription("2-N14 - Meteo - Orvalho Point Sensor");
//		m.setAccessType(AccessType.READ_WRITE);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/6/26");
//		m.setWriteDatapointAddress("0/6/26");
//		knxDatapoints.put(new DatapointAddress("knx2n14meteoorvalhopointsensor"), m.build());

		m.setDescription("2-N14 - Meteo - Absolute Humidity");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/6/27");
		m.setWriteDatapointAddress("0/6/27");
		knxDatapoints.put(new DatapointAddress("knx2n14meteoabsolutehumidity"), m.build());

		m.setDescription("2-N14 - Meteo - Exterior Entalpia");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/6/28");
		m.setWriteDatapointAddress("0/6/28");
		knxDatapoints.put(new DatapointAddress("knx2n14meteoexteriorentalpia"), m.build());

		m.setDescription("2-N14 - Global Solar Radiation Sensor");
		m.setAccessType(AccessType.READ_ONLY);
		m.setDatatype(Datatype.BYTE_2);
		m.setReadDatapointAddress("0/6/29");
		m.setWriteDatapointAddress("0/6/29");
		knxDatapoints.put(new DatapointAddress("knx2n14globalsolarradiationsensor"), m.build());

//		m.setDescription("2-N14 - Global Radiation Max. Lim. Value");
//		m.setAccessType(AccessType.READ_ONLY);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/6/30");
//		m.setWriteDatapointAddress("0/6/30");
//		knxDatapoints.put(new DatapointAddress("knx2n14globalradiationmaxlimvalue"), m.build());
//
//		m.setDescription("2-N14 - Global Radiation Min. Lim. Value");
//		m.setAccessType(AccessType.READ_ONLY);
//		m.setDatatype(Datatype.SWITCH);
//		m.setReadDatapointAddress("0/6/31");
//		m.setWriteDatapointAddress("0/6/31");
//		knxDatapoints.put(new DatapointAddress("knx2n14globalradiationminlimvalue"), m.build());
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
