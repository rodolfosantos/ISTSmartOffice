/**
 * 
 */
package eu.smartcampus.api.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import eu.smartcampus.api.IDatapointConnectivityService;
import eu.smartcampus.api.IDatapointConnectivityService.ReadCallback;
import eu.smartcampus.api.impl.MeterDatapointServiceImpl;
import eu.smartcampus.util.DatapointAddress;
import eu.smartcampus.util.Reading;


/**
 * @author Diogo
 */
@Path("/deviceapi")
public final class DeviceConnectivityREST {

    private static IDatapointConnectivityService DEVICE_CONNECTIVITY_API = new MeterDatapointServiceImpl();

    private static final RestReadCallback restReadCallback = new RestReadCallback();

    @GET
    @Path("/readdatapoint/{addr}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response readDatapoint(@PathParam("addr") String addr) {
        Reading reading = null;
        DEVICE_CONNECTIVITY_API.requestDatapointRead(new DatapointAddress(addr), restReadCallback);
        while ((reading = restReadCallback.getReading()) == null)
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        return Response.ok(new Gson().toJson(reading)).build();
    }


    //http://localhost:8080/eu.smartcampus.api.rest/service/deviceapi/addReadListener


}
