package com.compli.rest;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
	public Response getAllActivityWithDescription(@QueryParam("month")String month,@QueryParam("companyId")String companyId,
			@QueryParam("year")String year,@QueryParam("quarter")String quarter,@HeaderParam("location")String location){
		ReportsManager reportsManager = null;
		if(location==null || "all".equals(location)){
			reportsManager = new ReportsManager();
		}else{
			reportsManager = new ReportsManager(location);
		}
		if(month!=null){
			return Response.ok(reportsManager.getReportsObject(companyId, month)).build();
		}else if(quarter!=null){
			return Response.ok(reportsManager.getReportsObjectByQuarter(companyId, quarter)).build();
		}else{
			year = year.split("-")[0];
			return Response.ok(reportsManager.getReportsObjectByYear(companyId, year)).build();
		}
	}
}
