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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.commonjs.module.provider.ModuleSource;
import org.mozilla.javascript.commonjs.module.provider.ModuleSourceProviderBase;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.IResource;
import com.sap.dirigible.runtime.logger.Logger;
import com.sap.dirigible.runtime.scripting.Messages;

public class RepositoryModuleSourceProvider extends ModuleSourceProviderBase {

	private static final long serialVersionUID = -5527033249080497877L;
	
	private static final String THERE_IS_NO_RESOURCE_AT_THE_SPECIFIED_SERVICE_PATH = Messages.getString("ScriptLoader.THERE_IS_NO_RESOURCE_AT_THE_SPECIFIED_SERVICE_PATH"); //$NON-NLS-1$
	private static final String THERE_IS_NO_SERVICE_OR_LIBRARY_MODULE_AT_THE_SPECIFIED_LOCATION = Messages.getString("ScriptLoader.THERE_IS_NO_SERVICE_OR_LIBRARY_MODULE_AT_THE_SPECIFIED_LOCATION"); //$NON-NLS-1$
	private static final String MODULE_LOCATION_CANNOT_BE_NULL = Messages.getString("ScriptLoader.MODULE_LOCATION_CANNOT_BE_NULL"); //$NON-NLS-1$
	private static final String JS_EXTENSION = ".js"; //$NON-NLS-1$
	private static final String JSLIB_EXTENSION = ".jslib"; //$NON-NLS-1$
	
	private IRepository repository;
	private String[] rootPaths;
	
	private static final Logger logger = Logger.getLogger(RepositoryModuleSourceProvider.class);

	public RepositoryModuleSourceProvider(IRepository repository, String ... rootPaths) {
		super();
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
			sourceCode = retrieveModule(moduleId, "");
			moduleSource = new ModuleSource(new InputStreamReader(
					new ByteArrayInputStream(sourceCode)), null, new URI(moduleId), null, null);
		} else {
			sourceCode = retrieveModule(moduleId, JSLIB_EXTENSION);
			moduleSource = new ModuleSource(new InputStreamReader(
					new ByteArrayInputStream(sourceCode)), null, new URI(moduleId + JSLIB_EXTENSION), null, null);
		}
		
		return moduleSource;
	}
	
	private byte[] retrieveModule(String module, String extension)
			throws IOException {
		
		for (String rootPath : rootPaths) {
			String resourcePath = createResourcePath(rootPath, module, extension);
			byte[] result;
			final IResource resource = repository.getResource(resourcePath);
			if (resource.exists()) {
				result = readResourceData(resourcePath);
				return result;
			}
		}
		
		logger.error(THERE_IS_NO_RESOURCE_AT_THE_SPECIFIED_SERVICE_PATH
				+ Arrays.toString(rootPaths));
			throw new FileNotFoundException(
				THERE_IS_NO_SERVICE_OR_LIBRARY_MODULE_AT_THE_SPECIFIED_LOCATION
					+ module + extension);
	}

	private String createResourcePath(String root, String module,
			String extension) {
		String resourcePath = new StringBuilder().append(root).append('/')
				.append(module).append(extension).toString();
		return resourcePath;
	}
	
	public byte[] readResourceData(String repositoryPath) throws IOException {
		final IResource resource = repository.getResource(repositoryPath);
		if (!resource.exists()) {
			logger.error(THERE_IS_NO_RESOURCE_AT_THE_SPECIFIED_SERVICE_PATH
					+ repositoryPath);
			throw new IOException(
					THERE_IS_NO_RESOURCE_AT_THE_SPECIFIED_SERVICE_PATH
							+ repositoryPath);
		}
		return resource.getContent();
	}

	@Override
	protected ModuleSource loadFromUri(URI uri, URI base, Object validator)
			throws IOException, URISyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

}
