package ist.smartoffice.historydatastorage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class DataFileStorage {

	
	/**
	 * Store a object to json file.
	 *
	 * @param filename the filename
	 * @param object the object
	 */
	public static void toJsonFile(String filename, Object object) {
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
	 * @param filename the filename
	 * @param objectType the object type
	 * @return the object
	 */
	public static Object fromJsonFile(String filename, Type objectType) {
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
	 * @param filename the filename
	 * @param classType the class type
	 * @return the object
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object fromJsonFile(String filename, Class classType) {
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
