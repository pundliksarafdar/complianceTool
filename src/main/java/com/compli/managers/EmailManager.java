package com.compli.managers;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.compli.bean.notification.EmailBean;
import com.compli.bean.notification.EmailLogBean;
import com.compli.db.bean.migration.v2.ActivityBean;
import com.notifier.Mail;
import com.notifier.SendMailSSL;
import com.notifier.emailbean.PendingActivitiesForMail;
import com.notifier.emailbean.PendingComplainceBean;
import com.notifier.emailbean.PendingForDiscrepancy;

public class EmailManager {
	public static void sendEmail(String to,String subject,String emailTemplate,Object mailContent){
		String mailToSend = Mail.formMailContent(emailTemplate, mailContent);
		SendMailSSL.sendEmail(to,subject,mailToSend);
		NotificationManager notificationManager = new NotificationManager();
		EmailLogBean emailLog = new EmailLogBean(to,subject, mailToSend);
		notificationManager.logEmail(emailLog);
	}
	
	//Send main to user and manager
	public static void sendActivityPendingForDescripancy(PendingForDiscrepancy activityBean){
		String email = SettingsManager.getCManagerEmail(activityBean.getEmail());
		System.out.println("Sending mail to email "+email);
		sendEmail(email, "Activity pending for discrepancy", "pendingForDiscrepancy.mustache", activityBean);
	}
	
	//Send main to user and manager
		public static void sendActivityRequestForReopen(PendingForDiscrepancy activityBean){
			String email = SettingsManager.getArtecEmail(activityBean.getEmail());
			System.out.println("Sending mail to email "+email);
			sendEmail(email, "Request to reopen activity ", "requestToReopen.mustache", activityBean);
		}
		
		//Send reminder email actualMail will be used only for logging
		public static void sendMailForReminder(String subject,PendingComplainceBean pendingComplainceBean,String email, String actualMail){
			System.out.println("Sending mail to email "+email);
			if (SettingsManager.settingsBean.testingmode.equals("true")){
				String mailToSend = Mail.formMailContent("remindersEmail.mustache", pendingComplainceBean);
				EmailLogBean emailLog = new EmailLogBean(email+ "/"+ actualMail, subject, mailToSend);
				NotificationManager notificationManager = new NotificationManager();
				notificationManager.logEmail(emailLog);
			}
			sendEmail(email, subject, "remindersEmail.mustache", pendingComplainceBean);
		}

		public static void sendMailToMultipleUsers(List<EmailBean>emails, String subject, String content,boolean isTestingMode){
			ExecutorService service = Executors.newFixedThreadPool(5);

			emails.forEach(emailBean -> {
				Runnable mailer = new Runnable() {
					@Override
					public void run() {
						SendMailSSL.sendEmail(emailBean.getEmailId(),isTestingMode? subject:emailBean.getName() + subject ,content,true);
					}
				};
				service.execute( mailer);
			});
			service.shutdown();
		}
}
