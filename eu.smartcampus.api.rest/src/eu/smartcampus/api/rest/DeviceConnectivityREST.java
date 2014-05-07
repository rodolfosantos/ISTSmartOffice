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
 * Wrapper of a {@link IDatapointConnectivityService} that creates a REST/JSON
 * interface to use the provided services.
 * 
 * <TABLE BORDER="1" WIDTH="100%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
 * <TR BGCOLOR="#FFCC99" CLASS="TableHeadingColor" style="background:#FFCC99;">
 * <TH ALIGN="left" COLSPAN="5"><FONT SIZE="+2"> <B>REST API Summary</B></FONT></TH>
 * </TR>
 * <TR BGCOLOR="white" CLASS="TableRowColor">
 * <TD align="center">
 * <b>Method</b></TD>
 * <TD align="center">
 * <b>Endpoint</b></TD>
 * <TD align="center">
 * <b>Inputs</b></TD>
 * <TD align="center">
 * <b>JSON Output</b></TD>
 * <TD align="center">
 * <b>Implementation</b></TD>
 * </TR>
 * 
 * <!-- Read Datapoint Row -->
 * 
 * <TR BGCOLOR="white" CLASS="TableRowColor">
 * <TD align="center">
 * GET</TD>
 * <TD align="left">
 * /deviceapi/readdatapoint/{addr}</TD>
 * <TD align="left">
 * <ul>
 * <li>addr - The desired datapoint's address.</li>
 * </ul>
 * </TD>
 * <TD align="left">
 * 
 * <pre>
 * &#123;
 * 	"value" : value,
 * 	"timestamp" : timestamp
 * &#125;
 * </pre>
 * 
 * </TD>
 * <TD align="left">
 * {@link DeviceConnectivityREST#readDatapoint(String)}</TD>
 * </TR>
 * 
 * <!-- Get Datapoint Metadata Row -->
 * 
 * <TR BGCOLOR="white" CLASS="TableRowColor">
 * <TD align="center">
 * GET</TD>
 * <TD align="left">
 * /deviceapi/getdatapointmetadata/{addr}</TD>
 * <TD align="left">
 * <ul>
 * <li>addr - The desired datapoint's address.</li>
 * </ul>
 * </TD>
 * <TD align="left">
 * 
 * <pre>
 * &#123;
 * 	"units" : units,
 * 	"datatype" : datatype,
 * 	"accesstype" : accesstype,
 * 	"precision" : precision,
 * 	"scale" : scale,
 * 	"smallestsamplinginterval" : smallestsamplinginterval,
 * 	"currentsamplinginterval" : currentsamplinginterval,
 * 	"changeofvalue" : changeofvalue,
 * 	"hysterisys" : hysterisys,
 * 	"displaymax" : displaymax,
 * 	"displaymin" : displaymin,
 * 	"readingmax" : readingmax,
 * 	"readingmin" : readingmin,
 * 	"readcachesize" : readcachesize 
 * &#125;
 * </pre>
 * 
 * </TD>
 * <TD align="left">
 * {@link DeviceConnectivityREST#getDatapointMetadata(String)}</TD>
 * </TR>
 * 
 * <!-- Read Datapoint Window Row -->
 * 
 * <TR BGCOLOR="white" CLASS="TableRowColor">
 * <TD align="center">
 * GET</TD>
 * <TD align="left">
 * /readdatapointwindow/{addr}/{starttimestamp}/{finishtimestamp}</TD>
 * <TD align="left">
 * <ul>
 * <li>addr - The desired datapoint's address.</li>
 * <li>starttimestamp - The timestamp that defines the initial window.</li>
 * <li>finishtimestamp - The timestamp that defines the final window. Should be
 * greater or equal to start.</li>
 * </ul>
 * </TD>
 * <TD align="left">
 * 
 * <pre>
 * &#123;
 * 	&#123;
 * 		"value" : value,
 * 		"timestamp" : timestamp
 * 	&#125;,
 * 	&#123;
 * 		"value" : value,
 * 		"timestamp" : timestamp
 * 	&#125;,
 * 		(...)
 * 	&#123;
 * 		"value" : value,
 * 		"timestamp" : timestamp
 * 	&#125;
 * &#125;
 * </pre>
 * 
 * </TD>
 * <TD align="left">
 * {@link DeviceConnectivityREST#readDatapointWindow(String, String, String)}</TD>
 * </TR>
 * 
 * <!-- Get All Datapoints Row -->
 * 
 * <TR BGCOLOR="white" CLASS="TableRowColor">
 * <TD align="center">
 * GET</TD>
 * <TD align="left">
 * /getalldatapoints/</TD>
 * <TD align="center">
 * None.</TD>
 * <TD align="left">
 * 
 * <pre>
 * &#123;
 * 	&#123;
 * 		"address" : address
 * 	&#125;,
 * 	&#123;
 * 		"address" : address
 * 	&#125;,
 * 		(...)
 * 	&#123;
 * 		"address" : address
 * 	&#125;
 * &#125;
 * </pre>
 * 
 * </TD>
 * <TD align="left">
 * {@link DeviceConnectivityREST#getAllDatapoints()}</TD>
 * </TR>
 * 
 * <!-- Write Datapoint Row -->
 * 
 * <TR BGCOLOR="white" CLASS="TableRowColor">
 * <TD align="center">
 * POST</TD>
 * <TD align="left">
 * /writedatapoint/</TD>
 * <TD align="left">
 * <ul>
 * <li>
 * <b>values:</b> The values to be written to the datapoint.</li>
 * <li>
 * <b>addr:</b> The address of the datapoint to write to.</li>
 * </ul>
 * </TD>
 * <TD align="left">
 * 
 * <pre>
 * &#123;
 * 	"operationstatus" : [
 * 				"success",
 * 				"datapointnotfound",
 * 				"deviceconnectionerror",
 * 				"devicenotresponding",
 * 				"devicevanished",
 * 				"devicebusy",
 * 				"gatewayconnectionerror",
 * 				"gatewaynotfound",
 * 				"gatewaynotresponding",
 * 				"gatewaybusy"
 * 			]    
 * &#125;
 * </pre>
 * 
 * </TD>
 * <TD align="left">
 * {@link DeviceConnectivityREST#writeDatapoint()}</TD>
 * </TR>
 * 
 * </TABLE>
 * <br>
 * <br>
 * 
 */
@Path("/deviceapi")
public final class DeviceConnectivityREST {

	private static IDatapointConnectivityService DEVICE_CONNECTIVITY_API = new MeterDatapointServiceImpl();

	private static final RestReadCallback restReadCallback = new RestReadCallback();

	/**
	 * This method wraps a read datapoint action from
	 * {@link IDatapointConnectivityService#requestDatapointRead(DatapointAddress, eu.smartcampus.api.IDatapointConnectivityService.ReadCallback)}
	 * .<br>
	 * 
	 * This method acts as a synchronous blocking call to the datapoint read
	 * service, returning as soon as the read request gets completed. <br>
	 * <p>
	 * <h3 style="color:blue;">REST-JSON Protocol:</h3> <b>Endpoint:</b>
	 * /readdatapoint/{addr}
	 * <p>
	 * <b>GET Parameters</b><br>
	 * <ul>
	 * <li>addr - The desired datapoint's address.</li>
	 * </ul>
	 * <br>
	 * <b>Call examples:</b><br>
	 * <br>
	 * <ul>
	 * <li><b>Reading a datapoint by its IP address:</b>
	 * /deviceapi/readdatapoint/123.123.111.111</li>
	 * <li><b>Reading a KNX datapoint:</b> /deviceapi/readdatapoint/1/2/10</li>
	 * </ul>
	 * <br>
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
	 *            The desired datapoint's address.
	 * @return a JSON response
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

	/**
	 * This method obtains a datapoint's metadata, through the API method
	 * {@link IDatapointConnectivityService#getDatapointMetadata(DatapointAddress)}
	 * .
	 * <p>
	 * <h3 style="color:blue;">REST-JSON Protocol:</h3> <b>Endpoint:</b>
	 * /getdatapointmetadata/{addr}
	 * <p>
	 * <b>GET Parameters</b><br>
	 * <ul>
	 * <li>addr - The desired datapoint's address.</li>
	 * </ul>
	 * <br>
	 * <b>Call examples:</b><br>
	 * <br>
	 * <ul>
	 * <li><b>Getting a datapoint metadata by its IP address:</b>
	 * /deviceapi/getdatapointmetadata/123.123.111.111</li>
	 * <li><b>Metadata of a KNX datapoint:</b>
	 * /deviceapi/getdatapointmetadata/1/2/10</li>
	 * </ul>
	 * <br>
	 * <b>JSON Response:</b><br>
	 * 
	 * <pre>
	 * &#123;
	 * 	"units" : units,
	 * 	"datatype" : datatype,
	 * 	"accesstype" : accesstype,
	 * 	"precision" : precision,
	 * 	"scale" : scale,
	 * 	"smallestsamplinginterval" : smallestsamplinginterval,
	 * 	"currentsamplinginterval" : currentsamplinginterval,
	 * 	"changeofvalue" : changeofvalue,
	 * 	"hysterisys" : hysterisys,
	 * 	"displaymax" : displaymax,
	 * 	"displaymin" : displaymin,
	 * 	"readingmax" : readingmax,
	 * 	"readingmin" : readingmin,
	 * 	"readcachesize" : readcachesize 
	 * &#125;
	 * </pre>
	 * 
	 * <b>Response Example:</b><br>
	 * 
	 * <pre>
	 * &#123;
	 * 	"units" : "VOLTAGE",
	 * 	"datatype" : "INTEGER",
	 * 	"accesstype" : "readonly",
	 * 	"precision" : "3",
	 * 	"scale" : "3",
	 * 	"smallestsamplinginterval" : "300",
	 * 	"currentsamplinginterval" : "2000",
	 * 	"changeofvalue" : "0.001",
	 * 	"hysterisys" : "0",
	 * 	"displaymax" : "280",
	 * 	"displaymin" : "100",
	 * 	"readingmax" : "999",
	 * 	"readingmin" : "0",
	 * 	"readcachesize" : "128" 
	 * &#125;
	 * </pre>
	 * 
	 * @param addr
	 *            The desired datapoint's address.
	 * @return a JSON response
	 */
	@GET
	@Path("/getdatapointmetadata/{addr}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getDatapointMetadata(@PathParam("addr") String addr) {
		return null;
	}

	/**
	 * This method wraps a read datapoint window action from
	 * {@link IDatapointConnectivityService#requestDatapointWindowRead(DatapointAddress, java.security.Timestamp, java.security.Timestamp, eu.smartcampus.api.IDatapointConnectivityService.ReadCallback)}
	 * .<br>
	 * This method acts as a synchronous blocking call to the datapoint read
	 * service, returning as soon as the read request gets completed.
	 * <p>
	 * <h3 style="color:blue;">REST-JSON Protocol:</h3> <b>Endpoint:</b>
	 * /readdatapointwindow/{addr}/{starttimestamp}/{finishtimestamp}
	 * <p>
	 * <b>GET Parameters</b><br>
	 * <ul>
	 * <li>addr - The desired datapoint's address.</li>
	 * <li>starttimestamp - The timestamp that defines the initial window.</li>
	 * <li>finishtimestamp - The timestamp that defines the final window. Should
	 * be greater or equal to start.</li>
	 * </ul>
	 * <br>
	 * 
	 * <b>Call examples:</b><br>
	 * <br>
	 * <ul>
	 * <li><b>Reading a datapoint by its IP address:</b>
	 * /deviceapi/readdatapoint/123.123.111.111/12:33:08 25/11/2013/12:33:08
	 * 25/11/2014</li>
	 * <li><b>Reading a KNX datapoint:</b>
	 * /deviceapi/readdatapoint/1/2/10/12:33:08 25/11/2013/12:33:08 25/11/2014</li>
	 * </ul>
	 * <br>
	 * <b>JSON Response:</b><br>
	 * 
	 * <pre>
	 * &#123;
	 * 	&#123;
	 * 		"value" : value,
	 * 		"timestamp" : timestamp
	 * 	&#125;,
	 * 	&#123;
	 * 		"value" : value,
	 * 		"timestamp" : timestamp
	 * 	&#125;,
	 * 		(...)
	 * 	&#123;
	 * 		"value" : value,
	 * 		"timestamp" : timestamp
	 * 	&#125;
	 * &#125;
	 * </pre>
	 * 
	 * <b>Response Example:</b><br>
	 * 
	 * <pre>
	 * &#123;
	 * 	&#123;
	 * 		"value" : "1",
	 * 		"timestamp" : "12:33:08 25/11/2013"
	 * 	&#125;,
	 * 	&#123;
	 * 		"value" : "61",
	 * 		"timestamp" : "15:32:00 26/10/2013"
	 * 	&#125;
	 * &#125;
	 * </pre>
	 * 
	 * @param addr
	 *            The desired datapoint's address.
	 * @return a JSON response
	 */
	@GET
	@Path("/readdatapointwindow/{addr}/{starttimestamp}/{finishtimestamp}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response readDatapointWindow(@PathParam("addr") String addr,
			@PathParam("starttimestamp") String start,
			@PathParam("finishtimestamp") String finish) {
		return null;
	}

	/**
	 * This method obtains all registered datapoints' addresses, through the API
	 * method {@link IDatapointConnectivityService#getAllDatapoints()} .
	 * <p>
	 * <h3 style="color:blue;">REST-JSON Protocol:</h3> <b>Endpoint:</b>
	 * /getalldatapoints/
	 * <p>
	 * <b>GET Parameters:</b><br>
	 * <br>
	 * None.<br>
	 * <br>
	 * <b>Call examples:</b><br>
	 * <ul>
	 * <li><b>Getting all datapoint adresses by its IP address:</b>
	 * /deviceapi/getalldatapoints/</li>
	 * </ul>
	 * <br>
	 * <b>JSON Response:</b><br>
	 * 
	 * <pre>
	 * &#123;
	 * 	&#123;
	 * 		"address" : address
	 * 	&#125;,
	 * 	&#123;
	 * 		"address" : address
	 * 	&#125;,
	 * 		(...)
	 * 	&#123;
	 * 		"address" : address
	 * 	&#125;
	 * &#125;
	 * </pre>
	 * 
	 * <b>Response Example:</b><br>
	 * 
	 * <pre>
	 * &#123;
	 * 	&#123;
	 * 		"address" : 199.121.5.30
	 * 	&#125;,
	 * 	&#123;
	 * 		"address" : 1/15/22
	 * 	&#125;
	 * &#125;
	 * </pre>
	 * 
	 * @return a JSON response
	 */
	@GET
	@Path("/getalldatapoints/")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getAllDatapoints() {
		return null;
	}

	/**
	 * This method performs a datapoint write, through the API method
	 * {@link IDatapointConnectivityService#requestDatapointWrite(DatapointAddress, eu.smartcampus.util.Value[], eu.smartcampus.api.IDatapointConnectivityService.WriteCallback)}
	 * .
	 * <p>
	 * <h3 style="color:blue;">REST-JSON Protocol:</h3> <b>Endpoint:</b>
	 * /writedatapoint/
	 * <p>
	 * <b>POST Parameters:</b><br>
	 * <ul>
	 * <li>
	 * <b>values:</b> The values to be written to the datapoint.</li>
	 * <li>
	 * <b>addr:</b> The address of the datapoint to write to.</li>
	 * </ul>
	 * <br>
	 * <b>Call examples:</b><br>
	 * <br>
	 * <ul>
	 * <li><b>Writing to a KNX datapoint:</b> /deviceapi/writedatapoint/
	 * <ul>
	 * <li>
	 * <b>values:</b> "0xAF,0x3D,0x0F,0x00"</li>
	 * <li>
	 * <b>addr:</b> "1/20/5"</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * <br>
	 * <b>JSON Response:</b><br>
	 * 
	 * <pre>
	 * &#123;
	 * 	"operationstatus" : [
	 * 				"success",
	 * 				"datapointnotfound",
	 * 				"deviceconnectionerror",
	 * 				"devicenotresponding",
	 * 				"devicevanished",
	 * 				"devicebusy",
	 * 				"gatewayconnectionerror",
	 * 				"gatewaynotfound",
	 * 				"gatewaynotresponding",
	 * 				"gatewaybusy"
	 * 			]    
	 * &#125;
	 * </pre>
	 * 
	 * <b>Response Example:</b><br>
	 * 
	 * <pre>
	 * &#123;
	 * 	"operationstatus" : "success"  
	 * &#125;
	 * </pre>
	 * 
	 * @return a JSON response
	 */
	@GET
	@Path("/getalldatapoints/")
	@Produces(MediaType.TEXT_PLAIN)
	public Response writeDatapoint() {
		return null;
	}

	// http://localhost:8080/eu.smartcampus.api.rest/service/deviceapi/readdatapoint/172.20.70.229

	// http://localhost:8080/eu.smartcampus.api.rest/service/deviceapi/addReadListener

}
