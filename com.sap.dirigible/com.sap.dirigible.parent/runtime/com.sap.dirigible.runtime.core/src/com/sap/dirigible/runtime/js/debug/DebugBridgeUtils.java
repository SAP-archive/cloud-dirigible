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

import java.beans.PropertyChangeSupport;

import javax.naming.NamingException;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.sap.dirigible.repository.ext.debug.IDebugProtocol;
import com.sap.dirigible.runtime.RuntimeActivator;
import com.sap.dirigible.runtime.logger.Logger;

public class DebugBridgeUtils {

	public static final String DIRIGIBLE_DEBUGGER_BRIDGE = "dirigible.debugger.bridge"; //$NON-NLS-1$

	private static final Logger logger = Logger.getLogger(DebugBridgeUtils.class);

	/**
	 * Retrieve the DebuggerBridge(PropertyChangeSupport) from the target server
	 * environment
	 * 
	 * @return
	 * @throws NamingException
	 */
	public static IDebugProtocol lookupDebuggerBridge() {
		logger.debug("entering JavaScriptDebugServlet.lookupDebuggerBridge()");
//		PropertyChangeSupport debuggerBridge = (PropertyChangeSupport) System.getProperties().get(
//				DIRIGIBLE_DEBUGGER_BRIDGE);
		BundleContext context = RuntimeActivator.getContext();
		ServiceReference<IDebugProtocol> sr = context.getServiceReference(IDebugProtocol.class);
		IDebugProtocol debuggerBridge = context.getService(sr);
		if (debuggerBridge == null) {
			logger.error("DebuggerBridge not present");
		}
		
		logger.debug("exiting JavaScriptDebugServlet.lookupDebuggerBridge()");
		return debuggerBridge;
	}

	public static void send(IDebugProtocol debuggerBridge, String commandId,
			String clientId, String commandBody) {
		logger.debug("DebugBridgUtils send() commandId: " + commandId + ", clientId: " + clientId
				+ ", body: " + commandBody);
		debuggerBridge.firePropertyChange(commandId, clientId, commandBody);
	}

}
