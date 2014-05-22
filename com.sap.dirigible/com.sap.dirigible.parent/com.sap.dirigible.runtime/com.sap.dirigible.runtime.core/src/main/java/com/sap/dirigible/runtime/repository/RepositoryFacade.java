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

package com.sap.dirigible.runtime.repository;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.api.RepositoryException;
import com.sap.dirigible.repository.db.DBRepository;
import com.sap.dirigible.repository.ext.db.WrappedDataSource;

public class RepositoryFacade {
	
	private static final Logger logger = LoggerFactory.getLogger(RepositoryFacade.class);

	private static final String JAVA_COMP_ENV_JDBC_DEFAULT_DB = "java:comp/env/jdbc/DefaultDB"; //$NON-NLS-1$
	
	private static final String LOCAL_DB_ACTION = "create"; //$NON-NLS-1$
	
	private static final String LOCAL_DB_NAME = "derby"; //$NON-NLS-1$

	private static final String REPOSITORY = "repository-instance"; //$NON-NLS-1$

	private static RepositoryFacade instance;
	
	private static DataSource localDataSource;

	private WrappedDataSource dataSource;


	private RepositoryFacade() {

	}

	public static RepositoryFacade getInstance() {
		if (instance == null) {
			instance = new RepositoryFacade();
		}
		return instance;
	}

	public IRepository getRepository(HttpServletRequest request)
			throws RepositoryException {

		IRepository repository = getRepositoryInstance(request);

		if (repository != null) {
			return repository;
		}

		try {
			DataSource dataSource = getDataSource();
			String user = getUser(request);
			repository = new DBRepository(dataSource, user, false);
			saveRepositoryInstance(request, repository);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RepositoryException(e);
		}

		return repository;
	}

	public DataSource getDataSource() {
		if (dataSource == null) {
			dataSource = (WrappedDataSource) lookupDataSource();
			if (dataSource == null) {
				dataSource = createLocal();
			}
		}
		return dataSource;
	}
	
	private DataSource lookupDataSource() {
		InitialContext ctx;
		try {
			ctx = new InitialContext();
			return new WrappedDataSource((DataSource) ctx.lookup(JAVA_COMP_ENV_JDBC_DEFAULT_DB));
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	private WrappedDataSource createLocal() {
		localDataSource = (DataSource) System.getProperties().get(LOCAL_DB_NAME); 
		if (localDataSource == null) {
			localDataSource = new EmbeddedDataSource();
			((EmbeddedDataSource)localDataSource).setDatabaseName(LOCAL_DB_NAME);
			((EmbeddedDataSource)localDataSource).setCreateDatabase(LOCAL_DB_ACTION);
			System.getProperties().put(LOCAL_DB_NAME, localDataSource);
		}
		logger.error("Embedded DataSource is used!");
		
		WrappedDataSource wrappedDataSource = new WrappedDataSource(localDataSource); 
		return wrappedDataSource;
	}

	public static String getUser(HttpServletRequest request) {
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
		try {
			return (IRepository) request.getSession().getAttribute(REPOSITORY);
		} catch (IllegalStateException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public void saveRepositoryInstance(HttpServletRequest request,
			IRepository repository) {
		if (request == null) {
			return;
		}
		try {
			request.getSession().setAttribute(REPOSITORY, repository);
		} catch (Exception e) {
			repository.dispose();
		}
	}

}
