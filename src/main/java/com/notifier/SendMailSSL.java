package com.notifier;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailSSL {
	final String username = "pundlikproject@gmail.com";
	final String password = "Gaurav#26993";

	Properties props = new Properties();
	
	public SendMailSSL() {
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");		
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
			message.setFrom(new InternetAddress("sarafdarpundlik@gmail.com"));
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
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("sarafdarpundlik@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(email));
			message.setSubject("Complete registration");
			
			DataObject dataObject = new DataObject();
			dataObject.setUser(userFirstName);
			dataObject.setValidationCode(registrationId);
			
			String someHtmlMessage = Mail.getResendMailContent(dataObject);
			message.setContent(someHtmlMessage, "text/html; charset=utf-8");
			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}