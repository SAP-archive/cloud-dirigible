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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyParametersInjector implements Injector {
	
	private static final Logger logger = LoggerFactory.getLogger(ProxyParametersInjector.class); 
	
	public static final String HTTP_PROXY_HOST = "http.proxyHost"; //$NON-NLS-1$
	public static final String HTTP_PROXY_PORT = "http.proxyPort"; //$NON-NLS-1$
	public static final String HTTPS_PROXY_HOST = "https.proxyHost"; //$NON-NLS-1$
	public static final String HTTPS_PROXY_PORT = "https.proxyPort"; //$NON-NLS-1$
	public static final String HTTP_NON_PROXY_HOSTS = "http.nonProxyHosts"; //$NON-NLS-1$
	

	@Override
	public void inject(ServletConfig servletConfig, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String parameterHTTP_PROXY_HOST = SystemBridge.ENV_PROPERTIES.getProperty(HTTP_PROXY_HOST);
		if (parameterHTTP_PROXY_HOST != null) {
			req.getSession().setAttribute(HTTP_PROXY_HOST, parameterHTTP_PROXY_HOST);
			logger.debug("HTTP_PROXY_HOST:" + parameterHTTP_PROXY_HOST);
		} else {
			logger.debug("HTTP_PROXY_HOST not set");
		}
		String parameterHTTP_PROXY_PORT = SystemBridge.ENV_PROPERTIES.getProperty(HTTP_PROXY_PORT);
		if (parameterHTTP_PROXY_PORT != null) {
			req.getSession().setAttribute(HTTP_PROXY_PORT, parameterHTTP_PROXY_PORT);
			logger.debug("HTTP_PROXY_PORT:" + parameterHTTP_PROXY_PORT);
		} else {
			logger.debug("HTTP_PROXY_PORT not set");
		}
		String parameterHTTPS_PROXY_HOST = SystemBridge.ENV_PROPERTIES.getProperty(HTTPS_PROXY_HOST);
		if (parameterHTTPS_PROXY_HOST != null) {
			req.getSession().setAttribute(HTTPS_PROXY_HOST, parameterHTTPS_PROXY_HOST);
			logger.debug("HTTPS_PROXY_HOST:" + parameterHTTPS_PROXY_HOST);
		} else {
			logger.debug("HTTPS_PROXY_HOST not set");
		}	
		String parameterHTTPS_PROXY_PORT = SystemBridge.ENV_PROPERTIES.getProperty(HTTPS_PROXY_PORT);
		if (parameterHTTPS_PROXY_PORT != null) {
			req.getSession().setAttribute(HTTPS_PROXY_PORT, parameterHTTPS_PROXY_PORT);
			logger.debug("HTTPS_PROXY_PORT:" + parameterHTTPS_PROXY_PORT);
		} else {
			logger.debug("HTTPS_PROXY_PORT not set");
		}
		String parameterHTTP_NON_PROXY_HOSTS = SystemBridge.ENV_PROPERTIES.getProperty(HTTP_NON_PROXY_HOSTS);
		if (parameterHTTP_NON_PROXY_HOSTS != null) {
			req.getSession().setAttribute(HTTP_NON_PROXY_HOSTS, parameterHTTP_NON_PROXY_HOSTS);
			logger.debug("HTTP_NON_PROXY_HOSTS:" + parameterHTTP_NON_PROXY_HOSTS);
		} else {
			logger.debug("HTTP_NON_PROXY_HOSTS not set");
		}			

	}

}
