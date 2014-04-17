package eu.smartcampus.api.rest;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import eu.smartcampus.api.impl.UsersManagerImpl;

@Path("/usersmanager")
public class UsersManagerREST {

	@GET
	@Path("/getuserslist")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserList() {		
		return Response.ok(new Gson().toJson(UsersManagerImpl.getInstance().getUsersList())).build();
	}
	
	
	@POST
	@Path("/adduser")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addUser(@HeaderParam("username") String username, @HeaderParam("password") String password) {	
		boolean res = UsersManagerImpl.getInstance().addUser(username, password);
		return Response.ok(new Gson().toJson(res)).build();
	}
	
	@POST
	@Path("/removeuser")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeUser(@HeaderParam("username") String username) {	
		boolean res = UsersManagerImpl.getInstance().removeUser(username);
		return Response.ok(new Gson().toJson(res)).build();
	}
	
	@POST
	@Path("/checkcredentials")
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkUserName(@HeaderParam("username") String username, @HeaderParam("password") String password) {	
		boolean res = UsersManagerImpl.getInstance().checkCredentials(username, password);
		return Response.ok(new Gson().toJson(res)).build();
	}
	

}