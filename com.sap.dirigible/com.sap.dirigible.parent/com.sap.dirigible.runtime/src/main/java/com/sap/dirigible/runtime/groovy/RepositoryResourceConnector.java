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

import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;

import java.io.IOException;
import java.net.URLConnection;

import com.sap.dirigible.repository.api.IRepository;

public class RepositoryResourceConnector extends GroovyScriptEngine {
	
	private IRepository repository;
	private String rootPath;
	private String secondaryRootPath;
	
	public RepositoryResourceConnector(IRepository repository, String rootPath, String secondaryRootPath) throws IOException {
		super((String) IRepository.SEPARATOR);
		this.repository = repository;
		this.rootPath = rootPath;
		this.secondaryRootPath = secondaryRootPath;
	}
	
	public URLConnection getResourceConnection(String name) throws ResourceException {

			try {
				RepositoryURLConnection urlConnection = new RepositoryURLConnection(repository, rootPath, secondaryRootPath,  name);
				urlConnection.connect();
				return urlConnection;
			} catch (IOException e) {
				return super.getResourceConnection(name);
			}
			
	}
}
