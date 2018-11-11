package com.compli.rest;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.compli.annotation.Authorised;
import com.compli.annotation.Authorised.ROLE;
import com.compli.db.bean.LocationBean;
import com.compli.managers.LocationManager;

@Path("/location")
@Produces(MediaType.APPLICATION_JSON)
public class LocationRestApi extends Application{
	
	@GET
	@Authorised(role=ROLE.ALL)
	public Response getAllLocation() {
		LocationManager locationManager = new LocationManager();
		return Response.ok(locationManager.getAllLocations()).build();
	}
	
	@POST
	@Authorised(role=ROLE.ALL)
	public Response addLocation(List<LocationBean> locationBeans){
		LocationManager locationManager = new LocationManager();
		return Response.ok(locationManager.addLocation(locationBeans)).build();
	}
	
	@DELETE
	@Path("{locationId}")
	@Authorised(role=ROLE.ALL)
	public Response addLocation(@PathParam("locationId") String locationId){
		LocationManager locationManager = new LocationManager();
		return Response.ok(locationManager.deleteLocation(locationId)).build();
	}
	
	@GET
	@Path("/companylocations/{companyId}")
	@Authorised(role=ROLE.ALL)
	public Response getAllLocationForUser(@PathParam("companyId")String companyId) {
		LocationManager locationManager = new LocationManager();
		return Response.ok(locationManager.getCompanyLocation(companyId)).build();
	}
}
