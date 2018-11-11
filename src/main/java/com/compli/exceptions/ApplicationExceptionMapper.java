package com.compli.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<Exception>{

	@Override
	public Response toResponse(Exception ex) {
		Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		if(ex instanceof SessionExpiredException){
			response = Response.status(Response.Status.UNAUTHORIZED).build();
		}
		return response;
	}
	
}
