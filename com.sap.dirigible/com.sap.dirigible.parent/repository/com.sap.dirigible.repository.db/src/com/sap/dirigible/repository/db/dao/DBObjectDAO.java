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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sap.dirigible.repository.db.DBBaseException;
import com.sap.dirigible.repository.db.DBRepository;
import com.sap.dirigible.repository.db.init.DBScriptsMap;
import com.sap.dirigible.repository.logging.Logger;

public class DBObjectDAO {

	private static Logger logger = Logger.getLogger(DBObjectDAO.class);

	private DBRepositoryDAO dbRepositoryDAO;

	DBObjectDAO(DBRepositoryDAO dbRepositoryDAO) {
		this.dbRepositoryDAO = dbRepositoryDAO;
	}

	/**
	 * Getter for DBRepositoryDAO object
	 * 
	 * @return
	 */
	public DBRepositoryDAO getDbRepositoryDAO() {
		return dbRepositoryDAO;
	}

	/**
	 * Getter for the Repository instance
	 * 
	 * @return
	 */
	protected DBRepository getRepository() {
		return this.dbRepositoryDAO.getRepository();
	}

	/**
	 * Check whether the database schema is initialized
	 * 
	 * @return
	 */
	protected void checkInitialized() {
		this.dbRepositoryDAO.checkInitialized();
	}

	/**
	 * Query the database and retrieve the database object based on the provided
	 * path
	 * 
	 * @param path
	 * @return
	 * @throws DBBaseException
	 */
	public DBObject getObjectByPath(String path) throws DBBaseException {
		logger.debug("entering getObjectByPath"); //$NON-NLS-1$

		checkInitialized();

		if (path == null || "".equals(path.trim())) { //$NON-NLS-1$
			return null;
		}

		DBObject dbObject = null;
		
		
		Object cached = getRepository().getCacheManager().get(path);
		if (cached != null && cached instanceof DBObject) {
			dbObject = (DBObject) cached;
		} else {
			// not cached - get from db
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			try {
				connection = getRepository().getDbUtils().getConnection();
				String script = getRepository().getDbUtils().readScript(connection,
						DBScriptsMap.SCRIPT_GET_FILE_BY_PATH, this.getClass());
				preparedStatement = getRepository().getDbUtils()
						.getPreparedStatement(connection, script);
				preparedStatement.setString(1, path);
				ResultSet resultSet = null;
				try {
					resultSet = preparedStatement.executeQuery();
	
					if (resultSet.next()) {
						dbObject = DBMapper.dbToObject(getRepository(), resultSet);
					}
				} finally {
					if (resultSet != null) {
						resultSet.close();
					}
					if (preparedStatement != null) {
						preparedStatement.close();
					}
				}
	
			} catch (SQLException e) {
				throw new DBBaseException(e);
			} catch (IOException e) {
				throw new DBBaseException(e);
			} finally {
				getRepository().getDbUtils().closeStatement(preparedStatement);
				getRepository().getDbUtils().closeConnection(connection);
			}
			getRepository().getCacheManager().put(path, dbObject);
		}
		
		logger.debug("exiting getObjectByPath"); //$NON-NLS-1$
		return dbObject;
	}

}
