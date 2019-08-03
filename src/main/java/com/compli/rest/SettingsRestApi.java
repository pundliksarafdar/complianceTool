package com.compli.rest;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.compli.annotation.Authorised;
import com.compli.annotation.Authorised.ROLE;
import com.compli.bean.ChangeDateBean;
import com.compli.bean.SettingsBean;
import com.compli.db.bean.CompanyBean;
import com.compli.db.bean.UserBean;
import com.compli.managers.ActivityManager;
import com.compli.managers.DataManager;
import com.compli.managers.SettingsManager;
import com.compli.managers.StorageManager;
import com.compli.services.GoogleServices;
import com.compli.util.bean.ActivityAssignnmentBean;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.services.calendar.CalendarScopes;

@Path("settings")
@Produces(MediaType.APPLICATION_JSON)
public class SettingsRestApi {
	@GET
	public Response getSettings(){
		SettingsManager settingsManager = new SettingsManager();
		return Response.ok(settingsManager.getStaticSettings()).build();
	}
	
	@POST
	public Response saveSettings(SettingsBean settingsBean){
		SettingsManager settingsManager = new SettingsManager();
		settingsManager.saveSettings(settingsBean);
		return Response.ok(settingsBean).build();
	}

	@GET
	@Path("/google")
	public Response googleUserAuth(@QueryParam("code")String code) throws URISyntaxException, GeneralSecurityException, IOException{
		if(null!=code){	
			return Response.seeOther(new URI(GoogleServices.authorise(code))).build();
		}else{
			return Response.seeOther(new URI(GoogleServices.authorise())).build();
		}
	}
	
	@GET
	@Path("/google/calevent")
	public Response getCalEvent(){
			return Response.ok(GoogleServices.calendarEvents()).build();		
	}
	
	@GET
	@Path("/companies")
	public Response getAllCompanies(){
		SettingsManager settingsManager = new SettingsManager();
		List<CompanyBean> companies = settingsManager.getAllCompanies();
		return Response.ok(companies).build();
	}
	
	@GET
	@Path("/getAllActivities/{companyId}/{year}/{month}")
	public Response getAllActivitiesForCompany(@PathParam("companyId") String companyId,@PathParam("year")String year,@PathParam("month")String month,@HeaderParam("auth")String auth){
		ActivityManager activityManager = new ActivityManager(auth);
		List<Map<String, Object>> activities = activityManager.getAllActivitiesWithDescriptionForCompanyByMonthWithRejected(companyId, month, year);
		return Response.ok(activities).build();
		
	}
	
	@GET
	@Path("/getUserForActivity/{activityId}")
	public Response getUsersForActivity(@PathParam("activityId") String activityId,@HeaderParam("auth")String auth){
		ActivityManager activityManager = new ActivityManager(auth);
		List<UserBean> users = activityManager.getUsersForActivity(activityId);
		return Response.ok(users).build();		
	}
	
	@POST
	@Path("/upload/{companyId}")
	@Authorised(role=ROLE.ALL)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(@PathParam("companyId")String companyId,MultipartFormDataInput  input){
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("uploadedFile");
		List<String> rejectReasons = new ArrayList<String>();
		for (InputPart inputPart : inputParts) {
			 try {
				java.io.InputStream inputStream = inputPart.getBody(java.io.InputStream.class,null);
				DataManager dataManager = new DataManager();
				rejectReasons = dataManager.uploadData(inputStream, companyId);
			}catch(Exception e){
				 e.printStackTrace();
			 }
		} 
		return Response.ok(rejectReasons).build();
	}
	
	@POST
	@Path("/changeDate")
	@Authorised(role=ROLE.ALL)
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response changeDate(ChangeDateBean changeDateBean,@HeaderParam("auth")String auth){
		ActivityManager activityManager = new ActivityManager(auth);
		activityManager.changeDate(changeDateBean);
		return Response.ok().build();
	}
	
	@POST
	@Path("/setUserForActivity")
	@Authorised(role=ROLE.ALL)
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response setUserForActivity(List<ActivityAssignnmentBean> activityAssignnmentBeans,@HeaderParam("auth")String auth){
		ActivityManager activityManager = new ActivityManager(auth);
		activityManager.addUserForActivity(activityAssignnmentBeans);
		return Response.ok().build();
	}
	
	@POST
	@Path("/removeUserForActivity")
	@Authorised(role=ROLE.ALL)
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response removeUserForActivity(ActivityAssignnmentBean activityAssignnmentBean,@HeaderParam("auth")String auth){
		ActivityManager activityManager = new ActivityManager(auth);
		activityManager.removeUserForActivity(activityAssignnmentBean);
		return Response.ok().build();
	}
	
	@POST
	@Path("/removeUserFromCompany")
	@Authorised(role=ROLE.ALL)
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response removeUserFromCompany(Map<String,String>dataMap,@HeaderParam("auth")String auth){
		ActivityManager activityManager = new ActivityManager(auth);
		String userId = dataMap.get("userId");
		String companyId = dataMap.get("companyId");
		activityManager.removeUserFromCompany(userId, companyId);
		return Response.ok().build();
	}
	
	@POST
	@Path("/addUserToCompany")
	@Authorised(role=ROLE.ALL)
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response addUserToCompany(Map<String,String>dataMap,@HeaderParam("auth")String auth){
		ActivityManager activityManager = new ActivityManager(auth);
		String userId = dataMap.get("userId");
		String companyId = dataMap.get("companyId");
		activityManager.addUserToCompany(userId, companyId);
		return Response.ok().build();
	}
}
