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
import java.net.MalformedURLException;

import org.jruby.Ruby;
import org.jruby.runtime.load.ExternalScript;
import org.jruby.runtime.load.Library;
import org.jruby.runtime.load.LoadService;
import org.jruby.runtime.load.LoadServiceResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.dirigible.repository.api.IResource;

public class RepositoryLoadService extends LoadService {

	private static final String LOAD_ERROR_S_S_S = "load error: %s -- %s: %s"; //$NON-NLS-1$

	private static final String RUBY_EXTENSION = ".rb"; //$NON-NLS-1$
	
	private static final String THERE_IS_NO_RESOURCE_AT_THE_SPECIFIED_SERVICE_PATH = Messages.getString("RubyExecutorTHERE_IS_NO_RESOURCE_AT_THE_SPECIFIED_SERVICE_PATH"); //$NON-NLS-1$

	private static final Logger logger = LoggerFactory
			.getLogger(RepositoryLoadService.class);

	private RepositoryLoadServiceCreator repositoryLoadServiceCreator;

	public RepositoryLoadService(Ruby runtime,
			RepositoryLoadServiceCreator repositoryLoadServiceCreator) {
		super(runtime);
		this.repositoryLoadServiceCreator = repositoryLoadServiceCreator;
	}

	@Override
	public boolean require(String requireName) {
		
		try {
			IResource resource = getRepositoryResourceByFile(requireName);
			if (resource.exists()) {
				loadLibraryFromRepository(requireName, false, resource);
				return true;
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return super.require(requireName);
	}

	@Override
	public void load(String file, boolean wrap) {
		try {
			IResource resource = getRepositoryResourceByFile(file);
			if (resource.exists()) {
				loadLibraryFromRepository(file, wrap, resource);
				return;
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		super.load(file, wrap);
	}

	private IResource getRepositoryResourceByFile(String requireName) throws IOException {
		String repositoryPath = this.repositoryLoadServiceCreator.getRoot()
				+ requireName;
		if (!repositoryPath.endsWith(RUBY_EXTENSION)) {
			repositoryPath += RUBY_EXTENSION;
		}
		IResource resource = this.repositoryLoadServiceCreator.getRepository()
				.getResource(repositoryPath);
		if (!resource.exists()) {
			repositoryPath = this.repositoryLoadServiceCreator.getSecondaryRoot()
					+ requireName;
			if (!repositoryPath.endsWith(RUBY_EXTENSION)) {
				repositoryPath += RUBY_EXTENSION;
			}
			resource = this.repositoryLoadServiceCreator.getRepository()
					.getResource(repositoryPath);
			if (!resource.exists()) {
				logger.error(THERE_IS_NO_RESOURCE_AT_THE_SPECIFIED_SERVICE_PATH
						+ repositoryPath);
			}
		}
		return resource;
	}

	private void loadLibraryFromRepository(String file, boolean wrap,
			IResource resource) throws MalformedURLException, IOException {
		LoadServiceResource loadServiceResource = new RepositoryLoadServiceResource(
				null, resource.getName(), resource.getContent());
		Library library = new ExternalScript(loadServiceResource, file);
		try {
			library.load(runtime, wrap);
			return;
		} catch (IOException e) {
			if (runtime.getDebug().isTrue())
				e.printStackTrace(runtime.getErr());
			throw runtime.newLoadError(String.format(LOAD_ERROR_S_S_S, file, e.getClass().getName(), e.getMessage())); // NOPMD
		}
	}

}
