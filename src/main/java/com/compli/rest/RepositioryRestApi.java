package com.compli.rest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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
import com.compli.managers.ActivityManager;

@Path("repositories")
@Produces(MediaType.APPLICATION_JSON)
public class RepositioryRestApi {
	@GET
	@Path("/{companyId}")
	@Authorised(role=ROLE.ALL)
	public Response getAllActivityWithDescription(@PathParam("companyId")String companyId,
			@QueryParam("activitySeverity")String activitySeverity,@QueryParam("month")String month,@QueryParam("year")String year,
			@QueryParam("quarter")String quarter, 
			@HeaderParam("auth")String auth,@HeaderParam("location")String location) throws ExecutionException{
		
		ActivityManager activityManager;
		if(location==null || "all".equals(location)){
			activityManager = new ActivityManager(auth);
		}else{
			activityManager = new ActivityManager(auth,location);
		}
		
		//Activity seviarity both means both checkbox in repository is checked and complied in time and delayed
		//all means send all reports
		if(year!=null){
			year = year.split("-")[0];
		}
		
		List<Map<String, Object>> activities = null;
		if("both".equals(activitySeverity)){
			activities = activityManager.getAllActivitiesWithDescriptionForCompanyWithSeverityForMonthAndYear(companyId, "Complied-Delayed", month, year);
			activities.addAll(activityManager.getAllActivitiesWithDescriptionForCompanyWithSeverityForMonthAndYear(companyId, "Complied- In time", month, year));
		}else if("all".equals(activitySeverity)){
			activities = activityManager.getAllActivitiesWithDescriptionForCompanyForMonthAndYear(companyId, month, year);			
		}else{
			activities = activityManager.getAllActivitiesWithDescriptionForCompanyWithSeverityForMonthAndYear(companyId, activitySeverity, month, year);
		}
		return Response.ok(activities).build();
	}

}
