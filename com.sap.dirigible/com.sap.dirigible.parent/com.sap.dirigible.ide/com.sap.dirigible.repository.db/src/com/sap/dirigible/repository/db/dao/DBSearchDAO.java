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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.dirigible.repository.api.ICollection;
import com.sap.dirigible.repository.api.IEntity;
import com.sap.dirigible.repository.api.IResource;
import com.sap.dirigible.repository.db.DBBaseException;
import com.sap.dirigible.repository.db.DBCollection;
import com.sap.dirigible.repository.db.DBRepositoryPath;
import com.sap.dirigible.repository.db.DBResource;

public class DBSearchDAO extends DBObjectDAO {

	private static Logger logger = LoggerFactory.getLogger(DBSearchDAO.class
			.getCanonicalName());

	DBSearchDAO(DBRepositoryDAO dbRepositoryDAO) {
		super(dbRepositoryDAO);
	}

	/**
	 * Search for files and folders containing the parameter in their name
	 * 
	 * @param parameter
	 * @return
	 */
	List<IEntity> searchName(String parameter, boolean caseInsensitive)
			throws DBBaseException {
		return searchInPath("%" + parameter, caseInsensitive); //$NON-NLS-1$
	}

	/**
	 * Search for files and folders containing the parameter in their name
	 * 
	 * @param parameter
	 * @return
	 */
	List<IEntity> searchPath(String parameter, boolean caseInsensitive)
			throws DBBaseException {
		return searchInPath("%" + parameter + "%", caseInsensitive); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private List<IEntity> searchInPath(String parameter, boolean caseInsensitive)
			throws DBBaseException {
		logger.debug(this.getClass().getCanonicalName(),
				"entering searchInPath"); //$NON-NLS-1$

		checkInitialized();

		if (parameter == null) {
			return null;
		}

		List<IEntity> result = new ArrayList<IEntity>();

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getRepository().getDbUtils().getConnection();
			String script;
			if (caseInsensitive) {
				parameter = parameter.toUpperCase();
				script = getRepository().getDbUtils().readScript(connection,
						DBScriptsMap.SCRIPT_SEARCH_NAME, this.getClass());
			} else {
				script = getRepository().getDbUtils().readScript(connection,
						DBScriptsMap.SCRIPT_SEARCH_NAME_SENSE, this.getClass());
			}
			preparedStatement = getRepository().getDbUtils()
					.getPreparedStatement(connection, script);
			preparedStatement.setString(1, parameter);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				DBObject dbObject = DBMapper.dbToObject(getRepository(),
						resultSet);
				if (dbObject instanceof DBFolder) {
					ICollection collection = new DBCollection(getRepository(),
							new DBRepositoryPath(dbObject.getPath()));
					result.add(collection);
				} else {
					IResource resource = new DBResource(getRepository(),
							new DBRepositoryPath(dbObject.getPath()));
					result.add(resource);
				}
			}
			return result;
		} catch (SQLException e) {
			throw new DBBaseException(e);
		} finally {
			getRepository().getDbUtils().closeStatement(preparedStatement);
			getRepository().getDbUtils().closeConnection(connection);
			logger.debug(this.getClass().getCanonicalName(),
					"exiting searchInPath"); //$NON-NLS-1$
		}
	}

	public List<IEntity> searchInPathAndText(String parameter,
			boolean caseInsensitive) throws DBBaseException {
		logger.debug(this.getClass().getCanonicalName(),
				"entering searchInPathAndText"); //$NON-NLS-1$

		checkInitialized();

		if (parameter == null) {
			return null;
		}

		parameter = "%" + parameter + "%"; //$NON-NLS-1$ //$NON-NLS-2$

		List<IEntity> result = new ArrayList<IEntity>();

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getRepository().getDbUtils().getConnection();
			String script;
			if (caseInsensitive) {
				parameter = parameter.toUpperCase();
				script = getRepository().getDbUtils().readScript(connection,
						DBScriptsMap.SCRIPT_SEARCH_TEXT, this.getClass());
			} else {
				script = getRepository().getDbUtils().readScript(connection,
						DBScriptsMap.SCRIPT_SEARCH_TEXT_SENSE, this.getClass());
			}
			preparedStatement = getRepository().getDbUtils()
					.getPreparedStatement(connection, script);
			preparedStatement.setString(1, parameter);
			preparedStatement.setString(2, parameter);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String path = resultSet.getString(1);
				IResource resource = getRepository().getResource(path);
				result.add(resource);
			}
			return result;
		} catch (SQLException e) {
			throw new DBBaseException(e);
		} finally {
			getRepository().getDbUtils().closeStatement(preparedStatement);
			getRepository().getDbUtils().closeConnection(connection);
			logger.debug(this.getClass().getCanonicalName(),
					"exiting searchInPathAndText"); //$NON-NLS-1$
		}
	}
}
