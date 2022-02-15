package org.openmrs.module.imbpathologyrequest.notify;

import org.openmrs.Obs;
import org.openmrs.PersonAttribute;
import org.openmrs.Provider;
import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.CustomFormSubmissionAction;
import org.openmrs.module.htmlformentry.FormEntrySession;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class NotifyProviderOnHtmlFormSubmissionAction implements CustomFormSubmissionAction {
	
	@Override
	public void applyAction(FormEntrySession session) {

	//	String providerEmail = null;

		String familyName = null;

		String givenName = null;

		String pathologyResultApproveduuId=Context.getAdministrationService().getGlobalProperty("reports.pathologyReport.pathologyResultApproved");
		Obs pathologyResultApprovedObs=null;

		int patientId=session.getSubmissionActions().getCurrentPerson().getPersonId();

		List<Obs> obs = session.getSubmissionActions().getObsToCreate();

		for (Obs o : obs) {
				if ( pathologyResultApproveduuId!=null && o.getConcept().getUuid().equals(pathologyResultApproveduuId)) {
					pathologyResultApprovedObs=o;
					break;
				}
			}


		if(pathologyResultApprovedObs!=null) {

			//for (Obs o : obs) {
			//if (o.getConcept().getUuid().equals("a4cd8972-6a31-411c-ade2-54c24ddad631")) {
			//providerEmail = o.getValueText();
			familyName = session.getSubmissionActions().getCurrentPerson().getFamilyName();
			givenName = session.getSubmissionActions().getCurrentPerson().getGivenName();

			//break;
			//}
			//	}
			int patientLocationAttribute = 0;
			if (session.getSubmissionActions().getCurrentPerson().getAttribute("Health Center") != null) {
				patientLocationAttribute = Integer.parseInt(session.getSubmissionActions().getCurrentPerson().getAttribute("Health Center").getValue());
			}

			List<Provider> providers = Context.getProviderService().getAllProviders(true);
			List<String> providersEmails = new ArrayList<String>();
			for (Provider provider : providers) {

				if (provider.getPerson() != null)
					if (provider.getPerson().getAttribute("Health Center") != null)
						if (provider.getPerson().getAttribute("Health Center") != null &&
								session.getSubmissionActions().getCurrentPerson().getAttribute("Health Center") != null &&
								provider.getPerson().getAttribute("Health Center").getValue() != null &&
								session.getSubmissionActions().getCurrentPerson().getAttribute("Health Center").getValue() != null &&
								Integer.parseInt(provider.getPerson().getAttribute("Health Center").getValue()) == patientLocationAttribute) {
							//System.out.println(provider.getPerson().getAttribute("Email").getValue());
							if (provider.getPerson().getAttribute("Email") != null) {
								System.out.println(provider.getPerson().getAttribute("Email").getValue());
								providersEmails.add(provider.getPerson().getAttribute("Email").getValue());
							}
						}
			}

			//start Email
			for (String providerEmail : providersEmails) {

				if (providerEmail != null) {

					// Recipient's email ID needs to be mentioned.
					String to = providerEmail;

					// Sender's email ID needs to be mentioned
					String from = "butaro.pathology@gmail.com";

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
					Session sessionObject = Session.getInstance(properties, new Authenticator() {

						protected PasswordAuthentication getPasswordAuthentication() {

							return new PasswordAuthentication("butaro.pathology@gmail.com", "Pathology@3utaro");

						}

					});

					// Used to debug SMTP issues
					sessionObject.setDebug(true);

					try {
						// Create a default MimeMessage object.
						MimeMessage message = new MimeMessage(sessionObject);

						// Set From: header field of the header.
						message.setFrom(new InternetAddress(from));

						// Set To: header field of the header.
						message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

						// Set Subject: header field
						message.setSubject(familyName + "'s pathology results");
						String pathologyServeUrl = Context.getAdministrationService().getGlobalProperty("imbpathologyrequest.urlForServer");
						// Now set the actual message
						message.setText("The pathology results from Butaro Hospital for " + familyName + " " + givenName
								+ " are now available. Please log on to " + pathologyServeUrl + "/patientDashboard.form?patientId=" + patientId);

						System.out.println("sending...");
						// Send message
						Transport.send(message);
						System.out.println("Sent message successfully....");
					} catch (MessagingException mex) {
						mex.printStackTrace();
					}

				}
			}
			//End Email


		}

	}
	
}
