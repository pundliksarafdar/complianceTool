package com.compli.rest;

import java.awt.List;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.compli.annotation.Authorised;
import com.compli.bean.RegisterBean;
import com.compli.bean.UserBean;
import com.compli.managers.AuthorisationManager;
import com.compli.managers.NotificationManager;
import com.compli.managers.RegistrationManager;

@Path("/company")
@Produces(MediaType.APPLICATION_JSON)
public class CompaniesRestApi extends Application{
	@GET
	@Path("/test")
	//@Authorised(role=Authorised.ROLE.ALL)
	public Response test(){
		AuthorisationManager authorisationManager = new AuthorisationManager();
		authorisationManager.loginData("", "");
		return Response.ok(new HashMap()).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@GET
	public Response getUser(@HeaderParam("auth")String auth) throws ExecutionException{
		AuthorisationManager authorisationManager = new AuthorisationManager();
		System.out.println("auth....."+auth);
		return Response.ok(authorisationManager.getUserCatche(auth)).header("Access-Control-Allow-Origin", "*").build();
	}
	
	}

