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

import groovy.util.ResourceConnector;
import groovy.util.ResourceException;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import com.sap.dirigible.repository.logging.Logger;

public class RepositoryResourceConnector implements ResourceConnector {
	
	private static final Logger logger = Logger.getLogger(RepositoryResourceConnector.class);
	
	public URLConnection getResourceConnection(String name) throws ResourceException {

			try {
				URL url = null;
				if (name != null
						&& name.startsWith(RepositoryURLConnection.PROTOCOL)) {
					url = new URL(name);
				} else {
					url = new URL(RepositoryURLConnection.PROTOCOL + name);
				}
				RepositoryURLConnection repositoryURLConnection = new RepositoryURLConnection(url);
				repositoryURLConnection.connect();
				return repositoryURLConnection;
			} catch (IOException e) {
//				return super.getResourceConnection(name);
//				logger.error(e.getMessage(), e);
				throw new ResourceException("An IO exception occurred", e);
			}
			
	}
	
}
