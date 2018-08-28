package com.compli.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MimeMultipartProvider;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.resteasy.util.Base64.InputStream;

import com.compli.db.bean.Files;
import com.compli.managers.StorageManager;
import com.compli.util.UUID;


@Path("/file")
@Produces(MediaType.APPLICATION_JSON)
public class FilesRestApi {
	private static String UPLOADED_FILE_PATH = "documents";
	
	static{
		ResteasyProviderFactory instance=ResteasyProviderFactory.getInstance();
	    RegisterBuiltin.register(instance);
	    instance.registerProvider(MimeMultipartProvider.class);
	}
	@GET
	@Path("/{companyId}/{activityId}")
	public Response getFilesForActivityForCompany(
			@PathParam("companyId")String companyId,@PathParam("activityId")String activityId
			){
		StorageManager storageManager = new StorageManager();
		return Response.ok(storageManager.getFilesForActivity(activityId, companyId)).build();
	}
	
	@GET
	@Path("/download/{fileId}")
	public Response downloadFile(@PathParam("fileId")String fileId){
		StorageManager storageManager = new StorageManager();
		List<Files> files = storageManager.getFileById(fileId);
		File file = new File(UPLOADED_FILE_PATH+File.separator+files.get(0).getFilename());
		System.out.println(file.getAbsolutePath());
		
		return Response.ok(file).header("Content-Disposition","attachment; filename="+files.get(0).getFilename()).build();
	}
	
	@POST
	@Path("/upload/{companyId}/{activityId}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(@PathParam("companyId")String companyId,@PathParam("activityId")String activityId,MultipartFormDataInput  input){
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("uploadedFile");
		for (InputPart inputPart : inputParts) {
			 try {
				MultivaluedMap<String, String> header = inputPart.getHeaders();
				String fileName = getFileName(header);
				System.out.println(fileName);
				
				//convert the uploaded file to inputstream
				java.io.InputStream inputStream = inputPart.getBody(java.io.InputStream.class,null);

				byte [] bytes = IOUtils.toByteArray(inputStream);
					
				//constructs upload file path
				String filePath = UPLOADED_FILE_PATH+File.separator + fileName;
					
				writeFile(bytes,filePath);
				StorageManager storageManager = new StorageManager();
				String fileId = UUID.getUID();
				storageManager.saveFile(activityId, companyId, fileId, fileName);
			 }catch(Exception e){
				 e.printStackTrace();
			 }
		} 
		return Response.ok().build();
	}
	
	private String getFileName(MultivaluedMap<String, String> header) {

		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
		
		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {

				String[] name = filename.split("=");
				
				String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}
		return "unknown";
	}
	
	private void writeFile(byte[] content, String filename) throws IOException {

		File file = new File(filename);

		if (!file.exists()) {
			file.createNewFile();
		}

		FileOutputStream fop = new FileOutputStream(file);

		fop.write(content);
		fop.flush();
		fop.close();

	}
}
