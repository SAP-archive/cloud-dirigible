/******************************************************************************* 
 * Copyright (c) 2015 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   SAP - initial API and implementation
 *******************************************************************************/

package org.eclipse.dirigible.runtime.groovy;

import groovy.util.ResourceConnector;
import groovy.util.ResourceException;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.eclipse.dirigible.repository.logging.Logger;

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
