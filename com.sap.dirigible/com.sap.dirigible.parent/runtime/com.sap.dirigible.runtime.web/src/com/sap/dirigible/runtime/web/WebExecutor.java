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

package com.sap.dirigible.runtime.web;

import java.io.IOException;
import java.util.Map;

import javax.naming.Context;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.dirigible.repository.api.ICommonConstants;
import com.sap.dirigible.repository.api.IEntity;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IResource;
import com.sap.dirigible.repository.logging.Logger;
import com.sap.dirigible.runtime.js.RepositoryModuleSourceProvider;
import com.sap.dirigible.runtime.scripting.AbstractScriptExecutor;
import com.sap.dirigible.runtime.scripting.Module;

public class WebExecutor extends AbstractScriptExecutor {
	
	private static final Logger logger = Logger.getLogger(WebExecutor.class);

	private IRepository repository;
	private String[] rootPaths;

	public WebExecutor(IRepository repository, String... rootPaths) {
		super();
		logger.debug("entering: constructor()");
		this.repository = repository;
		this.rootPaths = rootPaths;
		if (this.rootPaths == null || this.rootPaths.length == 0) {
			this.rootPaths = new String[] { null, null };
		}
		logger.debug("exiting: constructor()");
	}

	@Override
	public Object executeServiceModule(HttpServletRequest request, HttpServletResponse response,
			Object input, String module, Map<Object, Object> executionContext) throws IOException {

		logger.debug("entering: executeServiceModule()"); //$NON-NLS-1$
		logger.debug("module=" + module); //$NON-NLS-1$
		
		if (module == null) {
			throw new IOException("Web module cannot be null");
		}

		Module scriptingModule = retrieveModule(this.repository, module, null, this.rootPaths);
		byte[] result = scriptingModule.getContent();
		
		result = preprocessContent(result, getResource(repository, scriptingModule.getPath()));
		
		response.getWriter().print(new String(result));
		response.getWriter().flush();
		logger.debug("exiting: executeServiceModule()");
		return result;
	}
	
	protected byte[] preprocessContent(byte[] rawContent, IEntity entity) throws IOException {
		return rawContent;
	}
	
	protected byte[] buildResourceData(final IEntity entity, final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		byte[] data = new byte[] {};
		data = readResourceData((IResource) entity);
		return data;
	}
	
	protected byte[] readResourceData(IResource resource) throws IOException {
		return resource.getContent();
	}

	protected void beforeExecution(HttpServletRequest request, HttpServletResponse response,
			String module, Context context) {
	}

	private RepositoryModuleSourceProvider createRepositoryModuleSourceProvider() {
		RepositoryModuleSourceProvider repositoryModuleSourceProvider = null;
		repositoryModuleSourceProvider = new RepositoryModuleSourceProvider(this, repository,
				rootPaths);
		return repositoryModuleSourceProvider;
	}

	@Override
	protected void registerDefaultVariable(Object scope, String name, Object value) {
		//
	}

	@Override
	protected String getModuleType(String path) {
		return ICommonConstants.ARTIFACT_TYPE.WEB_CONTENT;
	}

}
