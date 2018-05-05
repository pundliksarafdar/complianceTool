package com.compli.rest;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.compli.managers.ActivityManager;

@Path("activity")
@Produces(MediaType.APPLICATION_JSON)
public class ActivityRestApi {
	
	@GET
	@Path("/{companyId}")
	public Response getAllActivityWithDescription(@PathParam("companyId")String companyId){
		ActivityManager activityManager = new ActivityManager();
		return Response.ok(activityManager.getAllActivitiesWithDescriptionForCompany(companyId)).build();
	}
	
	@POST
	@Path("/changeStatus/{companyId}/{activityId}")
	public Response changeActivityStatus(@PathParam("companyId")String companyId,@PathParam("activityId")String activityId,
			@QueryParam("complied")boolean isComplied){
		ActivityManager activityManager = new ActivityManager();
		return Response.ok(activityManager.changeActivityStatus(companyId, activityId, isComplied)).build();
	}
}
