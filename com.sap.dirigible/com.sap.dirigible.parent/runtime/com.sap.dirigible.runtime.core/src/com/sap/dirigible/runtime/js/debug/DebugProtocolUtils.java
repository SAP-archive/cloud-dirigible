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

package com.sap.dirigible.runtime.js.debug;

import javax.naming.NamingException;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.sap.dirigible.repository.ext.debug.IDebugProtocol;
import com.sap.dirigible.repository.logging.Logger;
import com.sap.dirigible.runtime.RuntimeActivator;

public class DebugProtocolUtils {

	private static final Logger logger = Logger.getLogger(DebugProtocolUtils.class);

	/**
	 * Retrieve the DebugProtocol(PropertyChangeSupport) from the target server
	 * environment
	 * 
	 * @return
	 * @throws NamingException
	 */
	public static IDebugProtocol lookupDebugProtocol() {
		logger.debug("entering JavaScriptDebugServlet.lookupDebugProtocol()");
		BundleContext context = RuntimeActivator.getContext();
		ServiceReference<IDebugProtocol> sr = context.getServiceReference(IDebugProtocol.class);
		IDebugProtocol debugProtocol = context.getService(sr);
		if (debugProtocol == null) {
			logger.error("DebugProtocol not present");
		}
		
		logger.debug("exiting JavaScriptDebugServlet.lookupDebuggerBridge()");
		return debugProtocol;
	}

	public static void send(IDebugProtocol debugProtocol, String commandId,
			String clientId, String commandBody) {
		logger.debug("DebugBridgUtils send() commandId: " + commandId + ", clientId: " + clientId
				+ ", body: " + commandBody);
		debugProtocol.firePropertyChange(commandId, clientId, commandBody);
	}

}
