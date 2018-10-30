package com.compli.util;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class FileUpoadUtil {
	public static void main(String[] args) throws ClientProtocolException, IOException {
		String filesLocation = "C:\\report\\uploads\\";
		File file = new File(filesLocation);
		File[] docs = file.listFiles();
		for(File doc:docs){
			System.out.println(doc.getCanonicalPath());
			uploadFile(doc.getCanonicalPath());
		}
		//
	}
	
	private static void uploadFile(String fileName) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		 try {
		HttpPost httppost = new HttpPost("http://jws-app-indcopli.4b63.pro-ap-southeast-2.openshiftapps.com/ComplianceTool/rest/file/uploadSimply");

        FileBody bin = new FileBody(new File(fileName));
		
		HttpEntity httpEntity = MultipartEntityBuilder.create()
				.addPart("uploadedFile", bin).build();
		
		httppost.setEntity(httpEntity);
		httppost.setHeader("Accept-Encoding", "multipart/form-data");
		//httppost.setHeader("Content-Type", "multipart/form-data");
		 System.out.println("executing request " + httppost.getRequestLine());
         CloseableHttpResponse response = httpClient.execute(httppost);
       try {
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                 System.out.println("Response content length: " +    resEntity.getContentLength());
            }
          EntityUtils.consume(resEntity);
         } finally {
             response.close();
        }
	} finally {
        httpClient.close();
    }
	}
}
