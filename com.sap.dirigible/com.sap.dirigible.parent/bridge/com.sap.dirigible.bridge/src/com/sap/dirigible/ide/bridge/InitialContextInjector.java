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

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InitialContextInjector implements Injector {
	
	private static final Logger logger = LoggerFactory.getLogger(InitialContextInjector.class);
	
	public static final String INITIAL_CONTEXT = "INITIAL_CONTEXT"; //$NON-NLS-1$
	
	/* (non-Javadoc)
	 * @see com.sap.dirigible.ide.bridge.Injector#inject(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void inject(ServletConfig servletConfig, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		InitialContext initialContext = (InitialContext) req.getSession().getAttribute(INITIAL_CONTEXT);
		if (initialContext == null) {
			try {
				initialContext = new InitialContext();
				req.getSession().setAttribute(INITIAL_CONTEXT, initialContext);
			} catch (NamingException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
}
