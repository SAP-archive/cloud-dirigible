/*******************************************************************************
 * Copyright (c) 2014 SAP AG or an SAP affiliate company. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *******************************************************************************/

package com.sap.dirigible.runtime.mail;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;

import com.sap.dirigible.repository.logging.Logger;

public class MailSender {

	private static final Logger logger = Logger.getLogger(MailSender.class.getCanonicalName());

	public String sendMail(String from, String to, String subject, String content) {
		try {
			InitialContext ctx = new InitialContext();
			Session smtpSession = (Session) ctx
					.lookup("java:comp/env/mail/SAPInternalNWCloudSession"); //$NON-NLS-1$
			Transport transport = smtpSession.getTransport();
			transport.connect();

			MimeMessage mimeMessage = createMimeMessage(smtpSession, from, to, subject, content);
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
			transport.close();
		} catch (Exception e) {
			logger.error(this.getClass().getCanonicalName() + "#sendMail()", e); //$NON-NLS-1$
			// e.printStackTrace();
			return e.getMessage();
			// throw new Exception(e);
		}
		return ""; //$NON-NLS-1$
	}

	private static MimeMessage createMimeMessage(Session smtpSession, String from, String to,
			String subjectText, String mailText) throws MessagingException {

		MimeMessage mimeMessage = new MimeMessage(smtpSession);
		InternetAddress[] fromAddress = InternetAddress.parse(from);
		InternetAddress[] toAddresses = InternetAddress.parse(to);
		mimeMessage.setFrom(fromAddress[0]);
		mimeMessage.setRecipients(RecipientType.TO, toAddresses);
		mimeMessage.setSubject(subjectText, "UTF-8"); //$NON-NLS-1$

		MimeMultipart multiPart = new MimeMultipart("alternative"); //$NON-NLS-1$
		MimeBodyPart part = new MimeBodyPart();
		part.setText(mailText, "utf-8", "plain"); //$NON-NLS-1$ //$NON-NLS-2$
		multiPart.addBodyPart(part);
		mimeMessage.setContent(multiPart);

		return mimeMessage;
	}

}
