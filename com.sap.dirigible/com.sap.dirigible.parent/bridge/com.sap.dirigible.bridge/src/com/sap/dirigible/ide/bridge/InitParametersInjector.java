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

public class InitParametersInjector implements Injector {
	
	public static final String RUNTIME_URL = "runtimeUrl"; //$NON-NLS-1$
	public static final String SERVICES_URL = "servicesUrl"; //$NON-NLS-1$
	public static final String ENABLE_ROLES = "enableRoles"; //$NON-NLS-1$
	public static final String LOG_IN_SYSTEM_OUTPUT = "logInSystemOutput"; //$NON-NLS-1$
	
	@Override
	public void inject(ServletConfig servletConfig, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String parameter = servletConfig.getInitParameter(RUNTIME_URL);
		req.getSession().setAttribute(RUNTIME_URL, parameter);
		System.getProperties().put(RUNTIME_URL, parameter);
		
		parameter = servletConfig.getInitParameter(SERVICES_URL);
		req.getSession().setAttribute(SERVICES_URL, parameter);
		System.getProperties().put(RUNTIME_URL, parameter);
		
		parameter = servletConfig.getInitParameter(ENABLE_ROLES);
		req.getSession().setAttribute(ENABLE_ROLES, parameter);
		System.getProperties().put(RUNTIME_URL, parameter);
		
		parameter = servletConfig.getInitParameter(LOG_IN_SYSTEM_OUTPUT);
		req.getSession().setAttribute(LOG_IN_SYSTEM_OUTPUT, parameter);
		System.getProperties().put(RUNTIME_URL, parameter);
		
	}

}
