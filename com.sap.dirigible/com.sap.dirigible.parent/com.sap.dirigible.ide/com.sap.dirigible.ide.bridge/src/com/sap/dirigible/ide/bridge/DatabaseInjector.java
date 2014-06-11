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

package com.sap.dirigible.ide.bridge;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseInjector implements Injector {
	
	private static final String ERROR_WHILE_GETTING_DATABASE_METADATA = Messages.getString("DatabaseInjector.ERROR_WHILE_GETTING_DATABASE_METADATA"); //$NON-NLS-1$
	public static final String JAVA_COMP_ENV_JDBC_DEFAULT_DB = "java:comp/env/jdbc/DefaultDB"; //$NON-NLS-1$
	public static final String DATASOURCE_DEFAULT = "DEFAULT_DATASOURCE"; //$NON-NLS-1$
	public static final String DATABASE_PRODUCT_NAME = "DATABASE_PRODUCT_NAME"; //$NON-NLS-1$
	public static final String DATABASE_PRODUCT_VERSION = "DATABASE_PRODUCT_VERSION"; //$NON-NLS-1$
	public static final String DATABASE_MINOR_VERSION = "DATABASE_MINOR_VERSION"; //$NON-NLS-1$
	public static final String DATABASE_MAJOR_VERSION = "DATABASE_MAJOR_VERSION"; //$NON-NLS-1$
	public static final String DATABASE_DRIVER_NAME = "DATABASE_DRIVER_NAME"; //$NON-NLS-1$
	public static final String DATABASE_DRIVER_MINOR_VERSION = "DATABASE_DRIVER_MINOR_VERSION"; //$NON-NLS-1$
	public static final String DATABASE_DRIVER_MAJOR_VERSION = "DATABASE_DRIVER_MAJOR_VERSION"; //$NON-NLS-1$
	public static final String DATABASE_CONNECTION_CLASS_NAME = "DATABASE_CONNECTION_CLASS_NAME"; //$NON-NLS-1$
	
	private static final Logger logger = LoggerFactory.getLogger(DatabaseInjector.class);
	
	/* (non-Javadoc)
	 * @see com.sap.dirigible.ide.bridge.Injector#inject(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void inject(ServletConfig servletConfig, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		DataSource dataSource = (DataSource) req.getSession().getAttribute(DATASOURCE_DEFAULT);
		if (dataSource == null) {
			try {
				dataSource  = lookupDataSource();
				req.getSession().setAttribute(DATASOURCE_DEFAULT, dataSource);
				Connection connection = null;
				try {
					try {
						connection = dataSource.getConnection();
						DatabaseMetaData metaData = connection.getMetaData();
						req.getSession().setAttribute(DATABASE_PRODUCT_NAME, metaData.getDatabaseProductName());
						req.getSession().setAttribute(DATABASE_PRODUCT_VERSION, metaData.getDatabaseProductVersion());
						req.getSession().setAttribute(DATABASE_MINOR_VERSION, metaData.getDatabaseMinorVersion());
						req.getSession().setAttribute(DATABASE_MAJOR_VERSION, metaData.getDatabaseMajorVersion());
						req.getSession().setAttribute(DATABASE_DRIVER_NAME, metaData.getDriverName());
						req.getSession().setAttribute(DATABASE_DRIVER_MINOR_VERSION, metaData.getDriverMinorVersion());
						req.getSession().setAttribute(DATABASE_DRIVER_MAJOR_VERSION, metaData.getDriverMajorVersion());
						req.getSession().setAttribute(DATABASE_CONNECTION_CLASS_NAME, connection.getClass().getCanonicalName());
					} finally {
						if (connection != null) {
							connection.close();
						}
					}
				} catch (SQLException e) {
					logger.error(e.getMessage(), e);
					throw new ServletException(ERROR_WHILE_GETTING_DATABASE_METADATA, e);
				}
			} catch (NamingException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * Retrieve the DataSource from the target server environment
	 * 
	 * @return
	 * @throws NamingException
	 */
	private DataSource lookupDataSource() throws NamingException {
		final InitialContext ctx = new InitialContext();
		return (DataSource) ctx.lookup(JAVA_COMP_ENV_JDBC_DEFAULT_DB);
	}

}
