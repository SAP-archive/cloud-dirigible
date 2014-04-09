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

package com.sap.dirigible.repository.db.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.dirigible.repository.db.DBBaseException;
import com.sap.dirigible.repository.db.DBRepository;

/**
 * Initialize the database schema of Repository Supports incremental alteration
 * of the schema
 * 
 */
public class DBRepositoryInitializer {

	private static final String EXTENSION_POINTS = Messages.getString("DBRepositoryInitializer.EXTENSION_POINTS"); //$NON-NLS-1$

	private static final String INITIALIZING_SCRIPT_VERSION_S_FROM_S_ABOUT_S = Messages.getString("DBRepositoryInitializer.INITIALIZING_SCRIPT_VERSION_S_FROM_S_ABOUT_S"); //$NON-NLS-1$

	private static final String SECURITY_FEATURES = Messages.getString("DBRepositoryInitializer.SECURITY_FEATURES"); //$NON-NLS-1$

	private static final String FILE_VERSIONS_SUPPORT = Messages.getString("DBRepositoryInitializer.FILE_VERSIONS_SUPPORT"); //$NON-NLS-1$

	private static final String FREE_TEXT_SEARCH_IN_DOCUMENTS = Messages.getString("DBRepositoryInitializer.FREE_TEXT_SEARCH_IN_DOCUMENTS"); //$NON-NLS-1$

	private static final String TEST_UPDATE = Messages.getString("DBRepositoryInitializer.TEST_UPDATE"); //$NON-NLS-1$

	private static final String INITIAL_CREATION = Messages.getString("DBRepositoryInitializer.INITIAL_CREATION"); //$NON-NLS-1$

	private static Logger logger = LoggerFactory
			.getLogger(DBRepositoryInitializer.class.getCanonicalName());

	private static final String TABLE_NAME_DGB_SCHEMA_VERSIONS = "DGB_SCHEMA_VERSIONS"; //$NON-NLS-1$
	private static final String TABLE_COLUMN_SCHV_VERSION = "SCHV_VERSION"; //$NON-NLS-1$

	private DBRepository repository;
	private Connection connection;
	private boolean forceRecreate;

	static class ScriptDescriptor {
		int version;
		String description;
		String location;

		public ScriptDescriptor(int version, String description, String location) {
			super();
			this.version = version;
			this.description = description;
			this.location = location;
		}

	}

	/**
	 * The list with the scripts to be executed consequently
	 */
	static List<ScriptDescriptor> scriptDescriptors = new ArrayList<ScriptDescriptor>();
	static {
		scriptDescriptors.add(new ScriptDescriptor(1, INITIAL_CREATION,
				DBScriptsMap.SCRIPT_CREATE_SCHEMA_1));
		scriptDescriptors.add(new ScriptDescriptor(2, TEST_UPDATE,
				DBScriptsMap.SCRIPT_CREATE_SCHEMA_2));
		scriptDescriptors.add(new ScriptDescriptor(3,
				FREE_TEXT_SEARCH_IN_DOCUMENTS,
				DBScriptsMap.SCRIPT_CREATE_SCHEMA_3));
		scriptDescriptors.add(new ScriptDescriptor(4, FILE_VERSIONS_SUPPORT,
				DBScriptsMap.SCRIPT_CREATE_SCHEMA_4));
		scriptDescriptors.add(new ScriptDescriptor(5, SECURITY_FEATURES,
				DBScriptsMap.SCRIPT_CREATE_SCHEMA_5));
		scriptDescriptors.add(new ScriptDescriptor(6, EXTENSION_POINTS,
				DBScriptsMap.SCRIPT_CREATE_SCHEMA_6));
		
		
	}

	DBRepositoryInitializer(DBRepository repository, Connection connection,
			boolean forceRecreate) {
		logger.debug(this.getClass().getCanonicalName(), "entering constructor"); //$NON-NLS-1$
		this.repository = repository;
		this.connection = connection;
		this.forceRecreate = forceRecreate;
		logger.debug(this.getClass().getCanonicalName(), "exiting constructor"); //$NON-NLS-1$
	}

	/**
	 * The entry point method
	 * 
	 * @return
	 */
	boolean initialize() {
		boolean result = false;
		logger.debug(this.getClass().getCanonicalName(), "entering initialize"); //$NON-NLS-1$
		if (forceRecreate) {
			result = forceRecreate();
		} else {
			result = updateIncrements();
		}
		logger.debug(this.getClass().getCanonicalName(), "exiting initialize"); //$NON-NLS-1$
		return result;
	}

	DBRepository getRepository() {
		return repository;
	}

	/**
	 * Drop all tables and create all the schema from scratch
	 * 
	 * @return
	 */
	private boolean forceRecreate() {
		logger.warn(this.getClass().getCanonicalName(),
				"entering forceRecreate"); //$NON-NLS-1$
		boolean result = false;
		for (Iterator<ScriptDescriptor> iterator = scriptDescriptors.iterator(); iterator
				.hasNext();) {
			ScriptDescriptor scriptDescriptor = iterator.next();
			logger.info(String.format(
					INITIALIZING_SCRIPT_VERSION_S_FROM_S_ABOUT_S,
					scriptDescriptor.version, scriptDescriptor.location,
					scriptDescriptor.description));
			try {
				String script = getRepository().getDbUtils().readScript(connection,
						scriptDescriptor.location, this.getClass());
				result = getRepository().getDbUtils().executeUpdate(connection,
						script);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
			if (!result) {
				break;
			}
		}

		logger.warn(this.getClass().getCanonicalName(), "exiting forceRecreate"); //$NON-NLS-1$
		return result;
	}

	/**
	 * Check the tables one by one and try to repair the schema if needed
	 * 
	 * @return
	 */
	private boolean updateIncrements() {
		logger.debug(this.getClass().getCanonicalName(), "updateIncrements"); //$NON-NLS-1$

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getRepository().getDbUtils().getConnection();
			String script = getRepository().getDbUtils().readScript(connection,
					DBScriptsMap.SCRIPT_GET_SCHEMA_VERSION, this.getClass());
			if (versionExists()) {

				preparedStatement = getRepository().getDbUtils()
						.getPreparedStatement(connection, script);
				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					int version = resultSet.getInt(TABLE_COLUMN_SCHV_VERSION);

					for (Iterator<ScriptDescriptor> iterator = scriptDescriptors
							.iterator(); iterator.hasNext();) {
						ScriptDescriptor scriptDescriptor = iterator.next();
						if (scriptDescriptor.version > version) {
							logger.warn(String
									.format(INITIALIZING_SCRIPT_VERSION_S_FROM_S_ABOUT_S,
											scriptDescriptor.version,
											scriptDescriptor.location,
											scriptDescriptor.description));
							script = getRepository().getDbUtils().readScript(
									connection, scriptDescriptor.location,
									this.getClass());
							boolean result = getRepository().getDbUtils()
									.executeUpdate(connection, script);
							if (!result) {
								break;
							}
						}
					}
				} else {
					// table version is empty
					forceRecreate();
				}
			} else {
				// table version doesn't exist
				forceRecreate();
			}

		} catch (SQLException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			getRepository().getDbUtils().closeStatement(preparedStatement);
			getRepository().getDbUtils().closeConnection(connection);
		}

		logger.debug(this.getClass().getCanonicalName(),
				"exiting updateIncrements"); //$NON-NLS-1$
		return true;
	}

	private boolean versionExists() throws SQLException {
		boolean result = false;
		DatabaseMetaData databaseMetaData = this.connection.getMetaData();
		ResultSet rs = null;
		try {
			rs = databaseMetaData.getTables(null, null,
					TABLE_NAME_DGB_SCHEMA_VERSIONS, new String[] { "TABLE", //$NON-NLS-1$
							"VIEW" }); //$NON-NLS-1$
			if (rs.next()) {
				result = true;
			} else {
				result = false;
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		return result;
	}

}
