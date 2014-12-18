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

package com.sap.dirigible.ide.bridge;

import java.io.IOException;

import javax.mail.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailInjector implements Injector {
	
	public static final String JAVA_COMP_ENV_JDBC_DEFAULT_MAIL_SESSION = "java:comp/env/mail/SAPInternalNWCloudSession"; //$NON-NLS-1$
	public static final String DEFAULT_MAIL_SESSION = "DEFAULT_MAIL_SESSION"; //$NON-NLS-1$
	
	private static final Logger logger = LoggerFactory.getLogger(MailInjector.class);
	
	/* (non-Javadoc)
	 * @see com.sap.dirigible.ide.bridge.Injector#inject(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void inject(ServletConfig servletConfig, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
		Session session = (Session) req.getSession().getAttribute(DEFAULT_MAIL_SESSION);
		if (session == null) {
			try {
				session = lookupMailSession();
				req.getSession().setAttribute(DEFAULT_MAIL_SESSION, session);
			} catch (NamingException e) {
				logger.error(DirigibleBridge.class.getCanonicalName(), e);
			}
		}
				
	}
	
	/**
	 * Retrieve the MailSession from the target server environment
	 * 
	 * @return
	 * @throws NamingException
	 */
	private Session lookupMailSession() throws NamingException {
		final InitialContext ctx = new InitialContext();
		return (Session) ctx.lookup(JAVA_COMP_ENV_JDBC_DEFAULT_MAIL_SESSION);
	}


}
