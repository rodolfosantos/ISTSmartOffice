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
import eu.smartcampus.api.impl.MeterDatapointServiceImpl;
import eu.smartcampus.util.DatapointAddress;
import eu.smartcampus.util.Reading;

/**
 * <h3>Provided REST Service Endpoints:</h3> <b>GET</b>
 * /deviceapi/readdatapoint/{addr}
 * {@link DeviceConnectivityREST#readDatapoint(String)}<br>
 * 
 */
@Path("/deviceapi")
public final class DeviceConnectivityREST {

	private static IDatapointConnectivityService DEVICE_CONNECTIVITY_API = new MeterDatapointServiceImpl();

	private static final RestReadCallback restReadCallback = new RestReadCallback();

	/**
	 * <h3>Description:</h3> This method wraps a read datapoint action from
	 * {@link IDatapointConnectivityService#requestDatapointRead(DatapointAddress, IDatapointConnectivityService.ReadCallback)}
	 * .<br>
	 * This method acts as a synchronous blocking call to the datapoint read
	 * service, returning as soon as the read request gets completed.
	 * <p>
	 * <h3>REST-JSON Protocol:</h3>
	 * <b>GET Parameters</b><br>
	 * <ul>
	 * <li>addr - The desired datapoint's address.</li>
	 * </ul>
	 * <b>Call examples:</b><br>
	 * <ul>
	 * <li>Reading a datapoint by its IP address:
	 * /deviceapi/readdatapoint/123.123.111.111</li>
	 * <li>Reading a KNX datapoint: /deviceapi/readdatapoint/1/2/10</li>
	 * </ul>
	 * <b>JSON Response:</b><br>
	 * 
	 * <pre>
	 * &#123;
	 * 	"value" : value,
	 * 	"timestamp" : timestamp
	 * &#125;
	 * </pre>
	 * 
	 * <b>Response Example:</b><br>
	 * 
	 * <pre>
	 * &#123;
	 * 	"value" : "1",
	 * 	"timestamp" : "12:33:08 25/11/2013"
	 * &#125;
	 * </pre>
	 * 
	 * @param addr
	 * @return
	 */
	@GET
	@Path("/readdatapoint/{addr}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response readDatapoint(@PathParam("addr") String addr) {
		Reading reading = null;
		DEVICE_CONNECTIVITY_API.requestDatapointRead(
				new DatapointAddress(addr), restReadCallback);
		while ((reading = restReadCallback.getReading()) == null)
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		// TODO: actualizar o retorno disto para o que a documentação javadoc
		// indica.
		return Response.ok(new Gson().toJson(reading)).build();
	}

	// http://localhost:8080/eu.smartcampus.api.rest/service/deviceapi/readdatapoint/172.20.70.229

	// http://localhost:8080/eu.smartcampus.api.rest/service/deviceapi/addReadListener

}
