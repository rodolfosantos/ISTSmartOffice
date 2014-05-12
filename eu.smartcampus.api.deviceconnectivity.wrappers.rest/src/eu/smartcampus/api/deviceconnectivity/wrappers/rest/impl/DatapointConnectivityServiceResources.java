package eu.smartcampus.api.deviceconnectivity.wrappers.rest.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import eu.smartcampus.api.deviceconnectivity.DatapointAddress;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata;
import eu.smartcampus.api.deviceconnectivity.DatapointReading;
import eu.smartcampus.api.deviceconnectivity.DatapointValue;
import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;
import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService.OperationFailedException;
import eu.smartcampus.api.deviceconnectivity.wrappers.rest.DatapointConnectivityServiceRESTWrapper;

public class DatapointConnectivityServiceResources {

    /**
     * Builds a JSON error response ready to be sent to the client.
     * 
     * @param errorCode the error code describing the type of malfunction that caused the
     *            error situation
     * @param message the message describing the malfunction
     * @param resolutionHint resolution hint that may lead to the error resolution
     * @return the JSON object
     */
    private static JSONObject provideErrorResponse(IDatapointConnectivityService.ErrorType errorCode,
                                                   String message,
                                                   String resolutionHint) {
        JSONObject response = new JSONObject();
        try {
            response.put("errorcode", errorCode);
            response.put("message", message);
            response.put("resolution", resolutionHint);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Reads a given time window from a datapoint through a
     * {@link IDatapointConnectivityService} implementation. A JSON error response is
     * returned in case the datapoint address cannot be reached.
     * 
     * @author Rodolfo Santos (rodolfo.santos@ist.utl.pt)
     * @author Pedro Domingues (pedro.domingues@ist.utl.pt)
     */
    public static final class ReadDatapointWindow extends
            ServerResource {
        @Get
        public JSONObject doGet() {
            JSONObject result = new JSONObject();
            /**
             * Get the address
             */
            String addressRESTParam = getRequest().getAttributes().get("addr").toString();
            DatapointAddress address = new DatapointAddress(addressRESTParam);
            /**
             * Get the time windows
             */
            String startRESTParam = getRequest().getAttributes().get("start").toString();
            String finishRESTParam = getRequest().getAttributes().get("finish").toString();
            long startTimestamp = Long.valueOf(startRESTParam).longValue();
            long finishTimestamp = Long.valueOf(finishRESTParam).longValue();


            ReadCallback readCallback = new ReadCallback();
            DatapointConnectivityServiceRESTWrapper.getInstance().getServiceImplementation().requestDatapointWindowRead(
                    address, startTimestamp, finishTimestamp, readCallback);
            DatapointReading[] readings = readCallback.getReadings();

            if (readings == null) {
                return provideErrorResponse(readCallback.getErrorReason(), "An error occurred",
                        "Try again later.");
            }

            /**
             * Build the JSON response.
             */
            try {
                JSONArray readingsArray = new JSONArray();
                for (DatapointReading reading : readings) {
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
     * Requests the metadata of a datapoint to a {@link IDatapointConnectivityService}
     * implementation. A JSON error response is returned in case the datapoint address
     * cannot be reached.
     * 
     * @author Rodolfo Santos (rodolfo.santos@ist.utl.pt)
     * @author Pedro Domingues (pedro.domingues@ist.utl.pt)
     */
    public static final class GetDatapointMetadataResource extends
            ServerResource {
        @Get
        public JSONObject doGet() {
            /**
             * Get the address.
             */
            String addressRESTParam = getRequest().getAttributes().get("addr").toString();
            DatapointAddress address = new DatapointAddress(addressRESTParam);
            /**
             * Try getting the metadata.
             */
            DatapointMetadata metadata = null;
            try {
                metadata = DatapointConnectivityServiceRESTWrapper.getInstance().getServiceImplementation()
                        .getDatapointMetadata(address);
            } catch (OperationFailedException e) {
                return provideErrorResponse(e.getErrorType(), e.getErrorType().toString(), "");
            }
            /**
             * Build the JSON response.
             */
            JSONObject result = null;
            try {
                result = new JSONObject();
                result.put("units", metadata.getUnits());
                result.put("datatype", metadata.getDatatype());
                result.put("accesstype", metadata.getAccessType());
                result.put("precision", metadata.getPrecision());
                result.put("scale", metadata.getScale());
                result.put("smallestsamplinginterval", metadata.getSmallestReadInterval());
                result.put("currentsamplinginterval", metadata.getCurrentSamplingInterval());
                result.put("changeofvalue", metadata.getChangeOfValue());
                result.put("hysteresis", metadata.getHysteresis());
                result.put("displaymax", metadata.getDisplayMax());
                result.put("displaymin", metadata.getDisplayMin());
                result.put("readingmax", metadata.getReadingMax());
                result.put("readingmin", metadata.getReadingMin());
                result.put("readcachesize", metadata.getReadCacheSize());
            } catch (JSONException e) {
                getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                e.printStackTrace();
            }
            return result;
        }
    }


    /**
     * REST resource responsible for listing all datapoint addresses. A JSON error
     * response is returned in case the datapoint address cannot be reached.
     * 
     * @author Rodolfo Santos (rodolfo.santos@ist.utl.pt)
     * @author Pedro Domingues (pedro.domingues@ist.utl.pt)
     */
    public static final class DatapointListingResource extends
            ServerResource {
        @Get
        public JSONObject doGet() {
            JSONObject result = new JSONObject();
            DatapointAddress[] allDatapoints = DatapointConnectivityServiceRESTWrapper.getInstance().getServiceImplementation()
                    .getAllDatapoints();
            JSONArray array = new JSONArray();
            for (DatapointAddress datapointAddress : allDatapoints) {
                array.put(datapointAddress.getAddress());
            }
            try {
                result.put("addresses", array);
                return result;
            } catch (JSONException e) {
                getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                e.printStackTrace();
            }
            return result;
        }
    }


    /**
     * REST resource responsible for reading and writing to a datapoint. A JSON error
     * response is returned in case the datapoint address cannot be reached.
     * 
     * @author Rodolfo Santos (rodolfo.santos@ist.utl.pt)
     * @author Pedro Domingues (pedro.domingues@ist.utl.pt)
     */
    public static final class ReadWriteDatapointResource extends
            ServerResource {
        @Get
        public JSONObject doGet() {
            String address = getRequest().getAttributes().get("addr").toString();
            JSONObject result = new JSONObject();

            ReadCallback readCallback = new ReadCallback();
            DatapointConnectivityServiceRESTWrapper.getInstance().getServiceImplementation().requestDatapointRead(
                    new DatapointAddress(address), readCallback);
            DatapointReading reading = readCallback.getReading();
            if (reading == null) {
                return provideErrorResponse(readCallback.getErrorReason(), "An error occurred",
                        "Try again later.");
            }
            try {
                result.put("value", reading.getValue());
                result.put("timestamp", reading.getTimestamp());
                return result;
            } catch (JSONException e) {
                getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                return result;
            }
        }

        // curl -X PUT http://localhost:8182/deviceapi/datapoints/0-2-12 -H "Content-Type: application/json" -d '{"values" : [50]}'
        @Put
        public JSONObject doPost(JsonRepresentation entity) {
            JSONObject result = new JSONObject();

            try {
                JSONObject requestbody = entity.getJsonObject();
                JSONArray valuesRESTParam = requestbody.getJSONArray("values");
                String addrRESTParam = getRequest().getAttributes().get("addr").toString();
                DatapointAddress addr = new DatapointAddress(addrRESTParam);


                DatapointValue[] values = new DatapointValue[valuesRESTParam.length()];
                //-----
                try {
                    DatapointMetadata m = DatapointConnectivityServiceRESTWrapper.getInstance().getServiceImplementation()
                            .getDatapointMetadata(addr);
                    switch (m.getDatatype()) {
                        case INTEGER:
                            for (int i = 0; i < values.length; i++) {
                                values[i] = new DatapointValue(valuesRESTParam.getInt(i));
                            }
                            break;
                        case STRING:
                            for (int i = 0; i < values.length; i++) {
                                values[i] = new DatapointValue(valuesRESTParam.getString(i));
                            }
                            break;
                        case BOOLEAN:
                            for (int i = 0; i < values.length; i++) {
                                values[i] = new DatapointValue(valuesRESTParam.getBoolean(i));
                            }
                            break;
                    }
                } catch (OperationFailedException e) {
                    return provideErrorResponse(e.getErrorType(), "An error occurred",
                            "Try again later.");
                }

                //-----
                WriteCallback writeCallback = new WriteCallback();
				DatapointConnectivityServiceRESTWrapper.getInstance()
						.getServiceImplementation().requestDatapointWrite(addr,
                        values, writeCallback);
                boolean success = writeCallback.isWritten();
                if (!success) {
                    return provideErrorResponse(writeCallback.getErrorReason(),
                            "An error occurred", "Try again later.");
                }
                result.put("operationstatus", "success");
                return result;
            } catch (JSONException e1) {
                getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                return result;

            }
        }
    }

}