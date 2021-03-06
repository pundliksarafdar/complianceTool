package com.compli.rest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import com.compli.annotation.Authorised;
import com.compli.annotation.Authorised.ROLE;
import com.compli.managers.ActivityManager;
import com.compli.managers.ReportsManager;

@Path("report")
@Produces(MediaType.APPLICATION_JSON)
public class ReportRestApi {
	
	@GET
	@Authorised(role=ROLE.ALL)
	//mon is used to fetch report year and month
	public Response getAllActivityWithDescription(@QueryParam("month")String month,@QueryParam("mon")String mon,@QueryParam("companyId")String companyId,
			@QueryParam("year")String year,@QueryParam("quarter")String quarter,@HeaderParam("location")String location,
			@HeaderParam("auth")String auth){
		ReportsManager reportsManager = null;
		if(location==null || "all".equals(location)){
			reportsManager = new ReportsManager(auth);
		}else{
			reportsManager = new ReportsManager(location,auth);
		}
		if(month!=null){
			return Response.ok(reportsManager.getReportsObject(companyId, month)).build();
		}else if(quarter!=null){
			return Response.ok(reportsManager.getReportsObjectByQuarter(companyId, quarter)).build();
		}else{
			year = year.split("-")[0];
			return Response.ok(reportsManager.getReportsObjectByYear(companyId, year)).build();
		}
	}
	
	@GET
	@Path("generateReport")
	//@Authorised(role=ROLE.ALL)
	public Response generateReport(@QueryParam("month")String month,@QueryParam("companyId")String companyId,@QueryParam("auth")String auth,
			@QueryParam("year")String year,@QueryParam("quarter")String quarter,@QueryParam("location")String location) throws FileNotFoundException {
		final byte[] fileBytes;
		String filename = null;
		ReportsManager reportsManager = null;
		if(location==null || "all".equals(location)){
			reportsManager = new ReportsManager(auth);
		}else{
			reportsManager = new ReportsManager(location,auth);
		}
		
		if(month!=null){
			Map<String, Object> companyReport = reportsManager.generateReport(companyId, month);
			fileBytes = (byte[]) companyReport.get("pdfFile");
			filename = (String) companyReport.get("filename");
			
		}else if(quarter!=null){
			Map<String, Object> companyReport = reportsManager.generateReportForQuarter(companyId, quarter);
			fileBytes = (byte[]) companyReport.get("pdfFile");
			filename = (String) companyReport.get("filename");
		}else{
			year = year.split("-")[0];
			Map<String, Object> companyReport = reportsManager.generateReportForYear(companyId, year);
			fileBytes = (byte[]) companyReport.get("pdfFile");
			filename = (String) companyReport.get("filename");
		}
		
		if(fileBytes != null){
		StreamingOutput output = new StreamingOutput() {
            @Override
            public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                int length = fileBytes.length;
                outputStream.write(fileBytes, 0, length);
                outputStream.flush();
            }
        };
        
        return Response.ok(output, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + filename + "\"" )
                .build();
		}
		return null;
	}
}
