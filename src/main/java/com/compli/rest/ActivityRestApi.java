package com.compli.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import com.compli.annotation.Authorised;
import com.compli.annotation.Authorised.ROLE;
import com.compli.bean.ActivityUser;
import com.compli.bean.AddNewActivitiesBean;
import com.compli.bean.ComplyStatusBean;
import com.compli.managers.ActivityManager;
import com.compli.managers.AuthorisationManager;
import com.compli.managers.DataManager;

@Path("activity")
@Produces(MediaType.APPLICATION_JSON)
public class ActivityRestApi {
	
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
		if(null==activitySeverity || activitySeverity.isEmpty() || "undefined".equals(activitySeverity) || "total".equals(activitySeverity))  {
			String userType = AuthorisationManager.getUserCatche(auth).getUserTypeId();
			List<Map<String, Object>> activities = new ArrayList<Map<String,Object>>();
			//if user is ARTerch or sManager add Pending for review as it will contain Peding for descrepancy activities
			List<String>activityStatusLink = new ArrayList<String>();
			activityStatusLink.add("Pending compliance");
			//Total will come from dashboard total pending activity click so do not add pending for review in this case 
			if((userType.equals("sManager") || userType.equals("ArTechUser")) && !"total".equals(activitySeverity)){
				activityStatusLink.add("Pending for review");
				activities = activityManager.getAllActivitiesWithDescriptionForCompanyWithSeverity(companyId,activityStatusLink);
			}else{
				activityStatusLink.add("Pending for Discrepancy");
				activities = activityManager.getAllActivitiesWithDescriptionForCompanyWithSeverity(companyId,activityStatusLink);
			}
			//activities.addAll(activityManager.getAllActivitiesWithDescriptionForCompanyWithSeverity(companyId,"Pending compliance"));
			//activities.addAll(activityManager.getAllActivitiesWithDescriptionForCompanyWithSeverity(companyId,"Pending for review"));
			return Response.ok(activities).build();
				
		}else if("both".equalsIgnoreCase(activitySeverity)){//Both comes from repositories if both are not checked
			List<Map<String, Object>> activityList1 = new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> activityList2 = new ArrayList<Map<String,Object>>();
			List<String>activityStatusLink = new ArrayList<String>();
			activityStatusLink.add("Complied- In time");
			activityList1.addAll(activityManager.getAllActivitiesWithDescriptionForCompanyWithSeverity(companyId,activityStatusLink));
			if(month!=null){
				activityList1 = activityManager.getAllActivitiesWithDescriptionForCompanyWithSeverityForMonth(companyId,"Complied-Delayed",month);
			}else if(year!=null){
				year = year.split("-")[0];
				activityList1 = activityManager.getAllActivitiesWithDescriptionForCompanyWithSeverityForYear(companyId,"Complied-Delayed",year);			
			}else if(quarter!=null){
				activityList1 = activityManager.getAllActivitiesWithDescriptionForCompanyWithSeverityForQuarter(companyId,"Complied-Delayed",quarter);
			}
			
			if(month!=null){
				activityList2 = activityManager.getAllActivitiesWithDescriptionForCompanyWithSeverityForMonth(companyId,"Complied- In time",month);
			}else if(year!=null){
				year = year.split("-")[0];
				activityList2 = activityManager.getAllActivitiesWithDescriptionForCompanyWithSeverityForYear(companyId,"Complied- In time",year);			
			}else if(quarter!=null){
				activityList2 = activityManager.getAllActivitiesWithDescriptionForCompanyWithSeverityForQuarter(companyId,"Complied- In time",quarter);
			}
			activityList1.addAll(activityList2);
			return Response.ok(activityList1).build();
		}else if("all".equalsIgnoreCase(activitySeverity)){
			List<Map<String, Object>> activityList1 = null;
			if(month!=null){
				activityList1 = activityManager.getAllActivitiesWithDescriptionForCompanyForMonth(companyId,month);
			}else if(year!=null){
				year = year.split("-")[0];
				activityList1 = activityManager.getAllActivitiesWithDescriptionForCompanyForYear(companyId,year);			
			}else if(quarter!=null){
				activityList1 = activityManager.getAllActivitiesWithDescriptionForCompanyForQuarter(companyId,quarter);
			}
			return Response.ok(activityList1).build();
		}else if("low".equalsIgnoreCase(activitySeverity) || "medium".equalsIgnoreCase(activitySeverity) || "high".equalsIgnoreCase(activitySeverity) || "all".equalsIgnoreCase(activitySeverity)){
			return Response.ok(activityManager.getAllActivitiesWithDescriptionForCompanyWithRisk(companyId,activitySeverity)).build();
		}else{
			if(month!=null){
				return Response.ok(activityManager.getAllActivitiesWithDescriptionForCompanyWithSeverityForMonth(companyId,activitySeverity,month)).build();
			}else if(year!=null){
				year = year.split("-")[0];
				return Response.ok(activityManager.getAllActivitiesWithDescriptionForCompanyWithSeverityForYear(companyId,activitySeverity,year)).build();
			}else if(quarter!=null){
				return Response.ok(activityManager.getAllActivitiesWithDescriptionForCompanyWithSeverityForQuarter(companyId,activitySeverity,quarter)).build();
			}{
				List<String>activityType = new ArrayList<String>();
				activityType.add(activitySeverity);
				return Response.ok(activityManager.getAllActivitiesWithDescriptionForCompanyWithSeverity(companyId,activityType)).build();				
			}
		}
	}
	
	@POST
	@Path("/changeStatus/{companyId}/{activityId}")
	@Authorised(role=ROLE.ALL)
	public Response changeActivityStatus(@PathParam("companyId")String companyId,@PathParam("activityId")String activityId,
			@QueryParam("complied")boolean isComplied,@QueryParam("pendingComplied")boolean pendingComplied,@QueryParam("compliedInTime")boolean compliedInTime,
			@QueryParam("compliedDelayed")boolean compliedDelayed,@QueryParam("pendingDescrepancy")boolean pendingDescrepancy,@QueryParam("notDue")boolean notDue,
			@HeaderParam("auth")String auth,ComplyStatusBean complyStatusBean){
		ActivityManager activityManager = new ActivityManager(auth);
		return Response.ok(activityManager.changeActivityStatus(companyId, activityId, isComplied,pendingComplied,compliedInTime,compliedDelayed,pendingDescrepancy,notDue,
				complyStatusBean.getRemarks(),complyStatusBean.getCompliedDate())).build();
	}
	
	@GET
	@Path("/document/{documentId}")
	@Authorised(role=ROLE.ALL)
	public Response downloadDocument(@PathParam("documentId")String documentId){
		File file = new File("documents"+File.separator+documentId);
		System.out.println(file.getAbsolutePath());
		if(file.exists()){
			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename="+file.getName());
		    return response.build();
		}else{
			return Response.noContent().build();
		}
	}
	
	@GET
	@Path("/{companyId}/bylawandstatus")
	@Authorised(role=ROLE.ALL)
	public Response getAllActivityWithLawAndStatus(@PathParam("companyId")String companyId,
			@QueryParam("lawArea")String activityLaw,@QueryParam("activitySeverity")String status,
			@HeaderParam("auth")String auth,@HeaderParam("location")String location){
		ActivityManager activityManager;
		if(location==null || "all".equals(location)){
			activityManager = new ActivityManager(auth);
		}else{
			activityManager = new ActivityManager(auth,location);
		}
		return Response.ok(activityManager.getAllActivitiesWithDescriptionForCompanyWithSeverityAndLaw(companyId, status, activityLaw)).build();
	}
	
	@POST
	@Path("/requestToReopen/{companyId}/{activityId}")
	public Response requestToReopen(@PathParam("companyId")String companyId,@PathParam("activityId")String activityId,@HeaderParam("auth")String auth){
		ActivityManager activityManager = new ActivityManager(auth);
		boolean reOpened = activityManager.requestToReopen(activityId, companyId);
		return Response.ok(reOpened).build();
	}
	
	@POST
	@Path("/changeToReopen/{companyId}/{activityId}")
	public Response changeToOpen(@PathParam("companyId")String companyId,@PathParam("activityId")String activityId,@HeaderParam("auth")String auth){
		ActivityManager activityManager = new ActivityManager(auth);
		boolean reOpened = activityManager.changeToOpen(activityId, companyId);
		return Response.ok(reOpened).build();
	}
	
	@POST
	@Path("/addActivitiesToCompany")
	public Response addActivitiesToCompany(AddNewActivitiesBean addNewActivitiesBean){
		DataManager dataManager = new DataManager();
		Map<String, Map<String, List<String>>> errorMap = dataManager.uploadActivities(addNewActivitiesBean);
		if(errorMap==null || errorMap.isEmpty()){
			return Response.ok().build();
		}else{
			return Response.status(Status.BAD_REQUEST).entity(errorMap).build();
		}
	}
	
	@POST
	@Path("/setActivityUser")
	public Response setActivityUser(@HeaderParam("auth")String auth,ActivityUser activityUser){
		ActivityManager activityManager = new ActivityManager(auth);
		activityManager.setActivityUser(activityUser);
		return Response.ok().build();
	}
}
