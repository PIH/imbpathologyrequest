package org.openmrs.module.imbpathologyrequest;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendMail {
	
	public static void main(String[] args) {
		
		// Recipient's email ID needs to be mentioned.
		String to = "jberchmas@gmail.com";
		
		// Sender's email ID needs to be mentioned
		String from = "procurement.pih@gmail.com";
		
		// Assuming you are sending email from through gmails smtp
		String host = "smtp.gmail.com";
		
		// Get system properties
		Properties properties = System.getProperties();
		
		// Setup mail server
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		
		/*properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.socketFactory.port", "587");
		properties.put("mail.smtp.starttls.enable", "true");
		*/
		// Get the Session object.// and pass username and password
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			
			protected PasswordAuthentication getPasswordAuthentication() {
				
				return new PasswordAuthentication("procurement.pih@gmail.com", "Procurement@p1h");
				
			}
			
		});
		
		// Used to debug SMTP issues
		session.setDebug(true);
		
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));
			
			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			// Set Subject: header field
			message.setSubject("This is the Subject Line!");
			
			// Now set the actual message
			message.setText("This is a test message ");
			
			System.out.println("sending...");
			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		}
		catch (MessagingException mex) {
			mex.printStackTrace();
		}
		
	}
	
}
