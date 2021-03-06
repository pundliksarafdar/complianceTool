package com.compli.rest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MimeMultipartProvider;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.resteasy.util.Base64.InputStream;

import com.compli.annotation.Authorised;
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
	    File file = new File(UPLOADED_FILE_PATH);
	    if(!file.exists()){
	    	file.mkdirs();
	    }
	}
	
	@GET
	@Path("/{companyId}/{activityId}")
	public Response getFilesForActivityForCompany(
			@PathParam("companyId")String companyId,@PathParam("activityId")String activityId
			){
		StorageManager storageManager = new StorageManager();
		return Response.ok(storageManager.getFilesForActivity(activityId, companyId)).build();
	}

	@DELETE
	@Path("/{companyId}/{activityId}/{fileId}")
	@Authorised(role=Authorised.ROLE.ALL)
	public Response removeFileForActivityForCompany(
			@PathParam("companyId")String companyId,@PathParam("activityId")String activityId,@PathParam("fileId")String fileId
			){
		StorageManager storageManager = new StorageManager();
		return Response.ok(storageManager.markFileAsDeleted(fileId)).build();
	}
	
	@GET
	@Path("/download/{fileId}")
	//@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadFile(@PathParam("fileId")String fileId) throws IOException{
		StorageManager storageManager = new StorageManager();
		List<Files> files = storageManager.getFileById(fileId);
		ByteArrayOutputStream oStream = StorageManager.downloadFile(fileId);
		//File file = new File(UPLOADED_FILE_PATH+File.separator+files.get(0).getFilename());
		//System.out.println(file.getAbsolutePath());
		
		StreamingOutput stream = new StreamingOutput() {
	        public void write(OutputStream output) throws IOException, WebApplicationException {
	            try {
	                output.write(oStream.toByteArray());
	            }
	            catch (Exception e) {
	                throw new WebApplicationException(e);
	            }
	        }
	 };
		return Response.ok(stream,MediaType.APPLICATION_OCTET_STREAM).header("Content-Disposition","attachment; filename="+files.get(0).getFilename()).build();
	}
	
	@POST
	@Path("/upload/{companyId}/{activityId}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(@PathParam("companyId")String companyId,@PathParam("activityId")String activityId,MultipartFormDataInput  input){
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("uploadedFile");
		HashMap<String, String>statusMap = new HashMap<>();
		for (InputPart inputPart : inputParts) {
			 try {
				MultivaluedMap<String, String> header = inputPart.getHeaders();
				String fileName = getFileName(header);
				System.out.println(fileName);
				
				//convert the uploaded file to inputstream
				java.io.InputStream inputStream = inputPart.getBody(java.io.InputStream.class,null);

				byte [] bytes = IOUtils.toByteArray(inputStream);
				String mimeType = inputPart.getMediaType().toString();
				//constructs upload file path
				String filePath = UPLOADED_FILE_PATH+File.separator + fileName;
					
				writeFile(bytes,filePath);
				
				//upload to google drive
				File file = new File(filePath);
				String fileId = StorageManager.saveFileOnDrive(mimeType, fileName, file);
				if(fileId != null){
					StorageManager storageManager = new StorageManager();
					storageManager.saveFile(activityId, companyId, fileId, fileName);
					statusMap.put("uploaded", "yes");
				}else{
					statusMap.put("uploaded", "no");
				}
				
				if(file.exists()){
					file.delete();
				}
			 }catch(Exception e){
				 e.printStackTrace();
			 }
		} 
		
		return Response.ok(statusMap).build();
	}
	
	@GET
	@Path("/listAllFiles")
	public Response getAllUploadedFiles() throws IOException{
		HashMap<String, Object>fileData = new HashMap<String, Object>();
		String filePath = UPLOADED_FILE_PATH;
		File file = new File(filePath);
		File[] files = file.listFiles();
		List<String> alFiles = new ArrayList<String>();
		if(null!=files){
			for(File file2:files){
				alFiles.add(file2.getName());
			}
			fileData.put("files", alFiles);
			fileData.put("filecount", alFiles.size());
			fileData.put("filesLocation", file.getCanonicalFile());
			}
		return Response.ok(fileData).build();
	}
	
	@GET
	@Path("/listAllFilesDrive")
	public Response getAllUploadedFilesOnDrive() throws IOException{
		StorageManager manager = new StorageManager();
		List files = manager.getAllFilesOnGoogleDrive();
		return Response.ok(files).build();
	}

	@POST
	@Path("/uploadSimply")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadSimplyFile(MultipartFormDataInput  input){
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("uploadedFile");
		for (InputPart inputPart : inputParts) {
			 try {
				MultivaluedMap<String, String> header = inputPart.getHeaders();
				String mimeType = inputPart.getMediaType().toString();
				String fileName = getFileName(header);
				System.out.println(fileName);
				
				//convert the uploaded file to inputstream
				java.io.InputStream inputStream = inputPart.getBody(java.io.InputStream.class,null);

				byte [] bytes = IOUtils.toByteArray(inputStream);
					
				//constructs upload file path
				String filePath = UPLOADED_FILE_PATH+File.separator + fileName;
				//save file temperorily	
				writeFile(bytes,filePath);
				//upload to google drive
				File file = new File(filePath);
				String fileId = StorageManager.saveFileOnDrive(mimeType, fileName, file);
				System.out.println(fileId);
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
		System.out.println(file.getCanonicalPath());
		FileOutputStream fop = new FileOutputStream(file);

		fop.write(content);
		fop.flush();
		fop.close();

	}
}
