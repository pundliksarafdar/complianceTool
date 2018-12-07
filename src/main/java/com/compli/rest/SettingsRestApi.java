package com.compli.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.compli.bean.SettingsBean;
import com.compli.managers.SettingsManager;

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

}
