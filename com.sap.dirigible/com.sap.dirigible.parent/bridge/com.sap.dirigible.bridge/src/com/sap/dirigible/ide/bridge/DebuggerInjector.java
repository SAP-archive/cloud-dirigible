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

import java.beans.PropertyChangeSupport;
import java.io.IOException;

import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



// DISABLED - OSGI plugins get it from the System properties directly

public class DebuggerInjector implements Injector {
	
	public static final String DIRIGIBLE_DEBUGGER_BRIDGE = "dirigible.debugger.bridge"; //$NON-NLS-1$
	
	private static final Logger logger = LoggerFactory.getLogger(DebuggerInjector.class);
	
	/* (non-Javadoc)
	 * @see com.sap.dirigible.ide.bridge.Injector#inject(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void inject(ServletConfig servletConfig, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
//		PropertyChangeSupport sessionDebuggerBridge = (PropertyChangeSupport) req.getSession().getAttribute(DIRIGIBLE_DEBUGGER_BRIDGE);
//		if (sessionDebuggerBridge == null) {
//			PropertyChangeSupport debuggerBridge = lookupDebuggerBridge();
//			if (debuggerBridge == null) {
//				debuggerBridge = new PropertyChangeSupport(new Object());
//				System.getProperties().put(DIRIGIBLE_DEBUGGER_BRIDGE, debuggerBridge);
//				logger.info("Debugger Bridge put to environment: " + debuggerBridge.hashCode()); //$NON-NLS-1$
//			}
//			try {
//				req.getSession().setAttribute(DIRIGIBLE_DEBUGGER_BRIDGE, debuggerBridge);
//			} catch (Exception e) {
//				logger.error(e.getMessage(), e);
//			}
//		}
	}
	
//	/**
//	 * Retrieve the DebuggerBridge(PropertyChangeSupport) from the target server environment
//	 * 
//	 * @return
//	 * @throws NamingException
//	 */
//	private PropertyChangeSupport lookupDebuggerBridge() {
//		return (PropertyChangeSupport) System.getProperties().get(DIRIGIBLE_DEBUGGER_BRIDGE);
//	}

}
