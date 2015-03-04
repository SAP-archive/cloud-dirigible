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

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.eclipse.dirigible.repository.api.IRepository;
import org.eclipse.dirigible.repository.api.IResource;
import org.eclipse.dirigible.repository.logging.Logger;

public class RepositoryURLConnection extends URLConnection {
	
	

	private static final String GROOVY_EXTENSION = ".groovy";
	
	private byte[] content;
	private long  dateInMilliseconds;
	private String name;
	
	public static final ThreadLocal<RepositoryURLConnectionParams> PARAMS = new ThreadLocal<RepositoryURLConnectionParams>();
	
	private static final Logger logger = Logger.getLogger(RepositoryURLConnection.class);
	
	public static final String PROTOCOL = "groovy:";
	
	public RepositoryURLConnection(URL url) {
		super(url);
	}
	/**
	 * The method makes connection with the repository in order to retrieve the requested resource.
	 */
	@Override
	public void connect() throws IOException {
			//project1.module1 should be project1/module1 in order to retrieve it from the repository
		
			name = getURL().toString().substring(PROTOCOL.length());
			
			if (name.endsWith(".groovy")){
				int idx = name.lastIndexOf(".");
				name = name.substring(0,idx);
				name = name.replace("$",IRepository.SEPARATOR);
				name = name.replace(".", IRepository.SEPARATOR);
				name += ".groovy";
				
				
			}
			else{
				name = name.replace("$",IRepository.SEPARATOR);
				name = name.replace(".", IRepository.SEPARATOR);
			}	
			
			IResource resource = retrieveResource(name);
			if (resource.exists()) {
				content = resource.getContent();
				dateInMilliseconds = resource.getInformation().getModifiedAt().getTime();
			} else {
				throw new FileNotFoundException("Resource: "+name+" does not exist");
			}
	}
	
	/**
	 * The method returns the time of change of the last-modified header field. The result is the number of milliseconds since January 1, 1970 GMT.
	 */
	public long getLastModifed() throws IOException {
		return dateInMilliseconds;
	}
	
	/**
	 * The method returns the resource content as byte array. 
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		if (!connected)
			connect();
		return new ByteArrayInputStream(content);
	}
	
	/**
	 * The method retrieves a resource by name form the repository.. 
	 * @throws IOException 
	 */
	private IResource retrieveResource(String name) throws IOException {
		RepositoryURLConnectionParams params = PARAMS.get();
		String repositoryPath = params.getRootPath() + IRepository.SEPARATOR + name;
		if (!repositoryPath.endsWith(GROOVY_EXTENSION)) {
			repositoryPath += GROOVY_EXTENSION;
		}
		IResource resource = params.getRepository().getResource(repositoryPath);
		if (!resource.exists()) {
			repositoryPath = params.getSecondaryRootPath() + IRepository.SEPARATOR + name;
			if (!repositoryPath.endsWith(GROOVY_EXTENSION)) {
				repositoryPath += GROOVY_EXTENSION;
			}
			resource = params.getRepository().getResource(repositoryPath);
		}
		return resource;
	}
	
}  
