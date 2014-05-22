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

package com.sap.dirigible.runtime.camel;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.runtime.filter.SandboxFilter;
import com.sap.dirigible.runtime.repository.RepositoryFacade;
import com.sap.dirigible.runtime.scripting.AbstractScriptingServlet;

public abstract class AbstractBeanProcessor {

	private static final String FAILED = Messages.getString("AbstractBeanProcessor.FAILED"); //$NON-NLS-1$
	private static final String COULD_NOT_INITIALIZE_REPOSITORY = Messages.getString("AbstractBeanProcessor.COULD_NOT_INITIALIZE_REPOSITORY"); //$NON-NLS-1$
	private static final Logger logger = LoggerFactory
			.getLogger(AbstractBeanProcessor.class.getCanonicalName());

	public String process(Exchange exchange) {

		HttpServletRequest request = exchange.getIn().getBody(
				HttpServletRequest.class);
		HttpServletResponse response = exchange.getIn().getBody(
				HttpServletResponse.class);

		Object input = exchange.getIn().getBody();

		String serviceName = (String) exchange.getProperty("serviceName"); //$NON-NLS-1$

		try {
			Object result = doExecution(request, response, input, serviceName);
			if (result != null) {
				return result.toString();
			}
			return null;
		} catch (ServletException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return serviceName + FAILED;
	}

	public void initRepository(HttpServletRequest request)
			throws ServletException {
		try {
			final IRepository repository = RepositoryFacade.getInstance()
					.getRepository(request);
			request.getSession().setAttribute(
					AbstractScriptingServlet.REPOSITORY_ATTRIBUTE, repository);
		} catch (Exception ex) {
			throw new ServletException(COULD_NOT_INITIALIZE_REPOSITORY, ex);
		}
	}

	public abstract Object doExecution(HttpServletRequest request,
			HttpServletResponse response, Object input, String module)
			throws ServletException, IOException;

	protected IRepository getRepository(HttpServletRequest req)
			throws IOException {
		IRepository repository = (IRepository) req.getSession().getAttribute(
				AbstractScriptingServlet.REPOSITORY_ATTRIBUTE);
		if (repository == null) {
			try {
				initRepository(req);
				repository = (IRepository) req.getSession().getAttribute(
						AbstractScriptingServlet.REPOSITORY_ATTRIBUTE);
			} catch (ServletException e) {
				throw new IOException(e);
			}
		}
		return repository;
	}
	
	protected String getScriptingRegistryPath(HttpServletRequest request) {
		if (request.getAttribute(SandboxFilter.SANDBOX_CONTEXT) != null
				&& (Boolean) request.getAttribute(SandboxFilter.SANDBOX_CONTEXT)) {
			return AbstractScriptingServlet.getSandboxScripting(request);
		}
		return AbstractScriptingServlet.REGISTRY_SCRIPTING_DEPLOY_PATH;
	}

}
