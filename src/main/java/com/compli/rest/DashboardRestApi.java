package com.compli.rest;

import java.util.HashMap;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.compli.annotation.Authorised;
import com.compli.annotation.Authorised.ROLE;
import com.compli.bean.dashboard.DashboardparamsBean;
import com.compli.managers.DashBoardManager;

@Path("/dashboard")
@Produces(MediaType.APPLICATION_JSON)
public class DashboardRestApi {
	
	@POST
	@Path("/{companyId}")
	@Authorised(role=ROLE.ALL)
	public Response getAllData(@PathParam("companyId")String companyId, @HeaderParam("auth")String auth, @QueryParam("location")String locationId, DashboardparamsBean dashboardparamsBean){
		DashBoardManager boardManager = new DashBoardManager(companyId,auth,locationId);
		HashMap<String, Object> dashBoardData = boardManager.getDashBoardData(dashboardparamsBean);
		return Response.ok(dashBoardData).build();
	}
}
