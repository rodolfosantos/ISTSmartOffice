package eu.smartcampus.api.historydatastorage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import com.google.gson.Gson;

public class DataStorage {

	
	/**
	 * Store a object to json file.
	 *
	 * @param filename the filename
	 * @param object the object
	 */
	public static void toJsonFile(String filename, Object object) {
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
