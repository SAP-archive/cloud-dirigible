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

import groovy.util.ResourceException;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.osgi.service.url.AbstractURLStreamHandlerService;

public class RepositoryURLStreamHandlerService extends AbstractURLStreamHandlerService {
	
	@Override
	public URLConnection openConnection(URL u) throws IOException {
		try {
			RepositoryResourceConnector repositoryResourceConnector =
					new RepositoryResourceConnector();
			return repositoryResourceConnector.getResourceConnection(u.toString());
		} catch (ResourceException e) {
			throw new IOException(e);
		}
	}

}
