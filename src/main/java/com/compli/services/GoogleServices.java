package com.compli.services;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.compli.bean.SettingsBean;
import com.compli.managers.SettingsManager;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.Calendar.Events;
import com.google.api.services.drive.Drive;

public class GoogleServices {
	private static final String APPLICATION_NAME = "Google Calendar API Quickstart";
	private static HttpTransport httpTransport;
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static com.google.api.services.calendar.Calendar client;
	
	static GoogleClientSecrets clientSecrets;
	static GoogleAuthorizationCodeFlow flow;
	static Credential credential;
	static String clientId = "292147606397-1nj7ik7mq3mkvlqv68lgoi8vnjor5fuj.apps.googleusercontent.com";
	static String clientSecret = "aTXw-nMM7gCAzGg4e3wXA1oq";
	static String redirectURI = "/rest/settings/google";
	static Details web;
	
	static{
		web = new Details();
		web.setClientId(clientId);
		web.setClientSecret(clientSecret);
		clientSecrets = new GoogleClientSecrets().setWeb(web);
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String authorise() throws GeneralSecurityException, IOException{
		AuthorizationCodeRequestUrl authorizationUrl;
		Set<String> scope = new HashSet<String>();
		scope.add(CalendarScopes.CALENDAR);
		scope.add("https://www.googleapis.com/auth/drive");
		flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
				scope).setAccessType("offline").build();
	
	authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(SettingsManager.getStaticSettings().getServerName()+redirectURI);
	System.out.println("cal authorizationUrl->" + authorizationUrl);
	return authorizationUrl.build();
	}
	
	public static String authorise(String code) throws GeneralSecurityException, IOException{
		TokenResponse response = flow.newTokenRequest(code).setRedirectUri(SettingsManager.getStaticSettings().getServerName()+redirectURI).execute();
		credential = flow.createAndStoreCredential(response, "userID");
		client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential)
		.setApplicationName(APPLICATION_NAME).build();
		
		saveTokens(response);
		return SettingsManager.getStaticSettings().getServerName();
	}
	
	public static Map<String,String> getAccessToken(String refreshToken, String client_id, String client_secret) throws IOException {
		Map<String,String> tokenMap = new HashMap<String, String>();
	    HttpTransport transport         = new NetHttpTransport();
	    JsonFactory jsonFactory         = new JacksonFactory();
	    GoogleRefreshTokenRequest req = new GoogleRefreshTokenRequest(transport, jsonFactory, refreshToken, client_id, client_secret);
	    GoogleTokenResponse res = req.execute();
	    String accessTokenNew = res.getAccessToken();
	    String refreshTokenNew = res.getRefreshToken();
	    Long expiresSec = res.getExpiresInSeconds();
	    tokenMap.put("accessTokenNew", accessTokenNew);
	    tokenMap.put("refreshTokenNew", refreshTokenNew);
	    tokenMap.put("expiresInSec", expiresSec.toString());
	    return tokenMap;
	}
	
	public static String getAccessToken() throws IOException{
		String accessToken = null;
		SettingsBean settingsBeanNew = SettingsManager.getStaticSettings();
		long expirationTime = settingsBeanNew.getExpirationTime()!=null?Long.parseLong(settingsBeanNew.getExpirationTime()):0;
		if(expirationTime<new Date().getTime()){
			Map<String,String>tokens = getAccessToken(settingsBeanNew.getRefreshToken(), clientId, clientSecret);
			accessToken = tokens.get("accessTokenNew");
			settingsBeanNew.setAccessToken(accessToken);
			settingsBeanNew.setRefreshToken(tokens.get("refreshTokenNew"));
			//set new expiration time
			long expirationTimeNew = (new Date().getTime())+(Long.parseLong(tokens.get("expiresInSec"))*1000)-(60000);
			settingsBeanNew.setExpirationTime(expirationTimeNew+"");
			SettingsManager manager = new SettingsManager();
			manager.saveSettings(settingsBeanNew);
		}else{
			accessToken = settingsBeanNew.getAccessToken();
		}
		return accessToken;
	}
	
	public static void saveTokens(TokenResponse response ){
		SettingsBean settings = SettingsManager.getStaticSettings();
		settings.setAccessToken(response.getAccessToken());
		settings.setRefreshToken(response.getRefreshToken());
		long currTime = new Date().getTime();
		long expireinSec = response.getExpiresInSeconds();
		//-60000 will expire token 1 min before just a buffer time
		long expirationTime = (currTime)+(expireinSec*1000)-(60000);
		settings.setExpirationTime(expirationTime+"");
		SettingsManager manager = new SettingsManager();
		manager.saveSettings(settings);
	}
	
	public static String calendarEvents(){
		String message = null;
		final DateTime date1 = new DateTime("2017-05-05T16:30:00.000+05:30");
		final DateTime date2 = new DateTime(new Date());
		try{
			String accessToken = getAccessToken();
			GoogleCredential googleCredential = new GoogleCredential().setAccessToken(accessToken);
				client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, googleCredential)
						.setApplicationName(APPLICATION_NAME).build();
				Events events = client.events();
				com.google.api.services.calendar.model.Events eventList = events.list("primary").setTimeMin(date1).setTimeMax(date2).execute();
				message = eventList.getItems().toString();
				System.out.println("My:" + eventList.getItems());		
			} catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println("cal message:" + message);
			return message;
	}
	
	public static Calendar getCalendarService() throws IOException{
		String accessToken = getAccessToken();
		GoogleCredential googleCredential = new GoogleCredential().setAccessToken(accessToken);
			client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, googleCredential)
					.setApplicationName(APPLICATION_NAME).build();
		
			return client;
	}
	
	public static Drive getDriveService() throws IOException{
		String accessToken = getAccessToken();
		GoogleCredential googleCredential = new GoogleCredential().setAccessToken(accessToken);
		Drive service = new Drive.Builder(httpTransport, JSON_FACTORY, googleCredential)
        .setApplicationName(APPLICATION_NAME)
        .build();
		return service;
	}

}
