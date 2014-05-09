package eu.smartcampus.api.rest.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import eu.smartcampus.api.DatapointAddress;
import eu.smartcampus.api.DatapointReading;

public class DatapointConnectivityServiceResources {

    // curl http://localhost:8182/deviceapi/datapoint/all
    public static class DatapointListingResource extends
            ServerResource {
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

    // curl http://localhost:8182/deviceapi/datapoint/172.20.70.233
    public static class ReadWriteDatapointResource extends
            ServerResource {
        @Get
        public JSONObject doGet() {
            String addr = getRequest().getAttributes().get("addr").toString();
            JSONObject result = new JSONObject();

            ReadCallback restReadCallback = new ReadCallback();
            DatapointConnectivityServiceREST.service.requestDatapointRead(
                    new DatapointAddress(addr), restReadCallback);
            DatapointReading reading = restReadCallback.getReading();

            try {
                result.put("value", reading.getValue());
                result.put("timestamp", reading.getTimestamp());
                return result;
            } catch (JSONException e) {
                getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                return result;
            }
        }

        // curl -X PUT http://localhost:8182/deviceapi/datapoint/172.20.70.233
        // -H "Content-Type: application/json" -d '{"values" : [1,2,3]}'
        @Put
        public JSONObject doPost(JsonRepresentation entity) {
            JSONObject result = new JSONObject();

            try {
                JSONObject requestbody = entity.getJsonObject();
                JSONArray values = requestbody.getJSONArray("values");
                String addr = getRequest().getAttributes().get("addr").toString();

                System.out.println(values);
                System.out.println(addr);

                WriteCallback writeCallback = new WriteCallback();
                DatapointConnectivityServiceREST.service.requestDatapointWrite(
                        new DatapointAddress(addr), null, writeCallback);
                boolean success = writeCallback.isWritten();

                if (success)
                    result.put("operationstatus", "success");
                else
                    result.put("operationstatus", "unsuccess");

                return result;

            } catch (JSONException e1) {
                getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                return result;

            }
        }
    }


}
