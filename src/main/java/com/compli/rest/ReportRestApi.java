package com.compli.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.compli.managers.ActivityManager;
import com.compli.managers.ReportsManager;

@Path("report")
@Produces(MediaType.APPLICATION_JSON)
public class ReportRestApi {
	
	@GET
	public Response getAllActivityWithDescription(@QueryParam("month")String month,@QueryParam("companyId")String companyId){
		ReportsManager reportsManager = new ReportsManager();
		return Response.ok(reportsManager.getReportsObject(companyId, month)).build();
	}
}
