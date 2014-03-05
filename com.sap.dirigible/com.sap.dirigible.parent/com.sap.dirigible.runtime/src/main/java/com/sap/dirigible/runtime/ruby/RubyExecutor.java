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

package com.sap.dirigible.runtime.ruby;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jruby.embed.LocalContextScope;
import org.jruby.embed.ScriptingContainer;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.runtime.scripting.AbstractScriptExecutor;

public class RubyExecutor extends AbstractScriptExecutor {

	private static final String RUBY_MODULE_NAME_CANNOT_BE_NULL = Messages.getString("RubyExecutor.RUBY_MODULE_NAME_CANNOT_BE_NULL"); //$NON-NLS-1$
	
	private IRepository repository;
	private String rootPath;
	private String secondaryRootPath;

	public RubyExecutor(IRepository repository, String rootPath, String secondaryRootPath) {
		super();
		this.repository = repository;
		this.rootPath = rootPath;
		this.secondaryRootPath = secondaryRootPath;
	}

	@Override
	public Object executeServiceModule(HttpServletRequest request,
			HttpServletResponse response, Object input, String module)
			throws IOException {

		Object result = null;

		if (module == null) {
			throw new IOException(RUBY_MODULE_NAME_CANNOT_BE_NULL);
		}

		ScriptingContainer container = new ScriptingContainer(
				LocalContextScope.THREADSAFE);
		container.setLoadServiceCreator(new RepositoryLoadServiceCreator(
				repository, rootPath, secondaryRootPath));
		Map<Object, Object> executionContext = new HashMap<Object, Object>();
		registerDefaultVariables(request, response, input, 
				executionContext, repository, container);
		String scriptlet = new String(readResourceData(repository, rootPath
				+ module));
		result = container.runScriptlet(scriptlet);
		return result;
	}

	@Override
	protected void registerDefaultVariable(Object scope, String name,
			Object value) {

		if (scope instanceof ScriptingContainer) {
			((ScriptingContainer) scope).put("$" + name, value); //$NON-NLS-1$
		}

	}

}
