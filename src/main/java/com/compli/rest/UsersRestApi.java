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

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UsersRestApi extends Application{
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
	
	@GET
	@Path("/notifications")
	public Response getNotification(@HeaderParam("auth")String auth) throws ExecutionException{
		NotificationManager manager = new NotificationManager(); 
		return Response.ok(manager.getNotification()).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(UserBean userBean) throws ExecutionException{ 
		if(userBean.getUsername().trim().length()==0 || userBean.getPassword().trim().length()==0){
			return Response.status(Status.UNAUTHORIZED).build();
		}else{
			String authId = AuthorisationManager.setUserCache(userBean);
			if(authId==null){
				return Response.status(Status.UNAUTHORIZED).build();
			}
			HashMap<String, Object>  hashMap = new HashMap<>();
			hashMap.put("authKey", authId);
			userBean.setSessionToken(authId);
			com.compli.db.bean.UserBean userBeanFull = AuthorisationManager.getUserCatche(authId);
			AuthorisationManager authorisationManager = new AuthorisationManager();
			hashMap.put("companies", authorisationManager.getCompanies(userBeanFull.getUserId()));
			boolean isUserActive = AuthorisationManager.isUserActive(authId);
			hashMap.put("isUserActive", isUserActive);
			
			String userType = AuthorisationManager.getUserType(authId);
			hashMap.put("userType", userType);
			return Response.ok(hashMap).build();
		}
	}
	
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(RegisterBean registerBean) throws ExecutionException{ 
		System.out.println(registerBean);
		RegistrationManager registrationManager = new RegistrationManager();
		boolean success = registrationManager.registerUser(registerBean.getUserBean());
		if(success){
			registrationManager.addCompany(registerBean.getCompanyBean(),registerBean.getUserBean());
			return Response.ok(registerBean).build();
		}else{
			return Response.notModified().build();
		}
		
	}
	
	@GET
	@Path("/userAvailaible/{username}")
	public Response isUserNameExist(@PathParam("username")String username){
		RegistrationManager registrationManager = new RegistrationManager();
		return Response.ok(registrationManager.isUserExists(username)).build();
	}
	
	@GET
	@Path("/registration/emailvalidation/{registrationId}")
	public Response activateEmail(@PathParam("registrationId") String registrationId) throws URISyntaxException{
		RegistrationManager registrationManager = new RegistrationManager();
		boolean isValid = registrationManager.validateEmail(registrationId);
		URI location = new URI("http://localhost:4200/#/activated");
		return Response.seeOther(location).build();
	}
	
	@POST
	@Path("/resendActivationEmail")
	public Response resendActivationEmail(@HeaderParam("auth")String auth) throws ExecutionException{
		RegistrationManager registrationManager = new RegistrationManager();
		registrationManager.sendActivationLinkFor(auth);
		return Response.accepted().build();
	}
}

