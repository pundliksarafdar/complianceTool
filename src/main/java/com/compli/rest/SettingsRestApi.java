package com.compli.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.compli.bean.SettingsBean;
import com.compli.managers.SettingsManager;
import com.compli.services.GoogleServices;
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
}
