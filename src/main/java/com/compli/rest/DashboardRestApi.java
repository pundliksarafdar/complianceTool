package com.compli.rest;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.compli.annotation.Authorised;
import com.compli.annotation.Authorised.ROLE;
import com.compli.managers.DashBoardManager;

@Path("/dashboard")
@Produces(MediaType.APPLICATION_JSON)
public class DashboardRestApi {
	
	@GET
	@Path("/{companyId}")
	@Authorised(role=ROLE.ALL)
	public Response getAllData(@PathParam("companyId")String companyId,@HeaderParam("auth")String auth,@QueryParam("location")String locationId){
		DashBoardManager boardManager = new DashBoardManager(companyId,auth,locationId);
		HashMap<String, Object> dashBoardData = boardManager.getDashBoardData();
		return Response.ok(dashBoardData).build();
	}
}
