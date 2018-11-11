package com.compli.filter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import com.compli.annotation.Authorised;
import com.compli.db.bean.UserBean;
import com.compli.exceptions.SessionExpiredException;
import com.compli.managers.AuthorisationManager;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFIlter implements ContainerRequestFilter{
	 @Context private ResourceInfo resourceInfo;
	public void filter(ContainerRequestContext req) throws IOException {
		Method name = resourceInfo.getResourceMethod();
		 Authorised authorised = name.getAnnotation(Authorised.class);		 
		if(null!=authorised){
			List<String> authToken = req.getHeaders().get("auth");
			if(authToken == null || authToken.isEmpty()){
				req.abortWith(Response.status(Status.UNAUTHORIZED).build());
			}else{
				try {
					UserBean userBean = null;
					try{
						userBean = AuthorisationManager.getUserCatche(authToken.get(0));}catch(SessionExpiredException e){}
					if(null==userBean){
						req.abortWith(Response.status(Status.UNAUTHORIZED).build());
					}
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
