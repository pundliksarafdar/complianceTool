package com.compli.rest;

import java.awt.List;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.compli.annotation.Authorised;
import com.compli.annotation.Authorised.ROLE;
import com.compli.bean.RegisterBean;
import com.compli.bean.UserBean;
import com.compli.bean.access.UserAccess;
import com.compli.db.bean.UsersForCompany;
import com.compli.db.bean.migration.v2.UserCompanyBean;
import com.compli.db.dao.UserCompanyDao;
import com.compli.managers.AuthorisationManager;
import com.compli.managers.NotificationManager;
import com.compli.managers.RegistrationManager;
import com.compli.managers.SettingsManager;
import com.compli.managers.UserManager;
import com.compli.services.GoogleServices;

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

		if(auth == null){
			return Response.status(Status.UNAUTHORIZED).header("Access-Control-Allow-Origin", "*").build();
		}

		com.compli.db.bean.UserBean userCache = authorisationManager.getUserCatche(auth);
		if(userCache == null){
			return Response.status(Status.UNAUTHORIZED).header("Access-Control-Allow-Origin", "*").build();
		}
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

	//This method gives same as login response by session id
	@GET
	@Path("/logindata")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response loginData(@QueryParam("auth")String authParam) throws ExecutionException{
		com.compli.db.bean.UserBean authData = AuthorisationManager.getUserCatche(authParam);
		if(authData==null){
			return Response.status(Status.UNAUTHORIZED).build();
		}
		HashMap<String, Object>  hashMap = new HashMap<>();
		hashMap.put("authKey", authParam);
		com.compli.db.bean.UserBean userBeanFull = authData;
		AuthorisationManager authorisationManager = new AuthorisationManager();
		hashMap.put("companies", authorisationManager.getCompanies(userBeanFull.getUserId()));
		boolean isUserActive = AuthorisationManager.isUserActive(authParam);
		hashMap.put("isUserActive", isUserActive);

		String userType = AuthorisationManager.getUserType(authParam);
		hashMap.put("userType", userType);
		return Response.ok(hashMap).build();
	}
	
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(RegisterBean registerBean) throws ExecutionException{ 
		System.out.println(registerBean);
		RegistrationManager registrationManager = new RegistrationManager();
		boolean success = registrationManager.registerUser(registerBean.getUserBean());
		return Response.ok(success).build();

		/*
		* if(success){
			registrationManager.addCompany(registerBean.getCompanyBean(),registerBean.getUserBean());
			return Response.ok(registerBean).build();
		}else{
			return Response.notModified().build();
		}
		 */
	}
	
	@POST
	@Path("/registeruser")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerUser(RegisterBean registerBean) throws ExecutionException{ 
		System.out.println(registerBean);
		RegistrationManager registrationManager = new RegistrationManager();
		boolean success = registrationManager.registerUser(registerBean.getUserBean());
		return Response.ok(success).build();		
	}
	
	@POST
	@Path("/registeruserformaster/{companyId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerUserForMasterUser(RegisterBean registerBean,@PathParam("companyId")String companyId) throws ExecutionException{ 
		RegistrationManager registrationManager = new RegistrationManager();
		boolean success = registrationManager.insertUserValuesForMaster(registerBean.getUserBean(),companyId);
		return Response.ok(success).build();		
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

	@GET
	@Path("/register/google")
	public Response googleUserAuth(@QueryParam("code")String code) throws URISyntaxException, GeneralSecurityException, IOException, ExecutionException {
		if(null!=code){
			return Response.seeOther(new URI(GoogleServices.authoriseForRegistration(code))).build();
		}else{
			return Response.seeOther(new URI(GoogleServices.authoriseForRegistration())).build();
		}
	}

	@GET
	@Path("/login/google")
	public Response googleUserLogin(@QueryParam("code")String code) throws URISyntaxException, GeneralSecurityException, IOException, ExecutionException {
		if(null!=code){
			return Response.seeOther(new URI(GoogleServices.loginGoogleUser(code))).build();
		}else{
			return Response.seeOther(new URI(GoogleServices.loginGoogleUser())).build();
		}
	}
	
	@POST
	@Path("/updateUser")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(@HeaderParam("auth")String auth,com.compli.db.bean.UserBean userBean) throws ExecutionException{ 
		RegistrationManager registrationManager = new RegistrationManager();
		boolean success = registrationManager.updateUser(auth, userBean);
		return Response.ok().build();		
	}
	
	@GET
	@Path("/allUserForCompany/{companyId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Authorised(role=Authorised.ROLE.ALL)
	public Response getAllUserForCompany(@PathParam("companyId") String companyId){
		SettingsManager settingsManager = new SettingsManager(); 
		java.util.List<UsersForCompany> users = settingsManager.getUserForCompany(companyId);
		return Response.ok(users).build();
	}
	
	@GET
	@Path("/allUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Authorised(role=Authorised.ROLE.ALL)
	public Response getAllUser(){
		UserManager userManager = new UserManager();
		return Response.ok(userManager.getAllUser()).build();
	}
	
	@POST
	@Path("/updateUserForMaster")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUserForMaster(@HeaderParam("auth")String auth,com.compli.db.bean.UserBean userBean) throws ExecutionException{ 
		RegistrationManager registrationManager = new RegistrationManager();
		boolean success = registrationManager.updateUserForMaster(auth, userBean);
		return Response.ok().build();		
	}
	
	@GET
	@Path("/getAllAccess")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getUserAccess(@HeaderParam("auth")String auth,com.compli.db.bean.UserBean userBean) throws ExecutionException{ 
		UserManager userManager = new UserManager();
		if(auth == null ){
			return Response.status(Status.UNAUTHORIZED).build();
		}
		java.util.List<String> access = userManager.getUserAccess(auth);
		return Response.ok(access).build();		
	}
	
	@GET
	@Path("/getAllUsersAccess")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAllUsersAccess(){
		UserManager userManager = new UserManager();
		return Response.ok(userManager.getAllUserAccess()).build();
	}
	
	@POST
	@Authorised(role=ROLE.ALL)
	@Path("/updateUsersAccess")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUsersAccess(UserAccess userAccess){
		UserManager userManager = new UserManager();
		return Response.ok(userManager.updateUserAccess(userAccess)).build();
	}
}
