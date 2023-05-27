/*
 * Copyright (c) VW All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by VW Software as part
 * of an VW Software product for use ONLY by licensed users of the product,
 * includes CONFIDENTIAL and PROPRIETARY information of VW Software.
 *
 * USE OF THIS SOFTWARE IS GOVERNED BY THE TERMS AND CONDITIONS
 * OF THE LICENSE STATEMENT AND LIMITED WARRANTY FURNISHED WITH
 * THE PRODUCT.
 *
 * IN PARTICULAR, YOU WILL INDEMNIFY AND HOLD INPRISE, ITS RELATED
 * COMPANIES AND ITS SUPPLIERS, HARMLESS FROM AND AGAINST ANY CLAIMS
 * OR LIABILITIES ARISING OUT OF THE USE, REPRODUCTION, OR DISTRIBUTION
 * OF YOUR PROGRAMS, INCLUDING ANY CLAIMS OR LIABILITIES ARISING OUT OF
 * OR RESULTING FROM THE USE, MODIFICATION, OR DISTRIBUTION OF PROGRAMS
 * OR FILES CREATED FROM, BASED ON, AND/OR DERIVED FROM THIS SOURCE
 * CODE FILE.
 */

package vwg.vw.km.application.service.logic;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.UserModel;

/**
 * <p>
 * Title: EKM
 * <p>
 * Description : Class description goes here
 * </p>
 * <p>
 * Copyright: VW (c) 2020
 * </p>
 * 
 * @author abidh changed by $Author: saidi $
 * @version $Revision: 1.13 $ $Date: 2022/02/08 09:58:32 $
 */
public class MailServiceImpl implements MailService {
	private final Log log = LogManager.get().getLog(MailServiceImpl.class);

	JavaMailSender mailSender;
	SimpleMailMessage templateMessage;
	private String reactivationURL;
	private String loginURL;

	public String getLoginURL() {
		return loginURL;
	}

	public void setLoginURL(String loginURL) {
		this.loginURL = loginURL;
	}

	public String getReactivationURL() {
		return reactivationURL;
	}

	public void setReactivationURL(String reactivationURL) {
		this.reactivationURL = reactivationURL;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public SimpleMailMessage getTemplateMessage() {
		return templateMessage;
	}

	public void setTemplateMessage(SimpleMailMessage templateMessage) {
		this.templateMessage = templateMessage;
	}

	@Override
	public void sendMail(UserModel user) {
		log.info("START send MAIL to user ID " + user.getUserId());
		SimpleMailMessage mailToSend = new SimpleMailMessage(templateMessage);

		mailToSend.setSubject("Zugang deaktiviert");
		mailToSend.setTo(user.getEmail());

		// mailToSend.get

		MimeMessage message = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom(mailToSend.getFrom());
			helper.setTo(mailToSend.getTo());
			helper.setSubject(mailToSend.getSubject());
			helper.setText(getMessageDesactivMail(user), true);

		} catch (MessagingException e) {
			log.error("No data mail ", e);
		}
		mailSender.send(message);
	}

	public String getMessageDesactivMail(UserModel user) {
		StringBuilder message = new StringBuilder();

		message.append("<html><body>");
		if (user.getGender().equals("Herr")) {
			message.append("<p><span>Sehr geehrter Herr</span>&nbsp;");
		} else {
			message.append("<p><span>Sehr geehrte Frau</span>&nbsp;");
		}

		message.append(user.getLastName());
		message.append("&nbsp;");
		message.append(user.getFirstName());
		message.append(",");
		message.append("<br><br>Aufgrund von Inaktivität wurden Sie im System eKM ( ");

		message.append("<a href=\"");
		message.append(getLoginURL());
		message.append("\">");
		message.append(getLoginURL());
		message.append("</a>");
		message.append(" ) deaktiviert und werden nach DSGVO Vorgaben in sechs Monaten automatisch anonymisiert.");
		message.append(
				"<br>Bis dahin können Sie Ihren Account reaktivieren. Klicken Sie dazu auf diesen Link und melden sich mit Ihrem Usernamen und Passwort in eKM an.");
		message.append("<br><br>&emsp; &emsp; &emsp;");

		message.append("<a href=\"");
		message.append(getReactivationURL());
		message.append(user.getActivationCode());
		message.append("\">Account reaktivieren</a>");
		message.append("<br>");
		message.append("<br>Nach der Anonymisierung kann Ihr Account nicht mehr reaktiviert werden");
		message.append("<br>Alle Ihre Persondaten wurden aus dem System gelöscht.<br>");
		message.append("<br>Sollten Sie eKM wieder nutzen wollen, wird nach Antrag ein neuer User für Sie erstellt.");
		message.append("<br>");
		message.append("<br>Diese Nachricht wurde vom System erstellt.</p>");
		message.append("</body></html>");

		return message.toString();
	}

}
