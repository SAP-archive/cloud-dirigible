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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.commonjs.module.provider.ModuleSource;
import org.mozilla.javascript.commonjs.module.provider.ModuleSourceProviderBase;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.runtime.scripting.AbstractScriptExecutor;
import com.sap.dirigible.runtime.scripting.Messages;

public class RepositoryModuleSourceProvider extends ModuleSourceProviderBase {

	private static final long serialVersionUID = -5527033249080497877L;
	
	private static final String MODULE_LOCATION_CANNOT_BE_NULL = Messages.getString("ScriptLoader.MODULE_LOCATION_CANNOT_BE_NULL"); //$NON-NLS-1$
	private static final String JS_EXTENSION = ".js"; //$NON-NLS-1$
	private static final String JSLIB_EXTENSION = ".js"; //$NON-NLS-1$
	
	private AbstractScriptExecutor executor;
	private IRepository repository;
	private String[] rootPaths;

	public RepositoryModuleSourceProvider(AbstractScriptExecutor executor, IRepository repository, String ... rootPaths) {
		super();
		this.executor = executor;
		this.repository = repository;
		this.rootPaths = rootPaths;
	}
	
	@Override
	public ModuleSource loadSource(String moduleId, Scriptable paths,
			Object validator) throws IOException, URISyntaxException {

		if (moduleId == null) {
			throw new IOException(MODULE_LOCATION_CANNOT_BE_NULL);
		}
		
		byte[] sourceCode = null;
		ModuleSource moduleSource = null;
		if (moduleId.endsWith(JS_EXTENSION)) {
			sourceCode = executor.retrieveModule(repository, moduleId, "", rootPaths).getContent();
			moduleSource = new ModuleSource(new InputStreamReader(
					new ByteArrayInputStream(sourceCode)), null, new URI(moduleId), null, null);
		} else {
			sourceCode = executor.retrieveModule(repository, moduleId, JSLIB_EXTENSION, rootPaths).getContent();
			moduleSource = new ModuleSource(new InputStreamReader(
					new ByteArrayInputStream(sourceCode)), null, new URI(moduleId + JSLIB_EXTENSION), null, null);
		}
		
		return moduleSource;
	}

	@Override
	protected ModuleSource loadFromUri(URI uri, URI base, Object validator)
			throws IOException, URISyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

}
