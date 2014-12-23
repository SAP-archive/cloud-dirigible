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

package com.sap.dirigible.runtime.registry;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.IEntity;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IRepositoryPaths;
import com.sap.dirigible.repository.api.IResource;
import com.sap.dirigible.runtime.repository.RepositoryFacade;

/**
 * Servlet implementation class AbstractRegistryServlet
 */
public abstract class AbstractRegistryServlet extends HttpServlet {

	private static final long serialVersionUID = -9115022531455267478L;

	protected static final String REPOSITORY_ATTRIBUTE = "com.sap.dirigible.services.registry.repository"; //$NON-NLS-1$

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AbstractRegistryServlet() {
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		initRepository(request);
		super.service(request, response);
		
	}

	private void initRepository(HttpServletRequest request) throws ServletException {
		try {
			final IRepository repository = RepositoryFacade.getInstance().getRepository(request);
			getServletContext().setAttribute(REPOSITORY_ATTRIBUTE, repository);
			if (!repository.hasCollection(IRepositoryPaths.REGISTRY_DEPLOY_PATH)) {
				repository.createCollection(IRepositoryPaths.REGISTRY_DEPLOY_PATH);
			}
		} catch (Exception ex) {
			throw new ServletException("Could not initialize repository.", ex); //$NON-NLS-1$
		}
	}

	protected IRepository getRepository(HttpServletRequest request) throws IOException {
		IRepository repository = (IRepository) getServletContext().getAttribute(
				REPOSITORY_ATTRIBUTE);
		if (repository == null) {
			try {
				initRepository(request);
				repository = (IRepository) getServletContext().getAttribute(REPOSITORY_ATTRIBUTE);
			} catch (ServletException e) {
				throw new IOException(e);
			}
		}
		return repository;
	}

	protected String extractRepositoryPath(HttpServletRequest request)
			throws IllegalArgumentException {
		String requestPath = PathUtils.extractPath(request);
		return IRepositoryPaths.REGISTRY_DEPLOY_PATH + requestPath;
	}

	protected IEntity getEntity(String repositoryPath, HttpServletRequest request)
			throws FileNotFoundException, IOException {
		IEntity result = null;
		final IRepository repository = getRepository(request);
		final IResource resource = repository.getResource(repositoryPath);
		if (!resource.exists()) {
			final ICollection collection = repository.getCollection(repositoryPath);
			if (collection.exists()) {
				result = collection;
			}
			// else {
			// throw new
			// FileNotFoundException("There is no collection or resource at the path specified: "
			// + repositoryPath);
			// }
		} else {
			result = resource;
		}
		return result;
	}

	protected byte[] readResourceData(IResource resource) throws IOException {
		return resource.getContent();
	}

	protected void sendData(OutputStream out, byte[] data) throws IOException {
		out.write(data);
	}

}
