package eu.smartcampus.api.rest.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import eu.smartcampus.api.DatapointAddress;

// curl http://localhost:8182/deviceapi/datapoint/all
public class DatapointListingService extends ServerResource {
	@Get
	public JSONObject doGet() {
		JSONObject result = new JSONObject();
		DatapointAddress[] allDatapoints = DatapointConnectivityServiceREST.service
				.getAllDatapoints();
		JSONArray array = new JSONArray();
		for (DatapointAddress datapointAddress : allDatapoints) {
			array.put(datapointAddress.getAddress());
		}
		try {
			result.put("response", array);
			return result;
		} catch (JSONException e) {
			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
			return result;
		}
	}
}