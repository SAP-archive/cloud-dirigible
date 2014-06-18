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
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.sap.dirigible.runtime.js.JavaScriptExecutor;
import com.sap.dirigible.runtime.js.JavaScriptServlet;
import com.sap.dirigible.runtime.logger.Logger;

/**
 * Servlet for JavaScript scripts execution
 */
public class JavaScriptDebugServlet extends JavaScriptServlet {

	private static final long serialVersionUID = -9115022531455267478L;

	private static final Logger logger = Logger.getLogger(JavaScriptDebugServlet.class);

	@Override
	protected JavaScriptExecutor createExecutor(HttpServletRequest request) throws IOException {

		logger.debug("entering JavaScriptDebugServlet.createExecutor()");

		PropertyChangeSupport debuggerBridge = DebugBridgeUtils.lookupDebuggerBridge();

		String rootPath = getScriptingRegistryPath(request);
		logger.debug("rootPath=" + rootPath);
		JavaScriptDebuggingExecutor executor = new JavaScriptDebuggingExecutor(
				getRepository(request), rootPath, REGISTRY_SCRIPTING_DEPLOY_PATH, debuggerBridge);

		logger.debug("exiting JavaScriptDebugServlet.createExecutor()");

		return executor;
	}

}
