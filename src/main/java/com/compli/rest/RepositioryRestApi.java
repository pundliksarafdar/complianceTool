package com.compli.rest;

import java.util.ArrayList;
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
import com.compli.managers.RepositoryManager;
import com.compli.util.Util;

@Path("repositories")
@Produces(MediaType.APPLICATION_JSON)
public class RepositioryRestApi {
	@GET
	@Path("/{companyId}")
	@Authorised(role=ROLE.ALL)
	public Response getAllActivityWithDescription(@PathParam("companyId")String companyId,
			@QueryParam("activityStatus")List<String> activityStatus,@QueryParam("month")String month,@QueryParam("year")String year,
			@QueryParam("quarter")String quarter, 
			@HeaderParam("auth")String auth,@HeaderParam("location")String location) throws ExecutionException{
		
		RepositoryManager activityManager;
		if(location==null || "all".equals(location)){
			activityManager = new RepositoryManager(auth);
		}else{
			activityManager = new RepositoryManager(auth,location);
		}
		
		//Activity seviarity both means both checkbox in repository is checked and complied in time and delayed
		//all means send all reports
		int fyYear = 0;
		if(year!=null && month != null){
			fyYear = Util.getFinancialYearForMonth(Integer.parseInt(month), year);
		}
		
		List<Map<String, Object>> activities = null;
		if(month != null){
			activities = activityManager.getAllActivitiesWithDescriptionForCompanyWithSeverityForMonthAndYear(companyId, activityStatus, month, fyYear+"");
		}else if(quarter != null){
			activities = activityManager.getAllActivitiesWithDescriptionForCompanyByQuarter(companyId,true,quarter,activityStatus);
		}else if(year != null){
			year = year.split("-")[0];
			activities = activityManager.getAllActivitiesWithDescriptionForCompanyForYear(companyId,year,activityStatus);
		}

		return Response.ok(activities).build();
	}
}

