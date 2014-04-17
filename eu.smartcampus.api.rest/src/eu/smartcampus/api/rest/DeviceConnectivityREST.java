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

import eu.smartcampus.api.DeviceConnectivityAPI;
import eu.smartcampus.api.impl.DeviceConnectivityImpl;
import eu.smartcampus.util.DatapointAddress;
import eu.smartcampus.util.Reading;


/**
 * @author Diogo
 */
@Path("/deviceapi")
public final class DeviceConnectivityREST {
    
    private static DeviceConnectivityAPI DEVICE_CONNECTIVITY_API = new DeviceConnectivityImpl();
    
    @GET
    @Path("/readdatapoint/{addr}")    
    @Produces(MediaType.TEXT_PLAIN)
    public Response readDatapoint(@PathParam("addr")String addr) {
        Reading reading = DEVICE_CONNECTIVITY_API.readDatapoint(new DatapointAddress(addr));
        return Response.ok(new Gson().toJson(reading)).build();
    }
    
}