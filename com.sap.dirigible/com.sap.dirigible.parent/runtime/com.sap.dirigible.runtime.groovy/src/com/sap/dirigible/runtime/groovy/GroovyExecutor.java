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

package com.sap.dirigible.runtime.groovy;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.runtime.logger.Logger;
import com.sap.dirigible.runtime.scripting.AbstractScriptExecutor;

public class GroovyExecutor extends AbstractScriptExecutor {
	
	private static final String DOT = "."; //$NON-NLS-1$

	private static final String SLASH = "/"; //$NON-NLS-1$

	private static final String GROOVY_MODULE_NAME_CANNOT_BE_NULL = com.sap.dirigible.runtime.groovy.Messages.getString("GroovyExecutor.GROOVY_MODULE_NAME_CANNOT_BE_NULL");  //$NON-NLS-1$

	private static final Logger logger = Logger
			.getLogger(GroovyExecutor.class.getCanonicalName());

	private IRepository repository;
	private String rootPath;
	private String secondaryRootPath;
	
	public GroovyExecutor(IRepository repository, String rootPath, String secondaryRootPath) {
		super();
		this.repository = repository;
		this.rootPath = rootPath;
		this.secondaryRootPath = secondaryRootPath;
	}

	@Override
	public Object executeServiceModule(HttpServletRequest request,
			HttpServletResponse response, Object input, String module, Map<Object, Object> executionContext)
			throws IOException {

			if (module == null) {
				throw new IOException(GROOVY_MODULE_NAME_CANNOT_BE_NULL);
			}
		
		RepositoryResourceConnector repositoryResourceConnector = 
				new RepositoryResourceConnector(this.repository, this.rootPath, this.secondaryRootPath);
		
		GroovyScriptEngine groovyScriptEngine = new GroovyScriptEngine(repositoryResourceConnector);
		
        Binding binding = new Binding();
        
		try {
			// /project1/module1.groovy
			String name = module;
			name = name.replace(SLASH, DOT);
			name = name.substring(1);
			
			registerDefaultVariables(request, response, input, executionContext, repository, binding);
			
//			groovyScriptEngine.getGroovyClassLoader().clearCache();
			groovyScriptEngine.run(name, binding);
		} catch (ResourceException e) {
			logger.error(e.getMessage(), e);
			throw new IOException(e);
		} catch (ScriptException e) {
			logger.error(e.getMessage(), e);
			throw new IOException(e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new IOException(e);
		}
		

		return binding;
	}

	@Override
	protected void registerDefaultVariable(Object scope, String name,
			Object value) {
		if (scope instanceof Binding) {
			((Binding) scope).setVariable(name, value);
		}
		else{
			logger.error("Scope class is: " + scope.getClass().getCanonicalName());
		}
	}

		
}
