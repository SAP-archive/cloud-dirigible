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

package com.sap.dirigible.runtime.js;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.dirigible.runtime.scripting.AbstractScriptingServlet;

/**
 * Servlet for JavaScript scripts execution
 */
public class JavaScriptServlet extends AbstractScriptingServlet {

	private static final long serialVersionUID = -9115022531455267478L;

	private static final Logger logger = LoggerFactory
			.getLogger(JavaScriptServlet.class.getCanonicalName());

	protected void doExecution(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String module = request.getPathInfo();

		JavaScriptExecutor executor = createExecutor(request);
		try {
			executor.executeServiceModule(request, response, module);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
			response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
		}

	}

	protected JavaScriptExecutor createExecutor(HttpServletRequest request)
			throws IOException {
		JavaScriptExecutor executor = new JavaScriptExecutor(
				getRepository(request), getScriptingRegistryPath(request), REGISTRY_SCRIPTING_DEPLOY_PATH);
		return executor;
	}

}
