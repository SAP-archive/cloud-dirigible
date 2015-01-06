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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.equinox.servletbridge.BridgeServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Enhanced Bridge Servlet which retrieves specific parameters and objects 
 * from the target environment and provide them to RAP OSGi environment thru the Session object  
 *
 */
public class DirigibleBridge extends BridgeServlet {
	
	private static final long serialVersionUID = -8043662807856187626L;
	
	private static final Logger logger = LoggerFactory.getLogger(DirigibleBridge.class);
	
	static Class<Injector>[] injectorClasses;
	static {
		injectorClasses = new Class[]{
			DatabaseInjector.class,
			InitParametersInjector.class,
			ProxyParametersInjector.class,
			LocalParametersInjector.class,
			MailInjector.class,
			DebuggerInjector.class,
			RuntimeBridgeInjector.class,
			JavaToolsInjector.class
		};
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		ServletConfig servletConfig = getServletConfig();
		
		for (Class injectorClass : injectorClasses) {
			try {
				Injector injector = (Injector) injectorClass.newInstance();
				injector.inject(servletConfig, req, resp);
			} catch (InstantiationException e) {
				logger.error(e.getMessage(), e);
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
			}
		}

		super.service(req, resp);
	}
	

}
