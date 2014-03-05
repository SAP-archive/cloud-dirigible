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

package com.sap.dirigible.ide.datasource;

import javax.sql.DataSource;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.eclipse.rap.rwt.RWT;

import com.sap.dirigible.ide.logging.Logger;
import com.sap.dirigible.repository.ext.db.WrappedDataSource;

/**
 * 
 */
public class DataSourceFacade {

	private static final String EMBEDDED_DATA_SOURCE_IS_USED = Messages.DataSourceFacade_EMBEDDED_DATA_SOURCE_IS_USED;

	private static final String LOCAL_DB_ACTION = "create"; //$NON-NLS-1$
	private static final String LOCAL_DB_NAME = "derby"; //$NON-NLS-1$

	private static final String DATASOURCE_DEFAULT = "DEFAULT_DATASOURCE"; //$NON-NLS-1$

	public static final Logger logger = Logger.getLogger(DataSourceFacade.class
			.getCanonicalName());

	private static EmbeddedDataSource localDataSource;

	private static DataSourceFacade instance;

	private WrappedDataSource dataSource;

	public static DataSourceFacade getInstance() {
		if (instance == null) {
			instance = new DataSourceFacade();
		}
		return instance;
	}

	public DataSource getDataSource() {
		if (dataSource == null) {
			dataSource = getFromSession();
			if (dataSource == null) {
				dataSource = createLocal();
			}
		}
		return dataSource;
	}

	private WrappedDataSource getFromSession() {
		DataSource dataSource = null;
		dataSource = (DataSource) RWT.getRequest().getSession()
				.getAttribute(DATASOURCE_DEFAULT);
		if (dataSource != null) {
			WrappedDataSource wrappedDataSource = new WrappedDataSource(dataSource); 
			return wrappedDataSource;
		}
		return null;
	}

	private WrappedDataSource createLocal() {
		localDataSource = new EmbeddedDataSource();
		localDataSource.setDatabaseName(LOCAL_DB_NAME);
		localDataSource.setCreateDatabase(LOCAL_DB_ACTION);
		logger.warn(EMBEDDED_DATA_SOURCE_IS_USED);
		WrappedDataSource wrappedDataSource = new WrappedDataSource(localDataSource); 
		return wrappedDataSource;
	}

}
