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

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.runtime.filter.SandboxFilter;
import com.sap.dirigible.runtime.logger.Logger;
import com.sap.dirigible.runtime.repository.RepositoryFacade;
import com.sap.dirigible.runtime.scripting.AbstractScriptingServlet;

public abstract class AbstractBeanProcessor {

	private static final String FAILED = Messages.getString("AbstractBeanProcessor.FAILED"); //$NON-NLS-1$
	private static final String COULD_NOT_INITIALIZE_REPOSITORY = Messages.getString("AbstractBeanProcessor.COULD_NOT_INITIALIZE_REPOSITORY"); //$NON-NLS-1$

	private static final Logger logger = Logger.getLogger(AbstractBeanProcessor.class.getCanonicalName());

	public String process(Exchange exchange) {

		logger.debug("entering: process()");
		
		HttpServletRequest request = exchange.getIn().getBody(
				HttpServletRequest.class);
		HttpServletResponse response = exchange.getIn().getBody(
				HttpServletResponse.class);

		Object input = exchange.getIn().getBody();

//		String serviceName = (String) exchange.getProperty("serviceName"); //$NON-NLS-1$
		String serviceName = (String) exchange.getIn().getHeader("serviceName"); //$NON-NLS-1$
		logger.debug("serviceName=" + serviceName);
		try {
			Object result = doExecution(request, response, input, serviceName);
			if (result != null) {
				logger.debug("exiting: process() - " + result.toString()); //$NON-NLS-1$
				return result.toString();
			}
			logger.debug("exiting: process() - null"); //$NON-NLS-1$
			return null;
		} catch (ServletException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		
		logger.debug("exiting: process() - failed"); //$NON-NLS-1$
		return serviceName + FAILED;
	}

	public IRepository initRepository(HttpServletRequest request)
			throws ServletException {
		logger.debug("entering: initRepository()");
		IRepository repository = null;
		try {
			repository = RepositoryFacade.getInstance()
					.getRepository(request);
			if (request != null) {
				request.getSession().setAttribute(
						AbstractScriptingServlet.REPOSITORY_ATTRIBUTE, repository);
			}
		} catch (Exception ex) {
			throw new ServletException(COULD_NOT_INITIALIZE_REPOSITORY, ex);
		}
		logger.debug("exiting: initRepository()");
		return repository;
	}

	public abstract Object doExecution(HttpServletRequest request,
			HttpServletResponse response, Object input, String module)
			throws ServletException, IOException;

	protected IRepository getRepository(HttpServletRequest req)
			throws IOException {
		logger.debug("entering: getRepository()");
		
		IRepository repository = null;
		try {
			if (req != null && req.getSession() != null) {
				repository = (IRepository) req.getSession().getAttribute(
						AbstractScriptingServlet.REPOSITORY_ATTRIBUTE);
				if (repository == null) {
					repository = initRepository(req);
				}
			} else {
				repository = initRepository(null);
			}
		} catch (ServletException e) {
			throw new IOException(e);
		}
		logger.debug("exiting: getRepository()");
		return repository;
	}
	
	protected String getScriptingRegistryPath(HttpServletRequest request) {
		logger.debug("entering: getScriptingRegistryPath()");
		if (request != null && request.getAttribute(SandboxFilter.SANDBOX_CONTEXT) != null
				&& (Boolean) request.getAttribute(SandboxFilter.SANDBOX_CONTEXT)) {
			return AbstractScriptingServlet.getSandboxScripting(request);
		}
		logger.debug("exiting: getScriptingRegistryPath()");
		return AbstractScriptingServlet.REGISTRY_SCRIPTING_DEPLOY_PATH;
	}

}
