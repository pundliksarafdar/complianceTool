package com.compli.rest;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.compli.managers.DashBoardManager;

@Path("/dashboard")
@Produces(MediaType.APPLICATION_JSON)
public class DashboardRestApi {
	
	@GET
	@Path("/{companyId}")
	public Response getAllData(@PathParam("companyId")String companyId){
		DashBoardManager boardManager = new DashBoardManager(companyId);
		HashMap<String, Object> dashBoardData = boardManager.getDashBoardData();
		return Response.ok(dashBoardData).build();
	}
}
