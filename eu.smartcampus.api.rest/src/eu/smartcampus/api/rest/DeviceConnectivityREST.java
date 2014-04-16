/**
 * 
 */
package eu.smartcampus.api.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import eu.smartcampus.api.DeviceConnectivityAPI;
import eu.smartcampus.util.DatapointAddress;
import eu.smartcampus.util.Reading;

/**
 * @author Diogo
 */
@Path("/deviceapi")
public final class DeviceConnectivityREST {

    private static DeviceConnectivityAPI DEVICE_CONNECTIVITY_API;

    public static void setConnectivityAPI(DeviceConnectivityAPI api) {
        DEVICE_CONNECTIVITY_API = api;
    }
    
    @GET
    @Path("/readdatapoint")    
    @Produces(MediaType.TEXT_PLAIN)
    public Reading readDatapoint(DatapointAddress address) {
        System.out.println("readdatapoint chamado");
        Reading res = new Reading(1024, 42);
        return res;
    }


}
