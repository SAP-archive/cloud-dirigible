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

import org.eclipse.dirigible.repository.api.IRepository;

public class RepositoryURLConnectionParams {
	
	private IRepository repository;
	private String rootPath;
	private String secondaryRootPath;
	
	
	
	public RepositoryURLConnectionParams(IRepository repository,
			String rootPath, String secondaryRootPath) {
		super();
		this.repository = repository;
		this.rootPath = rootPath;
		this.secondaryRootPath = secondaryRootPath;
	}
	public IRepository getRepository() {
		return repository;
	}
	public void setRepository(IRepository repository) {
		this.repository = repository;
	}
	public String getRootPath() {
		return rootPath;
	}
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	public String getSecondaryRootPath() {
		return secondaryRootPath;
	}
	public void setSecondaryRootPath(String secondaryRootPath) {
		this.secondaryRootPath = secondaryRootPath;
	}
	
	
}
