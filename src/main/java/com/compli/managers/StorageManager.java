package com.compli.managers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.http.entity.InputStreamEntity;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.db.bean.Files;
import com.compli.db.dao.FilesDao;
import com.compli.services.GoogleServices;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.common.net.MediaType;

public class StorageManager {
	private static FilesDao filesDao;
	public StorageManager() {
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		filesDao = (FilesDao) ctx.getBean("filesDao");
	}
	
	public List<Map<String, Object>> getFilesForActivity(String activityId, String companyId) {
		return filesDao.getFilesForActivity(activityId, companyId);
	}
	
	public List<Files> getFileById(String fileId) {
		return filesDao.getFileById(fileId);
	}
	
	public boolean markFileAsDeleted(String fileId) {
		return filesDao.markFileAsDeleted(fileId);
	}
	
	public boolean saveFile(String activityId,String companyId,String fileId,String filename) {
		return filesDao.saveFile(activityId, companyId, fileId, filename,new Date());		
	}
	
	public static String saveFileOnDrive(String mimeType,String fileName,java.io.File fileToUpload) throws IOException{
		Drive driveService = GoogleServices.getDriveService();
		File fileMetadata = new File();
		fileMetadata.setName(fileName);
		java.io.File filePath = fileToUpload;
		FileContent mediaContent = new FileContent("application/pdf", filePath);
		File file = driveService.files().create(fileMetadata,mediaContent)
		    .setFields("id")
		    .execute();
		String fileId = file.getId();
		return fileId;
	}
	
	public static java.io.ByteArrayOutputStream downloadFile(String fileId) throws IOException{
		Drive driveService = GoogleServices.getDriveService();
		java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
		driveService.files().get(fileId).executeMediaAndDownloadTo(outputStream);
		return outputStream;
	}
	
	public static List getAllFilesOnGoogleDrive() throws IOException{
		Drive driveService = GoogleServices.getDriveService();
		FileList fileList = driveService.files().list().execute();
		List<File> files = fileList.getFiles();
		return files;
	}
	
	public static void main(String[] args) throws IOException {
		getAllFilesOnGoogleDrive();
	}
}
