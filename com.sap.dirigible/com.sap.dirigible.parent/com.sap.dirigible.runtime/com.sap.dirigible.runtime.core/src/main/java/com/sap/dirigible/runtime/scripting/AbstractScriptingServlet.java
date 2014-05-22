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

package com.sap.dirigible.runtime.scripting;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.runtime.common.ICommonConstants;
import com.sap.dirigible.runtime.filter.SandboxFilter;
import com.sap.dirigible.runtime.registry.AbstractRegistryServlet;
import com.sap.dirigible.runtime.repository.RepositoryFacade;

/**
 * Abstract Servlet for Scripting Engines
 */
public abstract class AbstractScriptingServlet extends HttpServlet {

	private static final String COULD_NOT_INITIALIZE_REPOSITORY = Messages.getString("AbstractScriptingServlet.COULD_NOT_INITIALIZE_REPOSITORY"); //$NON-NLS-1$

	private static final long serialVersionUID = -9115022531455267478L;

	public static final String REGISTRY_SCRIPTING_DEPLOY_PATH = AbstractRegistryServlet.REGISTRY_DEPLOY_PATH
			+ ICommonConstants.SEPARATOR + ICommonConstants.ARTIFACT_TYPE.SCRIPTING_SERVICES; //$NON-NLS-1$
	
	public static String getSandboxScripting(HttpServletRequest request) {
		return AbstractRegistryServlet.SANDBOX_DEPLOY_PATH + ICommonConstants.SEPARATOR 
				+ RepositoryFacade.getUser(request) + ICommonConstants.SEPARATOR
				+ ICommonConstants.ARTIFACT_TYPE.SCRIPTING_SERVICES; //$NON-NLS-1$
	}

	public static final String REPOSITORY_ATTRIBUTE = "com.sap.dirigible.services.scripting.repository"; //$NON-NLS-1$

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		super.service(request, response);
		initRepository(request);
	}

	private void initRepository(HttpServletRequest request)
			throws ServletException {
		try {
			final IRepository repository = RepositoryFacade.getInstance()
					.getRepository(request);
			getServletContext().setAttribute(REPOSITORY_ATTRIBUTE, repository);
		} catch (Exception ex) {
			throw new ServletException(COULD_NOT_INITIALIZE_REPOSITORY, ex);
		}
	}

	protected abstract void doExecution(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException;

	protected String getScriptingRegistryPath(HttpServletRequest request) {
		if ((request.getAttribute(SandboxFilter.SANDBOX_CONTEXT) != null
				&& (Boolean) request.getAttribute(SandboxFilter.SANDBOX_CONTEXT)
				|| (request.getAttribute(SandboxFilter.DEBUG_CONTEXT) != null
				&& (Boolean) request.getAttribute(SandboxFilter.DEBUG_CONTEXT)))) {
			return getSandboxScripting(request);
		}
		return REGISTRY_SCRIPTING_DEPLOY_PATH;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doExecution(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doExecution(req, resp);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doExecution(req, resp);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doExecution(req, resp);
	}

	@Override
	protected void doHead(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doExecution(req, resp);
	}

	protected IRepository getRepository(HttpServletRequest req)
			throws IOException {
		IRepository repository = (IRepository) getServletContext()
				.getAttribute(REPOSITORY_ATTRIBUTE);
		if (repository == null) {
			try {
				initRepository(req);
				repository = (IRepository) getServletContext().getAttribute(
						REPOSITORY_ATTRIBUTE);
			} catch (ServletException e) {
				throw new IOException(e);
			}
		}
		return repository;
	}

}
