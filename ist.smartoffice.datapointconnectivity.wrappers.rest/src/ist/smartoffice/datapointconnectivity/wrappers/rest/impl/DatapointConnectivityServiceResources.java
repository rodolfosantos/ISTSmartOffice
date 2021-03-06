package ist.smartoffice.datapointconnectivity.wrappers.rest.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Options;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import ist.smartoffice.datapointconnectivity.DatapointAddress;
import ist.smartoffice.datapointconnectivity.DatapointMetadata;
import ist.smartoffice.datapointconnectivity.DatapointValue;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService.OperationFailedException;
import ist.smartoffice.datapointconnectivity.ReadCallbackImpl;
import ist.smartoffice.datapointconnectivity.WriteCallbackImpl;
import ist.smartoffice.datapointconnectivity.wrappers.rest.DatapointConnectivityServiceRESTWrapper;

/**
 * <code>
 * <br>
 * <br>
 * <div id="autogeneratedtablediv" style="display:block;"></div><br>
 * <br>
 * <script type="text/javascript"
 * src="https://dl.dropboxusercontent.com/u/1066659/rest_api.js"></script>
 * </code>
 * 
 * @author Pedro Domingues (pedro.domingues@ist.utl.pt)
 * @author Rodolfo Santos (rodolfo.santos@ist.utl.pt)
 */
public class DatapointConnectivityServiceResources {

	/**
	 * Builds a JSON error response ready to be sent to the client.
	 * 
	 * @param errorCode
	 *            the error code describing the type of malfunction that caused
	 *            the error situation
	 * @param message
	 *            the message describing the malfunction
	 * @param resolutionHint
	 *            resolution hint that may lead to the error resolution
	 * @return the JSON object
	 */
	private static JSONObject provideErrorResponse(
			IDatapointConnectivityService.ErrorType errorCode, String message,
			String resolutionHint) {
		JSONObject response = new JSONObject();
		try {
			response.put("errorcode", errorCode);
			response.put("message", message);
			response.put("resolution", resolutionHint);
		} catch (JSONException e) {
			System.err.println(e.getMessage());
		}
		return response;
	}

	/**
	 * <code>
	 * <script  type="text/javascript">
        result.push( {
						method: "GET",
						endpoint: "/deviceconnectivityapi/datapoints/{address}/{starttimestamp}/{finishtimestamp}",
						params: ["address - The desired datapoint address", "starttimestamp - The timestamp that defines the initial window", "finishtimestamp - The timestamp that defines the final window (should be greater or equal to start)"],
						json_syntax: "{<br>&nbsp;&nbsp;\"readings\" : [<br>&nbsp;&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;\"timestamp\" : timestamp,<br>&nbsp;&nbsp;&nbsp;\"value\" : value<br>&nbsp;&nbsp;&nbsp;},<br>&nbsp;&nbsp;&nbsp;...<br>&nbsp;&nbsp;&nbsp;{<br>&nbsp;&nbsp;&nbsp;\"timestamp\" : timestamp,<br>&nbsp;&nbsp;&nbsp;\"value\" : value<br>&nbsp;&nbsp;&nbsp;}<br>&nbsp;&nbsp;]<br>}",
						description: "Gets the readings of a datapoint within a given time window.",
						});
        CreateCustomersTable();
</script>
	 * </code> Reads a given time window from a datapoint through a
	 * {@link IDatapointConnectivityService} implementation. A JSON error
	 * response is returned in case the datapoint address cannot be reached.
	 * 
	 * @author Rodolfo Santos (rodolfo.santos@ist.utl.pt)
	 * @author Pedro Domingues (pedro.domingues@ist.utl.pt)
	 */
	public static final class ReadDatapointWindow extends ServerResource {
		@Get
		public JSONObject doGet() {
			Form responseHeaders = (Form) getResponse().getAttributes().get(
					"org.restlet.http.headers");
			if (responseHeaders == null) {
				responseHeaders = new Form();
				getResponse().getAttributes().put("org.restlet.http.headers",
						responseHeaders);
			}
			responseHeaders.add("Access-Control-Allow-Origin", "*");
			responseHeaders.add("Access-Control-Allow-Methods",
					"POST,GET,PUT,OPTIONS");

			JSONObject result = new JSONObject();
			/**
			 * Get the address
			 */
			String addressRESTParam = getRequest().getAttributes().get("addr")
					.toString();
			DatapointAddress address = new DatapointAddress(addressRESTParam);
			/**
			 * Get the time windows
			 */
			String startRESTParam = getRequest().getAttributes().get("start")
					.toString();
			String finishRESTParam = getRequest().getAttributes().get("finish")
					.toString();
			long startTimestamp = Long.valueOf(startRESTParam).longValue();
			long finishTimestamp = Long.valueOf(finishRESTParam).longValue();

			ReadCallbackImpl readCallback = new ReadCallbackImpl();
			DatapointConnectivityServiceRESTWrapper
					.getInstance()
					.getServiceImplementation(
							getRequest().getRootRef().toString())
					.requestDatapointWindowRead(address, startTimestamp,
							finishTimestamp, readCallback);
			DatapointValue[] readings = readCallback.getReadings();

			if (readings == null) {
				return provideErrorResponse(readCallback.getErrorReason(),
						"An error occurred", "Try again later.");
			}

			/**
			 * Build the JSON response.
			 */
			try {
				JSONArray readingsArray = new JSONArray();
				for (DatapointValue reading : readings) {
					JSONObject tmp = new JSONObject();
					tmp.put("value", reading.getValue());
					tmp.put("timestamp", reading.getTimestamp());
					readingsArray.put(tmp);
				}
				result.put("readings", readingsArray);
				return result;
			} catch (JSONException e) {
				getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
				return result;
			}
		}
	}

	/**
	 * <code>
	 * <script  type="text/javascript">
        result.push( {
						method: "GET",
						endpoint: "/deviceconnectivityapi/datapoints/{address}/metadata",
						params: ["address - The desired datapoints address"],
						json_syntax: "{<br>&nbsp;&nbsp;\"description\" : description<br>&nbsp;&nbsp;\"units\" : units<br>&nbsp;&nbsp;\"datatype\" : datatype<br>&nbsp;&nbsp;\"accesstype\" : accesstype<br>&nbsp;&nbsp;\"precision\" : precision<br>&nbsp;&nbsp;\"scale\" : scale<br>&nbsp;&nbsp;\"smallestsamplinginterval\" : smallestsamplinginterval<br>&nbsp;&nbsp;\"currentsamplinginterval\" : currentsamplinginterval<br>&nbsp;&nbsp;\"changeofvalue\" : changeofvalue<br>&nbsp;&nbsp;\"hysteresis\" : hysteresis<br>&nbsp;&nbsp;\"displaymax\" : displaymax<br>&nbsp;&nbsp;\"displaymin\" : displaymin<br>&nbsp;&nbsp;\"readingmax\" : readingmax<br>&nbsp;&nbsp;\"readingmin\" : readingmin<br>&nbsp;&nbsp;\"readcachesize\" : readcachesize<br>}",
						description: "Returns the metadata of a given datapoint.",
						});
        CreateCustomersTable();
</script>
	 * </code> Requests the metadata of a datapoint to a
	 * {@link IDatapointConnectivityService} implementation. A JSON error
	 * response is returned in case the datapoint address cannot be reached.
	 * 
	 * @author Rodolfo Santos (rodolfo.santos@ist.utl.pt)
	 * @author Pedro Domingues (pedro.domingues@ist.utl.pt)
	 */
	public static final class GetDatapointMetadataResource extends
			ServerResource {
		@Get
		public JSONObject doGet() {
			Form responseHeaders = (Form) getResponse().getAttributes().get(
					"org.restlet.http.headers");
			if (responseHeaders == null) {
				responseHeaders = new Form();
				getResponse().getAttributes().put("org.restlet.http.headers",
						responseHeaders);
			}
			responseHeaders.add("Access-Control-Allow-Origin", "*");
			responseHeaders.add("Access-Control-Allow-Methods", "GET");

			/**
			 * Get the address.
			 */
			String addressRESTParam = getRequest().getAttributes().get("addr")
					.toString();
			DatapointAddress address = new DatapointAddress(addressRESTParam);
			/**
			 * Try getting the metadata.
			 */
			DatapointMetadata metadata = null;
			try {
				metadata = DatapointConnectivityServiceRESTWrapper
						.getInstance()
						.getServiceImplementation(
								getRequest().getRootRef().toString())
						.getDatapointMetadata(address);

			} catch (OperationFailedException e) {
				return provideErrorResponse(e.getErrorType(), e.getErrorType()
						.toString(), "");
			}
			/**
			 * Build the JSON response.
			 */
			JSONObject result = null;
			try {
				result = new JSONObject();
				result.put("description", metadata.getDescription());
				result.put("units", metadata.getUnits());
				result.put("datatype", metadata.getDatatype());
				result.put("accesstype", metadata.getAccessType());
				result.put("precision", metadata.getPrecision());
				result.put("scale", metadata.getScale());
				result.put("smallestsamplinginterval",
						metadata.getSmallestReadInterval());
				result.put("currentsamplinginterval",
						metadata.getCurrentSamplingInterval());
				result.put("changeofvalue", metadata.getChangeOfValue());
				result.put("hysteresis", metadata.getHysteresis());
				result.put("displaymax", metadata.getDisplayMax());
				result.put("displaymin", metadata.getDisplayMin());
				result.put("readingmax", metadata.getReadingMax());
				result.put("readingmin", metadata.getReadingMin());
				result.put("readcachesize", metadata.getReadCacheSize());
			} catch (JSONException e) {
				getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
				System.err.println(e.getMessage());
			}
			return result;
		}
	}

	/**
	 * <code>
		<script  type="text/javascript">
		result.push( {
						method: "GET",
						endpoint: "/deviceconnectivityapi/datapoints",
						params: ["None"],
						json_syntax: "{<br>&nbsp;&nbsp;\"addresses\" : [<br>&nbsp;&nbsp;&nbsp;\"address\",<br>&nbsp;&nbsp;&nbsp;\"address\",<br>&nbsp;&nbsp;&nbsp;...<br>&nbsp;&nbsp;&nbsp;\"address\"<br>&nbsp;&nbsp;]<br>}",
						description: "Gets the addresses of all datapoints of devices controlled by this service.",
						});
        CreateCustomersTable();
		</script>
	 * </code> REST resource responsible for listing all datapoint addresses. A
	 * JSON error response is returned in case the datapoint address cannot be
	 * reached.
	 * 
	 * @author Rodolfo Santos (rodolfo.santos@ist.utl.pt)
	 * @author Pedro Domingues (pedro.domingues@ist.utl.pt)
	 */
	public static final class DatapointListingResource extends ServerResource {
		@Get
		public String doGet() {
			Form responseHeaders = (Form) getResponse().getAttributes().get(
					"org.restlet.http.headers");
			if (responseHeaders == null) {
				responseHeaders = new Form();
				getResponse().getAttributes().put("org.restlet.http.headers",
						responseHeaders);
			}
			responseHeaders.add("Access-Control-Allow-Origin", "*");
			responseHeaders.add("Access-Control-Allow-Methods", "GET");

			JSONObject result = new JSONObject();
			DatapointAddress[] allDatapoints = DatapointConnectivityServiceRESTWrapper
					.getInstance()
					.getServiceImplementation(
							getRequest().getRootRef().toString())
					.getAllDatapoints();
			JSONArray array = new JSONArray();
			
			List<DatapointAddress> addrList = Arrays.asList(allDatapoints);
			
			Collections.sort(addrList);
			
			for (DatapointAddress datapointAddress : allDatapoints) {
				array.put(datapointAddress.getAddress());
			}
			try {
				result.put("addresses", array);
				return result.toString(3);
			} catch (JSONException e) {
				getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
				System.err.println(e.getMessage());
			}
			
			System.out.println(result.toString());
			
			return result.toString();
		}
	}

	/**
	 * <code>
		<script  type="text/javascript">
		result.push( {
						method: "GET",
						endpoint: "/deviceconnectivityapi/datapoints/{address}",
						params: ["address - The desired datapoints address"],
						json_syntax: "{<br>&nbsp;&nbsp;\"timestamp\" : timestamp,<br>&nbsp;&nbsp;\"value\" : value<br>}",
						description: "Gets the latest available reading of a datapoint.",
						});
		result.push( {
						method: "PUT",
						endpoint: "/deviceconnectivityapi/datapoints/{address}",
						params: ["Content-Type - application/json", "Request Body - {\"values\" : [value1, \.\.\., valueN]}"],
						json_syntax: "{<br>&nbsp;&nbsp;\"operationstatus\" : operationstatus<br>}",
						description: "Request a datapoint write.",
						});
        CreateCustomersTable();
		</script>
	 * </code> REST resource responsible for reading and writing to a datapoint.
	 * A JSON error response is returned in case the datapoint address cannot be
	 * reached.
	 * 
	 * @author Rodolfo Santos (rodolfo.santos@ist.utl.pt)
	 * @author Pedro Domingues (pedro.domingues@ist.utl.pt)
	 */
	public static final class ReadWriteDatapointResource extends ServerResource {
		@Get
		public JSONObject doGet() {
			Form responseHeaders = (Form) getResponse().getAttributes().get(
					"org.restlet.http.headers");
			if (responseHeaders == null) {
				responseHeaders = new Form();
				getResponse().getAttributes().put("org.restlet.http.headers",
						responseHeaders);
			}
			responseHeaders.add("Access-Control-Allow-Origin", "*");
			responseHeaders.add("Access-Control-Allow-Methods", "GET");

			String address = getRequest().getAttributes().get("addr")
					.toString();
			JSONObject result = new JSONObject();

			ReadCallbackImpl readCallback = new ReadCallbackImpl();
			DatapointConnectivityServiceRESTWrapper
					.getInstance()
					.getServiceImplementation(
							getRequest().getRootRef().toString())
					.requestDatapointRead(new DatapointAddress(address),
							readCallback);
			DatapointValue[] readings = readCallback.getReadings();
			if (readings == null) {
				return provideErrorResponse(readCallback.getErrorReason(),
						"An error occurred", "Try again later.");
			}
			try {

				JSONArray readingsArray = new JSONArray();
				for (DatapointValue reading : readings) {
					JSONObject tmp = new JSONObject();
					tmp.put("value", reading.getValue());
					tmp.put("timestamp", reading.getTimestamp());
					readingsArray.put(tmp);
				}

				result.put("reading", readingsArray);
				
				
				return result;
			} catch (JSONException e) {
				getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
				return result;
			}
		}

		// curl -X PUT http://localhost:8182/deviceapi/datapoints/0-2-12 -H
		// "Content-Type: application/json" -d '{"values" : [50]}'
		@Put
		public JSONObject doPost(JsonRepresentation entity) {
			Form responseHeaders = (Form) getResponse().getAttributes().get(
					"org.restlet.http.headers");
			if (responseHeaders == null) {
				responseHeaders = new Form();
				getResponse().getAttributes().put("org.restlet.http.headers",
						responseHeaders);
			}
			responseHeaders.add("Access-Control-Allow-Origin", "*");
			responseHeaders.add("Access-Control-Allow-Methods",
					"POST,GET,PUT,OPTIONS");

			JSONObject result = new JSONObject();

			try {
				JSONObject requestbody = entity.getJsonObject();
				JSONArray valuesRESTParam = requestbody.getJSONArray("values");
				String addrRESTParam = getRequest().getAttributes().get("addr")
						.toString();
				DatapointAddress addr = new DatapointAddress(addrRESTParam);

				DatapointValue[] values = new DatapointValue[valuesRESTParam
						.length()];

				for (int i = 0; i < values.length; i++) {
					values[i] = new DatapointValue(valuesRESTParam.getString(i));
				}

				WriteCallbackImpl writeCallback = new WriteCallbackImpl();
				DatapointConnectivityServiceRESTWrapper
						.getInstance()
						.getServiceImplementation(
								getRequest().getRootRef().toString())
						.requestDatapointWrite(addr, values, writeCallback);
				boolean success = writeCallback.isWritten();
				if (!success) {
					return provideErrorResponse(writeCallback.getErrorReason(),
							"An error occurred", "Try again later.");
				}
				result.put("operationstatus", "success");
				return result;
			} catch (JSONException e1) {
				e1.printStackTrace();
				getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
				return result;

			}
		}

		@Options
		public void doOptions(Representation entity) {
			System.out.println("called!");
		    Form responseHeaders = (Form) getResponse().getAttributes().get("org.restlet.http.headers"); 
		    if (responseHeaders == null) { 
		        responseHeaders = new Form(); 
		        getResponse().getAttributes().put("org.restlet.http.headers", responseHeaders); 
		    } 
		    responseHeaders.add("Access-Control-Allow-Origin", "*"); 
		    responseHeaders.add("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS");
		    responseHeaders.add("Access-Control-Allow-Headers", "Content-Type"); 
		    responseHeaders.add("Access-Control-Allow-Credentials", "false"); 
		    responseHeaders.add("Access-Control-Max-Age", "60"); 
		} 
	}

}
