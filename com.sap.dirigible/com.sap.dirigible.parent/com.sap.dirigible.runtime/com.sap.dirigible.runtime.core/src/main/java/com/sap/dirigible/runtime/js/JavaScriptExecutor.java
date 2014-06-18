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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.commonjs.module.ModuleScriptProvider;
import org.mozilla.javascript.commonjs.module.Require;
import org.mozilla.javascript.commonjs.module.RequireBuilder;
import org.mozilla.javascript.commonjs.module.provider.ModuleSource;
import org.mozilla.javascript.commonjs.module.provider.ModuleSourceProvider;
import org.mozilla.javascript.commonjs.module.provider.SoftCachingModuleScriptProvider;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.runtime.scripting.AbstractScriptExecutor;

public class JavaScriptExecutor extends AbstractScriptExecutor {

	private static final String JAVA_SCRIPT_MODULE_NAME_CANNOT_BE_NULL = Messages
			.getString("JavaScriptExecutor.JAVA_SCRIPT_MODULE_NAME_CANNOT_BE_NULL"); //$NON-NLS-1$

	private IRepository repository;
	private String[] rootPaths;

	public JavaScriptExecutor(IRepository repository, String... rootPaths) {
		super();
		this.repository = repository;
		this.rootPaths = rootPaths;
		if (this.rootPaths == null || this.rootPaths.length == 0) {
			this.rootPaths = new String[] { null, null };
		}
	}

	@Override
	public Object executeServiceModule(HttpServletRequest request, HttpServletResponse response,
			Object input, String module) throws IOException {

		if (module == null) {
			throw new IOException(JAVA_SCRIPT_MODULE_NAME_CANNOT_BE_NULL);
		}

		ModuleSourceProvider sourceProvider = createRepositoryModuleSourceProvider();
		ModuleScriptProvider scriptProvider = new SoftCachingModuleScriptProvider(sourceProvider);
		RequireBuilder builder = new RequireBuilder();
		builder.setModuleScriptProvider(scriptProvider);
		builder.setSandboxed(false);

		Object result = null;

		Context context = Context.enter();
		try {
			context.setLanguageVersion(Context.VERSION_1_2);
			context.getWrapFactory().setJavaPrimitiveWrap(false);
			Scriptable topLevelScope = context.initStandardObjects();
			Require require = builder.createRequire(context, topLevelScope);

			require.install(topLevelScope);

			Map<Object, Object> executionContext = new HashMap<Object, Object>();
			registerDefaultVariables(request, response, input, executionContext, repository,
					topLevelScope);

			beforeExecution(request, response, module, context);

			try {
				ModuleSource moduleSource = sourceProvider.loadSource(module, null, null);
				result = context.evaluateReader(topLevelScope, moduleSource.getReader(), module, 0,
						null);
			} catch (URISyntaxException e) {
				throw new IOException(e.getMessage(), e);
			}

		} finally {
			Context.exit();
		}
		return result;
	}

	protected void beforeExecution(HttpServletRequest request, HttpServletResponse response,
			String module, Context context) {
	}

	private RepositoryModuleSourceProvider createRepositoryModuleSourceProvider() {
		RepositoryModuleSourceProvider repositoryModuleSourceProvider = null;
		repositoryModuleSourceProvider = new RepositoryModuleSourceProvider(repository,
				rootPaths[0], rootPaths[1]);
		return repositoryModuleSourceProvider;
	}

	@Override
	protected void registerDefaultVariable(Object scope, String name, Object value) {
		if (scope instanceof ScriptableObject) {
			ScriptableObject local = (ScriptableObject) scope;
			local.put(name, local, value);
		}
	}

}
