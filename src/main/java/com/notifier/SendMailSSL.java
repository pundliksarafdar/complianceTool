package com.notifier;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.compli.managers.SettingsManager;
import com.compli.services.GoogleServices;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;

public class SendMailSSL {
	final static String username = SettingsManager.getStaticSettings().getMailId();
	final static String password = SettingsManager.getStaticSettings().getMailPass();

	static Properties props = new Properties();
	Session session;
	static MimeMessage message;
	
	static{
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", SettingsManager.getStaticSettings().getSmtpHost());
		props.put("mail.smtp.port", SettingsManager.getStaticSettings().getSmtpPort());
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		
		Session session = Session.getInstance(props,
				  new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				  });
		message = new MimeMessage(session);
	}

	public static com.google.api.services.gmail.model.Message createMessageWithEmail(MimeMessage emailContent)
			throws MessagingException, IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		emailContent.writeTo(buffer);
		byte[] bytes = buffer.toByteArray();
		String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
		com.google.api.services.gmail.model.Message message = new com.google.api.services.gmail.model.Message();
		message.setRaw(encodedEmail);
		return message;
	}
	
	public static void sendEmail(String to,String subject,String content){
		try {
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(to));
			message.setSubject(subject);
			
			message.setContent(content, "text/html; charset=utf-8");
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						com.google.api.services.gmail.model.Message msg = createMessageWithEmail(message);
						GoogleServices.getGmailService().users().messages().send(to,msg);
						//Transport.send(message);
					} catch (IOException | MessagingException e) {
						e.printStackTrace();
					}					
				}
			}).start();

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public void sendRegistrationMail(String registrationId,String userFirstName,String email){
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			//message.setFrom(new InternetAddress("sarafdarpundlik@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(email));
			message.setSubject("Complete registration");
			
			DataObject dataObject = new DataObject();
			dataObject.setUser(userFirstName);
			dataObject.setValidationCode(registrationId);
			
			String someHtmlMessage = Mail.getMailContent(dataObject);
			message.setContent(someHtmlMessage, "text/html; charset=utf-8");
			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void reSendRegistrationMail(String registrationId,String userFirstName,String email){
		try {
			message.setFrom("SarafdarPundlik");
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(email));
			message.setSubject("Complete registration");
			
			DataObject dataObject = new DataObject();
			dataObject.setUser(userFirstName);
			dataObject.setValidationCode(registrationId);
			
			String someHtmlMessage = Mail.getResendMailContent(dataObject);
			message.setContent(someHtmlMessage, "text/html; charset=utf-8");
			//Transport.send(message);
			com.google.api.services.gmail.model.Message msg = createMessageWithEmail(message);
			com.google.api.services.gmail.model.Message sendResult = GoogleServices.getGmailService().users().messages().send("me",msg).execute();

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
