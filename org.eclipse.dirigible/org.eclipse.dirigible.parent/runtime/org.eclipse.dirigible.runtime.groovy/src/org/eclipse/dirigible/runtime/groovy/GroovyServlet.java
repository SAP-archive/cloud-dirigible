/******************************************************************************* 
 * Copyright (c) 2015 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   SAP - initial API and implementation
 *******************************************************************************/

package org.eclipse.dirigible.runtime.groovy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.dirigible.repository.logging.Logger;
import org.eclipse.dirigible.runtime.scripting.AbstractScriptingServlet;

public class GroovyServlet extends AbstractScriptingServlet {

	private static final long serialVersionUID = 2580668894416114646L;
	
	private static final Logger logger = Logger
			.getLogger(GroovyServlet.class.getCanonicalName());

	protected void doExecution(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String module = request.getPathInfo();

		GroovyExecutor executor = createExecutor(request);
		Map<Object, Object> executionContext = new HashMap<Object, Object>();
		try {
			executor.executeServiceModule(request, response, module, executionContext);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	public GroovyExecutor createExecutor(HttpServletRequest request)
			throws IOException {
		
		GroovyExecutor executor = new GroovyExecutor(getRepository(request),
				getScriptingRegistryPath(request), REGISTRY_SCRIPTING_DEPLOY_PATH);
		return executor;
	}
}
