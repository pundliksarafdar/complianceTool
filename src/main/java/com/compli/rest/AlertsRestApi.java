package com.compli.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.compli.managers.AlertsManager;

@Path("/alerts")
public class AlertsRestApi {

	@Path("/sendNow")
	@POST
	public Response sendAlertsNow(){
		AlertsManager alertsManager = new AlertsManager();
		alertsManager.SendAlerts();
		return Response.ok().build();
	}
}
