package com.compli.managers;

import com.notifier.Mail;
import com.notifier.SendMailSSL;

public class EmailManager {
	public static void sendEmail(String to,String subject,String emailTemplate,Object mailContent){
		String mailToSend = Mail.formMailContent(emailTemplate, mailContent);
		SendMailSSL.sendEmail(to,subject,mailToSend);
	}
	
	//Send main to user and manager
	public static void sendActivityPendingForDescripancy(String activityId){
		sendEmail("sarafdarpundlik@gmail.com", "Activity pending for discrepancy", "pendingForDiscrepancy.mustache", new Object());
	}
}
