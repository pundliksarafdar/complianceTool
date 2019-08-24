package com.compli.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.compli.annotation.Authorised;
import com.compli.managers.ActivityManager;
import com.compli.managers.StatsManager;

@Path("/stats")
@Produces(MediaType.APPLICATION_JSON)
public class StatsRestApi extends Application{
	@GET
	@Path("/counts")
	@Authorised(role=Authorised.ROLE.ALL)
	public Response counts(){
		StatsManager manager = new StatsManager();
		return Response.ok(manager.getCounts()).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@POST
	@Path("/updateCount")
	@Authorised(role=Authorised.ROLE.ALL)
	public Response updateCount(){
		ActivityManager activityManager = new ActivityManager();
		activityManager.updateActivities();
		return Response.ok().header("Access-Control-Allow-Origin", "*").build();
	}	
}