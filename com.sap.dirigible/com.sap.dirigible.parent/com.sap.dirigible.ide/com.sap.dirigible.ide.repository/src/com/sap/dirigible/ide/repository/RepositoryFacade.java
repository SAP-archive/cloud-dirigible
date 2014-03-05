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

package com.sap.dirigible.ide.repository;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.eclipse.rap.rwt.RWT;

import com.sap.dirigible.ide.datasource.DataSourceFacade;
import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.RepositoryException;
import com.sap.dirigible.repository.db.DBRepository;

public class RepositoryFacade {

	private static final String REPOSITORY = "repository-instance"; //$NON-NLS-1$

	private static RepositoryFacade instance;

	public static RepositoryFacade getInstance() {
		if (instance == null) {
			instance = new RepositoryFacade();
		}
		return instance;
	}

	public IRepository getRepository() throws RepositoryException {

		HttpServletRequest request = RWT.getRequest();

		IRepository repository = getRepositoryInstance(request);

		if (repository != null) {
			return repository;
		}

		try {
			DataSource dataSource = lookupDataSource();
			String user = getUser(request);
			repository = new DBRepository(dataSource, user, false);
			saveRepositoryInstance(request, repository);
		} catch (Exception e) {
			throw new RepositoryException(e);
		}

		return repository;
	}

	public DataSource lookupDataSource() throws NamingException {
		return DataSourceFacade.getInstance().getDataSource();
	}

	public String getUser(HttpServletRequest request) {
		String user = "GUEST"; // shared one //$NON-NLS-1$
		try {
			if ((request != null) && (request.getUserPrincipal() != null)) {
				user = request.getUserPrincipal().getName();
			}
		} catch (Exception e) {
			// TODO - do nothing
		}
		return user;
	}

	private IRepository getRepositoryInstance(HttpServletRequest request) {
		if (request == null) {
			return null;
		}
		return (IRepository) request.getSession().getAttribute(REPOSITORY);
	}

	public void saveRepositoryInstance(HttpServletRequest request,
			IRepository repository) {
		if (request == null) {
			return;
		}
		request.getSession().setAttribute(REPOSITORY, repository);
	}

}
